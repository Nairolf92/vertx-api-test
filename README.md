En premier j'ai essayé de lister les différentes routes de l'API pour me faire une idée du projet :
* lister les messages
GET /communication_diary/teachers/messages
* voir le détails d’un message
GET /communication_diary/teachers/messages/id
* diffuser un message
PUT /communication_diary/teachers/messages


En tant que parent :
* lister les messages
GET /communication_diary/parents/messages
* voir le détail d’un message
GET /communication_diary/parents/messages/id
* confirmer la lecture d’un message
PUT /communication_diary/parents/messages/id


Problème d'itération sur l'objet JsonObject. Il s'avère que je regardais des exemples utilisant le JsonOBJECT de Java alors qu'ici c'est celui de Vertx qui était utilisé
Je me suis donc rendu sur la javadoc de Vertx pour savoir ensuite quelles méthodes je pouvais utiliser


Problème d'itération pour ressortir un json object avec un filtrage. Par exemple, ne sortir uniquement que les messages appartenant au professeur.
Je ne savais pas quoi utiliser comme et comme type d'itération pour pouvoir filtrer les différents résultats qui m'étaient sortis?

Problème de comparaison d'un string avec un autre string. J'utilisais la comparaison avec l'opérateur "==" alors que c'est equals() qui devait être utilisé