package com.roncoo.eshop.cache.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.roncoo.eshop.cache.kafka.KafkaConsumer;
import com.roncoo.eshop.cache.rebuild.RebuildCacheThread;
import com.roncoo.eshop.cache.spring.SpringContext;
import com.roncoo.eshop.cache.zk.ZooKeeperSession;

/**
 * 系统初始化的监听器
 * @author Administrator
 *
 */
public class InitListener implements ServletContextListener {
	
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
		SpringContext.setApplicationContext(context);  
		
		new Thread(new KafkaConsumer("cache-message")).start();
		new Thread(new RebuildCacheThread()).start();
		
		ZooKeeperSession.init();
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
