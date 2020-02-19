SET MODE PostgreSQL;

CREATE DATABASE news;
\c news;

CREATE TABLE departments (
 id SERIAL PRIMARY KEY,
 departmentName VARCHAR,
 numberOfEmployees INTEGER
);


CREATE TABLE users (
 id SERIAL PRIMARY KEY,
 name VARCHAR,
 positionInCompany VARCHAR
);

CREATE TABLE news (
 id SERIAL PRIMARY KEY,
 content VARCHAR,
 departmentid INTEGER
);

CREATE TABLE departments_users (
 id SERIAL PRIMARY KEY,
 userid INTEGER,
 departmentid INTEGER
);

CREATE DATABASE news_test WITH TEMPLATE news;