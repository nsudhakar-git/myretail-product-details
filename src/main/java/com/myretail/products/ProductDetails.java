package com.myretail.products;

/**
 * POJO to represent Product Price details.
 * 
 * @author Sudhakar
 *
 */
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
public class ProductDetails {

	@Id
	long productID;
	Price price;

	String productName;

	String priceError;
	String nameError;

	
	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}


	public long getProductID() {
		return productID;
	}

	public void setProductID(long productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return productName + ":" + price.getAmount() + " " + price.getCurrencycode();
	}
}
