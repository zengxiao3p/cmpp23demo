CREATE TABLE IF NOT EXISTS smp_monitor_web_overview_infos (id bigint(20) not null,   `SEND_COUNT` bigint(30) DEFAULT 0 COMMENT '发送次数',  `SUCCESS_COUNT` bigint(30) DEFAULT 0 COMMENT '发送成功次数',  `FAIL_COUNT` bigint(30) DEFAULT 0 COMMENT '发送失败次数', `SUCCESS_RATE` double DEFAULT 0.0 COMMENT '发送成功率',  `AVRAGE_SPEED` int DEFAULT 0.0 COMMENT '平均发送速率（毫秒）',  `MIN_SPEED` int DEFAULT 0.0 COMMENT '最小发送速率（毫秒）',  `MAX_SPEED` int DEFAULT 0.0 COMMENT '最大发送速率（毫秒）',  `CREATE_TIME` datetime COMMENT '创建时间',  `UPDATE_TIME` datetime COMMENT '更新时间时间', primary key(id));