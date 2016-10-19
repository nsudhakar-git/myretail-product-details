package com.myretail.util;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class AsyncRESTcall {

	private static final Logger logger = LoggerFactory.getLogger(AsyncRESTcall.class);

	private final RestTemplate restTemplate;

	public AsyncRESTcall(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Async
	public Future<JsonNode> getJSON(String uri) throws InterruptedException {
		logger.info("Looking up " + uri);
		JsonNode results = restTemplate.getForObject(uri, JsonNode.class);
		return new AsyncResult<>(results);
	}

}
