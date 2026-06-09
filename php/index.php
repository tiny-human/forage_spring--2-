<?php

require_once __DIR__ . '/lib/alertes.php';

// Récupération des données

$demande = trim($_GET['demande'] ?? '');
$erreur  = null;

// Liste des demandes pour le <select> (non bloquant si Spring est down)
$listeDemandes = obtenirListeDemandes();

// Mode "tout afficher" (pas de filtre) ou mode "une demande"
$modeTout    = ($demande === '');
$alertesUne  = [];   // mode une demande
$toutesData  = [];   // mode tout : tableau de {demande, statutCourant, alertes}

try {
    if ($modeTout) {
        $reponse    = appelerApiSpringTout();
        $toutesData = $reponse['demandes'] ?? [];
    } else {
        $reponse   = appelerApiSpring($demande);
        $alertesUne = $reponse['alertes'] ?? [];
    }
} catch (Throwable $e) {
    $erreur = $e->getMessage();
}

//Helpers

function h(?string $v): string
{
    return htmlspecialchars((string) $v, ENT_QUOTES, 'UTF-8');
}

/**
 * Retourne un badge HTML coloré selon la valeur de couleur Spring.
 */
function badgeCouleur(string $couleur): string
{
    $map = [
        'rouge'  => '#e53e3e',
        'orange' => '#dd6b20',
        'jaune'  => '#d69e2e',
        'vert'   => '#38a169',
        'bleu'   => '#3182ce',
    ];
    $bg  = $map[strtolower($couleur)] ?? '#718096';
    $txt = h($couleur);
    return "<span style=\"background:{$bg};color:#fff;padding:2px 10px;border-radius:12px;"
         . "font-size:.82em;font-weight:600;\">{$txt}</span>";
}

