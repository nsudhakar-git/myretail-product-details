package com.myretail.products;

/**
 * POJO to represent Product Price details.
 * 
 * @author Sudhakar
 *
 */

public class ProductDetails {


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
