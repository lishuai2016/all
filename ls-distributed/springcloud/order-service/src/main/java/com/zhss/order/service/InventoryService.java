package com.zhss.order.service;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.zhss.inventory.api.InventoryApi;

@FeignClient(value = "inventory-service")
public interface InventoryService extends InventoryApi {

}