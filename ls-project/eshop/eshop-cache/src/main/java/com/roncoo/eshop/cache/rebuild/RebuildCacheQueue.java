package com.roncoo.eshop.cache.rebuild;

import java.util.concurrent.ArrayBlockingQueue;

import com.roncoo.eshop.cache.model.ProductInfo;

/**
 * 重建缓存的内存队列
 * @author Administrator
 *
 */
public class RebuildCacheQueue {

	private ArrayBlockingQueue<ProductInfo> queue = new ArrayBlockingQueue<ProductInfo>(1000);
	
	public void putProductInfo(ProductInfo productInfo) {
		try {
			queue.put(productInfo); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ProductInfo takeProductInfo() {
		try {
			return queue.take();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 内部单例类
	 * @author Administrator
	 *
	 */
	private static class Singleton {
		
		private static RebuildCacheQueue instance;
		
		static {
			instance = new RebuildCacheQueue();
		}
		
		public static RebuildCacheQueue getInstance() {
			return instance;
		}
		
	}
	
	public static RebuildCacheQueue getInstance() {
		return Singleton.getInstance();
	}
	
	public static void init() {
		getInstance();
	}
	
}
