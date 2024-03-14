Create table ingredients(
    id serial primary key,
    name text,
    prix int
);


Create table pizzas(
    id int primary key,
    name text,
    pate text,
    prixBase text
);

Create table pizzasContient(
   idPizza int,
   idIngredient int,
   constraint fk_pizza foreign key(idPizza)
   references pizzas(id) on delete cascade,
   constraint fk_ingredient foreign key(idIngredient)
   references ingredients(id) on delete cascade,
   constraint pk primary key (idPizza,idIngredient)
);

Create table commandes(
    id int primary key,
    name text,
    date date
);

Create table commandesContient(
    idCommande int,
   idPizza int,
   constraint fk_commande foreign key(idCommande)
   references commandes(id) on delete cascade,
   constraint fk_pizza foreign key(idPizza)
   references pizzas(id) on delete cascade,
   constraint pk_commandesContient primary key (idCommande,idPizza)
);


Create table users(
    login text,
    mdp text
);