# AMIO_detect
Projet AMIO

Notre application est capable de:
- Afficher la liste des motes avec le nom du mote, la salle correspondante, la valeur du capteur de lumière et si cela correspond à une lumière allumée ou éteinte (+ coloration en vert ou rouge en fonction de cette valeur), la date et l'heure de dernière mise à jour.
- Un démarrage du service de mise à jour (actualisation en fond toutes les 20 secondes) lorsque le téléphone démarre.
- La possibilité de mettre à jour manuellement les motes en cliquant sur l'onglet contenant la liste des motes.
- L'envoi des notifications contenant le nom du ou des motes détectant une lumière en fonction des heures et jours choisis dans les préférences ainsi que l'heure de dernière mise à jour (Pas de notification si la valeur est vieille de plus d'une heure). Les notifications et les mails ne sont envoyés qu'une fois par détection et non à chaque mise à jour des valeurs et si nous ne sommes pas deja sur la page d'accueil.
- L'affichage du plan du bâtiment.

Dans les préférences:
- L'activation ou la désactivation à la volée du service de mise à jour
- L'activation ou la désactivation de l'envoi des notifications
- Le réglage des préférences des plages horaire de la semaine et du week end
- Le réglage des préférences des jours qui doivent être considérés comme le week end
- Le réglage de l'adresse mail auquel doivent être envoyé les mails

SERNIT Jeremy, PAOLI Cedric
