

- eureka-server    [注册中心](http://localhost:8761/)

- credit-api   
- credit-service [积分服务](http://localhost:8081)

- inventory-api
- inventory-service  [库存服务](http://localhost:8082)

- wms-api
- wms-service [仓储服务](http://localhost:8083)

- order-service [订单服务](http://localhost:9090)

- zuul-gateway  [zuul网关](http://localhost:9000)


请求网关发起服务调用：http://localhost:9000/order-service/order/create?productId=1&userId=1&count=1&totalPrice=1