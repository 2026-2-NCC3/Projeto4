import test from 'node:test';
import assert from 'node:assert/strict';
import { createApiClient, qrState } from '../../../src/Entrega2/Backend/public/admin/admin-core.mjs';

const session = { access_token: 'test-access', refresh_token: 'test-refresh', user: { id: 'student-test' } };
const response = (body, status = 200) => ({ ok: status >= 200 && status < 300, status, json: async () => body });
const deferred = () => { let resolve; const promise = new Promise(r => { resolve = r; }); return { promise, resolve }; };

test('QR ativo calcula segundos restantes e fim exclusivo', () => {
  const qr = { opensAt: '2026-11-09T10:00:00Z', expiresAt: '2026-11-09T11:00:00Z' };
  assert.deepEqual(qrState(qr, Date.parse(qr.opensAt)), { active: true, seconds: 3600, label: 'Válido por 60:00' });
  assert.equal(qrState(qr, Date.parse(qr.expiresAt)).active, false);
});
test('QR futuro ou datas inválidas nunca aparece válido', () => {
  assert.equal(qrState({ opensAt: 'invalid', expiresAt: 'invalid' }, 0).active, false);
  assert.equal(qrState({ opensAt: '2026-11-09T10:00:00Z', expiresAt: '2026-11-09T11:00:00Z' }, 0).active, false);
});
test('login envia senha exata e request usa Bearer apenas na API local', async () => {
  const calls = [];
  const api = createApiClient({ fetcher: async (url, options) => { calls.push({ url, options }); return response(url.endsWith('/login') ? session : { admin: true }); } });
  await api.login(' user@example.test ', ' secret ');
  assert.deepEqual(JSON.parse(calls[0].options.body), { email: 'user@example.test', senha: ' secret ' });
  assert.equal(api.isAuthenticated(), true);
  await api.request('/api/admin/me');
  assert.equal(calls[1].options.headers.Authorization, 'Bearer test-access');
  await assert.rejects(api.request('https://other.invalid/'), /Destino/);
});
test('requests simultâneas 401 renovam apenas uma vez', async () => {
  let refreshes = 0;
  const api = createApiClient({ fetcher: async (url, options) => {
    if (url.endsWith('/login')) return response(session);
    if (url.endsWith('/refresh')) { refreshes++; await new Promise(r => setTimeout(r, 5)); return response({ ...session, access_token: 'fresh' }); }
    return options.headers.Authorization === 'Bearer fresh' ? response({ ok: true }) : response({}, 401);
  } });
  await api.login('a@b.test', 'pass');
  assert.deepEqual(await Promise.all([api.request('/api/admin/me'), api.request('/api/admin/encontros')]), [{ ok: true }, { ok: true }]);
  assert.equal(refreshes, 1);
});
test('refresh 401 encerra sessão e não entra em loop', async () => {
  let ended = 0; let refreshes = 0;
  const api = createApiClient({ onSessionEnd: () => ended++, fetcher: async url => {
    if (url.endsWith('/login')) return response(session);
    if (url.endsWith('/refresh')) refreshes++;
    return response({}, 401);
  } });
  await api.login('a@b.test', 'pass');
  await assert.rejects(api.request('/api/admin/me'), e => e.status === 401);
  assert.equal(refreshes, 1); assert.equal(ended, 1); assert.equal(api.isAuthenticated(), false);
});
test('refresh indisponível preserva sessão para tentar depois', async () => {
  const api = createApiClient({ fetcher: async url => response(url.endsWith('/login') ? session : {}, url.endsWith('/login') ? 200 : url.endsWith('/refresh') ? 503 : 401) });
  await api.login('a@b.test', 'pass');
  await assert.rejects(api.request('/api/admin/me'), e => e.status === 503);
  assert.equal(api.isAuthenticated(), true);
});
test('logout durante resposta pendente impede retorno de dados antigos', async () => {
  const pending = deferred();
  const api = createApiClient({ fetcher: async url => url.endsWith('/login') ? response(session) : pending.promise });
  await api.login('a@b.test', 'pass');
  const request = api.request('/api/admin/encontros'); api.logout(); pending.resolve(response([{ private: true }]));
  await assert.rejects(request, e => e.code === 'SESSION_CHANGED');
  assert.equal(api.isAuthenticated(), false);
});
test('logout durante login impede autenticação tardia', async () => {
  const pending = deferred(); const api = createApiClient({ fetcher: () => pending.promise });
  const login = api.login('a@b.test', 'pass'); api.logout(); pending.resolve(response(session));
  await assert.rejects(login, e => e.code === 'SESSION_CHANGED');
  assert.equal(api.isAuthenticated(), false);
});
test('refresh de outra identidade encerra sessão', async () => {
  const api = createApiClient({ fetcher: async url => url.endsWith('/login') ? response(session) : url.endsWith('/refresh') ? response({ ...session, user: { id: 'other' } }) : response({}, 401) });
  await api.login('a@b.test', 'pass');
  await assert.rejects(api.request('/api/admin/me'), e => e.status === 401);
  assert.equal(api.isAuthenticated(), false);
});