function ligneAlerte(array $alerte): string
{
    ob_start(); ?>
    <tr>
        <td><?= h((string)($alerte['idDemande'] ?? '')) ?></td>
        <td><?= h($alerte['statutSource'] ?? '') ?></td>
        <td><?= h($alerte['statutCible']  ?? '') ?></td>
        <td><?= h((string)($alerte['dureeMinutes'] ?? '')) ?> min</td>
        <td><?= h((string)($alerte['minutesEcoulees'] ?? '')) ?> min</td>
        <td><?= badgeCouleur((string)($alerte['couleur'] ?? '')) ?></td>
    </tr>
    <?php return ob_get_clean();
}
?>
<!doctype html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Alertes demandes</title>
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        body   { font-family: system-ui, sans-serif; background: #f7f8fa; color: #2d3748; padding: 24px; }
        .wrap  { max-width: 1100px; margin: auto; }
        h1     { font-size: 1.5rem; margin-bottom: 20px; }
        h2     { font-size: 1.1rem; margin-bottom: 10px; color: #2b6cb0; }

        /* Formulaire filtre */
        .filtre { background: #fff; border: 1px solid #e2e8f0; border-radius: 8px;
                  padding: 18px 22px; margin-bottom: 24px; display: flex; gap: 12px; align-items: flex-end; flex-wrap: wrap; }
        .filtre label { font-size: .9rem; font-weight: 600; display: block; margin-bottom: 4px; }
        .filtre select { padding: 8px 12px; border: 1px solid #cbd5e0; border-radius: 6px;
                         font-size: .95rem; min-width: 220px; }
        .filtre button { padding: 8px 20px; background: #2b6cb0; color: #fff; border: none;
                         border-radius: 6px; font-size: .95rem; cursor: pointer; }
        .filtre button:hover { background: #2c5282; }
        .filtre a.reset { font-size: .85rem; color: #718096; text-decoration: none; align-self: center; }
        .filtre a.reset:hover { text-decoration: underline; }

        /* Alertes */
        .card  { background: #fff; border: 1px solid #e2e8f0; border-radius: 8px;
                 padding: 18px 22px; margin-bottom: 20px; }
        .card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
        .badge-statut { background: #ebf8ff; color: #2b6cb0; padding: 3px 10px;
                        border-radius: 12px; font-size: .82em; font-weight: 600; }

        table  { width: 100%; border-collapse: collapse; font-size: .9rem; }
        thead tr { background: #edf2f7; }
        th, td { padding: 8px 12px; text-align: left; border-bottom: 1px solid #e2e8f0; }
        tbody tr:hover { background: #f7fafc; }

        .ok    { color: #38a169; font-style: italic; padding: 8px 0; }
        .err   { background: #fff5f5; border: 1px solid #feb2b2; border-radius: 6px;
                 padding: 12px 16px; color: #c53030; margin-bottom: 20px; }
    </style>
</head>
<body>
<div class="wrap">
    <h1>Alertes demandes</h1>

    <!--Formulaire filtre-->
    <div class="filtre">
        <form method="get" action="" style="display:flex;gap:12px;align-items:flex-end;flex-wrap:wrap;">
            <div>
                <label for="sel-demande">Demande</label>
                <select name="demande" id="sel-demande">
                    <option value="">— Toutes les demandes —</option>
                    <?php foreach ($listeDemandes as $item): ?>
                        <?php
                            $val = h((string)($item['id'] ?? ''));
                            $ref = h((string)($item['reference'] ?? ''));
                            $sel = ($demande === (string)($item['id'] ?? '')
                                 || $demande === (string)($item['reference'] ?? ''))
                                 ? 'selected' : '';
                        ?>
                        <option value="<?= $val ?>" <?= $sel ?>><?= $ref ?></option>
                    <?php endforeach; ?>
                </select>
            </div>
            <button type="submit">Filtrer</button>
            <?php if (!$modeTout): ?>
                <a class="reset" href="?">Réinitialiser</a>
            <?php endif; ?>
        </form>
    </div>

    <!-- Erreur éventuelle -->
    <?php if ($erreur): ?>
        <div class="err"><strong>Erreur :</strong> <?= h($erreur) ?></div>
    <?php endif; ?>

    <!-- Mode : une demande précise-->
    <?php if (!$modeTout && !$erreur): ?>
        <div class="card">
            <h2>Alertes — demande <?= h($demande) ?></h2>
            <?php if (!empty($alertesUne)): ?>
                <table>
                    <thead>
                        <tr>
                            <th>ID demande</th>
                            <th>Statut source</th>
                            <th>Statut cible</th>
                            <th>Seuil</th>
                            <th>Durée écoulée</th>
                            <th>Couleur</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($alertesUne as $a): echo ligneAlerte($a); endforeach; ?>
                    </tbody>
                </table>
            <?php else: ?>
                <p class="ok">Aucune alerte déclenchée pour cette demande.</p>
            <?php endif; ?>
        </div>
    <?php endif; ?>

    <!--Mode : toutes les demandes  -->
    <?php if ($modeTout && !$erreur): ?>
        <?php if (empty($toutesData)): ?>
            <div class="card"><p class="ok">Aucune demande trouvée.</p></div>
        <?php endif; ?>

        <?php foreach ($toutesData as $bloc):
            $alertes   = $bloc['alertes']       ?? [];
            $ref       = h((string)($bloc['reference']     ?? '—'));
            $client    = h((string)($bloc['client']        ?? ''));
            $libStatut = h((string)($bloc['statutCourant'] ?? '—'));
        ?>
        <div class="card">
            <div class="card-header">
                <h2>
                    Demande <?= $ref ?>
                    <?php if ($client): ?><span style="font-weight:400;color:#718096;"> — <?= $client ?></span><?php endif; ?>
                </h2>
                <span class="badge-statut"><?= $libStatut ?></span>
            </div>

            <?php if (!empty($alertes)): ?>
                <table>
                    <thead>
                        <tr>
                            <th>ID demande</th>
                            <th>Statut source</th>
                            <th>Statut cible</th>
                            <th>Seuil</th>
                            <th>Durée écoulée</th>
                            <th>Couleur</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($alertes as $a): echo ligneAlerte($a); endforeach; ?>
                    </tbody>
                </table>
            <?php else: ?>
                <p class="ok">Aucune alerte déclenchée.</p>
            <?php endif; ?>
        </div>
        <?php endforeach; ?>
    <?php endif; ?>

</div>
</body>
</html>
