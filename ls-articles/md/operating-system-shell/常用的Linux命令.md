自己常用Linux命令
0、查看当前Linux是32位还是64位
uname -a 查看当前机器系统内核
getconf LONG_BIT

1、cp把文件夹tomcat7-1下的所有文件复制一份到tomcat7-3下

 cp -r /usr/local/tomcat7-1  /usr/local/tomcat7-3
2、tar解压 xxx.tar.gz包

tar   zxvf    xxx.tar.gz
分别是四个参数
x : 从 tar 包中把文件提取出来
z : 表示 tar 包是被 gzip 压缩过的，所以解压时需要用 gunzip 解压
v : 显示详细信息
f xxx.tar.gz :  指定被处理的文件是 xxx.tar.gz
3、lsof 查看端口是否被占用

  
lsof(list open files)是一个列出当前系统打开文件的工具
语法：

lsof abc.txt 显示开启文件abc.txt的进程
lsof -c abc 显示abc进程现在打开的文件
lsof -c -p 1234 列出进程号为1234的进程所打开的文件
lsof -g gid 显示归属gid的进程情况
lsof +d /usr/local/ 显示目录下被进程开启的文件
lsof +D /usr/local/ 同上，但是会搜索目录下的目录，时间较长
lsof -d 4 显示使用fd为4的进程
lsof -i 用以显示符合条件的进程情况
lsof -i[46] [protocol][@hostname|hostaddr][:service|port]
  46 --> IPv4 or IPv6
  protocol --> TCP or UDP
  hostname --> Internet host name
  hostaddr --> IPv4地址
  service --> /etc/service中的 service name (可以不止一个)
  port --> 端口号 (可以不止一个)


实用命令

lsof `which httpd` //那个进程在使用apache的可执行文件
lsof /etc/passwd //那个进程在占用/etc/passwd
lsof /dev/hda6 //那个进程在占用hda6
lsof /dev/cdrom //那个进程在占用光驱
lsof -c sendmail //查看sendmail进程的文件使用情况
lsof -c courier -u ^zahn //显示出那些文件被以courier打头的进程打开，但是并不属于用户zahn
lsof -p 30297 //显示那些文件被pid为30297的进程打开
lsof -D /tmp 显示所有在/tmp文件夹中打开的instance和文件的进程。但是symbol文件并不在列

lsof -u1000 //查看uid是100的用户的进程的文件使用情况
lsof -utony //查看用户tony的进程的文件使用情况
lsof -u^tony //查看不是用户tony的进程的文件使用情况(^是取反的意思)
lsof -i //显示所有打开的端口
lsof -i:80 //显示所有打开80端口的进程
lsof -i -U //显示所有打开的端口和UNIX domain文件
lsof -i UDP@[url]www.akadia.com:123 //显示那些进程打开了到www.akadia.com的UDP的123(ntp)端口的链接
lsof -i tcp@ohaha.ks.edu.tw:ftp -r //不断查看目前ftp连接的情况(-r，lsof会永远不断的执行，直到收到中断信号,+r，lsof会一直执行，直到没有档案被显示,缺省是15s刷新)
lsof -i tcp@ohaha.ks.edu.tw:ftp -n //lsof -n 不将IP转换为hostname，缺省是不加上-n参数

4、ps 命令会显示你系统当前的进程状态(例如：根据进程的id查看进程的具体信息)

也许你希望把结果按照 CPU 或者内存用量来筛选，这样你就找到哪个进程占用了你的资源 a all;u cpu;x  显示没有控制终端的进程
ps aux

ps axu | grep 2643
5、grep (global search regular expression(RE) and print out the line,全面搜索正则表达式并把行打印出来)是一种强大的文本搜索工具，它能使用正则表达式搜索文本，并把匹配的行打印出来。
grep常用用法
[root@www ~]# grep [-acinv] [--color=auto] '搜寻字符串' filename
选项与参数：
-a ：将 binary 文件以 text 文件的方式搜寻数据
-c ：计算找到 '搜寻字符串' 的次数
-i ：忽略大小写的不同，所以大小写视为相同
-n ：顺便输出行号
-v ：反向选择，亦即显示出没有 '搜寻字符串' 内容的那一行！
--color=auto ：可以将找到的关键词部分加上颜色的显示喔！

6、|管道

command1正确输出，作为command2的输入 然后comand2的输出作为，comand3的输入 ，comand3输出就会直接显示在屏幕上面了。

通过管道之后：comand1,comand2的正确输出不显示在屏幕上面

注意：

1、管道命令只处理前一个命令正确输出，不处理错误输出

