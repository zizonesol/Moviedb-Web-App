CREATE DEFINER=`root`@`localhost` FUNCTION `check_movie`(m_title varchar(100)) RETURNS varchar(1) CHARSET utf8
BEGIN
	IF(select exists(select 1 from movies where title LIKE m_title))THEN
		return '1';
	else
		return '0';
	END IF;
END