CREATE TABLE  IF NOT EXISTS `demo_order` (
  `id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_pin` VARCHAR (20) DEFAULT NULL COMMENT '用户pin',
  `order_price` VARCHAR (20) DEFAULT NULL COMMENT '订单金额',
  `created` datetime NOT NULL COMMENT '创建时间',
  `modified` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
);