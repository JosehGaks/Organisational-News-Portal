SET MODE PostgreSQL;

CREATE DATABASE jadle;
\c jadle;

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
 writtenby VARCHAR,
 rating VARCHAR,
 content VARCHAR,
 departmentid INTEGER,
 createdat BIGINT
);

CREATE TABLE departments_users (
 id SERIAL PRIMARY KEY,
 userid INTEGER,
 departmentid INTEGER
);

CREATE DATABASE news_test WITH TEMPLATE news;