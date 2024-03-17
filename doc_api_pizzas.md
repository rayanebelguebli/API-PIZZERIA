## API PIZZA

| URI                              | Opération | MIME                                                     | Requête    | Réponse                                                                                  |
| -------------------------------- | --------- | -------------------------------------------------------- | ---------- | ---------------------------------------------------------------------------------------- |
| /pizzas                          | GET       | <-application/json                                       |            | Liste des des pizzas (P1)                                                                |
| /pizzas/{id}                     | GET       | <-application/json                                       |            | La pizza ayant cette {id} (P1) ou 404                                                    |
| /pizzas/{id}/prixfinal           | GET       | <-text/plain                                             |            | Le prix de la pizza ayant cette {id} ou 404                                              |
| /pizzas                          | POST      | <-/->application/json->application/x-www-form-urlencoded | Pizza (P2) | La nouvelle pizza (P1) ou 409 si la pizza existe déjà (même nom ou même id)              |
| /pizzas/{idPizza}/{idIngredient} | POST      | <-/->application/json->application/x-www-form-urlencoded |            | Ajoute le nouvel ingrédient {idIngredient} à la pizza {idPizza} ou 404                   |
| /pizzas/{id}                     | DELETE    | <-text/plain                                             |            | "pizza supprimer : nom de la pizza correspondant à cette {id}" ou 404                    |
| /pizzas/{idPizza}/{idIngredient} | DELETE    | <-text/plain                                             |            | "ingredient supprimer : nom de l'ingrédient correspondant à cette {idIngredient}" ou 404 |
| /pizzas/{id}                     | PATCH     | <-application/json                                       | Pizza (P3) | La pizza ayant cette {id} (P1) avec le nouveau prixBase ou 404                           |

## Corps des requêtes

### P1

```json
{
  "id": 5,
  "name": "margharita",
  "pate": "bien cuite",
  "prixBase": 5,
  "ingredients": [
    {
      "id": 21,
      "name": "jambon",
      "prix": 12
    },
    {
      "id": 22,
      "name": "champignons",
      "prix": 9
    }
  ]
}
```

### P2

```json
{
  "id": 1,
  "name": "4 fromages",
  "pate": "bien cuite",
  "prixBase": 5,
  "ingredients": [{ "id": 17 }, { "id": 14 }, { "id": 21 }, { "id": 22 }]
}
```

### P3

```json
{
  "prix": "2"
}
```

## Exemples

### Lister tous les pizzas connus dans la base de données

> GET sae-rest_groupeh_belguebli_bouin/pizzas

Requête vers le serveur

```json
GET sae-rest_groupeh_belguebli_bouin/pizzas

[
  {
    "id": 5,
    "name": "margharita",
    "pate": "bien cuite",
    "prixBase": 5,
    "ingredients": [
      {
        "id": 21,
        "name": "jambon",
        "prix": 12
      },
      {
        "id": 22,
        "name": "champignons",
        "prix": 9
      }
    ]
  },
  {
    "id": 2,
    "name": "4 fromages",
    "pate": "bien cuite",
    "prixBase": 2,
    "ingredients": []
  },
  {
    "id": 7888,
    "name": "margfdfharita",
    "pate": "bien cufdfdite",
    "prixBase": 5,
    "ingredients": [
      {
        "id": 21,
        "name": "jambon",
        "prix": 12
      },
      {
        "id": 22,
        "name": "champignons",
        "prix": 9
      }
    ]
  }
]
```

Codes de status HTTP

| Status | Description                             |
| ------ | --------------------------------------- |
| 200 OK | La requête s'est effectuée correctement |

### Récupérer les détails d'un ingrédient

> GET sae-rest_groupeh_belguebli_bouin/ingredients/{id}

Requête vers le serveur

