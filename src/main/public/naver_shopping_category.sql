create table naver_shopping_category
(
    id                 integer not null
        primary key,
    name               varchar(255),
    parent_category_id integer
        constraint fkhnorbivuvr706jfhxn9rnk8yl
            references naver_shopping_category
);

alter table naver_shopping_category
    owner to postgres;

