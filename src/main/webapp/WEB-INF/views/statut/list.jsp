<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.*" %>
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
    <a href="/demandes" class="btn">Retour aux demandes</a>
    <a href="/statut/nouveau" class="btn">Ajouter un statut</a>
    <a href="/statut/modif" class="btn">Modifier un statut</a>

    <table class="table">
        <thead>
        <tr>
            <th>Demande</th>
            <th>Date</th>
            <th>Observations</th>
            <th>Statut</th>
            <th>DT (min)</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<DemandeStatut> historiqueStatuts = (List<DemandeStatut>) request.getAttribute("historiqueStatuts");
            if (historiqueStatuts != null) {
                for (DemandeStatut ds : historiqueStatuts) {
                    Demande d = ds.getDemande();
        %>
            <tr>
                <td><%= d != null ? d.getReference() : "" %></td>
                <td><%= ds.getDateStatut() %></td>
                <td><%= (ds.getObservation() != null) ? ds.getObservation() : "" %></td>
                <td><%= ds.getStatut() != null ? ds.getStatut().getLibelle() : "" %></td>
                <td><%= ds.getDureeTravailleeMinutes() != null ? ds.getDureeTravailleeMinutes() : "" %></td>
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