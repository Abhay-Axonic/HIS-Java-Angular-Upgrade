package com.param.rabbitmq.service.consumerService;

import org.json.simple.JSONObject;

import com.param.rabbitmq.dto.base.Request;

public interface IConsumerService {

	Object consume(Request request);

	void keywiseDataRead(String key, JSONObject payload);

}
