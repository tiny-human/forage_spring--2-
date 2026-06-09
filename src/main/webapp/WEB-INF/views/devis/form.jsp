<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="dto.DevisFormDTO" %>
<%@ page import="dto.DevisDetailDTO" %>
<%@ page import="model.TypeDevis" %>
<%@ page import="model.*" %>
<%@ page import="org.springframework.validation.BindingResult" %>
<%@ page import="org.springframework.validation.ObjectError" %>
<%
    DevisFormDTO devisForm = (DevisFormDTO) request.getAttribute("devisForm");
    BindingResult bindingResult = (BindingResult) request.getAttribute(BindingResult.MODEL_KEY_PREFIX + "devisForm");
    List<TypeDevis> types = (List<TypeDevis>) request.getAttribute("types");
    Demande demande = (Demande) request.getAttribute("demande");
    NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    String dateDevisValue = devisForm != null && devisForm.getDateDevis() != null ? devisForm.getDateDevis().format(formatter) : "";
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Nouveau devis</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">

    <h1>Nouveau devis</h1>

    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error">${error}</div>
    <% } %>
    <form id="devisForm" action="/devis" method="post">
        <input type="hidden" name="idDemande" value="<%= devisForm != null && devisForm.getIdDemande() != null ? devisForm.getIdDemande() : "" %>"/>
        <% if (bindingResult != null && bindingResult.hasErrors()) { %>
            <div class="alert alert-error">
                <ul>
                    <% for (ObjectError err : bindingResult.getAllErrors()) { %>
                        <li><%= err.getDefaultMessage() %></li>
                    <% } %>
                </ul>
            </div>
        <% } %>

        <div class="card">
            <h2>Demande liee</h2>
            <div class="demande-info">
                <div class="di">
                    <div class="di-label">Reference</div>
                    <div class="di-val"><%= demande != null ? demande.getReference() : "" %></div>
                </div>
                <div class="di">
                    <div class="di-label">Client</div>
                    <div class="di-val"><%= demande != null && demande.getClient() != null ? demande.getClient().getNom() : "" %></div>
                </div>
                <div class="di">
                    <div class="di-label">Lieu</div>
                    <div class="di-val"><%= demande != null ? demande.getLieu() : "" %></div>
                </div>
                <div class="di">
                    <div class="di-label">Commune</div>
                    <div class="di-val"><%= demande != null && demande.getCommune() != null ? demande.getCommune().getNom() : "" %></div>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Informations du devis</h2>
            <div class="grid-2">

                <div class="field">
                    <label for="dateDevis">Date du devis</label>
                    <input type="datetime-local" id="dateDevis" name="dateDevis"
                              value="<%= dateDevisValue %>"
                           required/>
                    <%--date/heure actuelle via JS si vide --%>
                </div>

                <div class="field">
                    <label for="idType">Type de devis</label>
                    <select id="idType" name="idType" required>
                        <option value="">— Choisir un type —</option>
                        <%
                            if (types != null) {
                                for (TypeDevis t : types) {
                                    boolean selected = devisForm != null && devisForm.getIdType() != null && devisForm.getIdType().equals(t.getId());
                        %>
                            <option value="<%= t.getId() %>" <%= selected ? "selected" : "" %>><%= t.getLibelle() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                <div class="field">
                    <label for="observations">Observations</label>
                    <textarea id="observations" name="observations" rows="4" placeholder="Observations sur le devis..."></textarea>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Détails du devis</h2>

            <table class="details-table">
                <thead>
                    <tr>
                        <th class="col-libelle">Libelle</th>
                        <th class="col-qte">Quantite</th>
                        <th class="col-pu">Prix unitaire (Ar)</th>
                        <th class="col-total">Total (Ar)</th>
                        <th class="col-action"></th>
                    </tr>
                </thead>
                <tbody id="detailsBody">
                    <%--
                        Si le formulaire est ré-affiché après erreur,
                        on restitue les lignes déjà saisies.
                        Sinon (première ouverture), le JS insère une ligne vide.
                    --%>
                    <%
                        List<DevisDetailDTO> details = devisForm != null ? devisForm.getDetails() : null;
                        if (details != null && !details.isEmpty()) {
                            for (int i = 0; i < details.size(); i++) {
                                DevisDetailDTO d = details.get(i);
                    %>
                                <tr class="detail-row">
                                    <td class="col-libelle">
                                        <input type="text"
                                               name="details[<%= i %>].libelle"
                                               value="<%= d.getLibelle() != null ? d.getLibelle() : "" %>"
                                               placeholder="Ex: Forage 80m"/>
                                    </td>
                                    <td class="col-qte">
                                        <input type="number" step="0.01" min="0"
                                               name="details[<%= i %>].quantite"
                                               value="<%= d.getQuantite() != null ? d.getQuantite() : "" %>"
                                               class="qte-input" placeholder="0"/>
                                    </td>
                                    <td class="col-pu">
                                        <input type="number" step="0.01" min="0"
                                               name="details[<%= i %>].pu"
                                               value="<%= d.getPu() != null ? d.getPu() : "" %>"
                                               class="pu-input" placeholder="0"/>
                                    </td>
                                    <td class="col-total">
                                        <span class="row-total">
                                            <%= (d.getQuantite() != null && d.getPu() != null) ? numberFormat.format(Math.round(d.getQuantite() * d.getPu())) : "0" %>
                                        </span>
                                    </td>
                                    <td class="col-action">
                                        <button type="button" class="btn-remove"
                                                onclick="supprimerLigne(this)" title="Supprimer">✕</button>
                                    </td>
                                </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>

            <button type="button" class="btn-add" onclick="ajouterLigne()">
                + Ajouter une ligne
            </button>

            <div class="total-row">
                <div class="total-box">
                    <div class="tl">Montant total</div>
                    <div class="tv" id="montantTotal">0 Ar</div>
                </div>
            </div>
        </div>

    </form>

    <div class="sticky-footer">
        <button type="submit" form="devisForm" class="btn btn-primary">
            Enregistrer le devis
        </button>
        <a href="/demandes" class="btn btn-secondary">Annuler</a>
        <span style="margin-left:auto; font-size:.82rem; color:#94a3b8;">
            Total : <strong id="totalFooter">0 Ar</strong>
        </span>
    </div>

</div>

<script src="/js/devis.js"></script>
</body>
</html>
