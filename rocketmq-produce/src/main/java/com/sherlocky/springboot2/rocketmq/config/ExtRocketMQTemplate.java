package com.sherlocky.springboot2.rocketmq.config;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

@ExtRocketMQTemplateConfiguration(nameServer = "${sherlocky.rocketmq.extNameServer}")
public class ExtRocketMQTemplate extends RocketMQTemplate {

}