package com.myretail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.myretail.exception.MyRetailException;
import com.myretail.products.ProductDetails;
import com.myretail.util.JSONMerge;
/**
 * Service controller for fetching the details about the product. Consolidates call to 
 * Product Name API and the Product price API
 * 
 * @author Sudhakar
 *
 */
@RestController
public class ProductDetailsController {
	static Logger logger = LoggerFactory.getLogger(ProductDetailsController.class);

	@Value("${url.product.price}")
	private String productPriceURL;

	@Value("${url.product.desc}")
	private String productDescURL;

	@Autowired
	private ApplicationContext appContext;

	public ApplicationContext getAppContext() {
		return appContext;
	}

	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	@RequestMapping("/")
	public String index() {
		return "My Retail Application to manage Product and prices";
	}

	/**
	 * Service to fetch the product details
	 * 
	 * @param productID
	 *            The product ID to be fetched
	 * @return
	 */
	@RequestMapping(value = "/product/{productID}", produces = "application/JSON", method = RequestMethod.GET)
	public JsonNode getProductDetail(@PathVariable(value = "productID") long productID) {
		logger.info("Fetch product detail..." + productID);
		RestTemplate restTemplate = new RestTemplate();
		
		//Call the rest services in sequence. This could be done using async /Future if 
		//further performance improvements are needed
		JsonNode node=null;		
		try{
		   node = restTemplate.getForObject(productPriceURL + productID, JsonNode.class);
		}catch(Exception e){
			logger.error("Error Requesting the price"+e);
		   //Perform error notifications
		}
		//Simple error JSON. This could be updated to add error codes based on data available
		String errorStr = "{'productID':" + productID + " ,'price':Price Unavailable}";
		if (node != null) {
			logger.info(node.toString());
		} else {
			node = JsonNodeFactory.instance.textNode(errorStr);

		}

		JsonNode nodeName = restTemplate.getForObject(productDescURL + productID, JsonNode.class);

		if (nodeName != null) {
			logger.info(node.toString());
			logger.info(nodeName.toString());
		} else {
			errorStr = "{'productID':" + productID + " ,'productName':'Product Name Unavailable'}";
			nodeName = JsonNodeFactory.instance.textNode(errorStr);
		}
		//Combine the JSON nodes.
		// Assumes the product name node details are merged to price 
		node = JSONMerge.merge(node, nodeName);
		logger.info(node.toString());
		return node;
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
		System.out.println("Update product..." + productID);
		//Setup the resource to be found
		productb.setProductID(productID);

		RestTemplate restTemplate = new RestTemplate();
		//Apply the JSON input to update the record
		restTemplate.put(productPriceURL + productID, productb);

		return "{updated(PUT): " + productb.toString() + "}";
	}

}
