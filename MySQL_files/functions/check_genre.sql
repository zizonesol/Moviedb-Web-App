CREATE DEFINER=`root`@`localhost` FUNCTION `check_genre`(g_name varchar(32)) RETURNS varchar(1) CHARSET utf8
BEGIN
	IF(select exists(select 1 from genres where name LIKE g_name))THEN
		return '1';
	else
		return '0';
	END IF;
END