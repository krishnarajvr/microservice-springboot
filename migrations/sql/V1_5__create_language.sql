create table IF NOT EXISTS language (
	id BIGINT AUTO_INCREMENT,
	locale VARCHAR(6),
	tenant_id BIGINT default 0,
	domain varchar(10) default 'core' COMMENT 'Domain for the langauge, core, scholar, health etc',
	message_key varchar(255),
	message varchar(255),
	primary key (id),
	unique(tenant_id,domain,locale,message_key)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;