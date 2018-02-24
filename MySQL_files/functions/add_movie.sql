CREATE DEFINER=`root`@`localhost` PROCEDURE `add_movie`(IN m_name varchar(100),
							In m_year Integer, IN m_dir varchar(100), IN m_star varchar(100)
                            , In m_genre varchar(32))
BEGIN
	Declare nid varchar(10);
    declare sid varchar(10);
    declare gid integer;
    
    Declare x integer;
    declare ts varchar(10);
    declare p1 varchar(2);
    
    set x = m_year;
	set sid = "aa0000000";
	IF (select exists(select 1 from movies where title like m_name
					and year = m_year and director LIKE m_dir))THEN
		set nid = (select id from movies where title like m_name
					and year = m_year and director LIKE m_dir);
	else
		set nid = CONCAT(SUBSTR(m_name,1,2),SUBSTR(m_dir,1,2),x);
		Insert into movies(id,title,year,director) values (nid,m_name,m_year,m_dir);
	END IF;

	if(select exists(select 1 from stars where name LIKE m_star)) THEN
		set sid = (select id from stars where name Like m_star);
	ELSE
		IF(select exists(select max(id) from stars where id like "aa%"))THEN
			set ts = (select max(id) from stars where id like "aa%");
			set p1 = "aa";
			set x = cast(substr(ts,3,7) as UNSIGNED INTEGER);
            set x = x + 1;
            set sid = concat(p1,x);
		else
			set sid = "aa0000000";
		end if;
        insert into stars(id,name) values (sid,m_star);
	end if;
    
    if(select exists(select 1 from genres where name LIKE m_genre)) THEN
		set gid = (select id from genres where name Like m_genre);
	else
		insert into genres(name) values (m_genre);
        set gid = (select id from genres where name Like m_genre);
    end if;
    
    if(select not exists(select 1 from stars_in_movies where starId like sid and movieId like nid))Then
		insert into stars_in_movies(starId,movieId) values((select id from stars where id like sid),
															(select id from movies where id like nid));
    end if;
    
    if(select not exists(select 1 from genres_in_movies where genreId like gid and movieId like nid))Then
		insert into genres_in_movies(genreId,movieId) values((select id from genres where id like gid),
															(select id from movies where id like nid));
	end if;
    

END