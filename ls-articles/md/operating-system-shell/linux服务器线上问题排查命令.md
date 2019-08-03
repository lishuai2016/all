# Linux命令类

## tail
最常用的tail -f  
`tail -300f shopbase.log #倒数300行并进入实时监听文件写入模式`

## grep 查找文件中包含的搜索字符串
- grep forest f.txt     #文件查找
- grep forest f.txt cpf.txt #多文件查找
- grep 'log' /home/admin -r -n #目录下查找所有符合关键字的文件  [-n 显示行号 都可以在grep后面添加上该参数]
- cat f.txt | grep -i shopbase    [-i 忽略大小写]
- grep keep /home/ads_app/lishuai -r -n --include=*.txt --include=*.jar   #指定文件后缀
- grep keep /home/ads_app/lishuai -r -n --exclude=*.txt #反匹配
- seq 10 | grep 5 -A 3    #上匹配   输出5 6 7 8
- seq 10 | grep 5 -B 3    #下匹配   输出2 3 4 5
- seq 10 | grep 5 -C 3    #上下匹配，    输出2 3 4 5 6 7 8
- cat f.txt | grep -c 'SHOPBASE'   [-c 统计   SHOPBASE出现的次数]


## awk 分析文件中的内容
### 1.基础命令

- awk '{print $4,$6}' f.txt
- awk '{print NR,$0}' f.txt cpf.txt    
- awk '{print FNR,$0}' f.txt cpf.txt
- awk '{print FNR,FILENAME,$0}' f.txt cpf.txt
- awk '{print FILENAME,"NR="NR,"FNR="FNR,"$"NF"="$NF}' f.txt cpf.txt
- echo 1:2:3:4 | awk -F: '{print $1,$2,$3,$4}'

说明：
- $0 代表整行
- $1 代表一行中第一个字段  默认的使用空格作为分隔符[而不是数组中常见的0开始]
- NR 代表行号，当有多个文件时，所有文件按照前后的顺序NR递增
- FNR 每个文件的行号，单个文件内的记录数递增
- FILENAME 文件的名称
- NF 一行的字段个数
- $NF表示该行的最后一个字段
- F：指定一行中的数据字段之间的分隔符为：

### 2.匹配

- awk '/ldb/ {print}' f.txt   #匹配包含ldb字串的行 然后打印
- awk '!/ldb/ {print}' f.txt  #不匹配ldb
- awk '/ldb/ && /LISTEN/ {print}' f.txt   #匹配ldb和LISTEN   多个匹配参数与的关系
- awk '$5 ~ /ldb/ {print}' f.txt #第五列匹配ldb


### 3.内建变量

- NR:NR表示从awk开始执行后，按照记录分隔符读取的数据次数，默认的记录分隔符为换行符，
因此默认的就是读取的数据行数，NR可以理解为Number of Record的缩写。

- FNR:在awk处理多个输入文件的时候，在处理完第一个文件后，NR并不会从1开始，而是继续累加，因此就出现了FNR，
每当处理一个新文件的时候，FNR就从1开始计数，FNR可以理解为File Number of Record。

- NF: NF表示目前的记录被分割的字段的数目，NF可以理解为Number of Field。

## find 查找文件
- sudo -u admin find /home/admin /tmp /usr -name \*.log(多个目录去找)  [注意最后的匹配必须加上\才可以]
- find . -iname \*.txt(大小写都匹配)
- find . -type d(当前目录下的所有子目录)
- find /usr -type l(当前目录下所有的符号链接)
- find /usr -type l -name "z*" -ls(符号链接的详细信息 eg:inode,目录)
- find /home/admin -size +250000k(超过250000k的文件，当然+改成-就是小于了) [-size 搜索文件大小在什么范围]
- find /home/admin f -perm 777 -exec ls -l {} \; (按照权限查询文件)  [-perm 按照权限查询文件]
- find /home/admin -atime -1  1天内访问过的文件 [按照时间的维度去搜索文件]
- find /home/admin -ctime -1  1天内状态改变过的文件    
- find /home/admin -mtime -1  1天内修改过的文件
- find /home/admin -amin -1  1分钟内访问过的文件
- find /home/admin -cmin -1  1分钟内状态改变过的文件    
- find /home/admin -mmin -1  1分钟内修改过的文件

说明：
命令的使用格式：find [-H] [-L] [-P] [-Olevel] [-D help|tree|search|stat|rates|opt|exec] [path...] [expression]


**备注：是不是可以简单理解，通过[find]命令查找到文件，然后再使用[grep]搜索文件中的内容，最后使用[awk]命令分析文本的额内容**




## top
这个命令适合用来实时掌握操作系统的整体情况，且能够实时反映出系统各个进程的资源的占用情况，类似于 windows 的任务管理器。使用 top 命令可以显示进程信息。

上半部分显示操作系统的各种信息，包括 CPU 使用情况、内存使用情况、进程执行情况等。下半部分显示了活动比较频繁的进程，
可以在这些进程中排查问题的端倪。确定可疑的进程后，可以指定相关进程，并设置信息更新时间，显示完整命令。
下面来看一个例子，其中，指定显示进程 9836，每隔 5 秒的进程的资源的占用情况。
`# top –d 5 –p 9836 -c`


