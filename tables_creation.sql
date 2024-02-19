Create table ingredients(
    id serial primary key,
    name text,
    prix int
);


Create table pizzas(
    id serial primary key,
    name text,
    pate text,
    prixBase text
);

Create table pizzasContient(
   idPizza int,
   idIngredient int,
   constraint fk_pizza foreign key(idPizza)
   references pizzas(id),
   constraint fk_ingredient foreign key(idIngredient)
   references ingredients(id),
   constraint pk primary key (idPizza,idIngredient)
);