2、管道命令右边命令，必须能够接收标准输入流命令才行。
[chengmo@centos5 shell]$ cat test.sh | grep -n 'echo'
5:  echo "very good!";
7:  echo "good!";
9:  echo "pass!";
11:  echo "no pass!";
#读出test.sh文件内容，通过管道转发给grep 作为输入内容
7、cat命令主要用来查看文件内容，创建文件，文件合并，追加文件内容等功能

查看文件内容主要用法：
1、cat f1.txt，查看f1.txt文件的内容。
2、cat -n f1.txt，查看f1.txt文件的内容，并且由1开始对所有输出行进行编号。
3、cat -b f1.txt，查看f1.txt文件的内容，用法与-n相似，只不过对于空白行不编号。
4、cat -s f1.txt，当遇到有连续两行或两行以上的空白行，就代换为一行的空白行。
5、cat -e f1.txt，在输出内容的每一行后面加一个$符号。
6、cat f1.txt f2.txt，同时显示f1.txt和f2.txt文件内容，注意文件名之间以空格分隔，而不是逗号。
7、cat -n f1.txt>f2.txt，对f1.txt文件中每一行加上行号后然后写入到f2.txt中，会覆盖原来的内容，文件不存在则创建它。
8、cat -n f1.txt>>f2.txt，对f1.txt文件中每一行加上行号后然后追加到f2.txt中去，不会覆盖原来的内容，文件不存在则创建它。
8、 lsb_release （ Linux Standard Base）
LSB是Linux Standard Base的缩写，lsb_release命令用来显示LSB和特定版本的相关信息，可通过yum -y install redhat-lsb命令安装。如果使用该命令时不带参数，则默认加上-v参数。
-v, --version
显示版本信息

-i, --id
显示发行版的ID

-d, --description
显示该发行版的描述信息

-r, --release
显示当前系统是发行版的具体版本号

-c, --codename
发行版代号

-a, --all
显示上面的所有信息

-h, --help
显示帮助信息
9、rpm
RPM是RedHat Package Manager（RedHat软件包管理工具）类似Windows里面的“添加/删除程序”

rpm 执行安装包
二进制包（Binary）以及源代码包（Source）两种。二进制包可以直接安装在计算机中，而源代码包将会由RPM自动编译、安装。源代码包经常以src.rpm作为后缀名。

常用命令组合：

－ivh：安装显示安装进度--install--verbose--hash
－Uvh：升级软件包--Update；
－qpl：列出RPM软件包内的文件信息[Query Package list]；
－qpi：列出RPM软件包的描述信息[Query Package install package(s)]；
－qf：查找指定文件属于哪个RPM软件包[Query File]；
－Va：校验所有的RPM软件包，查找丢失的文件[View Lost]；
－e：删除包
-qa：查询所有（符合后面的附件条件的所有信息）

rpm -qa|grep java                ／／ 查看jdk的信息
10、chmod----改变一个或多个文件的存取模式(mode)

$ chmod u+x file                　　　   给file的属主增加执行权限
$ chmod 751 file                　　　   给file的属主分配读、写、执行(7)的权限，给file的所在组分配读、执行(5)的权限，给其他用户分配执行(1)的权限
$ chmod u=rwx,g=rx,o=x file      上例的另一种形式
$ chmod =r file                 　　　　为所有用户分配读权限
$ chmod 444 file              　　　　 同上例
$ chmod a-wx,a+r   file   　　 　   同上例
$ chmod -R u+r directory       　   递归地给directory目录下所有文件和子目录的属主分配读的权限
$ chmod 4755                          　　设置用ID，给属主分配读、写和执行权限，给组和其他用户分配读、执行的权限。
11、 export设置或显示环境变量
将自定义变量设定为系统环境变量

export JAVA_HOME=/usr/java/jdk1.6.0_23
export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$PATH:$JAVA_HOME/bin
12、rm 删除一个目录中的一个或多个文件或目录，如果没有使用- r选项，则rm不会删除目录。如果使用 rm 来删除文件，通常仍可以将该文件恢复原状。
命令格式：
rm [选项] 文件… 
rm -rf test2命令会将 test2 子目录及子目录中所有档案删除,并且不用一一确认
命令参数：
    -f, --force    忽略不存在的文件，从不给出提示。
    -i, --interactive 进行交互式删除
    -r, -R, --recursive   指示rm将参数中列出的全部目录和子目录均递归地删除。
    -v, --verbose    详细显示进行的步骤
       --help     显示此帮助信息并退出
       --version  输出版本信息并退出

13、mv命令是move的缩写，可以用来移动文件或者将文件改名（move (rename) files），是Linux系统下常用的命令，经常用来备份文件或者目录。
在同一文件夹下改名字
mv apache-tomcat-7.0.29  tomcat
14、ln是linux中又一个非常重要命令，它的功能是为某一个文件在另外一个位置建立一个同不的链接，这个命令最常用的参数是-s，具体用法是：ln –s 源文件 目标文件。

