package com.roncoo.eshop.inventory.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.roncoo.eshop.inventory.dao.RedisDAO;
import com.roncoo.eshop.inventory.mapper.ProductInventoryMapper;
import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;

/**
 * 商品库存Service实现类
 * @author Administrator
 *
 */
@Service("productInventoryService")  
public class ProductInventoryServiceImpl implements ProductInventoryService {

	@Resource
	private ProductInventoryMapper productInventoryMapper;
	@Resource
	private RedisDAO redisDAO;
	
	@Override
	public void updateProductInventory(ProductInventory productInventory) {
		productInventoryMapper.updateProductInventory(productInventory); 
		System.out.println("===========日志===========: 已修改数据库中的库存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
	}
	
	@Override
	public void removeProductInventoryCache(ProductInventory productInventory) {
		String key = "product:inventory:" + productInventory.getProductId();
		redisDAO.delete(key);
		System.out.println("===========日志===========: 已删除redis中的缓存，key=" + key); 
	}
	
	/**
	 * 根据商品id查询商品库存
	 * @param productId 商品id 
	 * @return 商品库存
	 */
	public ProductInventory findProductInventory(Integer productId) {
		return productInventoryMapper.findProductInventory(productId);
	}
	
	/**
	 * 设置商品库存的缓存
	 * @param productInventory 商品库存
	 */
	public void setProductInventoryCache(ProductInventory productInventory) {
		String key = "product:inventory:" + productInventory.getProductId();
		redisDAO.set(key, String.valueOf(productInventory.getInventoryCnt()));
		System.out.println("===========日志===========: 已更新商品库存的缓存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt() + ", key=" + key);  
	}
	
	/**
	 * 获取商品库存的缓存
	 * @param productId
	 * @return
	 */
	public ProductInventory getProductInventoryCache(Integer productId) {
		Long inventoryCnt = 0L;
		
		String key = "product:inventory:" + productId;
		String result = redisDAO.get(key);
		
		if(result != null && !"".equals(result)) {
			try {
				inventoryCnt = Long.valueOf(result);
				return new ProductInventory(productId, inventoryCnt);
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}
		
		return null;
	}
	
}
