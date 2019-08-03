[Hash算法的讲解](http://www.cnblogs.com/xiohao/p/4389672.html)
[十一、从头到尾解析Hash表算法](https://blog.csdn.net/v_JULY_v/article/details/6256463)
[Hash学习（3）-冲突的解决](https://blog.csdn.net/qll125596718/article/details/7028322)
[]()



hash学习---常见的hash算法

常用构造方法
  1、直接定址法
            hash(key) = a*key + b
  2、折叠法
      取数据的某部分进行折叠计算：   12536798    ——>   1253 + 6798
 3、平方取中 ： 类似随机数产生的算法，取中间一部分作为地址
 4、除数取余法
           hash(key) = key mod p
           p一般取质素或者不包含小于20质因数的合数
  5、随机数法
           hash(key) = rand(key)   key作为随机数的种子。
            1-4方法都可以归入这类。因为平方取中和取余是常见随机数算法。
   6、根据数据特征分析
   7、综合法。
考虑因素：
1、计算hash的时间
2、字段长度
3、哈希表大小
4、关键字分布
5、查找频率
溢出处理方法：
1、再哈希法
2、链地址方法（java hashMap采用则个策略）
3、建立公共溢出区
3、开放地址
             H = (h\H(key) + d)MOD M
            d可以是线性改变，也可以平方，也可以用安随机函数进行改变。
分析： 查找只是和填充因子a有关 ～ -(1/a)ln(1-a)
            a = 表中记录数/哈希表长度。