ln -s /opt/eclipse/eclipse     /usr/bin/eclipse

15、gnome是一种用户界面（GUI，或者叫XWindows）


16、sudo （super user do!）




1.常用命令（tab  补全）
etc (etcetera)存放系统配置文件  
usr  (unix shared resources)用于存放共享的系统资源

ls：显示文件和目录列表(list)
ls -l  == ll

内部命令：属于Shell解析器的一部分
     cd 切换目录（change directory）  cd ../    切换到上一级路径
      pwd 显示当前工作目录（print working directory）
      help 帮助
外部命令：独立于Shell解析器之外的文件程序
      ls 显示文件和目录列表（list）
      mkdir 创建目录（make directoriy）
     cp 复制文件或目录（copy）
查看帮助文档 内部命令：help + 命令（help cd） 外部命令：man + 命令（man ls）

pwd 显示当前工作目录（print working directory）
touch 创建空文件  

echo   "hello" >1.txt        //把hello写入1.txt，不存在时创建 >>追加  >覆盖
echo   "hello"          //相当于system.out.print("");可以是常量或者变量    
cat显示文本文件内容 （catenate）  
    
mkdir 创建目录（make directoriy）
     -p 父目录不存在情况下先生成父目录 （parents）           
cp 复制文件或目录（copy）
      -r 递归处理，将指定目录下的文件与子目录一并拷贝（recursive）    
mv 移动文件或目录、文件或目录改名（move）

rm 删除文件（remove）
      -r 同时删除该目录下的所有文件（recursive）
     -f 强制删除文件或目录（force）
rmdir 删除空目录（remove directoriy）

more、less 分页显示文本文件内容
     more中按q退出，空格下一页

head、tail查看文本中开头或结尾部分的内容
      haed  -n  5  a.log 查看a.log文件的前5行
     tail  -f  b.log 循环读取（fellow）  //实时刷新文件的尾部

wc 统计文本的行数、字数、字符数（word count）
      -m 统计文本字符数
     -w 统计文本字数
     -l 统计文本行数 find 在文件系统中查找指定的文件
find /etc/ -name "aaa" grep 在指定的文本文件中查找指定的字符串
ln 建立链接文件（link）
      -s 对源文件建立符号连接，而非硬连接（symbolic）

top 显示当前系统中耗费资源最多的进程
ps 显示瞬间的进程状态
      -e /-A 显示所有进程，环境变量
      -f 全格式 -a 显示所有用户的所有进程（包括其它用户）
      -u 按用户名和启动时间的顺序来显示进程
      -x 显示无控制终端的进程
kill 杀死一个进程 kill -9 pid
df 显示文件系统磁盘空间的使用情况

du 显示指定的文件（目录）已使用的磁盘空间的总
     -h文件大小以K，M，G为单位显示（human-readable）
      -s只显示各档案大小的总合（summarize）
free 显示当前内存和交换空间的使用情况
netstat 显示网络状态信息
     -a 显示所有连接和监听端口
     -t (tcp)仅显示tcp相关选项
     -u (udp)仅显示udp相关选项
      -n 拒绝显示别名，能显示数字的全部转化成数字。
      -p 显示建立相关链接的程序名
ifconfig 网卡网络配置详解
ping 测试网络的连通性

2vi


:wq! 保存强制退出
shit zz

3权限管理
三种基本权限
r 读权限（read）
w 写权限（write）
x 执行权限 （execute）
更改操作权限
chmod修改文件权限命令（change mode）
   参数：-R 下面的文件和子目录做相同权限操作（Recursive递归的）  
  例如：chmod  u+x  a.txt 用数字来表示权限（r=4，w=2，x=1，-=0）  
  例如：chmod  750  b.txt   
rwx用二进制表示是111，十进制4+2+1=7   
r-x用二进制表示是101，十进制4+0+1=5

chown  root:root hello.txt   改变hello.txt的所属者   root用户权限下

whoami   查看当前用户
su切换到root用户
注释：上级目录的权限对本级文件或者文件夹的操作也有约束

四：用户管理

配置文件
保存用户信息的文件：/etc/passwd            用户id从500开始   shuai:x:500:500:lishuai:/home/shuai:/bin/bash   

保存密码的文件：/etc/shadow
保存用户组的文件：/etc/group
保存用户组密码的文件：/etc/gshadow
用户配置文件：/etc/default/useradd


添加用户命令：useradd        // useradd    shuai      ,给用户添加密码  passwd shuai  必须设置密码才能正常登陆
      -u 指定userID（uid）
     -g 指定所属的组名（gid）
      -G 指定多个组，用逗号“，”分开（Groups）
      -c 用户描述（comment）
      -e 失效时间（expire date）
