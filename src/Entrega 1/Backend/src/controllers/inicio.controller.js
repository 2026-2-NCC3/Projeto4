// Controller responsável pelos dados da Página Inicial.

const getDadosInicio = async (req, res) => {
    try {
        // ID do usuário que foi validado pelo middleware de autenticação.
        const userId = req.userId;

        // Client do Supabase autenticado com o token do usuário.
        const supabase = req.supabase;

        // Busca o nome do usuário na tabela profiles.
        const { data: perfil, error: perfilError } = await supabase
            .from('profiles')
            .select('full_name')
            .eq('id', userId)
            .single();

        if (perfilError) {
            console.error('Erro ao buscar perfil:', perfilError);
            return res.status(400).json({
                erro: 'Não foi possível carregar os dados do usuário.'
            });
        }

        // Busca a inscrição do usuário em um curso.
        const { data: inscricao, error: inscricaoError } = await supabase
            .from('enrollments')
            .select('course_id')
            .eq('user_id', userId)
            .eq('status', 'enrolled')
            .single();

        if (inscricaoError) {
            console.error('Erro ao buscar inscrição:', inscricaoError);
            return res.status(400).json({
                erro: 'Não foi possível encontrar o curso do usuário.'
            });
        }

        // Busca o curso usando o course_id encontrado na inscrição.
        const { data: curso, error: cursoError } = await supabase
            .from('courses')
            .select('title')
            .eq('id', inscricao.course_id)
            .single();

        if (cursoError) {
            console.error('Erro ao buscar curso:', cursoError);
            return res.status(400).json({
                erro: 'Não foi possível carregar o curso do usuário.'
            });
        }

        // Retorna somente os dados que a Página Inicial precisa.
        return res.status(200).json({
            nome: perfil.full_name,
            curso: curso.title
        });

    } catch (error) {
        console.error('Erro interno ao carregar Página Inicial:', error);

        return res.status(500).json({
            erro: 'Erro interno no servidor.'
        });
    }
};

module.exports = {
    getDadosInicio
};