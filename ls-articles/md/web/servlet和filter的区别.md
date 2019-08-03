# servlet和filter的区别

servlet是一种运行服务器端的java应用程序，具有独立于平台和协议的特性，并且可以动态的生成web页面，它工作在客户端请求与服务器响应的中间层。
Servlet 的主要功能在于交互式地浏览和修改数据，生成动态 Web 内容。

filter是一个可以复用的代码片段，可以用来转换HTTP请求、响应和头信息。Filter不像Servlet，它不能产生一个请求或者响应，它只是修改对某一资源的请求，或者修改从某一的响应。Servlet中的过滤器Filter是实现了javax.servlet.Filter接口的服务器端程序，主要的用途是过滤字符编码、做一些业务逻辑判断等。其工作原理是，只要你在web.xml文件配置好要拦截的客户端请求，它都会帮你拦截到请求，此时你就可以对请求或响应(Request、Response)统一设置编码，简化操作；同时还可进行逻辑判断，如用户是否已经登陆、有没有权限访问该页面等等工作。它是随你的web应用启动而启动的，只初始化一次，以后就可以拦截相关请求，只有当你的web应用停止或重新部署的时候才销毁。Filter可认为是Servlet的一种“变种”，它主要用于对用户请求进行预处理，也可以对HttpServletResponse进行后处理，是个典型的处理链。它与Servlet的区别在于：它不能直接向用户生成响应。完整的流程是：Filter对用户请求进行预处理，接着将请求交给Servlet进行处理并生成响应，最后Filter再对服务器响应进行后处理。

Filter有如下几个用处。

    - 在HttpServletRequest到达Servlet之前，拦截客户的HttpServletRequest。
    - 根据需要检查HttpServletRequest，也可以修改HttpServletRequest头和数据。
    - 在HttpServletResponse到达客户端之前，拦截HttpServletResponse。
    - 根据需要检查HttpServletResponse，也可以修改HttpServletResponse头和数据。

Filter有如下几个种类。

    - 用户授权的Filter：Filter负责检查用户请求，根据请求过滤用户非法请求。
    - 日志Filter：详细记录某些特殊的用户请求。
    - 负责解码的Filter：包括对非标准编码的请求解码。
    - 能改变XML内容的XSLT Filter等。
    - Filter可负责拦截多个请求或响应；一个请求或响应也可被多个请求拦截。

创建一个Filter只需两个步骤：

    - 建Filter处理类；
    - web.xml文件中配置Filter。
