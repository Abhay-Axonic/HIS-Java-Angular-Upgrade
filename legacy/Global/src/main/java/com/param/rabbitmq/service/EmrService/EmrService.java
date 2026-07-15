package com.param.rabbitmq.service.EmrService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ServiceException;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.util.conn.RabbitMQ;

@Service("EmrService")
public class EmrService implements IEmrService {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EmrService.class);

	@Autowired
	RabbitMQ rmq;

	@Override
	public Object publish(Request request) {
		try {
			boolean publish = rmq.publish("HIS.EXCHANGE", "EMR", "EMR", request);
			if (publish) {
				return "Message successfully published to RabbitMQ";
			} else {
				return "Message publish failed";
			}
		} catch (Exception ex) {
			logger.error("publish failed", ex);
			return null;
		}
	}
}
