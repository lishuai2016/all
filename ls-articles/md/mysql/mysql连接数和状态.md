---
title: mysql连接数和状态
categories: 
- mysql
tags:
---

#mysql连接数和状态


MySQL server 是多线程结构，包括后台线程和客户服务线程。多线程可以有效利用服务器资源，提高数据库的并发性能。在 MySQL 中，控制并发连接和线程的主要参数包括 max_connections、back_log、thread_cache_size 以及 table_open_cache 等。


1、查看mysql数据库连接数、并发数相关信息 
show status like 'Threads%';

+-------------------+-------+
| Variable_name     | Value |
+-------------------+-------+
| Threads_cached    | 96    |
| Threads_connected | 50    |
| Threads_created   | 146   |
| Threads_running   | 3     |
+-------------------+-------+


Threads_cached    96 ##mysql管理的线程池中还有多少可以被复用的资源
Threads_connected    50 ##打开的连接数
Threads_created    146 ##表示创建过的线程数，如果发现Threads_created值过大的话，表明MySQL服务器一直在创建线程，这也是比较耗资源，可以适当增加配置文件中thread_cache_size值，查询服务器
Threads_running    3 ##激活的连接数，这个数值一般远低于connected数值，准确的来说，Threads_running是代表当前并发数



2、查询数据库当前设置的最大连接数
show variables like '%max_connections%';
+-----------------+-------+
| Variable_name   | Value |
+-----------------+-------+
| max_connections | 510   |
+-----------------+-------+
可以在/etc/my.cnf里面设置数据库的最大连接数
[mysqld]
max_connections = 510
max_connections 参数可以用于控制数据库的最大连接数

3、查看服务器响应的最大连接数
show global status like 'Max_used_connections';
+----------------------+-------+
| Variable_name        | Value |
+----------------------+-------+
| Max_used_connections | 146   |
+----------------------+-------+
对于mysql服务器最大连接数值的设置范围比较理想的是：服务器响应的最大连接数值占服务器上限连接数值的比例值在10%以上，如果在10%以下，说明mysql服务器最大连接上限值设置过高。

调整 max_connections，提高并发连接参数 max_connections 控制允许连接到 MySQL 数据库的最大数量。如果状态变量 connection_errors_max_connections不为零，并且一直增长，就说明不断有连接请求因数据库连接数已达到最大允许的值而失败，应考虑增大max_connections 的值。

MySQL 最大可支持的数据库连接取决于很多因素，包括给定操作系统平台线程库的质量、内存大小、每个连接的符合以及期望的响应时间等。在 Linux 平台下，MySQL 支持 500~1000 个连接不是难事，如果内存足够，不考虑响应时间，甚至能达到上万个连接。而在 windows 平台下，受其所用线程库的影响，最大连接数有以下限制：
(open tables * 2 + open connections) < 2048
每个session 操作 MySQL 数据库表都需要占用文件描述符，数据库连接本身也要占用文件描述符，因此，在增大 max_connections 时，也要注意评估 open-files-limit (文件描述符)的设置是否够用。

4、查看链接相关信息
show variables like '%connect%';
+--------------------------+-----------------+
| Variable_name            | Value           |
+--------------------------+-----------------+
| character_set_connection | utf8            |
| collation_connection     | utf8_general_ci |
| connect_timeout          | 10              |
| init_connect             |                 |
| max_connect_errors       | 10              |
| max_connections          | 510             |
| max_user_connections     | 500             |
+--------------------------+-----------------+


5、调整 back_log
back_log 参数控制 MySQL 监听 tcp 端口时设置的积压请求栈大小，5.6.6版本以前的默认值是 50,5.6.6 版本以后的默认值是 50 + （max_connections/5）,但最大不能超过 900。

如果需要数据库在较短时间内处理大量连接请求，可以考虑适当增大 back_log 的值。

show variables like '%back_log%';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| back_log      | 1024  |
+---------------+-------+


6、table_open_cache表缓存

每一个 sql 执行线程至少都要打开一个表缓存，参数 table_open_cache 控制所有 sql 执行线程可打开表缓存的数量。这个参数的值应根据最大连接数 max_connections 以及每个连接执行关联查询中所涉及表的最大个数（用 N 表示）来设定：
max_connection * N
在未执行 flush tables 命令的情况下，如果 MySQL 状态值 opened_tables 的值较大，就说明 table_open_cache 设置的太小，应适当增大。增大 table_open_cache 的值，会增加 MySQL 对文件描述符的使用量，因此，也要注意评估 open-files-limit 的设置是或否够用。

show variables like '%table_open_cache%';
+------------------+-------+
| Variable_name    | Value |
+------------------+-------+
| table_open_cache | 16384 |
+------------------+-------+



