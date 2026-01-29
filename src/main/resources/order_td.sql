CREATE TABLE IF NOT EXISTS "Order" (
                         id SERIAL PRIMARY KEY,
                         reference VARCHAR(50) NOT NULL UNIQUE,
                         creation_datetime TIMESTAMP NOT NULL,

                         table_id INT NOT NULL,
                         arrival_datetime TIMESTAMP NOT NULL,
                         departure_datetime TIMESTAMP NOT NULL,

                         CONSTRAINT fk_order_table
                             FOREIGN KEY (table_id)
                                 REFERENCES table_restaurant(id)
);



create table if not exists DishOrder(
                                        id serial primary key,
                                        id_order int,
                                        id_dish int,
                                        quantity int,
                                        foreign key (id_order) references "Order"(id),
                                        foreign key (id_dish) references dish(id)
)