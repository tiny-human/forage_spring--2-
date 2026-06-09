// ─── Index courant pour nommer les champs ────────────────
// Initialisé selon le nombre de lignes déjà présentes (ré-affichage après erreur)
let nextIndex = 0;

// ─── Initialisation globale ───────
document.addEventListener('DOMContentLoaded', () => {
    nextIndex = document.querySelectorAll('#detailsBody .detail-row').length;

    const df = document.getElementById('dateDevis');
    if (df && !df.value) {
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        df.value = now.toISOString().slice(0, 16);
    }

    // Si aucune ligne n'existe encore, en ajouter une vide
    if (nextIndex === 0) ajouterLigne();

    // Attacher les événements oninput aux lignes existantes (ré-affichage)
    document.querySelectorAll('#detailsBody .detail-row').forEach(row => {
        row.querySelectorAll('.qte-input, .pu-input').forEach(input => {
            input.addEventListener('input', () => recalculerLigne(input));
        });
    });

    // Calculer le total initial (ré-affichage après erreur)
    recalculerTotal();
});

// ─── Ajoute une ligne vide au tableau ────────────────────
function ajouterLigne() {
    const tbody = document.getElementById('detailsBody');
    if(!tbody) return;
    
    const tr = document.createElement('tr');
    tr.className = 'detail-row';
    tr.innerHTML = `
        <td class="col-libelle">
            <input type="text"
                   name="details[${nextIndex}].libelle"
                   placeholder="Ex: Tubage PVC"/>
        </td>
        <td class="col-qte">
            <input type="number" step="0.01" min="0"
                   name="details[${nextIndex}].quantite"
                   class="qte-input" placeholder="0"
                   oninput="recalculerLigne(this)"/>
        </td>
        <td class="col-pu">
            <input type="number" step="0.01" min="0"
                   name="details[${nextIndex}].pu"
                   class="pu-input" placeholder="0"
                   oninput="recalculerLigne(this)"/>
        </td>
        <td class="col-total">
            <span class="row-total">0</span>
        </td>
        <td class="col-action">
            <button type="button" class="btn-remove"
                    onclick="supprimerLigne(this)" title="Supprimer">✕</button>
        </td>`;
    tbody.appendChild(tr);
    nextIndex++;
    
    // Focus sur le champ libellé de la nouvelle ligne
    const newLibelle = tr.querySelector('input[type=text]');
    if(newLibelle) newLibelle.focus();
}

// ─── Supprime une ligne et réindexe ──────────────────────
function supprimerLigne(btn) {
    const rows = document.querySelectorAll('#detailsBody .detail-row');
    if (rows.length <= 1) {
        alert('Le devis doit contenir au moins une ligne.');
        return;
    }
    btn.closest('tr').remove();
    reindexer();   // réindexe après suppression pour éviter les trous
    recalculerTotal();
}

// ─── Réindexe tous les champs après suppression ──────────
// Important : Spring MVC exige des indices CONTINUS (0, 1, 2, ...)
function reindexer() {
    const rows = document.querySelectorAll('#detailsBody .detail-row');
    rows.forEach((row, i) => {
        row.querySelectorAll('input').forEach(input => {
            input.name = input.name.replace(/details\[\d+\]/, `details[${i}]`);
        });
    });
    nextIndex = rows.length;
}

// ─── Recalcule le total d'une ligne ──────────────────────
function recalculerLigne(changedInput) {
    const row   = changedInput.closest('tr');
    const qte   = parseFloat(row.querySelector('.qte-input').value) || 0;
    const pu    = parseFloat(row.querySelector('.pu-input').value)  || 0;
    const total = qte * pu;
    row.querySelector('.row-total').textContent = formater(total);
    recalculerTotal();
}

// ─── Recalcule le total général ───────────────────────────
function recalculerTotal() {
    let grand = 0;
    document.querySelectorAll('#detailsBody .detail-row').forEach(row => {
        const qte = parseFloat(row.querySelector('.qte-input')?.value) || 0;
        const pu  = parseFloat(row.querySelector('.pu-input')?.value)  || 0;
        const t   = qte * pu;
        row.querySelector('.row-total').textContent = formater(t);
        grand += t;
    });
    const formatted = formater(grand) + ' Ar';
    
    const mt = document.getElementById('montantTotal');
    const tf = document.getElementById('totalFooter');
    if(mt) mt.textContent = formatted;
    if(tf) tf.textContent  = formatted;
}

// ─── Formatte un nombre avec séparateurs de milliers ─────
function formater(n) {
    return new Intl.NumberFormat('fr-FR').format(Math.round(n));
}