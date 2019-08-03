# netty使用的经典模式


## 服务端

            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                // 创建并初始化 Netty 服务端 Bootstrap 对象
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup);
                bootstrap.channel(NioServerSocketChannel.class);
                bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new RpcDecoder(RpcRequest.class)); // 解码 RPC 请求
                        pipeline.addLast(new RpcEncoder(RpcResponse.class)); // 编码 RPC 响应
                        pipeline.addLast(new RpcServerHandler(handlerMap)); // 处理 RPC 请求（自己的核心业务逻辑）
                    }
                });
                bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
                bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
                
                LOGGER.debug("server started on port {}", port); //阻塞在这里等待客户端连接
                // 关闭服务器
                future.channel().closeFuture().sync();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }

自己业务handler编写，继承SimpleChannelInboundHandler

    public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    
        private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);
    
        private final Map<String, Object> handlerMap;
    
        public RpcServerHandler(Map<String, Object> handlerMap) {
            this.handlerMap = handlerMap;
        }
    
        @Override
        public void channelRead0(final ChannelHandlerContext ctx, RpcRequest request) throws Exception {
            // 创建并初始化 RPC 响应对象
            RpcResponse response = new RpcResponse();
            response.setRequestId(request.getRequestId());
            try {
                Object result = handle(request);
                response.setResult(result);
            } catch (Exception e) {
                LOGGER.error("handle result failure", e);
                response.setException(e);
            }
            // 写入 RPC 响应对象并自动关闭连接
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    
        private Object handle(RpcRequest request) throws Exception {
            // 获取服务对象
            String serviceName = request.getInterfaceName();
            String serviceVersion = request.getServiceVersion();
            if (StringUtil.isNotEmpty(serviceVersion)) {
                serviceName += "-" + serviceVersion;
            }
            Object serviceBean = handlerMap.get(serviceName);
            if (serviceBean == null) {
                throw new RuntimeException(String.format("can not find service bean by key: %s", serviceName));
            }
            // 获取反射调用所需的参数
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = request.getMethodName();
            Class<?>[] parameterTypes = request.getParameterTypes();
            Object[] parameters = request.getParameters();
            // 执行反射调用
    //        Method method = serviceClass.getMethod(methodName, parameterTypes);
    //        method.setAccessible(true);
    //        return method.invoke(serviceBean, parameters);
            // 使用 CGLib 执行反射调用
            FastClass serviceFastClass = FastClass.create(serviceClass);
            FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
            return serviceFastMethod.invoke(serviceBean, parameters);
        }
    
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            LOGGER.error("server caught exception", cause);
            ctx.close();
        }
    }

## 客户端

    public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {
    
        private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);
    
        private final String host;
        private final int port;
    
        private RpcResponse response;
    
        public RpcClient(String host, int port) {
            this.host = host;
            this.port = port;
        }
    
        @Override
        public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
            this.response = response;
        }
    
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            LOGGER.error("api caught exception", cause);
            ctx.close();
        }
    
        public RpcResponse send(RpcRequest request) throws Exception {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                // 创建并初始化 Netty 客户端 Bootstrap 对象
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new RpcEncoder(RpcRequest.class)); // 编码 RPC 请求
                        pipeline.addLast(new RpcDecoder(RpcResponse.class)); // 解码 RPC 响应
                        pipeline.addLast(RpcClient.this); // 处理 RPC 响应
                    }
                });
                bootstrap.option(ChannelOption.TCP_NODELAY, true);
                // 连接 RPC 服务器
                ChannelFuture future = bootstrap.connect(host, port).sync();
                // 写入 RPC 请求数据并关闭连接
                Channel channel = future.channel();
                channel.writeAndFlush(request).sync();
                channel.closeFuture().sync();
                // 返回 RPC 响应对象
                return response;
            } finally {
                group.shutdownGracefully();
            }
        }
    }

