// src/routes/perfil.routes.js
const express = require('express');
const router = express.Router();
const { getPerfilAluno } = require('../controllers/perfil.controller');
const authMiddleware = require('../middleware/auth'); 

router.get('/', authMiddleware, getPerfilAluno);

module.exports = router;