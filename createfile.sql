DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;
USE moviedb;

DROP TABLE IF EXISTS movies;
CREATE TABLE movies(
	id varchar(10) NOT NULL,
    title varchar(100) NOT NULL,
    year integer NOT NULL,
    director varchar(100) NOT NULL,
    PRIMARY KEY(id));

DROP TABLE IF EXISTS stars;
CREATE TABLE stars(
	id varchar(10) NOT NULL,
    name varchar(100) NOT NULL,
    birthYear integer,
    PRIMARY KEY(id));

DROP TABLE IF EXISTS stars_in_movies;
CREATE TABLE stars_in_movies(
	starId varchar(10) NOT NULL,
    movieId varchar(10) NOT NULL,
    FOREIGN KEY(starId) REFERENCES stars(id) ON DELETE CASCADE,
    FOREIGN KEY(movieId) REFERENCES movies(id) ON DELETE CASCADE);

DROP TABLE IF EXISTS creditcards;
CREATE TABLE creditcards(
	id varchar(20)  NOT NULL,
    firstName varchar(50) NOT NULL,
    lastName varchar(50) NOT NULL,
    expiration date NOT NULL,
    PRIMARY KEY (id));

DROP TABLE IF EXISTS ratings;
CREATE TABLE ratings(
	movieId varchar(10) NOT NULL,
    rating float NOT NULL,
    numVotes integer NOT NULL,
    FOREIGN KEY(movieId) REFERENCES movies(id) ON DELETE CASCADE);

DROP TABLE IF EXISTS genres;
CREATE TABLE genres(
	id integer  NOT NULL AUTO_INCREMENT,
    name varchar(32) NOT NULL,
    PRIMARY KEY(id));

DROP TABLE IF EXISTS genres_in_movies;
CREATE TABLE genres_in_movies(
	genreId integer NOT NULL,
    movieId varchar(10) NOT NULL,
    FOREIGN KEY(genreId) REFERENCES genres(id) ON DELETE CASCADE,
    FOREIGN KEY(movieId) REFERENCES movies(id) ON DELETE CASCADE);

DROP TABLE IF EXISTS customers;
CREATE TABLE customers(
	id integer NOT NULL AUTO_INCREMENT,
    firstName varchar(50) NOT NULL,
    lastName varchar(50) NOT NULL,
    ccId varchar(20) NOT NULL,
    address varchar(50) NOT NULL,
    password varchar(20) NOT NULL,
    FOREIGN KEY(ccId) REFERENCES creditcards(id) ON DELETE CASCADE,
    PRIMARY KEY(id));
    
DROP TABLE IF EXISTS sales;
CREATE TABLE sales(
	id integer NOT NULL AUTO_INCREMENT,
    customerId integer NOT NULL,
    movieId varchar(10) NOT NULL,
    saleDate date NOT NULL,
    FOREIGN KEY(customerId) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY(movieId) REFERENCES movies(id) ON DELETE CASCADE,
    PRIMARY KEY(id));