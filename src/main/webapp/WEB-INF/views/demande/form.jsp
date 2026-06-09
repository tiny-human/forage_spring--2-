<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="dto.DemandeFormDTO" %>
<%@ page import="model.Client" %>
<%@ page import="model.Commune" %>
<%@ page import="org.springframework.validation.BindingResult" %>
<%@ page import="org.springframework.validation.FieldError" %>

<%
    DemandeFormDTO demandeForm = (DemandeFormDTO) request.getAttribute("demandeForm");
    BindingResult bindingResult = (BindingResult) request.getAttribute(BindingResult.MODEL_KEY_PREFIX + "demandeForm");
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    List<Commune> communes = (List<Commune>) request.getAttribute("communes");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    String selectedClient = demandeForm != null && demandeForm.getIdClient() != null ? String.valueOf(demandeForm.getIdClient()) : "";
    String selectedCommune = demandeForm != null && demandeForm.getIdCommune() != null ? String.valueOf(demandeForm.getIdCommune()) : "";
    String dateDemandeValue = demandeForm != null && demandeForm.getDateDemande() != null ? demandeForm.getDateDemande().format(formatter) : "";
    String lieuValue = demandeForm != null && demandeForm.getLieu() != null ? demandeForm.getLieu() : "";
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Nouvelle demande de forage</title>
    <link rel="stylesheet" href="/css/style.css">
    <style>
        .err { color: red; font-size: 0.8em; display: block; margin-top: 5px; }
        .field { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
    </style>
</head>
<body>
<div class="container small">
    <div class="card">
        <h1>Nouvelle demande de forage</h1>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error" style="color: red; border: 1px solid red; padding: 10px;">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form action="/demandes" method="post">

            <div class="field">
                <label for="idClient">Client</label>
                <select name="idClient" id="idClient">
                    <option value="">— Sélectionner un client —</option>
                    <%
                        if (clients != null) {
                            for (Client client : clients) {
                    %>
                    <option value="<%= client.getId() %>" <%= String.valueOf(client.getId()).equals(selectedClient) ? "selected" : "" %>><%= client.getNom() %></option>
                    <%
                            }
                        }
                    %>
                </select>
                <% if (bindingResult != null) {
                    for (FieldError error : bindingResult.getFieldErrors("idClient")) {
                %>
                    <span class="err"><%= error.getDefaultMessage() %></span>
                <%      }
                   } %>
            </div>

            <div class="field">
                <label for="dateDemande">Date de la demande</label>
                <input name="dateDemande" id="dateDemande" type="datetime-local" value="<%= dateDemandeValue %>" />
                <% if (bindingResult != null) {
                    for (FieldError error : bindingResult.getFieldErrors("dateDemande")) {
                %>
                    <span class="err"><%= error.getDefaultMessage() %></span>
                <%      }
                   } %>
            </div>

            <div class="field">
                <label for="lieu">Lieu du forage</label>
                <input name="lieu" id="lieu" placeholder="Ex: Terrain Sud" value="<%= lieuValue %>"/>
                <% if (bindingResult != null) {
                    for (FieldError error : bindingResult.getFieldErrors("lieu")) {
                %>
                    <span class="err"><%= error.getDefaultMessage() %></span>
                <%      }
                   } %>
            </div>

            <div class="field">
                <label for="idCommune">Commune</label>
                <select name="idCommune" id="idCommune">
                    <option value="">— Sélectionner une commune —</option>
                    <%
                        if (communes != null) {
                            for (Commune commune : communes) {
                    %>
                    <option value="<%= commune.getId() %>" <%= String.valueOf(commune.getId()).equals(selectedCommune) ? "selected" : "" %>><%= commune.getNom() %></option>
                    <%
                            }
                        }
                    %>
                </select>
                <% if (bindingResult != null) {
                    for (FieldError error : bindingResult.getFieldErrors("idCommune")) {
                %>
                    <span class="err"><%= error.getDefaultMessage() %></span>
                <%      }
                   } %>
            </div>

            <div class="actions">
                <button type="submit" class="btn btn-primary">Enregistrer la demande</button>
                <a href="/demandes" class="btn btn-secondary">Annuler</a>
            </div>

        </form>
    </div>
</div>
</body>
</html>