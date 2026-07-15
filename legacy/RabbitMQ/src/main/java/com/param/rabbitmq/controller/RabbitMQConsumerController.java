/*package com.param.rabbitmq.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.ErrorResponse;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.consumerService.ConsumerService;
import com.param.rabbitmq.service.consumerService.IConsumerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/rabbitmq")
public class RabbitMQConsumerController {

	@RequestMapping(value = "/consume/emr", method = RequestMethod.POST)
	@ApiOperation(value = "Consume from EMR queue", response = Request.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> publishEmr(@RequestBody Request request) throws Exception {
		try {
			IConsumerService consumerService = new ConsumerService();

			return new ResponseEntity<Object>(consumerService.consume(request), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<Object>(
					new ErrorResponse("HIS-0000", "Error while consuming master from RabbitMQ", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
*/