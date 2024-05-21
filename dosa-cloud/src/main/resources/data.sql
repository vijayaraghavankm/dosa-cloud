delete from Ingredient_Ref;
 delete from Dosa;
 delete from Dosa_Order;
 delete from Ingredient;
 insert into Ingredient (id, name, type) 
        values ('RIDO', 'Rice Dosa', 'BASE');
 insert into Ingredient (id, name, type) 
        values ('MIDO', 'Millet Dosa', 'BASE');
 insert into Ingredient (id, name, type) 
        values ('PANR', 'Paneer', 'TOPPING');
 insert into Ingredient (id, name, type) 
        values ('MHRM', 'Mushroom', 'TOPPING');
 insert into Ingredient (id, name, type) 
        values ('CHTY', 'Chutney', 'SPREAD');
 insert into Ingredient (id, name, type) 
        values ('PODI', 'Podi', 'SPREAD');
 insert into Ingredient (id, name, type) 
        values ('OTPM', 'Oothappam', 'STYLE');
 insert into Ingredient (id, name, type) 
        values ('RGLR', 'Regular', 'STYLE');
 insert into Ingredient (id, name, type) 
        values ('GRND', 'Groundnut Oil', 'FAT');
 insert into Ingredient (id, name, type) 
        values ('GHEE', 'Cow Ghee', 'FAT');