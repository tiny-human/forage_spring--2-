<?php

require_once __DIR__ . '/../lib/alertes.php';

header('Content-Type: application/json; charset=utf-8');

try {
    $demandeParam = trim($_GET['demande'] ?? '');

    if ($demandeParam === '') {
        // Aucun filtre → toutes les demandes avec leurs alertes
        echo json_encode(appelerApiSpringTout(), JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    } else {
        // Demande précise (id ou référence)
        echo json_encode(appelerApiSpring($demandeParam), JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    }

} catch (Throwable $e) {
    http_response_code(500);
    echo json_encode([
        'ok'      => false,
        'message' => 'Erreur serveur : ' . $e->getMessage(),
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}