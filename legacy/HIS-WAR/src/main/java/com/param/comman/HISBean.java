package com.param.comman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.param.rabbitmq.util.conn.RabbitMQ;

@Service("hisBean")
public class HISBean {
	
	@Autowired
	RabbitMQ rabbitMQ;
	
	/*@Scheduled(fixedRate = 1000)
	public void processEvent() {

		try {
			boolean publish =  rabbitMQ.consume("HIS.EXCHANGE", "HIS", "HIS");
			if (publish) {
				////System.out.println("Message successfully consumed from RabbitMQ");
			} else {
				////System.out.println("Message consume failed");
			}
		} catch (Exception ex) {
			//System.out.println("Exception !!"+ ex);
		}
	}*/
	
}
