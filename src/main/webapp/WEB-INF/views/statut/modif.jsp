<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="dto.ModifStatutDTO" %>
<%@ page import="model.Demande" %>
<%@ page import="model.Client" %>
<%@ page import="model.Commune" %>
<%@ page import="model.Statut" %>
<%@ page import="org.springframework.validation.BindingResult" %>
<%@ page import="org.springframework.validation.FieldError" %>

<%
    ModifStatutDTO demandeStatutForm = (ModifStatutDTO) request.getAttribute("modifStatutForm");
    BindingResult bindingResult = (BindingResult) request.getAttribute(BindingResult.MODEL_KEY_PREFIX + "modifStatutForm");
    List<Demande> demandes = (List<Demande>) request.getAttribute("demandes");
    List<Statut> statuts = (List<Statut>) request.getAttribute("statuts");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    String selectedDemande = demandeStatutForm != null && demandeStatutForm.getIdDemande() != null ? String.valueOf(demandeStatutForm.getIdDemande()) : "";
    String selectedStatut = demandeStatutForm != null && demandeStatutForm.getIdStatut() != null ? String.valueOf(demandeStatutForm.getIdStatut()) : "";
    String dateStatutValue = demandeStatutForm != null && demandeStatutForm.getDate() != null ? demandeStatutForm.getDate().format(formatter) : "";
    String observationValue = demandeStatutForm != null && demandeStatutForm.getObservation() != null ? demandeStatutForm.getObservation() : "";
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Nouveau statut</title>
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
        <h1>Modification de statut</h1>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error" style="color: red; border: 1px solid red; padding: 10px;">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form action="/statut/modif" method="post">

            <div class="field">
                <label for="idDemande">Demande</label>
                <select name="idDemande" id="idDemande">
                    <option value="">— Sélectionner une demande —</option>
                    <%
                        if (demandes != null) {
                            for (Demande demande : demandes) {
                    %>
                    <option value="<%= demande.getId() %>" <%= String.valueOf(demande.getId()).equals(selectedDemande) ? "selected" : "" %>><%= demande.getReference() %></option>
                    <%
                            }
                        }
                    %>
                </select>
                <% if (bindingResult != null) {
                    for (FieldError error : bindingResult.getFieldErrors("idDemande")) {
                %>
                    <span class="err"><%= error.getDefaultMessage() %></span>
                <%      }
                   } %>
            </div>

            <div class="field">
                <label for="date">Date du statut</label>
                <input name="date" id="date" type="datetime-local" value="<%= dateStatutValue %>" />
                <% if (bindingResult != null) {
                    for (FieldError error : bindingResult.getFieldErrors("date")) {
                %>
                    <span class="err"><%= error.getDefaultMessage() %></span>
                <%      }
                   } %>
            </div>
            <div class="field">
                <label for="observation">Observation</label>
                <textarea name="observation" id="observation" placeholder="Ex: Statut mis à jour"><%= observationValue %></textarea>
                <% if (bindingResult != null) {
                    for (FieldError error : bindingResult.getFieldErrors("observation")) {
                %>
                    <span class="err"><%= error.getDefaultMessage() %></span>
                <%      }
                   } %>
            </div>

            <div class="field">
                <label for="idStatut">Statut</label>
                <select name="idStatut" id="idStatut">
                    <option value="">— Sélectionner un statut —</option>
                    <%
                        if (statuts != null) {
                            for (Statut statut : statuts) {
                    %>
                    <option value="<%= statut.getId() %>" <%= String.valueOf(statut.getId()).equals(selectedStatut) ? "selected" : "" %>><%= statut.getLibelle() %></option>
                    <%
                            }
                        }
                    %>
                </select>
                <% if (bindingResult != null) {
                    for (FieldError error : bindingResult.getFieldErrors("idStatut")) {
                %>
                    <span class="err"><%= error.getDefaultMessage() %></span>
                <%      }
                   } %>
            </div>

            <script>
                document.addEventListener('DOMContentLoaded', function () {
                    const demandeSelect = document.getElementById('idDemande');
                    const statutSelect = document.getElementById('idStatut');
                    const dateInput = document.getElementById('date');
                    const observationInput = document.getElementById('observation');
                    if (!demandeSelect || !statutSelect) return;

                    function resetStatutOptions() {
                        statutSelect.innerHTML = '<option value="">— Sélectionner un statut —</option>';
                    }

                    function loadStatutsExistants(idDemande, selectedId) {
                        return fetch('/statut/existants/' + idDemande)
                            .then(r => r.json())
                            .then(items => {
                                resetStatutOptions();
                                items.forEach(item => {
                                    const option = document.createElement('option');
                                    option.value = item.idStatut;
                                    option.textContent = item.libelle;
                                    if (selectedId && String(item.idStatut) === String(selectedId)) {
                                        option.selected = true;
                                    }
                                    statutSelect.appendChild(option);
                                });
                            });
                    }

                    function loadDataForStatut(idDemande, idStatut) {
                        if (!idDemande || !idStatut) return;
                        fetch('/statut/current/' + idDemande + '/' + idStatut)
                            .then(r => r.json())
                            .then(data => {
                                dateInput.value = data.date || '';
                                observationInput.value = data.observation || '';
                            }).catch(err => console.error(err));
                    }

                    demandeSelect.addEventListener('change', function () {
                        const idDemande = this.value;
                        if (!idDemande) {
                            resetStatutOptions();
                            dateInput.value = '';
                            observationInput.value = '';
                            return;
                        }

                        fetch('/statut/current/' + idDemande)
                            .then(r => r.json())
                            .then(current => loadStatutsExistants(idDemande, current.idStatut)
                                .then(() => {
                                    if (current.idStatut) {
                                        statutSelect.value = current.idStatut;
                                        loadDataForStatut(idDemande, current.idStatut);
                                    } else {
                                        dateInput.value = '';
                                        observationInput.value = '';
                                    }
                                }))
                            .catch(err => console.error(err));
                    });

                    statutSelect.addEventListener('change', function () {
                        loadDataForStatut(demandeSelect.value, this.value);
                    });

                    if (demandeSelect.value) {
                        loadStatutsExistants(demandeSelect.value, statutSelect.value)
                            .then(() => {
                                if (statutSelect.value) {
                                    loadDataForStatut(demandeSelect.value, statutSelect.value);
                                }
                            });
                    }
                });
            </script>

            <div class="actions">
                <button type="submit" class="btn btn-primary">Enregistrer la demande</button>
                <a href="/demandes" class="btn btn-secondary">Annuler</a>
            </div>

        </form>
    </div>
</div>
</body>
</html>