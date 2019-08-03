---
title: 根据进程的pid来查看占用cpu较高的线程
categories: 
- shell
tags:
---

[参考](https://www.jianshu.com/p/90579ec3113f)


#思路
根据pid使用top命令查找占用CPU高的线程tid,这个tid是10进制的，要想和jstack输出的线程对应起来，需要把这个tid转化成十六进制的，
因为jstack的tid是16进制存储的。


在开发过程中，有时候我们发现JVM占用的CPU居高不下，跟我们的预期不符，这时，CPU在做什么呢？是什么线程让CPU如此忙碌呢？
我们通过如下几步，可以查看CPU在执行什么线程。

1.查找jvm进程ID: jps -lv 或者 ps aux | grep java

2.根据pid，查找占用cpu较高的线程：ps -mp pid  -o THREAD,tid,time 如图所示：找到占用cpu最高的tid 
(可以使用sort命令排序：sort -k 3 -r -n)
<div align="center"> <img src="/images/shell-1.png"/> </div><br>
<div align="center"> <img src="/images/shell-2.png"/> </div><br>

3.将tid转换为16进制的数字：printf “%x\n” tid
<div align="center"> <img src="/images/shell-3.png"/> </div><br>

4.使用jstack命令，查询线程信息，从而定位到具体线程和代码：jstack pid | grep 7ccd -A 30
<div align="center"> <img src="/images/shell-4.png"/> </div><br>


这样，你就看到CPU这么高，是什么线程在捣乱了！
怎么样，是不是觉得有点儿麻烦，没有关系，我把这几个步骤写成了一个脚本，直接使用就OK了。

# 脚本实例
使用说明：
sh thread_analysis.sh 541326 20
可输入两个参数：1、pid Java进程ID，必须参数  2、打印线程ID上下文行数，可选参数，默认打印10行


    #!/bin/bash
    #
    # 当JVM占用CPU特别高时，查看CPU正在做什么
    # 可输入两个参数：1、pid Java进程ID，必须参数  2、打印线程ID上下文行数，可选参数，默认打印10行
    #
    
    pid=$1
    
    if test -z $pid
    then
     echo "pid can not be null!"
     exit
    else
     echo "checking pid($pid)"
    fi
    
    if test -z "$(jps -l | cut -d '' -f 1 | grep $pid)"
    then
     echo "process of $pid is not exists"
     exit
    fi
    
    lineNum=$2
    if test -z $lineNum
    then
        #$lineNum=10
        lineNum=10
    fi
    
    jstack $pid >> "$pid".bak
    
    ps -mp $pid -o THREAD,tid,time | sort -k2r | awk '{if ($1 !="USER" && $2 != "0.0" && $8 !="-") print $8;}' | xargs printf "%x\n" >> "$pid".tmp
    
    tidArray="$( cat $pid.tmp)"
    
    for tid in $tidArray
    do
        echo "******************************************************************* ThreadId=$tid **************************************************************************"
        cat "$pid".bak | grep $tid -A $lineNum
    done
    
    rm -rf $pid.bak
    rm -rf $pid.tmp