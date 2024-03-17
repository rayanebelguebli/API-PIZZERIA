## API Ingredients

| URI                    | Opération | MIME                                                     | Requête         | Réponse                                                                        |
| ---------------------- | --------- | -------------------------------------------------------- | --------------- | ------------------------------------------------------------------------------ |
| /ingredients           | GET       | <-application/json                                       |                 | Liste des ingrédients (I1)                                                     |
| /ingredients/{id}      | GET       | <-application/json                                       |                 | L'ingrédient ayant cette {id} (I1) ou 404                                      |
| /ingredients/{id}/name | GET       | <-text/plain                                             |                 | Le nom de l'ingrédient ayant cette {id} ou 404                                 |
| /ingredients           | POST      | <-/->application/json->application/x-www-form-urlencoded | Ingrédient (I2) | Le nouvel ingrédients (I1) ou 409 si l'ingrédient existe déjà (même nom)       |
| /ingredients/{id}      | DELETE    | <-text/plain                                             |                 | "ingredient supprimer : {nom de l'ingrédient correspondant à cette id}" ou 404 |
| /ingredients/{id}      | PATCH     | <-application/json                                       | Ingrédient (I3) | L'ingrédient ayant cette {id} (I1) avec le nouveau prix ou 404                 |

## Corps des requêtes

### I1

```json
{
  "id": 20,
  "name": "basilic",
  "prix": 5
}
```

### I2

```json
{ "name": "poivron", "prix": "5" }
```

### I3

```json
{
  "prix": "443"
}
```

## Exemples

### Lister tous les ingrédients connus dans la base de données

> GET sae-rest_groupeh_belguebli_bouin/ingredients

Requête vers le serveur

```json
GET sae-rest_groupeh_belguebli_bouin/ingredients

{
    "id": 17,
    "name": "origan",
    "prix": 2
  },
  {
    "id": 18,
    "name": "anchois",
    "prix": 4
  },
  {
    "id": 19,
    "name": "olives",
    "prix": 3
  },
  {
    "id": 20,
    "name": "basilic",
    "prix": 5
  }
```

Codes de status HTTP

| Status | Description                             |
| ------ | --------------------------------------- |
| 200 OK | La requête s'est effectuée correctement |

### Récupérer les détails d'un ingrédient

> GET sae-rest_groupeh_belguebli_bouin/ingredients/{id}

Requête vers le serveur

```json
GET sae-rest_groupeh_belguebli_bouin/ingredients/17

{
    "id": 17,
    "name": "origan",
    "prix": 2
  }
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | L'ingrédient n'existe pas               |

### Récupérer le nom d'un ingrédient

> GET sae-rest_groupeh_belguebli_bouin/ingredients/{id}/name

Requête vers le serveur

```json
GET sae-rest_groupeh_belguebli_bouin/ingredients/17/name

"origan"
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | L'ingrédient n'existe pas               |

### Ajouter un ingrédient

> POST sae-rest_groupeh_belguebli_bouin/ingredients

Requête vers le serveur

```json
POST sae-rest_groupeh_belguebli_bouin/ingredients/

{"name":"poivron", "prix":"5"}
```

Réponse du serveur

```json
{
    "id": 32,
    "name": "poivron",
    "prix": 5
  },
```

Codes de status HTTP

| Status       | Description                                |
| ------------ | ------------------------------------------ |
| 200 OK       | La requête s'est effectuée correctement    |
| 409 CONFLICT | Un ingrédient avec le même nom existe déjà |

### Supprimer un ingrédient

> DELETE sae-rest_groupeh_belguebli_bouin/ingredients/{id}

Requête vers le serveur

```json
DELETE sae-rest_groupeh_belguebli_bouin/ingredients/17
```

Réponse du serveur

```json
"ingredient supprimer : origan"
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | L'ingrédient n'existe pas               |

### Changer le prix d'un ingrédient

> PATCH sae-rest_groupeh_belguebli_bouin/ingredients/{id}

Requête vers le serveur

```json
PATCH sae-rest_groupeh_belguebli_bouin/ingredients/17
{
  "prix":"443"
}
```

Réponse du serveur

```json
{
  "id": 13,
  "name": "lardon",
  "prix": 443
}
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | L'ingrédient n'existe pas               |
