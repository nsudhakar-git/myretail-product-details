package com.myretail.util;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;



@Component
public class RestTemplateUTIL {

	Logger logger = LoggerFactory.getLogger(RestTemplateUTIL.class);
	private final RestTemplate restTemplate;

    public RestTemplateUTIL(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    
    @Async
	public  Future<JsonNode> getJSONObject(String string) {
    	JsonNode node = null;
    	try{
		 node = restTemplate.getForObject(string, JsonNode.class);
    	}catch(Exception e){
    		
			logger.error("Error in getting Price"+e.getMessage());
    	}
		return new AsyncResult<> (node);
	}

}
