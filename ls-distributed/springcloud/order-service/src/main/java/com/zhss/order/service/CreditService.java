package com.zhss.order.service;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.zhss.credit.api.CreditApi;

@FeignClient(value = "credit-service")
public interface CreditService extends CreditApi {

}