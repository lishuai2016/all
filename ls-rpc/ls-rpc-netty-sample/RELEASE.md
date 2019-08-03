# 分布式 RPC 框架 - 发布日志

## v1.3.0 - 20151206

### 改进

- 重构包名为 com.xxx.rpc

## v1.2.1 - 20150602

### 改进

- 对代码进行了优化

## v1.2.0 - 20150527

### 改进

- 将 rpc 配置文件中的 server.port 改为 service.address（需要配置本地 IP 地址）

## v1.1.1 - 20150506

### Bug 修复

- 修复了一个接口多个实现的 bug

### 改进

- 在 RpcRequest 中增加 serviceVersion 属性
- 将 RpcService 注解的 name 属性更名为 version
- 增加了必要的代码注释
- 优化了相关文档

## v1.1.0 - 20150505

### 新特性

- 在 RpcService 注解中提供 name 属性，可根据 name 注册服务名称

### 改进

- 优化了 rpc-common 的代码结构
- 优化了发布 RPC 服务的性能，只需连接 ZooKeeper 一次
- 简化了 log4j 的配置

## v1.0.1 - 20150430

### Bug 修复

- 解决了解码过程中数组越界的问题

### 改进

- 优化了 Maven 依赖配置

## v1.0.0 - 20150429

### 新特性

- 提供一个可用的 RPC 框架
- 将 ZooKeeper 作为服务注册的默认实现
