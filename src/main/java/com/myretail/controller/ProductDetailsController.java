package com.myretail.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.myretail.exception.MyRetailException;
import com.myretail.products.ProductDetails;
import com.myretail.service.ProductService;
import com.myretail.util.JSONMerge;
import com.myretail.util.RestTemplateUTIL;

/**
 * Service controller for fetching the details about the product. Consolidates
 * call to Product Name API and the Product price API
 * 
 * @author Sudhakar
 *
 */
@RestController
public class ProductDetailsController {
	static Logger logger = LoggerFactory.getLogger(ProductDetailsController.class);

	private static final String APP_HOME = "My Retail Application to manage Product and prices";
	@Value("${url.product.price}")
	private String productPriceURL;

	@Value("${url.product.desc}")
	private String productDescURL;

	@Autowired
	private ProductService service;

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	RestTemplateUTIL restUtil;

	public ApplicationContext getAppContext() {
		return appContext;
	}

	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	@RequestMapping("/")
	public String index() {

		return APP_HOME;
	}

	/**
	 * Service to fetch the product details
	 * 
	 * @param productID
	 *            The product ID to be fetched
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/product/{productID}", produces = "application/JSON", method = RequestMethod.GET)
	public JsonNode getProductDetail(@PathVariable(value = "productID") long productID)
			throws InterruptedException, ExecutionException, IOException {
		logger.info("Fetch product detail..." + productID);

		// Call the rest services in sequence. This is done using async using
		// Future

		Future<JsonNode> node = service.fetchProductPrice(productID);

		Future<JsonNode> nodeName = service.fetchProductName(productID);

		// Wait for both calls to finish. Time out / max wait time can be set
		while (!(node.isDone() && nodeName.isDone())) {
			Thread.sleep(10);
		}
		// Combine the JSON nodes.
		// Assumes the product name node details are merged to price
		JsonNode nodenew = null;
		try {
			nodenew = JSONMerge.merge(node.get(), nodeName.get());
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Error in merging product price with name" + e.getMessage(), e);

		}
		logger.info(node.toString());
		return nodenew;
	}

	/**
	 * Service to update the product if it exists, else a new record is inserted
	 * 
	 * @param productID
	 *            The product to be updated
	 * @param productb
	 *            The request body containing the details
	 * @return
	 * @throws MyRetailException
	 */
	@RequestMapping(value = "/product/{productID}", produces = "application/text", method = RequestMethod.PUT)
	public String addProduct(@PathVariable(value = "productID") long productID, @RequestBody ProductDetails productb)
			throws MyRetailException {
		logger.info("Update product..." + productID);
		// Setup the resource to be found
		productb.setProductID(productID);

		RestTemplate restTemplate = new RestTemplate();
		// Apply the JSON input to update the record
		restTemplate.put(productPriceURL + productID, productb);

		return "{updated(PUT): " + productb.toString() + "}";
	}

}
