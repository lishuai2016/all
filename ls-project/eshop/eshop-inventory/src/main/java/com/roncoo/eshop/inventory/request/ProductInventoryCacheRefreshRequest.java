package com.roncoo.eshop.inventory.request;

import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;

/**
 * 重新加载商品库存的缓存
 * @author Administrator
 *
 */
public class ProductInventoryCacheRefreshRequest implements Request {

	/**
	 * 商品id
	 */
	private Integer productId;
	/**
	 * 商品库存Service
	 */
	private ProductInventoryService productInventoryService;
	
	public ProductInventoryCacheRefreshRequest(Integer productId,
			ProductInventoryService productInventoryService) {
		this.productId = productId;
		this.productInventoryService = productInventoryService;
	}
	
	@Override
	public void process() {
		// 从数据库中查询最新的商品库存数量
		ProductInventory productInventory = productInventoryService.findProductInventory(productId);
		System.out.println("===========日志===========: 已查询到商品最新的库存数量，商品id=" + productId + ", 商品库存数量=" + productInventory.getInventoryCnt());  
		// 将最新的商品库存数量，刷新到redis缓存中去
		productInventoryService.setProductInventoryCache(productInventory); 
	}
	
	public Integer getProductId() {
		return productId;
	}
	
}