top除了看一些基本信息之外，剩下的就是配合来查询vm的各种问题了
- ps -ef | grep java
- top -H -p pid
获得线程10进制转16进制后jstack去抓看这个线程到底在干啥

## ps
ps 命令 – 查看当前进程
这个命令适合用来查看某个瞬间存在哪些进程，这些进程的信息和状态等。通过 ps 命令，可以判断当前进程的状态，从而找出问题的原因。
使用 ps 命令可以显示系统中当前所有的进程。

`# ps -ef`

同时，也可以指定查看相关的进程。下面来看一个例子，其中，查看所有 java 进程。

`# ps –ef | grep java`


## netstat

netstat 命令 – 查看网络连接情况
这个命令可以知道 Linux 系统的网络情况，适合用来查看网络连接信息。其中，可以查看当前的所有连接。

`# netstat -a`

此外，可以监听 TCP 的连接。

`# netstat –atl`

甚至可以统计端口的当前连接数。下面来看一个例子，其中，查看 10090 端口的当前连接数。

`# netstat -an | grep 10090 | wc -l`

netstat -nat|awk  '{print $6}'|sort|uniq -c|sort -rn   统计tcp的链接情况

说明：
sort 排序  默认按照字母顺序排
uniq -c 去重统计各个字符出现的次数
sort -rn 按照单词出现的次数排序


## iostat
这个命令适合用来监控系统设备的 IO 负载情况，对系统的磁盘操作活动进行监控。
iostat 首次运行时，显示系统启动开始的各项统计信息，之后运行 iostat 将显示自上次运行该命令以后的统计信息。
用户可以通过指定统计的次数和时间来获得所需的统计信息。下面来看一个例子，其中，每秒采样一次，连续 5 次，观察磁盘 IO 的使用情况。

`# iostat –k 1 5`

## sar
sar 命令 – 性能监控
这个命令适合用来监控 CPU 的使用率和空闲情况，以及磁盘 I/O 的使用情况、网卡流量的使用情况等。监控 CPU 的情况，可以使用 –u 参数
，输出 CPU 使用情况的统计信息。下面来看一个例子，其中，每秒采样一次，连续 10 次，观察 CPU 的使用情况。

`# sar –u 1 10`

值得注意的是，如果 %user + %sys 超过 85%，进程可能要花时间在运行队列中等待，因此响应时间和吞吐量会受影响。
但是，使用率 100% 不一定意味着 CPU 就是性能瓶颈，此时可以进一步查看 vmstat 命令中的 r 值是否超出服务器的 CPU 数量。
此外，%system 比较大，说明系统管理方面花了很多时间。需要进一步的分析其它软硬件因素。监控磁盘 I/O 的情况，可以使用 –d 参数
，输出每个块设备的活动信息。下面来看一个例子，其中，每秒采样一次，连续 10 次，观察磁盘 I/O 的使用情况。其中，-p 参数可以打印出磁盘的设备名称。

`# sar –pd 1 10`

如果 %util 接近100%，可能由于产生的 I/O 请求太多，I/O 系统已经满负荷，因此磁盘存在瓶颈。此外，如果 %await 远大于 %svctm，
可能是因为磁盘 I/O 队列太长，导致响应时间变慢。

## vmstat
vmstat 命令 – 虚拟内存监控
这个命令适合用来监控 CPU 使用率，内存使用，虚拟内存交换情况，IO读写情况等。下面来看一个例子，其中，每秒采样一次，连续 5 次，观察虚拟内存的使用情况。

`# vmstat 1 5`

其中，第一行显示是 Linux 操作系统启动后的平均值，所以一般看第二行后面的值。

其中，swpd、 si、 so 三个指标的值比较高，很可能是内存不足。如果 cache 使用率非常低，而 swap 的 si 或 so 有比较高的数据值时，
应该警惕内存的性能问题。此外，注意的是，当内存严重不足时，系统会频繁使用调页和交换，这增加了磁盘 I/O 的负载，
进一步降低了系统对作业的执行速度，即系统 I/O 资源问题又会影响到内存资源的分配。



# java

## jps
我只用一条命令： jps -mlvV

## jstack

基本用法：jstack 进程id

native+java栈: jstack -m 进程id

## jinfo
可看系统启动的参数，如下info -flags 2815

## jmap
两个用途
- 1.查看堆的情况 jmap -heap 2815
- 2.dump
jmap -dump:live,format=b,file=/tmp/heap2.bin 2815  或者 jmap -dump:format=b,file=/tmp/heap3.bin 2815
- 3.看看堆都被谁占了? 再配合zprofiler和btrace，排查问题简直是如虎添翼
jmap -histo 2815 | head -10

## jstat
jstat参数众多，但是使用一个就够了   jstat -gcutil 2815 1000 




# 参考
[](https://mp.weixin.qq.com/s/KFuYd38esMFvR7M7Df8WoA)
[](https://mp.weixin.qq.com/s/93borgwF_rz7bdnWgyB8BA)
[](https://mp.weixin.qq.com/s/VoJcHMSQTywWUBvmPT2iKg)