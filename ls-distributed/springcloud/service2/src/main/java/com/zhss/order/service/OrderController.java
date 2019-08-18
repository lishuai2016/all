package com.zhss.order.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {



	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String greeting(
			@RequestParam("productId") Long productId,
			@RequestParam("userId") Long userId,
			@RequestParam("count") Long count,
			@RequestParam("totalPrice") Long totalPrice) {
		System.out.println("我是服务2：创建订单");

		return "success";
	}

}