```json
GET sae-rest_groupeh_belguebli_bouin/pizzas/4

{
    "id": 5,
    "name": "margharita",
    "pate": "bien cuite",
    "prixBase": 5,
    "ingredients": [
      {
        "id": 21,
        "name": "jambon",
        "prix": 12
      },
      {
        "id": 22,
        "name": "champignons",
        "prix": 9
      }
    ]
  }
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | La pizza n'existe pas                   |

### Récupérer le prix d'une pizza

> GET sae-rest_groupeh_belguebli_bouin/ingredients/{id}/prixfinal

Requête vers le serveur

```json
GET sae-rest_groupeh_belguebli_bouin/ingredients/5/prixfinal

"margharita : 26 euros"
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | La pizza n'existe pas                   |

### Ajouter une pizza

> POST sae-rest_groupeh_belguebli_bouin/pizzas

Requête vers le serveur

```json
POST sae-rest_groupeh_belguebli_bouin/pizzas

{
  "id":5,
  "name": "margharita",
  "pate": "bien cuite",
  "prixBase": 5,
  "ingredients": [
    { "id": 21},
    { "id": 22},
  ]
}
```

Réponse du serveur

```json
{
  "id": 5,
  "name": "margharita",
  "pate": "bien cuite",
  "prixBase": 5,
  "ingredients": [
    {
      "id": 21,
      "name": "jambon",
      "prix": 12
    },
    {
      "id": 22,
      "name": "champignons",
      "prix": 9
    }
  ]
}
```

Codes de status HTTP

| Status       | Description                                 |
| ------------ | ------------------------------------------- |
| 200 OK       | La requête s'est effectuée correctement     |
| 409 CONFLICT | Un pizza avec le même nom ou id existe déjà |

### Ajouter un ingrédient à une pizza

> POST sae-rest_groupeh_belguebli_bouin/pizzas/{idPizza}/{idIngredient}

Requête vers le serveur

```json
POST sae-rest_groupeh_belguebli_bouin/pizzas/5/17
```

Réponse du serveur

```json
{
    "id": 5,
    "name": "margharita",
    "pate": "bien cuite",
    "prixBase": 5,
    "ingredients": [
      {
        "id": 21,
        "name": "jambon",
        "prix": 12
      },
      {
        "id": 22,
        "name": "champignons",
        "prix": 9
      }
      {
        "id": 17,
        "name": "origan",
        "prix": 2
      }
    ]
  }
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | La pizza ou l'ingrédient n'existe pas   |

### Supprimer une pizza

> DELETE sae-rest_groupeh_belguebli_bouin/pizzas/{id}

Requête vers le serveur

```json
DELETE sae-rest_groupeh_belguebli_bouin/pizzas/5
```

Réponse du serveur

```json
"pizza supprimer : margharita"
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | La pizza n'existe pas                   |

### Supprimer un ingredient d'une pizza

> DELETE sae-rest_groupeh_belguebli_bouin/pizzas/{idPizza}/{idIngredient}

Requête vers le serveur

```json
DELETE sae-rest_groupeh_belguebli_bouin/pizzas/5/22
```

Réponse du serveur

```json
"ingredient supprimer : champignions"
```

Codes de status HTTP

| Status        | Description                                        |
| ------------- | -------------------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement            |
| 404 NOT FOUND | La pizza ou l'ingredient n'existe pas n'existe pas |

### Changer le prixBase d'une pizza

> PATCH sae-rest_groupeh_belguebli_bouin/pizzas/{id}

Requête vers le serveur

```json
PATCH sae-rest_groupeh_belguebli_bouin/pizzas/5
{
  "prix":"2"
}
```

Réponse du serveur

```json
{
  "id": 5,
  "name": "margharita",
  "pate": "bien cuite",
  "prixBase": 2,
  "ingredients": [
    {
      "id": 21,
      "name": "jambon",
      "prix": 12
    },
    {
      "id": 22,
      "name": "champignons",
      "prix": 9
    }
  ]
}
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | La pizza n'existe pas                   |
