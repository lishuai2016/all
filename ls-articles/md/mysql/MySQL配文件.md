
[我必须得告诉你的MySQL优化原理3](https://www.jianshu.com/p/78b6b7e2bb7f)

聊聊MySQL配置。
大多数开发者可能不太会关注MySQL的配置，毕竟在基本配置没有问题的情况下，把更多的精力放在schema设计、索引优化和SQL优化上，是非常务实的策略。这时，如果再花力气去优化配置项，获得的收益通常都比较小。更多的时候，基于安全因素的考量，普通开发者很少能够接触到生产环境的MySQL配置。正是这样，导致开发者（包括我）对MySQL的配置不甚了解，希望本文能帮你更好的了解MySQL配置。
如果让你在某种环境上安装配置MySQL，你会怎么做？安装后，直接copy修改示例配置文件，应该是大多数人的做法。但强烈建议不要怎么做，首先，示例配置文件有非常多注释掉的配置项，它可能会诱使你打开一个你并不了解的配置，而且这些注释还不一定准确。其次，MySQL的一些配置对于现代化的硬件和工作负载来说，有点过时了。
MySQL有非常多的配置项可以修改，但大多数情况下，你都不应该随便修改它，因为错误或者没用的配置导致的潜在风险非常大，而且还很难定位问题。确保基本配置正确，然后小心诊断问题，确认问题恰好可以通过某个配置项解决，紧接着再修改这个配置吧。
其实，创建一个好的配置，最快方法不是从学习配置项开始，也不是问哪个配置项应该怎么设置或者怎么修改开始，更不是从检查服务器行为和询问哪个配置项可以提升性能开始。最好是从理解MySQL内核和行为开始，然后利用这些知识来指导你配置MySQL。
就从理解MySQL配置的工作原理开始吧。
MySQL配置的工作原理
MySQL从哪儿获得配置信息：命令行参数和配置文件。类Unix系统中，配置文件一般位于 /etc/my.cnf 或者 /etc/mysql/my.cnf。在启动时，可以通过命令行参数指定配置文件的位置，当然命令行中也可以指定其它参数，服务器会读取配置文件的内容，删除所有注释和换行，然后和命令行选项一起处理。
任何打算长期使用的配置项都应该写入配置文件，而不是在命令行中指定。一定要清楚的知道MySQL使用的配置文件位置，在修改时不能想当然，比如，修改了/etc/my.cnf的配置项，但MySQL实际并未使用这个配置文件。如果你不知道当前使用的配置文件路径，可以尝试：
root@msc3:~# which mysqld
/usr/sbin/mysqld
root@msc3:~# /usr/sbin/mysqld --verbose --help |grep -A 1 'Default options'
Default options are read from the following files in the given order:
/etc/my.cnf /etc/mysql/my.cnf ~/.my.cnf

一个典型的配置文件包含多个部分，每个部分的开头是一个方括号括起来的分段名称。MySQL程序通常读取跟它同名的分段部分，比如，许多客户端程序读取client部分。服务器通常读取mysqld这一段，一定要确认配置项放在了文件正确的分段中，否则配置是不会生效的。
MySQL每一个配置项均使用小写，单词之间用下划线或者横线隔开，虽然我们常用的分隔符是下划线，但如果在命令行或者配置文件中见到如下配置，你要知道，它们其实是等价的：
# 配置文件
max_connections=5000
max-connections=5000
# 命令行
/usr/sbin/mysqld --max_connections=5000
/usr/sbin/mysqld --max-connections=5000

配置项可以有多个作用域：全局作用域、会话作用域(每个连接作用不同)、对象作用域。很多会话级配置项跟全局配置相等，可以认为是默认值，如果改变会话级配置项，它只影响改动的当前连接，当连接关闭时，所有的参数变更都会失效。下面有几个示例配置项：

query-cache-size 全局配置项
sort-buffer-size 默认全局相同，但每个线程里也可以设置
join-buffer-size 默认全局，且每个线程也可以设置。但若一个查询中关联多张表，可以为每个关联分配一个关联缓存(join-buffer)，所以一个查询可能有多个关联缓冲。

配置文件中的变量(配置项)有很多(但不是所有)可以在服务器运行时修改，MySQL把这些归为动态配置变量：
# 设置全局变量，GLOBAL和@@global作用是一样的
set   GLOBAL   sort-buffer-size  = <value>
set   @@global.sort-buffer-size := <value>

# 设置会话级变量，下面6种方式作用是一样的
# 即：没有修饰符、SESSION、LOCAL等修饰符作用是一致的
set  SESSION   sort-buffer-size  = <value>
set  @@session.sort-buffer-size := <value>
set          @@sort-buffer-size  = <value>
set  LOCAL     sort-buffer-size  = <value>
set     @@ocal.sort-buffer-size := <value>
set            sort-buffer-size  = <value>

# set命令可以同时设置多个变量，但其中只要有一个变量设置失败，所有的变量都未生效
SET GLOBAL sort-buffer-size = 100, SESSION sort-buffer-size = 1000;
SET GLOBAL max-connections = 1000, sort-buffer-size = 1000000;

动态的设置变量，MySQL关闭时这些变量都会失效。如果在服务器运行时修改了变量的全局值，这个值对当前会话和其他任何已经存在的会话都不起效果，这是因为会话的变量值是在连接创建时从全局值初始化而来的。注意，在配置修改后，需要确认是否修改成功。
你可能注意到，上面的示例中，有些使用“=”，有些使用“:=”。对于set命令本身来说，两种赋值运算符没有任何区别，在命令行中使用任一运算符符，均可以生效。而在其他语句中，赋值运算符必须是“:=”，因为在非set语句中“=”被视为比较运算符。具体可以参考如下示例：
详细示例可以参考：stackoverflow
// @exp 表示用户变量，上面的示例均是系统变量
// 错误
set @user = 123456;
set @group = select GROUP from USER where User = @user;
select * from USER where GROUP = @group;

// 正确
SET @user := 123456;
SELECT @group := `group` FROM user WHERE user = @user;
SELECT * FROM user WHERE `group` = @group;

有一些配置使用了不同的单位，比如table-cache变量指定表可以被缓存的数量，而不是表可以被缓存的字节数。而key-buffer-size则是以字节为单位。
还有一些配置可以指定后缀单位，比如1M=1024*1024字节，但需要注意的是，这只能在配置文件或者作为命令行参数时有效。当使用SQL的SET命令时，必须使用数字值1048576或者1024*1024这样的表达式，但在配置文件中不能使用表达式。
小心翼翼的配置MySQL
们常常动态的修改配置，但请务必小心，因为它们可能导致数据库做大量耗时的工作，从而影响数据库的整体性能。比如从缓存中刷新脏块，不同的刷新方式对I/O的影响差别很大(后文会具体说明)。最好把一些好的习惯作为规范合并到工作流程中去，就比如：
好习惯1：不要通过配置项的名称来推断一个变量的作用
不要通过配置项的名称来推断一个变量的作用，因为它可能跟你想象的完全不一样。比如：


read-buffer-size：当MySQL需要顺序读取数据时，如无法使用索引，其将进行全表扫描或者全索引扫描。这时，MySQL按照数据的存储顺序依次读取数据块，每次读取的数据块首先会暂存在缓存中，当缓存空间被写满或者全部数据读取结束后，再将缓存中的数据返回给上层调用者，以提高效率。

read-rnd-buffer-size：和顺序读取相对应，当MySQL进行非顺序读取（随机读取）数据块的时候，会利用这个缓冲区暂存读取的数据。比如：根据索引信息读取表数据、根据排序后的结果集与表进行Join等等。总的来说，就是当数据块的读取需要满足一定的顺序的情况下，MySQL 就需要产生随机读取，进而使用到read-rnd-buffer-size参数所设置的内存缓冲区。

这两个配置都是在扫描MyISAM表时有效，且MySQL会为每个线程分配内存。对于前者，MySQL只会在查询需要使用时才会为该缓存分配内存，并且一次性分配该参数指定大小的全部内存，而后者同样是需要时才分配内存，但只分配需要的内存大小而不是参数指定的数值，max-read-rnd-buffer-size(实际上没有这个配置项)这个名字更能表达这个变量的实际含义。
好习惯2：不要轻易在全局修改会话级别的配置
对于某些会话级别的设置，不要轻易的在全局增加它们的值，除非你确认这样做是对的。比如：sort-buffer-size，该参数控制排序操作的缓存大小，MySQL只会在查询需要做排序操作时才会为该缓冲分配内存，一旦需要排序，就会一次性分配指定大小的内存，即使是非常小的排序操作。因此在配置文件中应该配置的小一些，然后在某些查询需要排序时，再在连接中把它调大。比如：
SET @@seession.sort-buffer-size := <value>
-- 执行查询的sql
SET @@seession.sort-buffer-size := DEFAULT #恢复默认值
# 可以将类似的代码封装在函数中方便使用。

好习惯3：配置变量时，并不是值越大越好
配置变量时，并不是值越大越好，而且如果设置的值太高，可能更容易导致内存问题。在修改完成后，应该通过监控来确认变量的修改对服务器整体性能的影响。
好习惯4：规范注释，版本控制
在配置文件中写好注释，可能会节省自己和同事大量的工作，一个更好的习惯是把配置文件置于版本控制之下。
说完了好习惯，再来说说不好的习惯。
坏习惯1：根据一些“比率”来调优
一个经典的按“比率”调优的经验法则是，缓存的命中率应该高于某个百分比，如果命中率过低，则应该增加缓存的大小。这是非常错误的意见，大家可以仔细思考一下：缓存的命中率跟缓存大小有必然联系吗？(分母变大，值就变大了？)除非确实是缓存太小了。关于MyISAM键缓冲命中率，下文会详细说明。
坏习惯2：随便使用调优脚本
尽量不要使用调优脚本！不同的业务场景、不同的硬件环境对MySQL的性能要求是不一样的。比如有些业务对数据的完整性要求较高，那么就一定要保证数据不丢失，出现故障后可恢复数据，而有些业务却对数据的完整性要求没那么高，但对性能要求更高。因此，即使是同一个变量，在这两个不同场景下，其配置的值也应该是不同的。那你还能放心的使用网上找到的脚本吗 ？

本小节示例的几个配置项，仅用于举例说明，并不代表它们有多么重要，请根据实际应用场景配置它们。就比如sort-buffer-size，你真的需要100M内存来缓存10行数据？

给你一个基本的MySQL配置
前面已经说到，MySQL可配置性太强，看起来需要花很多时间在配置上，但其实大多数配置的默认值已经是最佳的，最好不要轻易改动太多的配置，你甚至不需要知道某些配置的存在。这里有一个最小的示例配置文件，可以作为服务器配置文件的一个起点，其中有一些配置项是必须的。本节将为你详细剖析每个配置有何作用？为什么要配置它？怎么确定合适的值？
[mysql]

# CLIENT #
port                           = 3306
socket                         = /var/lib/mysql/mysql.sock

[mysqld]

# GENERAL #
user                           = mysql
port                           = 3306
default-storage-engine         = InnoDB
socket                         = /var/lib/mysql/mysql.sock
pid-file                       = /var/lib/mysql/mysql.pid

# DATA STORAGE #
datadir                        = /var/lib/mysql/

# MyISAM #
key-buffer-size                = 32M
myisam-recover                 = FORCE,BACKUP

# SAFETY #
max-allowed-packet             = 16M
max-connect-errors             = 1000000

# BINARY LOGGING #
log-bin                        = /var/lib/mysql/mysql-bin
expire-logs-days               = 14
sync-binlog                    = 1

# LOGGING #
log-error                      = /var/lib/mysql/mysql-error.log
log-queries-not-using-indexes  = 1
slow-query-log                 = 1
slow-query-log-file            = /var/lib/mysql/mysql-slow.log

# CACHES AND LIMITS #
tmp-table-size                 = 32M
max-heap-table-size            = 32M
query-cache-type               = 0
query-cache-size               = 0
max-connections                = 500
thread-cache-size              = 50
open-files-limit               = 65535
table-definition-cache         = 4096
table-open-cache               = 10240

# INNODB #
innodb-flush-method            = O_DIRECT
innodb-log-files-in-group      = 2
innodb-log-file-size           = 256M
innodb-flush-log-at-trx-commit = 1
innodb-file-per-table          = 1
innodb-buffer-pool-size        = 12G

分段
MySQL配置文件的格式为集中式，通常会分成好几部分，可以为多个程序提供配置，如[client]、[mysqld]、[mysql]等等。MySQL程序通常是读取与它同名的分段部分。

[client] 客户端默认设置内容
[mysql]  使用mysql命令登录mysql数据库时的默认设置
[mysqld]  数据库本身的默认设置

例如服务器mysqld通常读取[mysqld]分段下的相关配置项。如果配置项位置不正确，该配置是不会生效的。
GENERAL
首先创建一个用户mysql来运行mysqld进程，请确保这个用户拥有操作数据目录的权限。设置默认端口为3306，有时为了安全，可能会修改一下。默认选择Innodb存储引擎，在大多数情况下是最好的选择。但如果默认是InnoDB，却需要使用MyISAM存储引擎，请显式地进行配置。许多用户认为其数据库使用了某种存储引擎但实际上却使用的是另外一种，就是因为默认配置的问题。
接着设置数据文件的位置，这里把pid文件和socket文件放到相同的位置，当然也可以选择其它位置，但要注意的是不要将socket文件和pid文件放到MySQL编译的默认位置，因为不同版本的MySQL，这两个文件的默认路径可能会不一致，最好明确地设置这些文件的位置，以免版本升级时出现问题。

在类UNIX系统下本地连接MySQL可以采用UNIX域套接字方式，这种方式需要一个套接字（socket）文件，即配置中的mysql.sock文件。
当MySQL实例启动时，会将自己的进程ID写入一个文件中——该文件即为pid文件。该文件可由参数pid-file控制，默认位于数据库目录下，文件名为主机名.pid

DATA STORAGE
datadir用于配置数据文件的存储位置，没有什么好说的。
为缓存分配内存
接下来有许多涉及到缓存的配置项，缓存设置多大，最直接的因素肯定是服务器内存的大小。如果服务器只运行MySQL，所有不需要为OS以及查询处理保留的内存都可以用在MySQL缓存。为MySQL缓存分配更多内存，可以有效的避免磁盘访问，提升数据库性能。大部分情况来说最为重要的缓存：

InnoDB缓冲池
InnoDB日志文件和MyISAM数据的操作系统缓存(MyISAM依赖于OS缓存数据)
MyISAM键缓存
查询缓存
无法配置的缓存，比如：bin-log或者表定义文件的OS缓存

还有一些其他缓存，但它们通常不会使用太多内存。关于查询缓存，前面文章(参考本系列的第一篇)已有介绍，大多数情况下我们不建议开启查询缓存，因此上文的配置中query-cache-type=0表示禁用了查询缓存，相应的查询缓存大小query-cache-size=0。除开查询缓存，剩下关于InnoDB和MyISAM的相关缓存，在接下来会做详细介绍。
如果只使用单一存储引擎，配置服务器就会简单许多。如果只使用MyISAM表，就可以完全关闭InnoDB，而如果只使用InnoDB，就只需要分配最少的资源给MyISAM（MySQL内部系统表使用MyISAM引擎）。但如果是混合使用各种存储引擎，就很难在他们之间找到恰当的平衡，因此只能根据业务做一个猜测，然后在运行中观察服务器运行状况后做出调整。
MyISAM
key-buffer-size
key-buffer-size用于配置MyISAM键缓存大小，默认只有一个键缓存，但是可以创建多个。MyISAM自身只缓存索引，不缓存数据(依赖OS缓存数据)。如果大部分表都是MyISAM，那么应该为键缓存设置较多的内存。但如何确定该设置多大？
假设整个数据库中表的索引大小为X，肯定不需要把缓存设置得比X还大，所以当前的索引大小就成为这个配置项的重要依据。可以通过下面两种方式来查询当前索引的大小：
// 1.通过SQL语句查询
SELECT SUM(INDEX_LENGTH) FROM INFORMATION_SCHEMA.TABLES WHERE ENGINE = 'MYISAM'
// 2.统计索引文件的大小
$ du -sch `find /path/to/mysql/data/directory/ -name "*.MYI"`
比如：
root@dev-msc3:# du -sch `find /var/lib/mysql -name "*.MYI"`
72K        /var/lib/mysql/static/t_global_region.MYI
40K        /var/lib/mysql/mysql/db.MYI
12K        /var/lib/mysql/mysql/proxies_priv.MYI
12K        /var/lib/mysql/mysql/tables_priv.MYI
4.0K       /var/lib/mysql/mysql/func.MYI
4.0K       /var/lib/mysql/mysql/columns_priv.MYI
4.0K       /var/lib/mysql/mysql/proc.MYI
4.0K       /var/lib/mysql/mysql/event.MYI
4.0K       /var/lib/mysql/mysql/user.MYI
4.0K       /var/lib/mysql/mysql/procs_priv.MYI
4.0K       /var/lib/mysql/mysql/ndb_binlog_index.MYI
164K       total

你可能会问，刚创建好的数据库，根本就没什么数据，索引文件大小为0，那如何配置键缓存大小？这时候只能根据经验值：不超过为操作系统缓存保留内存的25% ~ 50%。设置一个基本值，等运行一段时间后，根据运行情况来调整键缓存大小。总结来说，索引大小与OS缓存的25%~50%两者间取小者。当然还可以计算键缓存的使用情况，如果一段时间后还是没有使用完所有的键缓存，就可以把缓冲区调小一点，计算缓存区的使用率可以通过以下公式：
// key_blocks_unused的值可以通过 SHOW STATUS获取
// key_cache_block_size的值可以通过 SHOW VARIABLES获取 
(key_blocks_unused * key_cache_block_size) / key_buffer_size

键缓存块大小是一个比较重要的值，因为它影响MyISAM、OS缓存以及文件系统之间的交互。如果缓存块太小，可能会碰到写时读取(OS在写数据之前必须先从磁盘上读取一些数据)，关于写时读取的相关知识，大家可以自行查阅。
关于缓存命中率，这里再说一点。缓存命中率有什么意义？其实这个数字没太大的作用。比如99%和99.9%之间看起来差距很小，但实际上代表了10倍的差距。缓存命中率的实际意义与应用也有很大关系，有些应用可以在命中率99%下良好的工作，有些I/O密集型应用，可能需要99.99%。所以从经验上来说，每秒未命中次数这个指标实际上会更有用一些。比如每秒5次未命中可能不会导致IO繁忙，但每秒100次缓存未命中则可能出现问题。
MyISAM键缓存的每秒未命中次数可以通过如下命令监控：
# 计算每隔10s缓存未命中次数的增量
# 使用此命令时请带上用户和密码参数：mysqladmin -uroot -pxxx extended-status -r -i 10 | grep Key_reads
$ mysqladmin extended-status -r -i 10 | grep Key_reads

最后，即使没有使用任何MyISAM表，依然需要将key-buffer-size设置为较小值，比如32M，因为MySQL内部会使用MyISAM表，比如GROUP BY语句可能会创建MyISAM临时表。
myisam-recover
myisam-recover选项用于配置MyISAM怎样寻找和修复错误。打开这个选项会通知MySQL在打开表时，检查表是否损坏，并在找到问题时进行修复，它可以设置如下值：

DEFAULT：表示不设置，会尝试修复崩溃或者未完全关闭的表，但在恢复数据时不会执行其它动作
BACKUP：将数据文件备份到.bak文件，以便随后进行检查
FORCE：即使.myd文件中丢失的数据超过1行，也让恢复动作继续执行
QUICK：除非有删除块，否则跳过恢复

可以设置多个值，每个值用逗号隔开，比如配置文件中的BACKUP,FORCE会强制恢复并且创建备份，这样配置在只有一些小的MyISAM表时有用，因为服务器运行着一些损坏的MyISAM表是非常危险的，它们有时可能会导致更多数据损坏，甚至服务器崩溃。然而如果有很大的表，它会导致服务器打开所有的MyISAM表时都检查和修复，大表的检查和修复可能会耗费大量时间，且在这段时间里，MySQL会阻止这个连接做其它任何操作，这显然是不切实际的。
因此，在默认使用InnoDB存储引擎时，数据库中只有非常小的MyISAM表时，只需要配置key-buffe-size于一个很小的值(32M)以及myisam-recover=BACKUP,FORCE。当数据库中大部分表为MyISAM表时，请根据上文的公式合理配置key-buffer-size，而myisam-recover则可以关闭，在启动后使用CHECK TABLES和REPAIR TABLES命令来做检查和修复，这样对服务器的影响比较小。
SAFETY
基本配置设置到位后，MySQL已经比较安全了，这里仅仅列出两个需要注意的配置项，如果需要启用一些使服务器更安全和可靠的设置，可以参考MySQL官方手册，但需要注意的是，它们其中的一些选项可能会影响性能，毕竟保证安全和可靠需要付出一些代价。
max-allowed-packet
max-allowed-packet防止服务器发送太大的数据包，也控制服务器可以接收多大的包。默认值4M，可能会比较小。如果设置太小，有时复制上会出问题，表现为从库不能接收主库发过来的复制数据。如果表中有Blob或者Text字段，且数据量较大的话，要小心，如果数据量超过这个变量的大小，它们可能被截断或者置为NULL，这里建议设置为16M。
max-connect-errors
这个变量是一个MySQL中与安全相关的计数器值，它主要防止客户端暴力破解密码。如果某一个客户端尝试连接MySQL服务器失败超过n次，则MySQL会无条件强制阻止此客户端连接，直到再次刷新主机缓存或者重启MySQL服务器。
这个值默认为10，太小了，有时候网络抽风或者应用配置出现错误导致短时间内不断尝试重连服务器，客户端就会被列入黑名单，导致无法连接。如果在内网环境，可以确认没有安全问题可以把这个值设置的大一点，默认值太容易导致问题。
LOGGING
接下来看下日志的配置，对于MySQL来说，慢日志和bin-log是非常重要的两种日志，前者可以帮助应用程序监控性能问题，后者在数据同步、备份等方面发挥着非常重要的作用。
关于bin-log的3个配置，log-bin用于配置文件存放路径，expire_logs_days让服务器在指定天数之后清理旧的日志，即配置保留最近多少天的日志。除非有运维手动备份清理bin-log，否则强烈建议打开此配置，如果不启用，服务器空间最终将会被耗尽，导致服务器卡住或者崩溃。
sync-binlog
sync-binlog控制当事务提交之后，MySQL是否将bin-log刷新到磁盘。如果其值等于0或者大于1时，当事务提交之后，MySQL不会将bin-log刷新到磁盘，其性能最高，但存在的风险也是最大的，因为一旦系统崩溃，bin-log将会丢失。而当其值等于1时，是最安全的，这时候即使系统崩溃，最多也就丢失本次未完成的事务，对实际的数据没有实质性的影响，但性能较差。
需要注意的是，在5.7.7之前的版本，这个选择的默认值为0，而后默认值为1，也就是最安全的策略。对于高并发的性能，需要关注这一点，防止版本升级后出现性能问题。
剩下的4个配置项就没太多要说的。

log-error：用于配置错误日志的存放目录
slow-query-log：打开慢日志，默认关闭
slow-query-log-file：配置慢日志的存放目录
log-queries-not-using-indexes：如果该sql没有使用索引，会将其写入到慢日志，但是否真的执行很慢，需要区分，默认关闭。

CACHES AND LIMITS
tmp-table-size && max-heap-table-size
这两个配置控制使用Memory引擎的内存临时表可以使用多大的内存。如果隐式内存临时表的大小超过这两个值，将会被转为磁盘MyISAM表(隐式临时表由服务器创建，用户保存执行中的查询的中间结果)。
如果查询语句没有创建庞大的临时表(通过合理的索引和查询设计来避免)，可以把这个值设大一点，以免需要把内存临时表转换为磁盘临时表。但要谨防这个值设置得过大，如果查询确实会创建很大的临时表，那么还是使用磁盘比较好，毕竟并发数一起来，所需要的内存就会急剧增长。
应该简单的把这两个变量设为同样的值，这里选择了32M，可以通过仔细检查created-tmp-disk-tables和created-tmp-tables两个变量来指导你设置，这两个变量的值将展示临时表的创建有多频繁。
query-cache-type && query-cache-size
看前面
max-connections
用于设置用户的最大连接数，保证服务器不会应为应用程序激增的连接而不堪重负。如果应用程序有问题，或者服务器遇到连接延迟问题，会创建很多新连接。但如果这些连接不能执行查询，那打开一个连接没什么好处，所以被“太多的连接”错误拒绝是一种快速而且代价小的失败方式。
在服务器资源允许的情况下，可以把max-connections设置的足够大，以容纳正常可能达到的负载。若认为正常情况将有300或者更多连接，可以设置为500或者更多(应对高峰期)。默认值是100，太小了，这里设置为500，但并不意味着其是一个合理的值，应该监控应用有多少连接，然后根据监控值(观察max_used_connections随时间的变化)来设置。
thread-cache-size
线程缓存保存那些当前没有与连接关联但是准备为后面新连接服务的线程。当一个新的连接创建时，如果缓存中有线程存在，MySQL则从缓存中删除一个线程，并且把它分配给这个新连接。当连接关闭时，如果线程缓存还有空间的话，MySQL又会把线程放回缓存。如果没有空间的话，MySQL会销毁这个线程。只要MySQL在缓存里还有空闲的线程，它就可以迅速响应连接请求，因为这样就不用为每个连接创建新线程。thread-cache-size指定MySQL可以保存在缓存中的线程数量。如果服务器没有很多的连接请求，一般不需要配置这个值。
如何判断这个值该设置多大？
观察threads-connected变量，如果threads-connected在100-120，那么thread-cache-size设置为20。如果它保持在500-700，200的线程缓存应该足够大了。可以这么理解：当同时有700个连接时，可能缓存中没有线程。在500个连接时，有200个缓存的线程准备为负载再次增加到700个连接时使用。
open-files-limit
在类Uinux系统上我们把它设置得尽可能大。现代OS中打开句柄开销都很小，如果此参数设置过小，可能会遇到“打开的文件太多(too many open files)”错误。
table_cache_size
表缓存跟线程缓存类似，但存储的对象是表，其包含表.frm文件的解析结果和一些其他数据。准确的说，缓存的数据依赖于存储引擎，比如，对于MyISAM，缓存表的数据和索引的文件描述符。表缓存对InnoDB的存储引擎来说，重要性会小很多，因为InnoDB不依赖它来做那么多的事。
从5.1版本及以后，表缓存就被分为两个部分：打开表缓存和定义表缓存，分别通过table-open-cache-size和table-definition-cache-size变量来配置。通常可以把table-definition-cache-size设置得足够高，以缓存所有的表定义，因为大部分存储引擎都能从table-definition-cache获益。
INNODB
InnoDB应该是使用最广发的存储引擎，最重要的配置选项是下面这两个：innodb-buffer-pool-size与innodb-log-file-size，解决这两个配置基本上就解决了真实场景下的大部分配置问题。
innodb-buffer-pool-size
如果大部分是InnoDB表，那么InnoDB缓冲池或许比其他任何东西都更需要内存，InnoDB缓冲池缓冲的数据：索引、行数据、自适应哈希索引、插入缓冲、锁以及其他内部数据结构。InnoDB还使用缓冲池来帮助延迟写入，这样就可以合并多个写入操作，然后一起顺序写入，提升性能。总之，InnoDB严重依赖缓冲池，必须为其分配足够的内存。
当然，如果数据量不大且不会快速增长，就没有必要为缓冲池分配过多的内存，把缓冲池配置得比需要缓存的表和索引还要大很多，实际上也没有什么意义。很大的缓冲池也会带来一些挑战，例如，预热和关闭都会花费很长的时间。如果有很多脏页在缓冲池里，InnoDB关闭时可能会花很长时间来把脏页写回数据文件。虽然可以快速关闭，但是在启动时需要做更多的恢复工作，也就是说我们无法同时加速关闭和重启两个操作。当有一个很大的缓冲池，重启服务需要花费很长时间（几小时或者几天）来预热，尤其是磁盘很慢的时候，如果想加快预热时间，可以在重启后立刻进行全表扫描或者索引扫描，把索引载入缓冲池。
可以看到示例的配置文件中把这个值配置为12G，这不是一个标准配置，需要根据具体的硬件来估算。那如何估算？
前面的小节，我们说到，MySQL中最重要的缓存有5种，可以简单的使用下面的公式计算：
InnoDB缓冲池 = 服务器总内存 - OS预留 - 服务器上的其他应用占用内存 - MySQL自身需要的内存 - InnoDB日志文件占用内存 - 其它内存(MyISAM键缓存、查询缓存等)
具体来看，至少需要为OS保留1~2G内存，如果机器内存大的话可以预留多一些，建议2GB和总内存的5%为基准，以较大者为准，如果机器上还运行着一些内存密集型任务，比如，备份任务，那么可以为OS再预留多一些内存。不要为OS缓存增加任何内存，因为OS通常会利用所有剩下的内存来做文件缓存。
一般来说，运行MySQL的服务器很少会运行其他应用程序，但如果有的话，请为这些应用程序预留足够多的内存。
MySQL自身运行还需要一些内存，但通常都不会太大。需要考虑MySQL每个连接需要的内存，虽然每个连接需要的内存都很少，但它还要求一个基本量的内存来执行任何给定的查询，而且查询过程中还需要为排序、GROUP BY等操作分配临时表内存，因此需要为高峰期执行大量的查询预留足够的内存。这个内存有多大？只能在运行过程中监控。
如果大部分表都是InnoDB，MyISAM键缓存配置一个很小值足矣，查询缓存也建议关闭。
公式中就剩下InnoDB日志文件了，这就是我们接下来要说的。
innodb-log-file-size && innodb-log-files-in-group
如果对InnoDB数据表有大量的写入操作，那么选择合适的innodb-log-file-size值对提升MySQL性能很重要。InnoDB使用日志来减少提交事务时的开销。日志中记录了事务，就无须在每个事务提交时把缓冲池的脏块(缓存中与磁盘上数据不一致的页)刷新到磁盘。事务修改的数据和索引通常会映射到表空间的随机位置，所以刷新这些变更到磁盘需要很多随机I/O。一旦日志安全的写入磁盘，事务就持久化了，即使变更还没有写到数据文件，在一些意外情况发生时(比如断电了)，InnoDB可以重放日志并且恢复已经提交的事务。
InnoDB使用一个后台线程智能地刷新这些变更到数据文件。实际上，事务日志把数据文件的随机I/O转换为几乎顺序地日志文件和数据文件I/O，让刷新操作在后台可以更快的完成，并且缓存I/O压力。
整体的日志文件大小受控于innodb-log-file-size和innodb-log-files-in-group两个参数，这对写性能非常重要。日志文件的总大小是每个文件的大小之和。默认情况下，只有两个5M的文件，总共10M，对高性能工作来说太小了，至少需要几百M或者上G的日志文件。这里要注意innodb-log-files-in-group这个参数，它控制日志文件的数量，从名字上看好似配置一个日志组有几个文件，实际上，log group表示一个重做日志的文件集合，没有参数也没有必要配置有多少个日志组。
修改日志文件的大小，需要完全关闭MySQL，然后将旧的日志文件迁移到其他地方，重新配置参数，然后重启。重启时需要将旧的日志迁移回来，然后等待MySQL恢复数据后，再删除旧的日志文件，请一定要查看错误日志，确认MySQL重启成功后再删除旧的日志文件。
想要确定理想的日志文件大小，需要权衡正常数据变更的开销，以及崩溃时恢复需要的时间。如果日志太小，InnoDB将必须要做更多的检查点，导致更多的日志写，在极个别情况下，写语句还会被拖累，在日志没有空间继续写入前，必须等待变更被刷新到数据文件。另一方面，如果日志太大，在崩溃时恢复就得做大量的工作，这可能增大恢复时间。InnoDB会采用checkpoint机制来刷新和恢复数据，这会加快恢复数据的时间，具体可以参考：

MySQL-checkpoint技术
How InnoDB performs a checkpoint

innodb-flush-log-at-trx-commit
前面讨论了很多缓存，InnoDB日志也是有缓存的。当InnoDB变更任何数据时，会写一条变更记录到日志缓存区。在缓冲慢的时候、事务提交的时候，或者每一秒钟，InnoDB都会将缓冲区的日志刷新到磁盘的日志文件。如果有大事务，增加日志缓冲区大小可以帮助减少I/O，变量innodb-log-buffer-size可以控制日志缓冲区的大小。通常不需要把日志缓冲区设置的非常大，毕竟上述3个条件，任一条件先触发都会把缓冲区的内容刷新到磁盘，所以缓冲区的数据肯定不会太多，出入你的数据中有很多相当大的BLOB记录。通常来说，配置1M~8M即可。
既然存在缓冲区，怎样刷新日志缓冲就是我们需要关注的问题。日志缓冲必须刷新到磁盘，以确保提交的事务完全被持久化。如果和持久化相比，更在乎性能，可以修改innodb-flush-log-at-trx-commit变量来控制日志缓冲刷新的频率。

0：每1秒钟将日志缓冲写到日志文件并刷新到磁盘，事务提交时不做任何处理
1：每次事务提交时，将日志缓冲写到日志文件并刷新到磁盘
2：每次事务提交时，将日志缓冲写到日志文件，然后每秒刷新一次到磁盘

1是最安全的设置，保证不会丢失任何已经提交的事务，这也是默认的设置。0和2最主要的区别是，如果MySQL挂了，2不会丢失事务，但0有可能，2在每次事务提交时，至少将日志缓冲刷新到操作系统的缓存，而0则不会。如果整个服务器挂了或者断电了，则还是可能会丢失一些事务。
innodb-flush-method
前面都在讨论使用什么样的策略刷新、以及何时刷新日志或者数据，那InnoDB具体是怎样刷新数据的？使用innodb-flush-method选项可以配置InnoDB如何跟文件系统相互作用。从名字上看，会以为只能影响InnoDB怎么写数据，实际上还影响了InnoDB怎么读数据。windows和非Windows操作系统下这个选项的值是互斥的，也就是说有些值只能Windows下使用，有些只能在非Windows下使用，其中Windows下可取值：async_unbuffered、unbuffered、normal、Nosync与littlesync，非Windows取值：fdatasync、0_DIRECT、 0_DSYNC。
这个选项既会影响日志文件，也会影响数据文件，而且有时候对不同类型的文件的处理也不一样，导致这个选项有些难以理解。如果有一个选项来配置日志文件，一个选项来配置数据文件，应该会更好，但实际上它们混合在同一个配置项中。这里只介绍类Unix操作系统下的选项。
fdatasync
InnoDB调用fsync()和fdatasync()函数来刷新数据和日志文件，其中fdatasync()只刷文件的数据，但不包含元数据(比如：访问权限、文件拥有者、最后修改时间等描述文件特征的系统数据)，因此fsync()相比fdatasync()会产生更多的I/O，但在某些场景下fdatasync()会导致数据损坏，因此InnoDB开发者决定用fsync()来代替fdatasync()。
fsync()的缺点是操作系统会在自己的缓存中缓冲一些数据，理论上双重缓冲是浪费的，因为InnoDB自己会管理缓冲，而且比操作系统更加智能。但如果文件系统能有更智能的I/O调度和批量操作，双重缓冲也并不一定是坏事：

有的文件系统和os可以累积写操作后合并执行，通过对I/O的重排序来提升效率、或者并发写入多个设备
有的还可以做预读优化，比如连续请求几个顺序的块，它会通知硬盘预读下一个块

这些优化在特定的场景下才会起作用，fdatasync为innodb-flush-method的默认值。
0_DIRCET
这个设置不影响日志文件并且不是所有的类Unix系统都有效，但至少在Linux、FreeBSD以及Solaris是支持的。这个设置依然使用fsync来刷新文件到磁盘，但是它完全关闭了操作系统缓存，并且是所有的读和写都直接通过存储设置，避免了双重缓冲。如果存储设备支持写缓冲或预读，那么这个选项并不会影响到设备的设置，比如RAID卡。
0_DSYNC
这个选项使得所有的写同步，即只有数据写到磁盘后写操作才返回，但它只影响日志文件，而不影响数据文件。
说完了每个配置的作用，最后是一些建议：如果使用类Unix操作系统并且RAID控制器带有电池保护的写缓存，建议使用0_DIRECT，如果不是，默认值或者0_DIRECT都可能是最好的选择。
innodb-file-per-table
最后一个配置，说说InnoDB表空间，InnoDB把数据保存在表空间内，它本质上是一个由一个或者多个磁盘文件组成的虚拟文件系统。InnoDB表空间并不只是存储表和索引，它还保存了回滚日志、插入缓冲、双写缓冲以及其他内部数据结构，除此之外，表空间还实现了很多其它的功能。可以通过innodb-data-file-path配置项定制表空间文件，innodb-data-home-dir配置表空间文件存放的位置，比如：
innodb-data-home-dir = /var/lib/mysql 
innodb-data-file-path = ibdata1:1G;ibdata2:1G;ibdata3:1G 

这里在3个文件中创建了3G表空间，为了允许表空间在超过了分配的空间时还能增长，可以像这样配置最后一个文件自动扩展
innodb-data-file-path =ibdata1:1G;ibdata2:1G;ibdata3:1G:autoextend 

innodb-file-per-table选项让InnoDB为每张表使用一个文件，这使得在删除一张表时回收空间容易很多，而且特别容易管理，并且可以通过查看文件大小来确定表大小，所以这里建议打开这个配置。
总结
MySQL有太多的配置项，这里没有办法一一列举，重要的是了解每个配置的工作原理，从一个基础配置文件开始，设置符合服务器软硬件环境与工作负载的基本选项。


















[](https://mp.weixin.qq.com/s/l28KXbsce6NjUmfr-rw1kA)
优化MySQL配置
以下是我的配置示例。加了skip-name-resolve,快了4-5s。其他配置自行断定

```
[client]
port=3306
[mysql]
no-beep
default-character-set=utf8
[mysqld]
server-id=2
relay-log-index=slave-relay-bin.index
relay-log=slave-relay-bin 
slave-skip-errors=all #跳过所有错误
skip-name-resolve

port=3306
datadir="D:/mysql-slave/data"
character-set-server=utf8
default-storage-engine=INNODB
sql-mode="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"

log-output=FILE
general-log=0
general_log_file="WINDOWS-8E8V2OD.log"
slow-query-log=1
slow_query_log_file="WINDOWS-8E8V2OD-slow.log"
long_query_time=10

# Binary Logging.
# log-bin

# Error Logging.
log-error="WINDOWS-8E8V2OD.err"


# 整个数据库最大连接（用户）数
max_connections=1000
# 每个客户端连接最大的错误允许数量
max_connect_errors=100
# 表描述符缓存大小，可减少文件打开/关闭次数
table_open_cache=2000
# 服务所能处理的请求包的最大大小以及服务所能处理的最大的请求大小(当与大的BLOB字段一起工作时相当必要)  
# 每个连接独立的大小.大小动态增加
max_allowed_packet=64M
# 在排序发生时由每个线程分配
sort_buffer_size=8M
# 当全联合发生时,在每个线程中分配 
join_buffer_size=8M
# cache中保留多少线程用于重用
thread_cache_size=128
# 此允许应用程序给予线程系统一个提示在同一时间给予渴望被运行的线程的数量.
thread_concurrency=64
# 查询缓存
query_cache_size=128M
# 只有小于此设定值的结果才会被缓冲  
# 此设置用来保护查询缓冲,防止一个极大的结果集将其他所有的查询结果都覆盖
query_cache_limit=2M
# InnoDB使用一个缓冲池来保存索引和原始数据
# 这里你设置越大,你在存取表里面数据时所需要的磁盘I/O越少.  
# 在一个独立使用的数据库服务器上,你可以设置这个变量到服务器物理内存大小的80%  
# 不要设置过大,否则,由于物理内存的竞争可能导致操作系统的换页颠簸.  
innodb_buffer_pool_size=1G
# 用来同步IO操作的IO线程的数量
# 此值在Unix下被硬编码为4,但是在Windows磁盘I/O可能在一个大数值下表现的更好. 
innodb_read_io_threads=16
innodb_write_io_threads=16
# 在InnoDb核心内的允许线程数量.  
# 最优值依赖于应用程序,硬件以及操作系统的调度方式.  
# 过高的值可能导致线程的互斥颠簸.
innodb_thread_concurrency=9

# 0代表日志只大约每秒写入日志文件并且日志文件刷新到磁盘.  
# 1 ,InnoDB会在每次提交后刷新(fsync)事务日志到磁盘上
# 2代表日志写入日志文件在每次提交后,但是日志文件只有大约每秒才会刷新到磁盘上
innodb_flush_log_at_trx_commit=2
# 用来缓冲日志数据的缓冲区的大小.  
innodb_log_buffer_size=16M
# 在日志组中每个日志文件的大小.  
innodb_log_file_size=48M
# 在日志组中的文件总数. 
innodb_log_files_in_group=3
# 在被回滚前,一个InnoDB的事务应该等待一个锁被批准多久.  
# InnoDB在其拥有的锁表中自动检测事务死锁并且回滚事务.  
# 如果你使用 LOCK TABLES 指令, 或者在同样事务中使用除了InnoDB以外的其他事务安全的存储引擎  
# 那么一个死锁可能发生而InnoDB无法注意到.  
# 这种情况下这个timeout值对于解决这种问题就非常有帮助. 
innodb_lock_wait_timeout=30
# 开启定时
event_scheduler=ON
```