例子： useradd -u 888 -g users -G sys,root -c "hr zhang" zhangsan passwd zhangsan

修改用户命令：usermod（user modify）（也可以编辑配置文件来完成）
      -l 修改用户名 （login）usermod -l a b（b改为a）
     -g 修改组 usermod -g sys tom                    
     -G添加多个组 usermod -G sys,root tom
     –L 锁定用户账号密码（Lock）
      –U 解锁用户账号（Unlock）
     -d 修改用户的主目录

删除用户命令：userdel（user delete）
     -r 删除账号时同时删除目录（remove）

添加组：groupadd（组的操作和添加用户类似，添加组不需要设置密码）
     -g 指定gid

修改组：groupmod
     -n 更改组名（new group）

删除组：groupdel

groups 显示用户所属组

五：系统管理
ifconfig   查看ip
虚拟机中的网卡为eth0
网络设置
sudo vi /etc/sysconfig/network-scripts/ifcfg-eth0 （一般不在这里修改）可在这里设置静态ip

在伪图形界面操作：
setup-->network configuration-->device configuration-->eth0编辑即可
更改后的ip在从启网络服务后才生效   service network restart/stop/status


六：网络配置
主机名和ip的映射（类似host文件）
修改主机名：
vim /etc/sysconfig/network     修改其中的hostname配置项

vi /etc/hosts   主机和ip的映射
ping 一下测试


关闭防火墙（只有在root用户时才有效）
service iptables stop
查看防火墙的状态 
service iptables  status/start启

chkconfig iptables --list   查看防火墙默认(自动)启动的级别（启动时的模式，开机时）
 chkconfig iptables  off 关闭

编辑启动的配置：vi   /etc/inittab

查看当前进程链接网络的信息
netstat -nltp

七：常用工具指令
wc 统计文本信息（行数，词数，字符数）
date 查看或者修改系统的日期和时间
echo 输出字符串或者变量的值


shutdown系统关机
     -r 关机后立即重启
      -h 关机后不重新启动
halt 关机后关闭电源
reboot 重新启动

利用好Tab键
掌握好一些快捷键
     ctrl + c（停止当前进程）
      ctrl + r（查看命令历史）
      ctrl + l（清屏，与clear命令作用相同）


八：软件安装
切换图形界面和命令行界面
sudo init 5  图形    start x
sudo init3  命令行



安装jdk eclipse
然后修改环境变量  sudo vi /etc/profile


九备份压缩命令
gzip 压缩（解压）文件或目录，压缩文件后缀为gz
bzip2 压缩（解压）文件或目录，压缩文件后缀为bz2
tar 文件、目录打（解）包

gzip命令
命令格式：gzip [选项] 压缩（解压缩）的文件名
     -d将压缩文件解压（decompress）
      -l显示压缩文件的大小，未压缩文件的大小，压缩比（list）
     -v显示文件名和压缩比（verbose）
      -num用指定的数字num调整压缩的速度，-1或--fast表示最快压缩方法（低压缩比），-9或--best表示最慢压缩方法（高压缩比）。系统缺省值为6

bzip2命令
命令格式：bzip2 [-cdz] 文档名
     -c将压缩的过程产生的数据输出到屏幕上
     -d解压缩的参数（decompress）
     -z压缩的参数（compress）
     -num 用指定的数字num调整压缩的速度，-1或--fast表示最快压缩方法（低压缩比），-9或--best表示最慢压缩方法（高压缩比）。系统缺省值为6

tar命令（常见zxvf ）
     -c 建立一个压缩文件的参数指令（create）
      -x 解开一个压缩文件的参数指令（extract）
     -z 是否需要用 gzip 压缩
      -j 是否需要用 bzip2 压缩
      -v 压缩的过程中显示文件（verbose）
      -f 使用档名，在 f 之后要立即接档名（file）

十。RPM软件包管理
RPM是RedHat Package Manager（RedHat软件包管理工具）

RPM命令使用
rpm的常用参数
      i：安装应用程序（install）
     e：卸载应用程序（erase）
      vh：显示安装进度；（verbose   hash）
       U：升级软件包；（update）
      qa: 显示所有已安装软件包（query all） 结合grep命令使用
例子：rmp  -ivh  gcc-c++-4.4.7-3.el6.x86_64.rpm


YUM命令
Yum（全称为 Yellow dog Updater, Modified）是一个在Fedora和RedHat以及SUSE、CentOS中的Shell前端软件包管理器。基於RPM包管理，能够从指定的服务器自动下载RPM包并且安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包，无须繁琐地一次次下载、安装。

例子（需要上网，没有网络可以建本地源）：
      yum  install  gcc-c++
      yum  remove  gcc-c++
     yum  update  gcc-c++
