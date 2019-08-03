package com.zhss.order.service;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.zhss.wms_api.WmsApi;

@FeignClient(value = "wms-service")
public interface WmsService extends WmsApi {

}