7、调整 thread_cache_size
为加快连接数据库的速度，MySQL 会缓存一定数量的客户服务线程以备重用，通过参数 thread_cache_size 可控制 MySQL 缓存客户端线程的数量。 
可以通过计算线程 cache 的失效率 threads_created / connections 来衡量 thread_cahce_size 的设置是否合适，该值越接近 1，说明线程 cache 命中率越低，应考虑适当增加 thread_cahce_size 的值。

show variables like '%thread_cache_size%';
+-------------------+-------+
| Variable_name     | Value |
+-------------------+-------+
| thread_cache_size | 512   |
+-------------------+-------+


8、innodb_lock_wait_timeout 的设置
参数 innodb_lock_wait_timeout 可以控制 innodb 事务等待行锁的时间，默认值是 50ms，可以根据需要动态设置。对于需要快速反馈的交互式应用，可以将行锁等待超时时间调大，以避免发生大的回滚操作。

show variables like '%innodb_lock_wait_timeout%';
+--------------------------+-------+
| Variable_name            | Value |
+--------------------------+-------+
| innodb_lock_wait_timeout | 120   |
+--------------------------+-------+



9、/etc/my.cnf文件[mysqld]段落中的内容

[mysqld]
port = 3306
serverid = 1
socket = /tmp/mysql.sock
skip-locking
#避免MySQL的外部锁定，减少出错几率增强稳定性。
skip-name-resolve
#禁止MySQL对外部连接进行DNS解析，使用这一选项可以消除MySQL进行DNS解析的时间。但需要注意，如果开启该选项，则所有远程主机连接授权都要使用IP地址方式，否则MySQL将无法正常处理连接请求！
back_log = 384
#back_log 参数的值指出在MySQL暂时停止响应新请求之前的短时间内多少个请求可以被存在堆栈中。  如果系统在一个短时间内有很多连接，则需要增大该参数的值，该参数值指定到来的TCP/IP连接的侦听队列的大小。不同的操作系统在这个队列大小上有它自 己的限制。 试图设定back_log高于你的操作系统的限制将是无效的。默认值为50。对于Linux系统推荐设置为小于512的整数。
key_buffer_size = 256M
#key_buffer_size指定用于索引的缓冲区大小，增加它可得到更好的索引处理性能。对于内存在4GB左右的服务器该参数可设置为256M或384M。注意：该参数值设置的过大反而会是服务器整体效率降低！
max_allowed_packet = 4M
thread_stack = 256K
sort_buffer_size = 6M
#查询排序时所能使用的缓冲区大小。注意：该参数对应的分配内存是每连接独占，如果有100个连接，那么实际分配的总共排序缓冲区大小为100 × 6 ＝ 600MB。所以，对于内存在4GB左右的服务器推荐设置为6-8M。
read_buffer_size = 4M
#读查询操作所能使用的缓冲区大小。和sort_buffer_size一样，该参数对应的分配内存也是每连接独享。
join_buffer_size = 8M
#联合查询操作所能使用的缓冲区大小，和sort_buffer_size一样，该参数对应的分配内存也是每连接独享。
myisam_sort_buffer_size = 64M
table_cache = 512
thread_cache_size = 64
query_cache_size = 64M
# 指定MySQL查询缓冲区的大小。可以通过在MySQL控制台观察，如果Qcache_lowmem_prunes的值非常大，则表明经常出现缓冲不够的 情况；如果Qcache_hits的值非常大，则表明查询缓冲使用非常频繁，如果该值较小反而会影响效率，那么可以考虑不用查询缓 冲；Qcache_free_blocks，如果该值非常大，则表明缓冲区中碎片很多。
tmp_table_size = 256M
max_connections = 768
#指定MySQL允许的最大连接进程数。如果在访问论坛时经常出现Too Many Connections的错误提 示，则需要增大该参数值。
max_connect_errors = 10000000
wait_timeout = 10
#指定一个请求的最大连接时间，对于4GB左右内存的服务器可以设置为5-10。
thread_concurrency = 8
#该参数取值为服务器逻辑CPU数量*2，在本例中，服务器有2颗物理CPU，而每颗物理CPU又支持H.T超线程，所以实际取值为4*2=8
skip-networking
#开启该选项可以彻底关闭MySQL的TCP/IP连接方式，如果WEB服务器是以远程连接的方式访问MySQL数据库服务器则不要开启该选项！否则将无法正常连接！
table_cache=1024
#物理内存越大,设置就越大.默认为2402,调到512-1024最佳
innodb_additional_mem_pool_size=4M
#默认为2M
innodb_flush_log_at_trx_commit=1
#设置为0就是等到innodb_log_buffer_size列队满后再统一储存,默认为1
innodb_log_buffer_size=2M
#默认为1M
innodb_thread_concurrency=8
#你的服务器CPU有几个就设置为几,建议用默认一般为8
key_buffer_size=256M
#默认为218，调到128最佳
tmp_table_size=64M
#默认为16M，调到64-256最挂
read_buffer_size=4M
#默认为64K
read_rnd_buffer_size=16M
#默认为256K
sort_buffer_size=32M
#默认为256K
thread_cache_size=120
#默认为60
query_cache_size=32M
 
 
如果从数据库平台应用出发，我还是会首选myisam.
 
