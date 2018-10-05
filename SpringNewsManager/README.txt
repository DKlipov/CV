Для запуска приложения необходимо задать в SpringNewsManager\src\main\webapp\WEB-INF\spring\appServlet\servlet-context.xml параметры подключения к базе данных и выполнить запросы

create table category (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) not null);
create table news (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name varchar(100) not null, text varchar(1000), category_id INT not null REFERENCES category (id), newsDate Date not null);

insert into category (name) values ('test'),('test1');
insert into news(name,text,category_id,newsDate) values('Test news','Test text',1,'01.01.2018'), ('Test news1','Test text',2,'02.02.2018');

Приложение тестировалось только под СУБД MySQL 8.0.12.


Веб-приложение по управлению новостной лентой на сайте. 
Каждая новость состоит из названия, содержания, даты публикации и категории, к которой относится новость. 

Каждая категория содержит название, и к ней может быть привязано несколько новостей. 

Приложение предоставляет следующие возможности по работе с новостями: 
- просматривать список новостей 
- поиск новости по категории (возможность выбрать из существующих категорий), названию и содержанию 
- создание и редактирование новости 
- удаление новости 