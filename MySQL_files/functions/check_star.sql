CREATE DEFINER=`root`@`localhost` FUNCTION `check_star`(s_name varchar(32)) RETURNS varchar(1) CHARSET utf8
BEGIN
	IF(select exists(select 1 from stars where name LIKE s_name))THEN
		return '1';
	else
		return '0';
	END IF;
END