PS:可能有人会说你myisam无法抗太多写操作，但是我可以通过架构来弥补，说个我现有用的数据库平台容量：主从数据总量在几百T以上，每天十多亿 pv的动态页面，还有几个大项目是通过数据接口方式调用未算进pv总数，(其中包括一个大项目因为初期memcached没部署,导致单台数据库每天处理 9千万的查询)。而我的整体数据库服务器平均负载都在0.5-1左右。
 
MyISAM和InnoDB优化：
 
key_buffer_size – 这对MyISAM表来说非常重要。如果只是使用MyISAM表，可以把它设置为可用内存的 30-40%。合理的值取决于索引大小、数据量以及负载 — 记住，MyISAM表会使用操作系统的缓存来缓存数据，因此需要留出部分内存给它们，很多情况下数据比索引大多了。尽管如此，需要总是检查是否所有的 key_buffer 都被利用了 — .MYI 文件只有 1GB，而 key_buffer 却设置为 4GB 的情况是非常少的。这么做太浪费了。如果你很少使用MyISAM表，那么也保留低于 16-32MB 的 key_buffer_size 以适应给予磁盘的临时表索引所需。
 
innodb_buffer_pool_size – 这对Innodb表来说非常重要。Innodb相比MyISAM表对缓冲更为敏感。MyISAM可以在默认的 key_buffer_size 设置下运行的可以，然而Innodb在默认的 innodb_buffer_pool_size 设置下却跟蜗牛似的。由于Innodb把数据和索引都缓存起来，无需留给操作系统太多的内存，因此如果只需要用Innodb的话则可以设置它高达 70-80% 的可用内存。一些应用于 key_buffer 的规则有 — 如果你的数据量不大，并且不会暴增，那么无需把 innodb_buffer_pool_size 设置的太大了。
 
innodb_additional_pool_size – 这个选项对性能影响并不太多，至少在有差不多足够内存可分配的操作系统上是这样。不过如果你仍然想设置为 20MB(或者更大)，因此就需要看一下Innodb其他需要分配的内存有多少。
 
innodb_log_file_size 在高写入负载尤其是大数据集的情况下很重要。这个值越大则性能相对越高，但是要注意到可能会增加恢复时间。我经常设置为 64-512MB，跟据服务器大小而异。
 
innodb_log_buffer_size 默 认的设置在中等强度写入负载以及较短事务的情况下，服务器性能还可 以。如果存在更新操作峰值或者负载较大，就应该考虑加大它的值了。如果它的值设置太高了，可能会浪费内存 — 它每秒都会刷新一次，因此无需设置超过1秒所需的内存空间。通常 8-16MB 就足够了。越小的系统它的值越小。
 
innodb_flush_logs_at_trx_commit 是否为Innodb比MyISAM慢1000倍而头大？看来也许你忘了修改这个参数了。默认值是 1，这意味着每次提交的更新事务（或者每个事务之外的语句）都会刷新到磁盘中，而这相当耗费资源，尤其是没有电池备用缓存时。很多应用程序，尤其是从 MyISAM转变过来的那些，把它的值设置为 2 就可以了，也就是不把日志刷新到磁盘上，而只刷新到操作系统的缓存上。日志仍然会每秒刷新到磁盘中去，因此通常不会丢失每秒1-2次更新的消耗。如果设置 为 0 就快很多了，不过也相对不安全了 — MySQL服务器崩溃时就会丢失一些事务。设置为 2 指挥丢失刷新到操作系统缓存的那部分事务。
 
table_cache — 打开一个表的开销可能很大。例如MyISAM把MYI文件头标志该表正在使用中。你肯定不希望这种操作太频繁，所以通常要加大缓存数量，使得足以最大限度 地缓存打开的表。它需要用到操作系统的资源以及内存，对当前的硬件配置来说当然不是什么问题了。如果你有200多个表的话，那么设置为 1024 也许比较合适（每个线程都需要打开表），如果连接数比较大那么就加大它的值。我曾经见过设置为 100,000 的情况。
 
thread_cache — 线程的创建和销毁的开销可能很大，因为每个线程的连接/断开都需要。我通常至少设置为 16。如果应用程序中有大量的跳跃并发连接并且 Threads_Created 的值也比较大，那么我就会加大它的值。它的目的是在通常的操作中无需创建新线程。
 
query_cache — 如果你的应用程序有大量读，而且没有应用程序级别的缓存，那么这很有用。不要把它设置太大了，因为想要维护它也需要不少开销，这会导致MySQL变慢。通 常设置为 32-512Mb。设置完之后最好是跟踪一段时间，查看是否运行良好。在一定的负载压力下，如果缓存命中率太低了，就启用它。
 
sort_buffer_size –如果你只有一些简单的查询，那么就无需增加它的值了，尽管你有 64GB 的内存。搞不好也许会降低性能。


