const express = require('express');

const app = express();

app.use(express.json());

app.get('/',(req, res)=>{
    res.send('Backend funcionand');
});

app.post('/cadastro',(req, res)=>{

    const nome = req.body.nome;
    const email = req.body.email;
    const senha = req.body.senha;

    console.log('Nome:', nome);
    console.log('E-mail:', email);
    console.log('Senha:', senha);

    res.json({
        mensagem: 'Cadastro recebido',
        nome: nome,
        email: email
    });
});

app.post('/login',(req, res)=>{

    const email = req.body.email;
    const senha = req.body.senha;

    console.log('E-mail:', email);
    console.log('Senha:', senha);

    res.json({
        mensagem:'Login recebido', email: email
    });
});

app.listen(8000, ()=>{
    console.log('Servidor ta rodando na porta 8000');
});