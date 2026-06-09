<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.Demande" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des demandes</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <h1>Demandes</h1>
    <a href="/demandes/nouveau" class="btn">Nouvelle demande</a>
    <a href="/devis" class="btn">Voir tous les devis</a>
    <a href="/statut">Voir tous les statuts</a>

    <table class="table">
        <thead>
        <tr>
            <th>Réf</th>
            <th>Client</th>
            <th>Date</th>
            <th>Lieu</th>
            <th>Commune</th>
            <th>Statut courant</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Demande> demandes = (List<Demande>) request.getAttribute("demandes");
            Map<Long, String> statutCourant = (Map<Long, String>) request.getAttribute("statutCourant");
            if (demandes != null) {
                for (Demande d : demandes) {
        %>
            <tr>
                <td><%= d.getReference() %></td>
                <td><%= d.getClient() != null ? d.getClient().getNom() : "" %></td>
                <td><%= d.getDateDemande() %></td>
                <td><%= d.getLieu() %></td>
                <td><%= d.getCommune() != null ? d.getCommune().getNom() : "" %></td>
                <td><%= statutCourant != null ? statutCourant.get(d.getId()) : "" %></td>
                <td>
                    <a href="/devis/nouveau/<%= d.getId() %>">Créer devis</a>
                </td>
            </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>