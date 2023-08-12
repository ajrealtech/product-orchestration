package com.org.listener.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.listener.dto.ProductSearch;

@Service
public class KafkaConsumer {
	
	Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
	
	@Autowired
	ProductWorkFlowService workflowService;
	
	public void workflow() {
		while(true) {
			String brokers = "glider.srvs.cloudkafka.com:9094";
			String username = "eidflxfs";
			String password = "PxIX3BTEcbFJbljRiixvpCVAuuB1DkIN";
			KafkaExample c = new KafkaExample(brokers, username, password);
			ProductSearch message = c.consume();
			workflowService.processWorkflow(message);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
