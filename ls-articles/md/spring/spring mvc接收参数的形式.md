---
title: spring mvc接收参数的形式
categories: 
- spring
tags:
---

# springmvc请求参数获取的几种方法

1、直接把表单的参数写在Controller相应的方法的形参中，适用于get方式提交，不适用于post方式提交。

    /**
         * 1.直接把表单的参数写在Controller相应的方法的形参中
          * @param username
         * @param password
         * @return
         */
        @RequestMapping("/addUser1")
        public String addUser1(String username,String password) {
            System.out.println("username is:"+username);
            System.out.println("password is:"+password);
            return "demo/index";
        }

url形式：http://localhost/SSMDemo/demo/addUser1?username=lixiaoxi&password=111111 提交的参数需要和Controller方法中的入参名称一致。

2、通过HttpServletRequest接收，post方式和get方式都可以。

    /**
     * 2、通过HttpServletRequest接收
      * @param request
     * @return
     */
    @RequestMapping("/addUser2")
    public String addUser2(HttpServletRequest request) {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        System.out.println("username is:"+username);
        System.out.println("password is:"+password);
        return "demo/index";
    }

3、通过一个bean来接收,post方式和get方式都可以。
(1)建立一个和表单中参数对应的bean

    package demo.model;
    
    public class UserModel {
        
        private String username;
        private String password;
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        
    }

(2)用这个bean来封装接收的参数

    /**
     * 3、通过一个bean来接收
      * @param user
     * @return
     */
    @RequestMapping("/addUser3")
    public String addUser3(UserModel user) {
        System.out.println("username is:"+user.getUsername());
        System.out.println("password is:"+user.getPassword());
        return "demo/index";
    }


4、通过@PathVariable获取路径中的参数

    /**
     * 4、通过@PathVariable获取路径中的参数
      * @param username
     * @param password
     * @return
     */
    @RequestMapping(value="/addUser4/{username}/{password}",method=RequestMethod.GET)
    public String addUser4(@PathVariable String username,@PathVariable String password) {
        System.out.println("username is:"+username);
        System.out.println("password is:"+password);
        return "demo/index";
    }

例如，访问http://localhost/SSMDemo/demo/addUser4/lixiaoxi/111111 路径时，则自动将URL中模板变量{username}和{password}绑定到通过@PathVariable注解的同名参数上，即入参后username=lixiaoxi、password=111111。

5、使用@ModelAttribute注解获取POST请求的FORM表单数据
Jsp表单如下：

    <form action ="<%=request.getContextPath()%>/demo/addUser5" method="post"> 
         用户名:&nbsp;<input type="text" name="username"/><br/>
         密&nbsp;&nbsp;码:&nbsp;<input type="password" name="password"/><br/>
         <input type="submit" value="提交"/> 
         <input type="reset" value="重置"/> 
    </form> 

Java Controller如下：

    /**
     * 5、使用@ModelAttribute注解获取POST请求的FORM表单数据
      * @param user
     * @return
     */
    @RequestMapping(value="/addUser5",method=RequestMethod.POST)
    public String addUser5(@ModelAttribute("user") UserModel user) {
        System.out.println("username is:"+user.getUsername());
        System.out.println("password is:"+user.getPassword());
        return "demo/index";
    }

6、用注解@RequestParam绑定请求参数到方法入参
当请求参数username不存在时会有异常发生,可以通过设置属性required=false解决,例如: @RequestParam(value="username", required=false)

    /**
     * 6、用注解@RequestParam绑定请求参数到方法入参
      * @param username
     * @param password
     * @return
     */
    @RequestMapping(value="/addUser6",method=RequestMethod.GET)
    public String addUser6(@RequestParam("username") String username,@RequestParam("password") String password) {
        System.out.println("username is:"+username);
        System.out.println("password is:"+password);
        return "demo/index";
    }



问题：
有一次在部署应用的时候，在使用的jdk6 tomcat6 ，造成通过http get方式发生的参数均接收不到，而且HttpServletRequest也为null，换成jdk7  tomcat7就好了
其中1.7.0_71+tomcat6也可以

# 注解详解

handler method 参数绑定常用的注解,我们根据他们处理的Request的不同内容部分分为四类：（主要讲解常用类型）
A、处理requet uri 部分（这里指uri template中variable，不含queryString部分）的注解：   @PathVariable;

B、处理request header部分的注解：   @RequestHeader, @CookieValue;

C、处理request body部分的注解：@RequestParam,  @RequestBody;

D、处理attribute类型是注解： @SessionAttributes, @ModelAttribute;

 

## @PathVariable 
当使用@RequestMapping URI template 样式映射时， 即 someUrl/{paramId}, 这时的paramId可通过 @Pathvariable注解绑定它传过来的值到方法的参数上。
示例代码：

    @Controller  
    @RequestMapping("/owners/{ownerId}")  
    public class RelativePathUriTemplateController {  
      
      @RequestMapping("/pets/{petId}")  
      public void findPet(@PathVariable String ownerId, @PathVariable String petId, Model model) {      
        // implementation omitted  
      }  
    }  
上面代码把URI template 中变量 ownerId的值和petId的值，绑定到方法的参数上。若方法参数名称和需要绑定的uri template中变量名称不一致，需要在@PathVariable("name")指定uri template中的名称。

