# Riscos

- Alto, autorização/presença falsa: não expor HTTP antes do adaptador transacional e policies revisados.
- Alto, concorrência: exigir restrição única e gravação atômica no banco; fake testa contrato, não prova Postgres.
- Médio, divergência documental: declarar claramente que E2 não foi publicada.
- Médio, expiração offline: reenvio expirado é rejeitado; política de tolerância depende de definição posterior.
- Gate humano: SQL remoto, credenciais, push e deploy. Nenhum necessário para testes locais.
- Stop: não presumir schema/roles/chaves não observados; não simular aprovação independente.
