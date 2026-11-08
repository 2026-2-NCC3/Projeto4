# Architecture review

project-reviewer /root/entrega2_base/architecture_check: WARN, permite implementação local.
Design SHA256: cfceabe6cd03dc066e8ca9a948e1bdd4d3eebc70e0e5c3ab2a32945a18f67873.
Exigir revogação PUBLIC/anon/authenticated INSERT/UPDATE/DELETE attendances; testar privilégios efetivos.
user_roles deve impedir aluno de autoatribuir privilégios. Definer com search_path vazio e grants restritos.
Cursos passa a exigir JWT; registrar breaking change e atualizar Android E2 junto.
Apply remoto e publicação continuam bloqueados até aprovação e preflight.
