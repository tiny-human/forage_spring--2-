<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.DevisDetail" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Details devis</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Details du devis</h1>
        <a href="/devis" class="btn">Retour à la liste des devis</a>
        <table class="table">
            <thead>
                <tr>
                    <th>Demande liee</th>
                    <th>Description</th>
                    <th>Quantité</th>
                    <th>Prix unitaire</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
                <% List<DevisDetail> details = (List<DevisDetail>) request.getAttribute("devisDetail");
                    if (details != null) {
                        for (DevisDetail d : details) {%>
                            <tr>
                                <td><%= d.getDevis().getDemande().getReference() %></td>
                                <td><%= d.getMateriel() %></td>
                                <td><%= d.getQuantite() %></td>
                                <td><%= d.getPrixUnitaire() %></td>
                                <td><%= d.getSousTotal() %></td>
                            </tr>
                <% } } %>
            </tbody>
        </table>
    </div>
</body>
</html>