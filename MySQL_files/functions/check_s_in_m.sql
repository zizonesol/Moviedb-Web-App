CREATE DEFINER=`root`@`localhost` FUNCTION `check_s_in_m`(sid varchar(10), mid varchar(10)) RETURNS varchar(1) CHARSET utf8
BEGIN
	IF(select exists(select 1 from stars_in_movies where starId LIKE sid AND movieId LIKE mid))THEN
		return '1';
	else
		return '0';
	END IF;
END