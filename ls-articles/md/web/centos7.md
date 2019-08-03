# centos7 关闭防火墙
[centos7 关闭防火墙和selinux](https://www.jianshu.com/p/d6414b5295b8)

直接关闭防火墙
systemctl stop firewalld.service #停止firewall
systemctl disable firewalld.service #禁止firewall开机启动
systemctl list-unit-files|grep firewalld.service 

查看状态


# centos7安装nginx
[CentOS 7 安装 Nginx 1.10.1](https://www.linuxidc.com/Linux/2016-08/134450.htm)

下载对应当前系统版本的nginx包(package)
wget http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7-0.el7.ngx.noarch.rpm
建立nginx的yum仓库
rpm -ivh nginx-release-centos-7-0.el7.ngx.noarch.rpm
下载并安装nginx
yum install nginx
启动nginx服务
systemctl start nginx
　　
配置
默认的配置文件在 /etc/nginx 路径下，使用该配置已经可以正确地运行nginx；如需要自定义，修改其下的 nginx.conf 等文件即可。
user  nginx;
worker_processes  1;
error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;
events {
    worker_connections  1024;
}
http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';
    access_log  /var/log/nginx/access.log  main;
    sendfile        on;
    #tcp_nopush     on;
    keepalive_timeout  65;
    #gzip  on;
    include /etc/nginx/conf.d/*.conf;
upstream disconf {
    server 127.0.0.1:8015;
}
server {
    listen   8081;
    server_name disconf.com,localhost;
    access_log /home/work/var/logs/disconf/access.log;
    error_log /home/work/var/logs/disconf/error.log;
    location / {
        root /home/work/dsp/disconf-rd/war/html;
        if ($query_string) {
            expires max;
        }
    }
    location ~ ^/(api|export) {
        proxy_pass_header Server;
        proxy_set_header Host $http_host;
        proxy_redirect off;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Scheme $scheme;
        proxy_pass http://disconf;
    }
}
}


在虚拟机CentOS下，修改服务器配置文件中的端口号后，启动nginx，出现如下错误：
nginx: [emerg] bind() to 0.0.0.0:85 failed (13:Permission denied)
即权限拒绝，经查找资料是因为开启了SELinux导致。

getenforce 命令可以查看当前是否开启了SELinux。如果输出enforcing，那就是当前开启了SELinux；如果输出permissive或disabled，那就是当前关闭了SELinux。

1、临时关闭SELinux
     setenforce 0 ##关闭SELinux
     setenforce 1 ##开启SELinux
2、永久关闭SELinux
     修改/etc/selinux/config文件，将SELINUX=enforcing改为SELINUX=disabled。
     然后重启机器即可。


# centos7安装redis
http://www.cnblogs.com/hanshuai0921/p/7011994.html
另外一个安装教程

配置编译环境：
sudo yum install gcc-c++
centos快速安装redis
mkdir redis
cd redis
wget http://labfile.oss.aliyuncs.com/files0422/redis-2.8.9.tar.gz
解压
tar -xvfz redis-2.8.9.tar.gz
编译
cd redis-2.8.9
make
make install
 编译如果没报错,安装就算初步完成了.查看目录
cd src
adlist.c aof.c crc64.o hyperloglog.c memtest.c pqsort.c redis-benchmark.c redis.o sds.c solarisfixes.h t_string.o zipmap.o
adlist.h aof.o db.c hyperloglog.o memtest.o pqsort.h redis-benchmark.o redis-sentinel sds.h sort.c t_zset.c zmalloc.c
adlist.o asciilogo.h db.o intset.c migrate.c pqsort.o redis.c redis-server sds.o sort.o t_zset.o zmalloc.h
ae.c bio.c debug.c intset.h migrate.o pubsub.c redis-check-aof release.c sentinel.c syncio.c util.c zmalloc.o
ae_epoll.c bio.h debug.o intset.o mkreleasehdr.sh pubsub.o redis-check-aof.c release.h sentinel.o syncio.o util.h
ae_evport.c bio.o dict.c lzf_c.c multi.c rand.c redis-check-aof.o release.o setproctitle.c testhelp.h util.o
ae.h bitops.c dict.h lzf_c.o multi.o rand.h redis-check-dump replication.c setproctitle.o t_hash.c valgrind.sup
ae_kqueue.c bitops.o dict.o lzf_d.c networking.c rand.o redis-check-dump.c replication.o sha1.c t_hash.o version.h
ae.o config.c endianconv.c lzf_d.o networking.o rdb.c redis-check-dump.o rio.c sha1.h t_list.c ziplist.c
ae_select.c config.h endianconv.h lzf.h notify.c rdb.h redis-cli rio.h sha1.o t_list.o ziplist.h
anet.c config.o endianconv.o lzfP.h notify.o rdb.o redis-cli.c rio.o slowlog.c t_set.c ziplist.o
anet.h crc64.c fmacros.h Makefile object.c redisassert.h redis-cli.o scripting.c slowlog.h t_set.o zipmap.c
anet.o crc64.h help.h Makefile.dep object.o redis-benchmark redis.h scripting.o slowlog.o t_string.c zipmap.h
文件比较多,其中有用的就是几个可执行文件.
启动
./redis-server



# centos7安装webbench

一.安装webbench
1.如果没有安装GCC就先执行 
$ yum -y install gcc automake autoconf libtool make
2.安装ctags及创建一个文件夹
$ yum install ctags
$ mkdir -m 644 -p /usr/local/man/man1
3.获取及安装webbench
$ wget http://blog.zyan.cc/soft/linux/webbench/webbench-1.5.tar.gz
$ tar zxvf webbench-1.5.tar.gz
$ cd webbench-1.5
$ make && make install











