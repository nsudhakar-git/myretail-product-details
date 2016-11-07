package com.myretail.service;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.exception.MyRetailException;
import com.myretail.products.ProductDetails;
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

	public Future<JsonNode> fetchProductPrice(long productID) {
		logger.info("Fetch product detail..." + productID);
		String errorStr;
		char quotes = '"';
		// Call the rest services in sequence. This could be done using async
		// /Future if
		// further performance improvements are needed
		Future<JsonNode> node = null;
		try {
			node = restUtil.getJSONObject(productPriceURL + productID);

			// Simple error JSON. This could be updated to add error codes based
			// on data available
			if (node != null) {
				//Product Price Call successful
				logger.info(node.toString());
			} else {
				errorStr = "{ " + quotes + "productID" + quotes + ":" + productID + " , " + quotes + "priceError:" + quotes
						+ ": " + quotes + "Price Unavailable" + quotes + "}";
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonError = mapper.readTree(errorStr);
				return new AsyncResult<>(jsonError);
			}
		} catch (Exception e) {
			logger.error("Error Requesting the price" + e);

		}

		return node;
	}

	public Future<JsonNode> fetchProductName(long productID) {
		Future<JsonNode> nodeName = null;
		char quotes = '"';
		String errorStr = "{ " + quotes + "productID" + quotes + ":" + productID + " , " + quotes + "nameError"
				+ quotes + ":" + quotes + "Product Name Unavailable" + quotes + "}";
		JsonNode jsonError;
		try {
			nodeName = restUtil.getJSONObject(productDescURL + productID);
			logger.info("After node name");
			if (nodeName.get() != null) {
				//Product Name call successful
				logger.info(nodeName.toString());
			} else {
				ObjectMapper mapper = new ObjectMapper();
				jsonError = mapper.readTree(errorStr);
				return new AsyncResult<>(jsonError);
			}
		} catch (Exception e) {
			logger.error("Error Requesting the price" + e);

		}

		return nodeName;
	}
	
	public boolean updateProduct(long productID, ProductDetails productb)
			throws MyRetailException {
		logger.info("Update product..." + productID);
		// Setup the resource to be found
		productb.setProductID(productID);

		// Apply the JSON input to update the record
		return restUtil.putJSONObject(productPriceURL + productID, productb);

	}

}