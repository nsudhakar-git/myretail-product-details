package com.myretail.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.myretail.Application;
import com.myretail.products.ProductDetails;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes=Application.class )
@EnableAsync
public class ProductControllerIT {

    @LocalServerPort
    private int port;

    private String base;
    private static final double DELTA = 1e-15;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = "http://localhost:" + port + "/";
        
    }

    @Test
    public void getHome() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        assertEquals(response.getBody().toString(), "My Retail Application to manage Product and prices");
    }
    
    @Test
    public void getProductwithPriceandName() throws Exception {
    	long productID=10024;
        ResponseEntity<ProductDetails> response = template.getForEntity(base.toString()+"/product/"+productID,
        		ProductDetails.class);
        ProductDetails responseProd = response.getBody();
        assertNotNull("Object null",responseProd);
        assertEquals(response.getHeaders().getContentType(),TestUtil.APPLICATION_JSON_UTF8);
        assertEquals(responseProd.getClass(), ProductDetails.class);
		assertEquals("Error comparing price",responseProd.getPrice().getAmount(),24024.22,DELTA);
    }
    
    @Test
    public void getProductwithPriceandNoName() throws Exception {
    	long productID=10025;
        ResponseEntity<ProductDetails> response = template.getForEntity(base.toString()+"/product/"+productID,
        		ProductDetails.class);
        ProductDetails responseProd = response.getBody();
        assertNotNull("Object null",responseProd);
        assertEquals(response.getHeaders().getContentType(),TestUtil.APPLICATION_JSON_UTF8);
        assertEquals(responseProd.getClass(), ProductDetails.class);
		assertEquals("Error comparing price",responseProd.getPrice().getAmount(),25025.22,DELTA);
    }
    
    @Test
    public void getProductwithNoPriceandNoName() throws Exception {
    	long productID=10030;
        ResponseEntity<ProductDetails> response = template.getForEntity(base.toString()+"/product/"+productID,
        		ProductDetails.class);
        ProductDetails responseProd = response.getBody();
        assertNotNull("Object null",responseProd);
        assertEquals(response.getHeaders().getContentType(),TestUtil.APPLICATION_JSON_UTF8);
        assertEquals(responseProd.getClass(), ProductDetails.class);
		assertNull("Verifying  name",responseProd.getProductName());

		assertNull("Verifying  price",responseProd.getPrice());
    }
    
    @Test
    public void getProductwithNameandNoPrice() throws Exception {
    	long productID=10026;
        ResponseEntity<ProductDetails> response = template.getForEntity(base.toString()+"/product/"+productID,
        		ProductDetails.class);
        ProductDetails responseProd = response.getBody();
        assertNotNull("Object null",responseProd);
        assertEquals(response.getHeaders().getContentType(),TestUtil.APPLICATION_JSON_UTF8);
        assertEquals(responseProd.getClass(), ProductDetails.class);
		assertNull("Verifying comparing price",responseProd.getPrice());
    }
}
