create type date_precision as enum ('year', 'month', 'day');

alter type date_precision owner to spotify;

CREATE CAST (CHARACTER VARYING AS spotify.date_precision) WITH INOUT AS ASSIGNMENT;

create table users
(
	id bigserial not null
		constraint users_pk
			primary key,
	name varchar(100) not null,
	email varchar(200) not null,
	token varchar(300) not null
);

alter table users owner to postgres;

create unique index users_email_uindex
	on users (email);

create unique index users_id_uindex
	on users (id);

create table artists
(
	id varchar(100) not null
		constraint artists_pk
			primary key,
	name varchar(200) not null,
	external_urls varchar(100) [],
	followers integer default 0 not null,
	genres varchar(100) [],
	href varchar(200) not null,
	popularity smallint default 0,
	uri varchar(100) not null
);

alter table artists owner to postgres;

create unique index artists_id_uindex
	on artists (id);

create table images
(
	id bigserial not null
		constraint images_pk
			primary key,
	height integer not null,
	width integer not null,
	url varchar(200) not null
);

alter table images owner to postgres;

create unique index images_url_uindex
	on images (url);

create table user_artists
(
	user_id bigint
		constraint user_artists_users_id_fk
			references users
				on delete cascade,
	artist_id varchar(100)
		constraint user_artists_artists_id_fk
			references artists
				on delete cascade,
	constraint user_artists_pk
		unique (user_id, artist_id)
);

alter table user_artists owner to postgres;

create table artists_images
(
	artist_id varchar(100) not null
		constraint artists_images_artists_id_fk
			references artists,
	image_id bigint not null
		constraint artists_images_images_id_fk
			references images,
	constraint artists_images_pk
		unique (artist_id, image_id)
);

alter table artists_images owner to spotify;

create table albums
(
	id varchar(100) not null
		constraint albums_pk
			primary key,
	album_group varchar(100),
	album_type varchar(100) not null,
	available_markets char(2) [],
	external_urls varchar(100) [],
	href varchar(200) not null,
	name varchar(300) not null,
	type varchar(100),
	uri varchar(100) not null,
	release_date_precision date_precision not null,
	release_date varchar(10) not null
);

alter table albums owner to spotify;

create table artists_albums
(
	artist_id varchar(100)
		constraint artists_albums_artists_id_fk
			references artists,
	album_id varchar(100)
		constraint artists_albums_albums_id_fk
			references albums,
	constraint artists_albums_pk
		unique (artist_id, album_id)
);

alter table artists_albums owner to spotify;

create table albums_images
(
	album_id varchar(100)
		constraint album_images_albums_id_fk
			references albums,
	image_id bigint
		constraint album_images_images_id_fk
			references images,
	constraint album_images_pk
		unique (album_id, image_id)
);

alter table albums_images owner to spotify;

