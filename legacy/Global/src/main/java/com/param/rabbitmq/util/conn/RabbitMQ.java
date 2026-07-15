package com.param.rabbitmq.util.conn;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.rabbitmq.service.consumerService.IConsumerService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Component
public class RabbitMQ {
	final static Logger logger = Logger.getLogger(RabbitMQ.class);

	//private static RabbitMQ rabbitMQ;

	private ConnectionFactory factory;

	private Connection conn;

	private Channel channel;
	
	@Autowired
	IConsumerService iConsumerService;
	
	
	public RabbitMQ() {

	}

	/*public static RabbitMQ getInstance() {
		if (rabbitMQ == null) {
			rabbitMQ = new RabbitMQ();
		}
		return rabbitMQ;
	}*/

	public boolean publish(String exchange, String queue, String routingKey, Object message) {
		boolean publish = false;

		try {
			if (conn == null || !conn.isOpen()) {
				getConnection();
			}

			if (conn != null) {
				if (this.channel == null || !this.channel.isOpen()) {
					this.channel = conn.createChannel();
				}

				if (channel != null) {
					channel.queueDeclare(queue, true, false, false, null);
					channel.basicPublish(exchange, routingKey, null, new ObjectMapper().writeValueAsString(message).getBytes());
					publish = true;
					String msg = new String(new ObjectMapper().writeValueAsString(message).getBytes(), "UTF-8");
					//System.out.println("published.................."+msg);
				}
			}
		} catch (Exception ex) {
		logger.error("publish failed", ex);
		}

		return publish;
	}


	private void getConnection() {
		try {
			
			
			//System.out.println("factory"+this.factory);
			if (this.factory == null) {
				RMQConnection rmqConn = RMQConnection.getInstance();
				this.factory = new ConnectionFactory();
				this.factory.setUsername(rmqConn.getUser());
				this.factory.setPassword(rmqConn.getPassword());
				this.factory.setVirtualHost(rmqConn.getVhost());
				this.factory.setHost(rmqConn.getHost());

			}
		
			this.conn = this.factory.newConnection();
	
		
				
				
				
			}
		catch (Exception ex) 
		{
		logger.error("getConnection failed", ex);
		}
		
	}

	public boolean consume(String exchange, String queue, String routingKey) {
		boolean consume = false;

		try {
			if (this.conn == null || !this.conn.isOpen()) {
				getConnection();
			}

			if (this.conn != null) {
				if (this.channel == null || !this.channel.isOpen()) {
					this.channel = this.conn.createChannel();
				}

				if (channel != null) {
					channel.queueDeclare(queue, true, false, false, null);
					consumeQueueData(channel,queue);
					consume = true;
				}
			}
		} catch (Exception ex) {
		logger.error("consume failed", ex);
		}

		return consume;
	}
	
	public boolean consumeQueueData(Channel channel, String queue) {
		boolean consume = false;

		Consumer consumer = new DefaultConsumer(channel) {
			  @Override
			  public void handleDelivery(String consumerTag, Envelope envelope,
			                             AMQP.BasicProperties properties, byte[] body)
			      throws IOException {
			      String message = new String(body, "UTF-8");
			      //System.out.println(" Received '" + message + "'");
			      getPayloadKeyFromString(message);
			  }
			};
		try {
			channel.basicConsume(queue, true, consumer);
		} catch (IOException e) {
		logger.error("consumeQueueData failed", e);
		}
	    consume = true;
	    
		return consume;
	}
	
	public void getPayloadKeyFromString(String message){
		try {
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(message);
		String key =  (String) json.get("key");
		JSONObject payload =  (JSONObject) json.get("payload");
		iConsumerService.keywiseDataRead(key,payload);
		
		} catch (ParseException e) {
		logger.error("getPayloadKeyFromString failed", e);
		} 
	}
}