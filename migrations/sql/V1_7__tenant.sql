CREATE TABLE tenant (
'id' bigint(20) NOT NULL,
'name' varchar(100) NOT NULL,
'domain' varchar(10) DEFAULT 'SCHOLAR',
'created_time' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
'updated_time' datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
PRIMARY KEY (id),
UNIQUE KEY tenant_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;