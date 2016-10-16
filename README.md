
#Product Detail Service

This directory contains the Spring boot aggregation service that merges the results from Product name service and Product Price service.

To run , use maven install and run the jar.

##Setup
The API format is ```http://<hostname>:<port>/product/{productID}```

The springboot application is set to the default port 8080 and could be updated in the application.properties.

The application.properties contains the URLs that are invoked. The should be updated accordingly based on the hostname it is deployed.
* url.product.price=http://127.0.0.1:8081/product/
* url.product.desc=http://127.0.0.1:8082/product/

##Sample response

A sample response for the URL 
http://127.0.0.1:8080/product/10023
is shown below.

```
{
  "productID": 10023,
  "price": {
    "currencycode": "USD",
    "amount": 23.22
  },
  "productName": "PName 10023"
}
```


