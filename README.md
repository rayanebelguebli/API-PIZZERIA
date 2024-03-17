# SAE-REST_GroupeH_Belguebli_Bouin

> Auteurs: Bouin Julien, Belguebli Rayane

## Documentation API

- [Documentation ingrédients](./doc_api_ingredients.md)
- [Documentation pizzas](./doc_api_pizzas.md)
- [Documentation commandes](./doc_api_commandes.md)

## L'authentification

- L'authentification se fait via la méthode BASIC . En allant sur `http://localhost:8080/sae-rest_groupeh_belguebli_bouin/users/token`, nous récupérons un token d'authentification qui nous permet en mettant ce token dans le header de la requête d'accéder à toutes les méthodes de modification .

## MCD & MLD

### MCD

![MCD](img/MCD.png)

## MLD

- Ingrédients(<u>id</u>, name, prix)
- Pizzas(<u>id</u>, name, pate, prixBase)
- Commandes(<u>id</u>, name, date)
- PizzasContient(<u>#idPizza, #idIngredient</u>)
- CommandesContient(<u>#idCommande, #idPizza</u>)
