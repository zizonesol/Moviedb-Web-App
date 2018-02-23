CREATE DEFINER=`root`@`localhost` FUNCTION `check_movie`(m_title varchar(100),m_id varchar(10)) RETURNS varchar(1) CHARSET utf8
BEGIN
	IF(select exists(select 1 from movies where title LIKE m_title or id LIKE m_id))THEN
		return '1';
	else
		return '0';
	END IF;
END