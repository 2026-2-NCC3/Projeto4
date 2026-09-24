// Controller de agenda.
// Responsável por listar os encontros/aulas do aluno logado.
// A RLS já garante que só vêm os encontros do próprio aluno; aqui só
// aplicamos ordenação e o filtro opcional de data.

/**
 * GET /api/agenda
 * Retorna os encontros do aluno logado (view my_agenda), ordenados por
 * activity_date e depois start_time, ambos crescentes.
 * Aceita o query param opcional "from" (YYYY-MM-DD) para filtrar
 * activity_date >= from.
 */
async function listAgenda(req, res) {
  try {
    let query = req.supabase
      .from('my_agenda')
      .select(
        'activity_date, start_time, end_time, course_title, title, location, attended'
      )
      .order('activity_date', { ascending: true })
      .order('start_time', { ascending: true });

    const { from } = req.query;
    if (from) {
      query = query.gte('activity_date', from);
    }

    const { data, error } = await query;

    if (error) {
      return res.status(400).json({ error: error.message });
    }

    res.json(data);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
}

module.exports = { listAgenda };