## @RequestHeader、@CookieValue
@RequestHeader 注解，可以把Request请求header部分的值绑定到方法的参数上。
示例代码：这是一个Request 的header部分：

    Host                    localhost:8080  
    Accept                  text/html,application/xhtml+xml,application/xml;q=0.9  
    Accept-Language         fr,en-gb;q=0.7,en;q=0.3  
    Accept-Encoding         gzip,deflate  
    Accept-Charset          ISO-8859-1,utf-8;q=0.7,*;q=0.7  
    Keep-Alive              300  


    @RequestMapping("/displayHeaderInfo.do")  
    public void displayHeaderInfo(@RequestHeader("Accept-Encoding") String encoding,  
                                  @RequestHeader("Keep-Alive") long keepAlive)  {  
      
      //...  
      
    }  
上面的代码，把request header部分的 Accept-Encoding的值，绑定到参数encoding上了， Keep-Alive header的值绑定到参数keepAlive上。


@CookieValue 可以把Request header中关于cookie的值绑定到方法的参数上。
例如有如下Cookie值：
JSESSIONID=415A4AC178C59DACE0B2C9CA727CDD84  
参数绑定的代码：

    @RequestMapping("/displayHeaderInfo.do")  
    public void displayHeaderInfo(@CookieValue("JSESSIONID") String cookie)  {  
      
      //...  
      
    }  
即把JSESSIONID的值绑定到参数cookie上。


## @RequestParam, @RequestBody
1、@RequestParam 
A） 常用来处理简单类型的绑定，通过Request.getParameter() 获取的String可直接转换为简单类型的情况（ String--> 简单类型的转换操作由ConversionService配置的转换器来完成）；因为使用request.getParameter()方式获取参数，所以可以处理get 方式中queryString的值，也可以处理post方式中 body data的值；
B）用来处理Content-Type: 为 application/x-www-form-urlencoded编码的内容，提交方式GET、POST；
C) 该注解有两个属性： value、required； value用来指定要传入值的id名称，required用来指示参数是否必须绑定；

示例代码：


    @Controller  
    @RequestMapping("/pets")  
    @SessionAttributes("pet")  
    public class EditPetForm {  
  
    // ...  
  
    @RequestMapping(method = RequestMethod.GET)  
    public String setupForm(@RequestParam("petId") int petId, ModelMap model) {  
        Pet pet = this.clinic.loadPet(petId);  
        model.addAttribute("pet", pet);  
        return "petForm";  
    }  
  
    // ...  


2、@RequestBody
该注解常用来处理Content-Type: 不是application/x-www-form-urlencoded编码的内容，例如application/json, application/xml等；
它是通过使用HandlerAdapter 配置的HttpMessageConverters来解析post data body，然后绑定到相应的bean上的。
因为配置有FormHttpMessageConverter，所以也可以用来处理 application/x-www-form-urlencoded的内容，处理完的结果放在一个MultiValueMap<String, String>里，这种情况在某些特殊需求下使用，详情查看FormHttpMessageConverter api;
示例代码：

    @RequestMapping(value = "/something", method = RequestMethod.PUT)  
    public void handle(@RequestBody String body, Writer writer) throws IOException {  
      writer.write(body);  
    }  

## @SessionAttributes, @ModelAttribute
1、@SessionAttributes:
该注解用来绑定HttpSession中的attribute对象的值，便于在方法中的参数里使用。
该注解有value、types两个属性，可以通过名字和类型指定要使用的attribute 对象；

示例代码：

    @Controller  
    @RequestMapping("/editPet.do")  
    @SessionAttributes("pet")  
    public class EditPetForm {  
        // ...  
    }  


2、@ModelAttribute
该注解有两个用法，一个是用于方法上，一个是用于参数上；
用于方法上时：  通常用来在处理@RequestMapping之前，为请求绑定需要从后台查询的model；
用于参数上时： 用来通过名称对应，把相应名称的值绑定到注解的参数bean上；要绑定的值来源于：

A） @SessionAttributes 启用的attribute 对象上；

B） @ModelAttribute 用于方法上时指定的model对象；

C） 上述两种情况都没有时，new一个需要绑定的bean对象，然后把request中按名称对应的方式把值绑定到bean中。

用到方法上@ModelAttribute的示例代码：

    // Add one attribute  
    // The return value of the method is added to the model under the name "account"  
    // You can customize the name via @ModelAttribute("myAccount")  
      
    @ModelAttribute  
    public Account addAccount(@RequestParam String number) {  
        return accountManager.findAccount(number);  
    }  

这种方式实际的效果就是在调用@RequestMapping的方法之前，为request对象的model里put（“account”， Account）；

用在参数上的@ModelAttribute示例代码：

    @RequestMapping(value="/owners/{ownerId}/pets/{petId}/edit", method = RequestMethod.POST)  
    public String processSubmit(@ModelAttribute Pet pet) {  
         
    }  
首先查询 @SessionAttributes有无绑定的Pet对象，若没有则查询@ModelAttribute方法层面上是否绑定了Pet对象，若没有则将URI template中的值按对应的名称绑定到Pet对象的各属性上。


# 参考
https://www.cnblogs.com/xiaoxi/p/5695783.html
https://blog.csdn.net/kobejayandy/article/details/12690161