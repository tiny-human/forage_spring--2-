<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.Devis" %>
<%@ page import="model.DemandeStatut" %>
<!DOCTYPE html>
<html lang="f
<head>
    <meta charset="UTF-8">
    <title>Liste des devis</title>
    <link rel="stylesheet" href="/css/style.css">
</he
<body>
    <div class="container">
        <h1>Devis</h1>
        <a href="/demandes" class="btn">Retour aux demandes</a>
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Date devis</th>
                    <th>Type</th>
                    <th>Demande (réf)</th>
                    <th>Client</th>
                    <th>Prix total</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% List<Devis> devisList = (List<Devis>) request.getAttribute("devisList");
                    Map<Long, List<DemandeStatut>> historiqueStatuts = (Map<Long,List<DemandeStatut>>) request.getAttribute("historiqueStatuts");
                        if (devisList != null) {
                            for (Devis d : devisList) {%>
                                <tr>
                                    <td>
                                        <%= d.getId() %>
                                    </td>
                                    <td>
                                        <%= d.getDateDevis() %>
                                    </td>
                                    <td>
                                        <%= d.getType() !=null ? d.getType().getLibelle() : "" %>
                                    </td>
                                    <td>
                                        <%= d.getDemande() !=null ? d.getDemande().getReference() : ""
                                            %>
                                    </td>
                                    <td>
                                        <%= d.getDemande() !=null && d.getDemande().getClient() !=null ?
                                            d.getDemande().getClient().getNom() : "" %>
                                    </td>
                                    <td>
                                        <%= d.getPrixTotal() %>
                                    </td>
                                    <td><a href="/devis/details/<%= d.getId() %>">Voir details</a></td>
                                </tr>
                        <% } } %>
            </tbody>
        </table>
    </div>
</body>
</html>