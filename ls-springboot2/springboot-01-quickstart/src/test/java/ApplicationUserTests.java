import com.ls.Application;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-15 11:04
 */
//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
@RunWith(SpringRunner.class)
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
@SpringBootTest(classes = Application.class)
//测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
@WebAppConfiguration
public class ApplicationUserTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception{
        //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }


    @Test
    public void getHello() throws Exception{

        /**
         * 1、mockMvc.perform执行一个请求。
         * 2、MockMvcRequestBuilders.get("XXX")构造一个请求。
         * 3、ResultActions.param添加请求传值
         * 4、ResultActions.accept(MediaType.TEXT_HTML_VALUE))设置返回类型
         * 5、ResultActions.andExpect添加执行完成后的断言。
         * 6、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情
         *   比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
         * 5、ResultActions.andReturn表示执行完成后返回相应的结果。
         */

        MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.get("/")
                .param("name","lishuai")
                .accept(MediaType.TEXT_HTML_VALUE))
                // .andExpect(MockMvcResultMatchers.status().isOk())             //等同于Assert.assertEquals(200,status);
                // .andExpect(MockMvcResultMatchers.content().string("hello lvgang"))    //等同于 Assert.assertEquals("hello lvgang",content);
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status=mvcResult.getResponse().getStatus();                 //得到返回代码
        String content=mvcResult.getResponse().getContentAsString();    //得到返回结果

        Assert.assertEquals(200,status);                        //断言，判断返回代码是否正确
        Assert.assertEquals("hello lishuai",content);            //断言，判断返回的值是否正确
    }



//    @Test
//    public void testHello() throws Exception {
//        mvc.perform(
//                MockMvcRequestBuilders.get("/hello")
//                .andExpect(status().isOk())// 判断接收到的状态是否是200（静态导入）
//                .andDo(print());// 打印请求和响应的详情
//    }
//
//
//
//    @Test
//    public void getHello() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcRequestBuilders.status().isOk())
//                .andExpect(content().string(equalTo("Hello World")));
//    }
//
//    @Test
//    public void testUserController() throws Exception {
////  	测试UserController
//        RequestBuilder request = null;
//
//        // 1、get查一下user列表，应该为空
//        request = get("/users/");
//        mvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(content().string(equalTo("[]")));
//
//        // 2、post提交一个user
//        request = post("/users/")
//                .param("id", "1")
//                .param("name", "测试大师")
//                .param("age", "20");
//        mvc.perform(request)
////				.andDo(MockMvcResultHandlers.print())
//                .andExpect(content().string(equalTo("success")));
//
//        // 3、get获取user列表，应该有刚才插入的数据
//        request = get("/users/");
//        mvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(content().string(equalTo("[{\"id\":1,\"name\":\"测试大师\",\"age\":20}]")));
//
//        // 4、put修改id为1的user
//        request = put("/users/1")
//                .param("name", "测试终极大师")
//                .param("age", "30");
//        mvc.perform(request)
//                .andExpect(content().string(equalTo("success")));
//
//        // 5、get一个id为1的user
//        request = get("/users/1");
//        mvc.perform(request)
//                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"测试终极大师\",\"age\":30}")));
//
//        // 6、del删除id为1的user
//        request = delete("/users/1");
//        mvc.perform(request)
//                .andExpect(content().string(equalTo("success")));
//
//        // 7、get查一下user列表，应该为空
//        request = get("/users/");
//        mvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(content().string(equalTo("[]")));
//
//    }


}
