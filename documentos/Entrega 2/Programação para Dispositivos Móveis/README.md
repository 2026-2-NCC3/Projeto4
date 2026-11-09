# Programação para Dispositivos Móveis - Entrega 2

[APK debug instalável](proxima-etapa-entrega2.apk) · [Código Android](../../../src/Entrega2/Frontend) · [Roteiro de testes](../../../src/Entrega2/TESTAR.md)

Versão 2.0, versionCode 2, minSdk 24, assinatura debug de desenvolvimento.
API configurada: `https://nextgeneration-seven.vercel.app/`. Este APK não usa a fixture local.
A migração Supabase foi aplicada em 08/11/2026. Deploy E2 e teste integrado no ambiente
publicado ainda precisam de confirmação.

SHA256: `f526c4d0aca2fc382039d5303b4fd88890b52f86164282212da357ed298e4f41`.

## Verificação

Em 08/11/2026: build aprovado, 16/16 testes JVM, lint com 0 erros e 93 avisos.
O APK desta pasta é idêntico ao artefato gerado na verificação. Há também registro anterior
de 6/6 testes instrumentados no emulador; eles não foram repetidos nesta revisão documental.

No Redmi Note 8, usando APK de teste com backend local, Luiz confirmou:
- leitura de QR e presença online;
- leitura sem conexão, pendência e sincronização posterior sem duplicação;
- câmera do leitor na vertical após atualização do APK;
- abertura e download do PDF de demonstração.

O teste usou um certificado fictício ("Dummy PDF file"), não um certificado final da ONG.
Continuam sem comprovação completa: fluxo hospedado sem cabo, fonte ampliada e cenários
de permissão negada no celular. [Situação e limites](../../../src/Entrega2/STATUS.md).
