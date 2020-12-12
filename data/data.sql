--
-- Database: `micro`
--

-- --------------------------------------------------------

--
-- Table structure for table `field`
--

CREATE TABLE `field` (
  `id` bigint(20) NOT NULL,
  `entity_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `meta` json NOT NULL,
  `definition_name` varchar(100) NOT NULL,
  `definition` json NOT NULL,
  `level` tinyint(2) NOT NULL DEFAULT '0',
  `type` varchar(20) NOT NULL DEFAULT 'CORE' COMMENT 'CORE, DOMAIN',
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `field`
--

INSERT INTO `field` (`id`, `entity_id`, `name`, `code`, `meta`, `definition_name`, `definition`, `level`, `type`, `is_active`, `created_time`, `updated_time`) VALUES
(1, 0, 'code', 'code', '{\"type\": \"string\", \"description\": \"The unique identifier for a product\"}', '', 'null', 0, 'CORE', 1, '2020-05-27 21:07:33', '2020-05-27 21:07:33'),
(2, 0, 'Name', 'name', '{\"type\": \"string\", \"format\": \"evenlength\", \"maxLength\": 10, \"minLength\": 2, \"description\": \"Name of the product\"}', '', 'null', 0, 'CORE', 1, '2020-05-27 21:07:33', '2020-05-27 21:07:33'),
(3, 0, 'Price', 'price', '{\"type\": \"number\", \"exclusiveMinimum\": 10}', '', 'null', 0, 'CORE', 1, '2020-05-27 21:07:33', '2020-05-27 21:07:33'),
(4, 0, 'code', 'code', '{\"type\": \"string\", \"description\": \"The unique identifier for a product\"}', '', 'null', 1, 'learn', 1, '2020-05-27 21:07:33', '2020-05-27 21:07:33'),
(5, 1, 'Name', 'name', '{\"help\": \"Name of t he product. Min 2 and Max 10 with pattern mathching\", \"type\": \"string\", \"pattern\": \"^[a-fA-F0-9]{8}$\", \"maxLength\": 10, \"minLength\": 2, \"description\": \"Name of the product\"}', '', 'null', 2, 'ebook', 1, '2020-05-27 21:07:33', '2020-05-27 21:07:33'),
(6, 0, 'Price', 'price', '{\"type\": \"number\", \"exclusiveMinimum\": 10}', '', 'null', 1, 'ebook', 1, '2020-05-27 21:07:33', '2020-05-27 21:07:33'),
(7, 1, 'custom', 'custom', '{\"type\": \"string\", \"description\": \"The unique identifier for a product\"}', '', 'null', 2, 'ebook', 1, '2020-05-27 21:07:33', '2020-05-27 21:07:33'),
(8, 0, 'Authors', 'authors', '{\"type\": \"array\", \"items\": {\"$ref\": \"#/definitions/author\"}, \"default\": []}', 'author', '{\"type\": \"object\", \"required\": [\"name\", \"contributionType\"], \"properties\": {\"name\": {\"type\": \"string\", \"pattern\": \"^[a-fA-F0-9]{8}$\"}, \"email\": {\"type\": \"string\"}, \"contributionType\": {\"type\": \"string\"}}}', 0, 'CORE', 1, '2020-05-27 21:07:33', '2020-05-27 21:07:33');

-- --------------------------------------------------------

--
-- Table structure for table `form`
--

CREATE TABLE `form` (
  `id` bigint(20) NOT NULL,
  `entity_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `level` smallint(2) NOT NULL DEFAULT '0',
  `type` varchar(20) NOT NULL,
  `fields` json NOT NULL,
  `validations` json NOT NULL,
  `definitions` json NOT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `form`
--

INSERT INTO `form` (`id`, `entity_id`, `name`, `code`, `level`, `type`, `fields`, `validations`, `definitions`, `created_time`, `updated_time`, `is_active`) VALUES
(1, 0, 'Product', 'product', 0, 'CORE', '{\"fields\": [\"code\", \"name\", \"price\", \"authors\"]}', '[\"code\", \"name\", \"price\"]', '{}', '2020-05-27 23:40:15', '2020-05-27 23:40:15', b'1'),
(2, 0, 'Product', 'product', 1, 'learn', '{\"fields\": [\"code\", \"name\"]}', '[\"code\", \"name\"]', '{}', '2020-05-27 23:40:15', '2020-05-27 23:40:15', b'1'),
(3, 2, 'Product', 'product', 2, 'ebook', '{\"fields\": [\"code\", \"name\", \"custom\"]}', '[\"code\", \"name\"]', '{}', '2020-05-27 23:40:15', '2020-05-27 23:40:15', b'1');

-- --------------------------------------------------------

--
-- Table structure for table `language`
--

CREATE TABLE `language` (
  `id` bigint(20) NOT NULL,
  `locale` varchar(6) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT '0',
  `domain` varchar(10) DEFAULT 'core' COMMENT 'Domain for the langauge, core, ebook, learn etc',
  `level` smallint(6) NOT NULL DEFAULT '0',
  `message_key` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `language`
--

INSERT INTO `language` (`id`, `locale`, `tenant_id`, `domain`, `level`, `message_key`, `message`) VALUES
(1, 'en', 0, 'core', 0, 'error-message.bad-request', 'Request is invalid'),
(2, 'en', 0, 'core', 0, 'error-message.already-exists', '{0} {1} already exists-core'),
(3, 'en', 0, 'core', 0, 'entity.product', 'Product'),
(4, 'en', 0, 'core', 0, 'entity.user', 'User'),
(5, 'en', 0, 'core', 0, 'entity.products', 'Product(s)'),
(6, 'en', 0, 'core', 0, 'entity.users', 'User(s)'),
(7, 'zh', 0, 'core', 0, 'error-message.bad-request', '此頁面以中文顯示.'),
(8, 'zh', 0, 'core', 0, 'error-message.already-exists', '{0} {1} 英語'),
(9, 'zh', 0, 'core', 0, 'entity.product', 'zh-Product'),
(10, 'zh', 0, 'core', 0, 'entity.user', 'zh-User'),
(11, 'zh', 0, 'core', 0, 'entity.products', 'zh-Product(s)'),
(12, 'zh', 0, 'core', 0, 'entity.users', 'zh-User(s)'),
(13, 'en', 1, 'core', 0, 'error-message.already-exists', '{0} {1} already exists-tenant-1'),
(14, 'en', 0, 'core', 0, 'validation.required', '{0}.{1} is required'),
(15, 'en', 0, 'core', 0, 'validation.pattern', '{0}: does not match the pattern {1}');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `code` varchar(100) NOT NULL,
  `meta` json NOT NULL,
  `price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `tenant_id`, `name`, `code`, `meta`, `price`, `created_time`, `updated_time`) VALUES
(1, 1, 'Name 1', 'Code 1', 'null', '12.00', '2020-07-18 16:03:34', '2020-07-18 16:03:34'),
(2, 1, 'Name 2', 'Code 2', 'null', '12.00', '2020-07-18 16:20:58', '2020-07-18 16:20:58'),
(3, 1, 'Duplicate Product', 'duplicate', 'null', '12.00', '2020-07-18 16:22:54', '2020-07-18 16:22:54'),
(12, 2, 'duplicate product', 'duplicate', 'null', '123.00', '2020-07-27 07:18:06', '2020-07-27 07:18:06'),
(19, 1, '13212', '123123qwe', 'null', '12.00', '2020-08-03 08:52:55', '2020-08-03 08:52:55'),
(20, 1, '132125', '123123qw4', 'null', '12.00', '2020-08-03 08:53:07', '2020-08-03 08:53:07');

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings` (
  `id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `settings` json NOT NULL,
  `type` varchar(10) DEFAULT 'SYSTEM',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`id`, `tenant_id`, `settings`, `type`, `created_time`, `updated_time`) VALUES
(1, 0, '{\"config1\": \"Core Config 1\", \"config2\": \"Core Config 2\", \"config3\": \"Core config 3\", \"config4\": \"Core config 4\", \"config5\": \"Core config 5\"}', 'SYSTEM', '2020-05-27 06:16:48', '2020-05-27 06:16:48'),
(2, 0, '{\"config2\": \"ebook Config 2\"}', 'ebook', '2020-05-27 06:16:48', '2020-05-27 06:16:48'),
(3, 0, '{\"config2\": \"learn Config 2\"}', 'learn', '2020-05-27 06:16:48', '2020-05-27 06:16:48'),
(4, 1, '{\"config3\": \"KIMS Config 3\"}', 'learn', '2020-05-27 06:16:48', '2020-05-27 06:16:48'),
(5, 2, '{\"config3\": \"Wiely Config 3\", \"wildyConfg1\": \"Dynamic Config for Wiley\"}', 'ebook', '2020-05-27 06:16:48', '2020-05-27 06:16:48');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `field`
--
ALTER TABLE `field`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `form`
--
ALTER TABLE `form`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `language`
--
ALTER TABLE `language`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `tenant_id` (`tenant_id`,`domain`,`locale`,`message_key`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_product_code` (`code`,`tenant_id`) USING BTREE;

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `field`
--
ALTER TABLE `field`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `form`
--
ALTER TABLE `form`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `language`
--
ALTER TABLE `language`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
--
-- AUTO_INCREMENT for table `settings`
--
ALTER TABLE `settings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
