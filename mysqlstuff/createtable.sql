CREATE DATABASE IF NOT EXISTS moviedb;
use moviedb;

CREATE TABLE movies (
	id 		varchar(10) DEFAULT '' NOT NULL,
    title 	varchar(100) DEFAULT '' NOT NULL,
    year	integer NOT NULL,
    director varchar(100) DEFAULT '' NOT NULL,
    primary key(id)
);

CREATE TABLE stars(
	id 		varchar(10) DEFAULT '' NOT NULL,
	name 	varchar(100) DEFAULT '' NOT NULL,
    birthYear integer,
	primary key(id)
);

CREATE TABLE stars_in_movies(
	starId	varchar(10) DEFAULT '' NOT NULL,
	movieId varchar(10) DEFAULT '' NOT NULL,
    foreign key(starId) REFERENCES stars(id) ON DELETE CASCADE,
    foreign key(movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE genres(
	id		integer NOT NULL AUTO_INCREMENT,
	name	varchar(32) DEFAULT '' NOT NULL,
	primary key(id)
);

CREATE TABLE genres_in_movies(
	genreId	integer NOT NULL,
    movieId	varchar(10) DEFAULT '' NOT NULL,
    foreign key(genreId) REFERENCES genres(id) ON DELETE CASCADE,
    foreign key(movieId) REFERENCES movies(id) ON DELETE CASCADE
);


CREATE TABLE creditcards(
	id		varchar(20) DEFAULT '' NOT NULL,
    firstName	varchar(50) DEFAULT '' NOT NULL,
    lastName	varchar(50) DEFAULT '' NOT NULL,
    expiration	date NOT NULL,
    primary key(id)
);

CREATE TABLE customers(
	id		integer NOT NULL auto_increment,
	firstName	varchar(50) DEFAULT '' NOT NULL,
    lastName	varchar(50) DEFAULT '' NOT NULL,
    ccId	varchar(20) DEFAULT '' NOT NULL,
    address varchar(200) DEFAULT '' NOT NULL,
    email	varchar(50) DEFAULT '' NOT NULL,
    password varchar(20) DEFAULT '' NOT NULL,
	primary key(id),
    foreign key(ccId) REFERENCES creditcards(id)
);

CREATE TABLE sales(
	id		integer NOT NULL AUTO_INCREMENT,
    customerId	integer NOT NULL,
    movieId varchar(10) DEFAULT ''  NOT NULL,
    saleDate date NOT NULL,
	primary key(id),
	foreign key(customerId) REFERENCES customers(id) ON DELETE CASCADE,
    foreign key(movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE ratings(
	movieId		varchar(10) DEFAULT '' NOT NULL,
    rating	float NOT NULL,
    numVotes	integer,
    foreign key(movieId) REFERENCES movies(id) ON DELETE CASCADE

);