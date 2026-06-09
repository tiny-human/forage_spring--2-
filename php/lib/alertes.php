<?php

function configPhp(): array
{
    return require __DIR__ . '/../config.php';
}

function construireUrlSpring(string $demande): string
{
    $cfg  = configPhp();
    $base = rtrim($cfg['spring_base_url'] ?? 'http://127.0.0.1:8085', '/');
    return $base . '/api/demandes/alertes?demande=' . rawurlencode($demande);
}

function construireUrlSpringTout(): string
{
    $cfg  = configPhp();
    $base = rtrim($cfg['spring_base_url'] ?? 'http://127.0.0.1:8085', '/');
    return $base . '/api/demandes/toutes-alertes';
}

function construireUrlListeDemandes(): string
{
    $cfg  = configPhp();
    $base = rtrim($cfg['spring_base_url'] ?? 'http://127.0.0.1:8085', '/');
    return $base . '/api/demandes';
}

/**
 * Exécute un appel HTTP GET vers l'URL donnée.
 * Retourne le tableau PHP décodé depuis le JSON.
 *
 * @throws RuntimeException en cas d'erreur réseau ou JSON invalide
 */
function httpGet(string $url): array
{
    $cfg     = configPhp();
    $timeout = (int) ($cfg['spring_timeout'] ?? 15);

    if (function_exists('curl_init')) {
        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_TIMEOUT        => $timeout,
            CURLOPT_CONNECTTIMEOUT => 5,
        ]);
        $response   = curl_exec($ch);
        $statusCode = (int) curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $error      = curl_error($ch);
        curl_close($ch);

        if ($response === false) {
            throw new RuntimeException('Impossible d\'appeler l\'API Spring : ' . $error);
        }
    } else {
        $context = stream_context_create(['http' => [
            'timeout'       => $timeout,
            'ignore_errors' => true,
        ]]);
        $response   = @file_get_contents($url, false, $context);
        $statusCode = 0;
        if (isset($http_response_header[0])
            && preg_match('/\s(\d{3})\s/', $http_response_header[0], $m)) {
            $statusCode = (int) $m[1];
        }
        if ($response === false) {
            throw new RuntimeException('Impossible de récupérer la réponse de l\'API Spring.');
        }
    }

    $decoded = json_decode($response, true);
    if (!is_array($decoded)) {
        throw new RuntimeException('Réponse JSON invalide renvoyée par l\'API Spring.');
    }
    if ($statusCode >= 400) {
        throw new RuntimeException($decoded['message'] ?? 'Erreur API Spring (HTTP ' . $statusCode . ')');
    }

    return $decoded;
}

/** Alertes pour une demande précise (id ou référence). */
function appelerApiSpring(string $demande): array
{
    return httpGet(construireUrlSpring($demande));
}

/** Alertes de toutes les demandes. */
function appelerApiSpringTout(): array
{
    return httpGet(construireUrlSpringTout());
}

/** Liste id+référence de toutes les demandes (pour le <select>). */
function obtenirListeDemandes(): array
{
    try {
        return httpGet(construireUrlListeDemandes());
    } catch (Throwable $e) {
        return [];   // non bloquant : le <select> sera vide
    }
}