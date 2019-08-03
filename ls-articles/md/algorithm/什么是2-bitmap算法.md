# bitmap和2-bitmap总结

bitmap可以用来排序（知道数据的范围且不可以重复）
2bitmap可以用来找大数组中出现一次，两次等情况

## 2-BitMap
2-BitMap算法是BitMap算法的一种扩展，核心是：每个数分配2个bit，00表示不存在，01表示出现一次，10表示出现2次，11表示多次或无意义。
一个int类型，占4个字节，在2-BitMap中可以表示16个数，表示全部int类型数据需要内存2^32*2bit=1G。时间复杂度O（n）。

经常用来解决的问题如下：2.5亿个整数中找出不重复的整数的个数，内存空间不足以容纳这2.5亿个整数。

将bit-map扩展一下，用2bit表示一个数即可，0表示未出现，1表示出现一次，2表示出现2次及以上。或者我们不用2bit来进行表示，
我们用两个bit-map即可模拟实现这个2bit-map。

分析：

每个数据分配2个bit,然后扫描这 2.5亿个整数，查看 Bitmap中相对应位，如果是 00 变 01 ，01 变 10 ，10 保持不变。
所描完 事后，查看bitmap，把对应位是 01 的.
代码如下：

```java
public class BitMapSort {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int[] array = { 1,4,1,32,2,6,4,2,69,9,4,185};
        //bitMapSort(array);
        //System.out.println((long)Math.pow(2, 28)/16);
        bit2MapSort(array);
    }

    public static void bit2MapSort(int[] a) {
        int bnum = 2;//用两个bit为来标记某个元素的个数
        int bsize = 32 / bnum;//一个32位字节能标记多少个数 (一个int类型 ) 这里可以表示16个数
//      long numSize = (long)Math.pow(2, 32);//数据范围(0到2^32内的数  4294967296)   2147483647最大的整数
//      int arraysize = (int) (numSize / bsize);//是2的28次方,小于最大整数
        //数据范围(0到2^32内的数)
        int numSize = 160000;//此处是16万，便于测试
        //定义bitmap数组大小
        int arraySize =(int)Math.ceil((double)numSize/bsize);
        int[] bitmap = new int[arraySize];
        //初始化0
        for (int i = 0;i < bitmap.length;i++) {
            bitmap[i] = 0;
        }
        //遍历数组，把元素放入
        for (int i = 0;i < a.length;i++) {
            int pos = a[i] >> 4;//相当于除以32，找到元素在哪个位置,获得bitMap的下标
            int index = a[i] % bsize;//获得对应的位置
            int count = (bitmap[pos] & (0x3 << (2*index))) >> (2*index);//获取x在BitMap中的数量
            if (count < 3) {//只处理count小于3的
                bitmap[pos] &= ~((0x3<<(2*index)));  //将x对应位置上的数值先清零，但是有要保证其他位置上的数不变
                bitmap[pos] |= (((count+1)&0x3)<<(2*index));  //重新对x的个数赋值
            }
        }
        //再次遍历输出元素
        for (int i = 0;i < numSize;i++) {
            int pos = i >> 4;//相当于除以32，找到元素在哪个位置,获得bitMap的下标
            int index = i % bsize;//获得对应的位置
            int count = (bitmap[pos] & (0x3 << 2*index)) >> 2*index;//获取x在BitMap中的数量
            if (count == 2) {//在这里可以根据1 2 3判别原来数组中只出现一次 二次 多余3次的
                System.out.println(i);
            }
        }


    }

//利用BitMap可以对某些数据进行排序，但是限制条件是必须实现知道数据的范围，而且不能重复，类似于桶排序，但是比桶排序更加节省内存。
//原理很简单，就是设置数组某一位的数在BitMap中对应位为1，然后遍历数组就可以得到结果。这里以100以内的一个数组排序为例

//1bitmap排序要求
    public static void bitMapSort(int[] a) {
        if (a == null || a.length == 0) {
            return;
        }
        byte[] bitmap = new byte[13];
        for (int i = 0;i < a.length;i++) {
            int pos = a[i] / 8;//找到对应的位置
            int index = a[i] % 8;//设置标志位的位置
            bitmap[pos] = (byte)(bitmap[pos] | (byte)(0x01<<index));//设置标志
        }
        int count = 0;
        for (int i = 0;i < bitmap.length;i++) {
            byte base = bitmap[i];
            for (int j = 0;j < 8;j++) {
                 if ((base & (0x01 << j)) != 0) {
                     a[count++] = 8 * i + j;//恢复出原始的数据
                 }
            }
        }
    }
}

```
