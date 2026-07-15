package com.param.rabbitmq.service.HisService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ServiceException;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.util.conn.RabbitMQ;

@Service("HisService")
public class HisService implements IHisService {

	private static final Logger logger = Logger.getLogger(HisService.class);

	@Autowired
	RabbitMQ rMQ;

	@Override
	public Object publish(Request request) {
		try {
			boolean publish = rMQ.publish("HIS.EXCHANGE", "HIS", "HIS", request);
			if (publish) {
				return "Message successfully published to RabbitMQ";
			} else {
				return "Message publish failed";
			}
		} catch (Exception ex) {
			logger.error("publish failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}