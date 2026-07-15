package com.param.rabbitmq.service.OtimsService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ServiceException;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.util.conn.RabbitMQ;


@Service("OtimsService")
public class OtimsService implements IOtimsService {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OtimsService.class);

	@Autowired
	RabbitMQ rabbitMQ;
	@Override
	public Object publish(Request request) {
		try {
			boolean publish = rabbitMQ.publish("OTIMS.EXCHANGE", "OTIMS", "OTIMS", request);
			if (publish) {
				return "Message successfully published to RabbitMQ";
			} else {
				return "Message publish failed";
			}
		} catch (Exception ex) {
			logger.error("publish failed", ex);
		}
		return null;
	}
}
