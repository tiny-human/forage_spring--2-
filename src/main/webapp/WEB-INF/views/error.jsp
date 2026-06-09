<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Erreur</title>
    <link rel="stylesheet" href="/css/style.css">
    <style>
        body { display: flex; align-items: center; justify-content: center; min-height: 100vh; }
        h1   { font-size: 1.1rem; color: #dc2626; margin-bottom: .75rem; }
        p    { color: #64748b; font-size: .9rem; margin-bottom: 1.5rem; }
        a    { color: #2563eb; text-decoration: none; font-size: .9rem; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="box">
    <h1>Une erreur est survenue</h1>
    <p>${not empty errorMessage ? errorMessage : 'Erreur inattendue. Veuillez réessayer.'}</p>
    <a href="/demandes">← Retour à la liste</a>
</div>
</body>
</html>
