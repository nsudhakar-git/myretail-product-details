#Product Detail Service

This directory contains the aggregation service that merges the results from Product name service and Product Price service.

##Setup
The application.properties contains the URLs that are used. The should be updated accordingly.

url.product.price=http://127.0.0.1:8081/product/
url.product.desc=http://127.0.0.1:8082/product/

##Sample response

A sample response for the URL 
http://127.0.0.1:8080/product/10023
is shown below.


{
  "productID": 10023,
  "price": {
    "currencycode": "USD",
    "amount": 23.22
  },
  "productName": "PName 10023"
}



