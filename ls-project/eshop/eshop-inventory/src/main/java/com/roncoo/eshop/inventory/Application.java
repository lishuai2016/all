package com.roncoo.eshop.inventory;

import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.roncoo.eshop.inventory.listener.InitListener;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@MapperScan("com.roncoo.eshop.inventory.mapper")
public class Application {
 
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return new DataSource();
    }
    
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }
 
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
    @Bean
	public JedisCluster JedisClusterFactory() {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort("192.168.31.19", 7003));
		jedisClusterNodes.add(new HostAndPort("192.168.31.19", 7004));
		jedisClusterNodes.add(new HostAndPort("192.168.31.227", 7006));
		JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes);
		return jedisCluster;
	}
    
    /**
     * 注册监听器
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
    	ServletListenerRegistrationBean servletListenerRegistrationBean = 
    			new ServletListenerRegistrationBean();
    	servletListenerRegistrationBean.setListener(new InitListener());  
    	return servletListenerRegistrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
 
}