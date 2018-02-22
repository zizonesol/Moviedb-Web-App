CREATE DEFINER=`root`@`localhost` FUNCTION `check g_in_m`(gid integer, mid varchar(10)) RETURNS varchar(1) CHARSET utf8
BEGIN
	IF(select exists(select 1 from genres_in_movies where genreId LIKE gid AND movieId LIKE mid))THEN
		return '1';
	else
		return '0';
	END IF;
END