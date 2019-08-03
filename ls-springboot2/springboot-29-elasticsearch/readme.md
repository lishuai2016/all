# ELT整合记录


[官方文档]
https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-bulk.html
https://blog.csdn.net/ming_311/article/details/50619859


## 1、elasticsearch-6.5.3 批量导入json数据

kibana官方给的测试数据
https://www.elastic.co/products/kibana
https://www.elastic.co/guide/en/kibana/current/tutorial-load-dataset.html
下载后使用curl导入，格式如下：

curl -H "Content-Type: application/x-ndjson" -XPOST "localhost:9200/bank/account/_bulk?pretty" --data-binary @accounts.json
curl -H "Content-Type: application/x-ndjson" -XPOST "localhost:9200/shakespeare/doc/_bulk?pretty" --data-binary @shakespeare_6.0.json
curl -H "Content-Type: application/x-ndjson" -XPOST "localhost:9200/_bulk?pretty" --data-binary @logs.jsonl
备注：需要加双引号，官方给的是单引号

导入自己编写的json数据
curl -XPOST localhost:9200/_bulk -H "Content-Type: application/json" --data-binary @movie_names.json
movie_names.json  自己写的json数据，需要在文件的最后保留一个空行
```json
{"index": {"_index": "wordbank", "_type": "movie", "_id": 1}}
{"doc": {"name": "权力的游戏"}}
{"index": {"_index": "wordbank", "_type": "movie", "_id": 2}}
{"doc": {"name": "熊出没"}}

```



## 2、启动elasticsearch、head插件、kibana

### 2.1、启动elasticsearch
D:\anzhuangbao\elasticsearch-6.5.3\bin\elasticsearch.bat
elasticsearch启动访问路径：
http://localhost:9200/



### 2.2、启动head插件(不启动，访问不了)
D:\anzhuangbao\elasticsearch-head-master>grunt server
head插件访问路径：
http://localhost:9100/

### 2.3、
D:\anzhuangbao\kibana-6.5.4-windows-x86_64\bin\kibana.bat
访问路径：
http://localhost:5601/app/kibana








## 3、elasticsearch6.5.3、Head插件、kibana
### 3.1、安装elasticsearch6.5.3、Head插件
es5以上版本安装head需要安装node和grunt(之前的直接用plugin命令即可安装)

(一)从地址：https://nodejs.org/en/download/ 下载相应系统的msi，双击安装。node-v10.14.2-x64.msi

（二）安装完成用cmd进入安装目录执行 node -v可查看版本号
C:\Users\lishuai29>node -v
v10.14.2

（三）执行 npm install -g grunt-cli 安装grunt ，安装完成后执行grunt -version查看是否安装成功，会显示安装的版本号
C:\Users\lishuai29>grunt -version
grunt-cli v1.3.2

（四）开始安装head
① 进入安装目录下的config目录，修改elasticsearch.yml文件.在文件的末尾加入以下代码
http.cors.enabled: true 
http.cors.allow-origin: "*"
node.master: true
node.data: true
然后去掉network.host: 192.168.0.1的注释并改为network.host: 0.0.0.0，
去掉cluster.name；node.name；http.port的注释（也就是去掉#）

②双击elasticsearch.bat重启es
③在https://github.com/mobz/elasticsearch-head中下载head插件，选择下载zip

④解压到指定文件夹下，D:\anzhuangbao\elasticsearch-head-master  进入该文件夹，
修改D:\anzhuangbao\elasticsearch-head-master\Gruntfile.js 在对应的位置加上hostname:'*'
如：
		connect: {
			server: {
				options: {
					hostname:'*',
					port: 9100,
					base: '.',
					keepalive: true
				}
			}
		}
⑤在D:\anzhuangbao\elasticsearch-head-master
下执行npm install 安装完成后执行grunt server 或者npm run start 运行head插件，如果不成功重新安装grunt。成功如下


D:\anzhuangbao\elasticsearch-head-master>grunt server
Running "connect:server" (connect) task
Waiting forever...
Started connect web server on http://localhost:9100

⑥浏览器下访问http://localhost:9100/
[参考](https://www.cnblogs.com/hts-technology/p/8477258.html)

### 3.2、安装elasticsearch6.5.3和kibana
直接在官网https://www.elastic.co/downloads下载安装包*.zip，解压即可启动bat文件
