package com.ls.mini.spring.ioc.beans.xml;

import com.ls.mini.spring.ioc.beans.AbstractBeanDefinitionReader;
import com.ls.mini.spring.ioc.beans.BeanDefinition;
import com.ls.mini.spring.ioc.BeanReference;
import com.ls.mini.spring.ioc.beans.PropertyValue;
import com.ls.mini.spring.ioc.beans.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 15:07
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(ResourceLoader resourceLoader) { //传入配置文件的加载类
        super(resourceLoader);
    }
    @Override
    public void loadBeanDefinitions(String location) throws Exception {

        InputStream inputStream = getResourceLoader().getResource(location).getInputStream();//获取配置文件加载器
        doLoadBeanDefinitions(inputStream);//解析配置文件，生成BeanDefinitions
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(inputStream);
        // 解析bean
        registerBeanDefinitions(doc);
        inputStream.close();
    }

    public void registerBeanDefinitions(Document doc) {
        Element root = doc.getDocumentElement();

        parseBeanDefinitions(root);
    }

    protected void parseBeanDefinitions(Element root) {
        NodeList nl = root.getChildNodes();//拿到的是根标签下的所有子标签列表
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                processBeanDefinition(ele);
            }
        }
    }
    // 把配置文件的Element 元素 封装成
    protected void processBeanDefinition(Element ele) {
        String name = ele.getAttribute("id");
        String className = ele.getAttribute("class");
        BeanDefinition beanDefinition = new BeanDefinition();
        processProperty(ele,beanDefinition);//处理属性标签
        beanDefinition.setBeanClassName(className);//设置bean对应的实现类
        getRegistry().put(name, beanDefinition);//注册beanDefinition
    }



    //带有引用属性的解析
    private void processProperty(Element ele, BeanDefinition beanDefinition) {
        NodeList propertyNode = ele.getElementsByTagName("property");
        for (int i = 0; i < propertyNode.getLength(); i++) {
            Node node = propertyNode.item(i);
            if (node instanceof Element) {
                Element propertyEle = (Element) node;
                String name = propertyEle.getAttribute("name");
                String value = propertyEle.getAttribute("value");//如果有value标签，且有值，则为普通的属性
                if (value != null && value.length() > 0) {
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
                } else { //在解析是否为ref属性
                    String ref = propertyEle.getAttribute("ref");
                    if (ref == null || ref.length() == 0) {
                        throw new IllegalArgumentException("Configuration problem: <property> element for property '"
                                + name + "' must specify a ref or value");
                    }
                    BeanReference beanReference = new BeanReference(ref);//封装引用类的名称，这里并没有实例化被引用的类
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
                }
            }
        }
    }
}
