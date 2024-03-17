## API Commande

| URI                               | Opération | MIME                                                     | Requête       | Réponse                                                                             |
| --------------------------------- | --------- | -------------------------------------------------------- | ------------- | ----------------------------------------------------------------------------------- |
| /commandes                        | GET       | <-application/json                                       |               | Liste des commandes (C1)                                                            |
| /commandes/{id}                   | GET       | <-application/json                                       |               | La commande ayant cette {id} (C1) ou 404                                            |
| /commandes/{id}/prixfinal         | GET       | <-text/plain                                             |               | Le prix de la commande ayant cette {id} ou 404                                      |
| /commandes                        | POST      | <-/->application/json->application/x-www-form-urlencoded | Commande (C2) | La nouvelle commande (c1) ou 409 si la commande existe déjà (même nom ou même id)   |
| /commandes/{id}                   | DELETE    | <-text/plain                                             |               | "commande supprimer : nom de la commande correspondant à cette {id}" ou 404         |
| /commandes/{idCommande}/{idPizza} | DELETE    | <-text/plain                                             |               | "pizza supprimer : nom de l'ingrédient correspondant à cette {idIngredient}" ou 404 |

## Corps des requêtes

### C1

```json
{
  "id": 1,
  "name": "cm1",
  "date": "2023-12-28",
  "list": [
    {
      "id": 2,
      "name": "4 fromages",
      "pate": "bien cuite",
      "prixBase": 2,
      "ingredients": []
    },
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
  ]
}
```

### C2

```json
{
  "id": 2,
  "name": "cm2 ",
  "date": "2004-05-45",
  "list": [
    {
      "id": 1
    },
    {
      "id": 2
    },
    {
      "id": 5
    }
  ]
}
```

## Exemples

### Lister tous les commandes connus dans la base de données

> GET sae-rest_groupeh_belguebli_bouin/commandes

Requête vers le serveur

```json
GET sae-rest_groupeh_belguebli_bouin/commandes

[
  {
    "id": 1,
    "name": "cm1",
    "date": "2023-12-28",
    "list": [
      {
        "id": 2,
        "name": "4 fromages",
        "pate": "bien cuite",
        "prixBase": 2,
        "ingredients": []
      },
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
    ]
  }
  {
    "id": 1,
    "name": "cm2",
    "date": "2024-01-28",
    "list": [
      {
        "id": 2,
        "name": "napolitaine",
        "pate": "bien cuite",
        "prixBase": 2,
        "ingredients": [
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
            },
            {
                "id": 21,
                "name": "jambon",
                "prix": 12
            }
        ]
      },
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
    ]
  }
]
```

Codes de status HTTP

| Status | Description                             |
| ------ | --------------------------------------- |
| 200 OK | La requête s'est effectuée correctement |

### Récupérer les détails d'une commande

> GET sae-rest_groupeh_belguebli_bouin/ingredients/{id}

Requête vers le serveur

```json
GET sae-rest_groupeh_belguebli_bouin/pizzas/1


  {
    "id": 1,
    "name": "cm1",
    "date": "2023-12-28",
    "list": [
      {
        "id": 2,
        "name": "4 fromages",
        "pate": "bien cuite",
        "prixBase": 2,
        "ingredients": []
      },
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
    ]
  }

```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | La commande n'existe pas                |

### Récupérer le prix d'une commande

> GET sae-rest_groupeh_belguebli_bouin/commandes/{id}/prixfinal

Requête vers le serveur

```json
GET sae-rest_groupeh_belguebli_bouin/ingredients/1/prixfinal

"cm1 : 25 euros"
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | La commande n'existe pas                |

### Ajouter une commande

> POST sae-rest_groupeh_belguebli_bouin/commandes

Requête vers le serveur

```json
POST sae-rest_groupeh_belguebli_bouin/commandes

{
    "id": 2,
    "name": "cm2 ",
    "date": "2004-05-45",
    "list": [
      {
        "id": 5
      }
    ]
}
```

Réponse du serveur

```json
{
  "id": 2,
  "name": "cm2 ",
  "date": "2004-05-45",
  "list": [
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
  ]
}
```

Codes de status HTTP

| Status       | Description                                                 |
| ------------ | ----------------------------------------------------------- |
| 200 OK       | La requête s'est effectuée correctement                     |
| 409 CONFLICT | Une commande avec le même nom ou id existe déjà existe déjà |

### Ajouter une pizza à une commande

> POST sae-rest_groupeh_belguebli_bouin/commande/{idCommande}/{idPizza}

Requête vers le serveur

```json
POST sae-rest_groupeh_belguebli_bouin/pizzas/2/2
```

Réponse du serveur

```json
{
  "id": 2,
  "name": "cm2 ",
  "date": "2004-05-45",
  "list": [
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
    },
    {
      "id": 2,
      "name": "4 fromages",
      "pate": "bien cuite",
      "prixBase": 2,
      "ingredients": []
    }
  ]
}
```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | La commande ou la pizza n'existe pas    |

### Supprimer une commande

> DELETE sae-rest_groupeh_belguebli_bouin/commandes/{id}

Requête vers le serveur

```json
DELETE sae-rest_groupeh_belguebli_bouin/commandes/2

"commande supprimer : cm2 "

```

Codes de status HTTP

| Status        | Description                             |
| ------------- | --------------------------------------- |
| 200 OK        | La requête s'est effectuée correctement |
| 404 NOT FOUND | La commande n'existe pas                |
