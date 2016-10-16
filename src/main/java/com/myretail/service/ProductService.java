package com.myretail.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.util.RestTemplateUTIL;

@Service
public class ProductService {
	static Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Value("${url.product.price}")
	private String productPriceURL;

	@Value("${url.product.desc}")
	private String productDescURL;

	@Autowired
	RestTemplateUTIL restUtil;

	public Future<JsonNode> fetchProductPrice(long productID)
			throws InterruptedException, ExecutionException, JsonProcessingException, IOException {
		logger.info("Fetch product detail..." + productID);
		String errorStr = "";
		char quotes = '"';
		// Call the rest services in sequence. This could be done using async
		// /Future if
		// further performance improvements are needed
		Future<JsonNode> node = null;
		try {
			node = restUtil.getJSONObject(productPriceURL + productID);
		} catch (Exception e) {
			logger.error("Error Requesting the price" + e);
			errorStr = "{ " + quotes + "productID " + quotes + ":" + productID + " , " + quotes + "price " + quotes
					+ ": " + quotes + "Price Unavailable" + quotes + "}";

			// Perform error notifications
		}
		// Simple error JSON. This could be updated to add error codes based on
		// data available
		if (node.get() != null) {
			logger.info(node.toString());
		} else {
			errorStr = "{ " + quotes + "productID " + quotes + ":" + productID + " , " + quotes + "price " + quotes
					+ ": " + quotes + "Price Unavailable" + quotes + "}";
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonError = mapper.readTree(errorStr);
			// jsonError = JsonNodeFactory.instance.textNode(errorStr);
			return new AsyncResult<>(jsonError);
		}

		return node;
	}

	public Future<JsonNode> fetchProductName(long productID)
			throws InterruptedException, ExecutionException, JsonProcessingException, IOException {
		Future<JsonNode> nodeName = null;
		char quotes = '"';
		String errorStr = "{ " + quotes + "productID" + quotes + ":" + productID + " , " + quotes + "productName"
				+ quotes + ":" + quotes + "Product Name Unavailable" + quotes + "}";
		JsonNode jsonError = null;

		nodeName = restUtil.getJSONObject(productDescURL + productID);
		logger.info("After node name");
		if (nodeName.get() != null) {
			logger.info(nodeName.toString());
		} else {
			ObjectMapper mapper = new ObjectMapper();
			jsonError = mapper.readTree(errorStr);
			// jsonError = JsonNodeFactory.instance.textNode(errorStr);
			return new AsyncResult<>(jsonError);
		}

		return nodeName;
	}
}