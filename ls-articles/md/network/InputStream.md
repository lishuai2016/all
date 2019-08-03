---
title: 标准输入流抽象类java.io.InputStream
categories: 
- IO
tags:
---




核心是public int read() throws IOException     这个方法解决的问题我们从哪里来获取数据
ByteArrayInputStream、StringBufferInputStream（1.7废弃）、FileInputStream 是三种基本的介质流，它们分别从Byte 数组、StringBuffer、和本地文件中读取数据。
PipedInputStream 是从与其它线程共用的管道中读取数据（比如一个输出流）
ObjectInputStream 和所有FilterInputStream 的子类都是装饰流（装饰器模式的主角：继承了InputStream类，并且持有一个InputStream类型的对象，通过构造函数传入）。

字节数组流的实现（原始数据来自于字节数组）

```java
public class ByteArrayInputStream extends InputStream {
 public ByteArrayInputStream(byte buf[]) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }
	
	//重点：这里从传进来的字节数组中读取数据
	public synchronized int read() {
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }
    
    public synchronized int read(byte b[], int off, int len) {
            if (b == null) {
                throw new NullPointerException();
            } else if (off < 0 || len < 0 || len > b.length - off) {
                throw new IndexOutOfBoundsException();
            }
    
            if (pos >= count) {
                return -1;
            }
    
            int avail = count - pos;
            if (len > avail) {
                len = avail;
            }
            if (len <= 0) {
                return 0;
            }
            System.arraycopy(buf, pos, b, off, len);  //提高读取数据的效率，比循环一个字节一个字节的效率要高
            pos += len;
            return len;
        }
	
	
}
```

 


文件输入流的实现（原始数据来自native方法从文件中读取的数据）
```java
public class FileInputStream extends InputStream {

	public int read() throws IOException {
        Object traceContext = IoTrace.fileReadBegin(path);
        int b = 0;
        try {
            b = read0();
        } finally {
            IoTrace.fileReadEnd(traceContext, b == -1 ? 0 : 1);
        }
        return b;
    }

    private native int read0() throws IOException;  //调用底层的native来实现


	//重写read(byte b[])
	public int read(byte b[]) throws IOException {
        Object traceContext = IoTrace.fileReadBegin(path);
        int bytesRead = 0;
        try {
            bytesRead = readBytes(b, 0, b.length);  //调用原生的方法来读取数据
        } finally {
            IoTrace.fileReadEnd(traceContext, bytesRead == -1 ? 0 : bytesRead);
        }
        return bytesRead;
    }
	
	
	 public int read(byte b[], int off, int len) throws IOException {   将读取的第一个字节存储在元素 b[off] 中，下一个存储在 b[off+1] 中，依次类推
        Object traceContext = IoTrace.fileReadBegin(path);
        int bytesRead = 0;
        try {
            bytesRead = readBytes(b, off, len);
        } finally {
            IoTrace.fileReadEnd(traceContext, bytesRead == -1 ? 0 : bytesRead);
        }
        return bytesRead;
    }
	
	private native int readBytes(byte b[], int off, int len) throws IOException;

}
```

ObjectInputStream装饰类输入流（从外部传入的输入流数据）

```java
public class ObjectInputStream  extends InputStream implements ObjectInput, ObjectStreamConstants {
public ObjectInputStream(InputStream in) throws IOException {
        verifySubclass();
        bin = new BlockDataInputStream(in);   //传入输入流
        handles = new HandleTable(10);
        vlist = new ValidationList();
        enableOverride = false;
        readStreamHeader();
        bin.setBlockDataMode(true);
    }

 public int read() throws IOException {   //数据来源
        return bin.read();
    }
	
	
	public int read(byte[] buf, int off, int len) throws IOException {
        if (buf == null) {
            throw new NullPointerException();
        }
        int endoff = off + len;
        if (off < 0 || len < 0 || endoff > buf.length || endoff < 0) {
            throw new IndexOutOfBoundsException();
        }
        return bin.read(buf, off, len, false);
    }
	
	
	int read(byte[] b, int off, int len, boolean copy) throws IOException {
            if (len == 0) {
                return 0;
            } else if (blkmode) {
                if (pos == end) {
                    refill();
                }
                if (end < 0) {
                    return -1;
                }
                int nread = Math.min(len, end - pos);
                System.arraycopy(buf, pos, b, off, nread);
                pos += nread;
                return nread;
            } else if (copy) {
                int nread = in.read(buf, 0, Math.min(len, MAX_BLOCK_SIZE));
                if (nread > 0) {
                    System.arraycopy(buf, 0, b, off, nread);
                }
                return nread;
            } else {
                return in.read(b, off, len);
            }
        }

}
```









在Java7中，InputStream被定义为一个抽象类，相应的，该类下的read()方法也是一个抽象方法，这也就意味着必须有一个类继承InputStream并且实现这个read方法。 
我们可以看到，在InputStream中定义了三个重载的read()方法：
方法1：public int read() throws IOException   // 从输入流中读取数据的下一个字节，以int返回
方法2：public int read(byte b[]) throws IOException   这个方法调用方法3     // 从输入流中读取数据的一定数量字节，并存储在缓存数组b
方法3：public int read(byte b[], int off, int len) throws IOException  方法3调用方法1           // 从输入流中读取数据最多len个字节，并存储在缓存数组b
同时，此方法为阻塞方法，解除阻塞的条件：
有可读的字节。
检测到已经是输入流的结尾了。
抛出异常。

这里有两点需要注意：一是这个方法的返回值是int类型；二是在这个方法每次从数据源中读取一个byte并返回。
read()
在计算机中，所有的文件都是以二进制的形式存储的，换句话说，每个文件不管是什么类型，在计算机中的形式都是一串0和1。
而read()方法在读的时候是每次读取8个二进制位，这8个0或1就是我们所谓的一个byte（字节）。在这里通常容易产生的疑问就是将字节和字符混为一谈。
无论在什么语言什么系统中，只要它符合当今世界对于计算机技术的主流定义，那么一个byte就是8个二进制位。而字符则不同，字符是与人为定义的编码规则相关的，
一个字符的大小（也就是其所占的二进制位）是由编码规则决定的，比如在GBK编码中一个汉字用两个字节表示，而在utf-8中，一个汉字由3到4个字节表示。
言归正传，既然一个byte表示8个二进制位，那么这8个二进制位就是一个0-255之间的十进制数字，实际上在Java中，byte就是一个0-255之间的整数，
而将从文件中读取的二进制转化成十进制这一过程是由read()方法完成的。也就是说read()这个方法完成的事情就是从数据源中读取8个二进制位，并将这8个0或1转换成十进制的整数，然后将其返回。 

read(byte[] b)这个方法，
使用一个byte的数组作为一个缓冲区，每次从数据源中读取和缓冲区大小（二进制位）相同的数据并将其存在缓冲区中。当然byte数组中存放的仍然是0-255的整数，
将二进制转换为十进制这个过程仍然是read方法实现的。 
需要注意的是，虽然我们可以指定缓冲区的大小，但是read方法在读取数据的时候仍然是按照字节来读取的。在utf-8等变长编码中，一个复杂字符（比如汉字）
所占字节往往大于1，并且长度往往是不固定的。（参照UTF-8编码规则）按照字节读取数据会将字符割裂，这就导致我们在使用read(byte[] b)方法读取文件时，
虽然指定了缓冲区的大小，但是仍然会出现乱码。下面这段代码可以很好地解释这一点
```java
public class Main {
    public static void main(String[] args) throws IOException {
        InputStream in = null;
        File f = new File("D:/test.txt");
        byte[] b = new byte[2];
        in = new FileInputStream(f);
        int i = 0;
        while ((i = in.read(b)) != -1) {
            //String str = new String(b);
			String str = new String(b,"GBK");  //指定解码方式
            System.out.print(str);
        }
    }
}
```

文件如下（采用ANSI编码）： 
how are you ? i am fine 
你好吗？我很好！
运行结果如下： 
how are you ? i am fine 
����������������
补充一点：在调用new String(byte[] b)这个构造方法时，java会根据传入的数据按照当前编码规则创建String，如果将编码方式改为GBK，则可以正常输出中文： 
这是因为，GBK编码每个汉字占两个字节，缓冲区大小设为2就可以避免字符编码割裂的情况。
如果前面的英文不是偶数个字符的话，也会输出乱码





```java
public abstract class InputStream implements Closeable {

private static final int MAX_SKIP_BUFFER_SIZE = 2048;

public abstract int read() throws IOException;    //下面重载的方法其核心还是调用这个read() ，每次读取一个字节

public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

 public int read(byte b[], int off, int len) throws IOException {   //循环读取，然后，每次把读取的结果放到字节数组中
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte)c;

        int i = 1;
        try {
            for (; i < len ; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte)c;
            }
        } catch (IOException ee) {
        }
        return i;    真正读取到的数组长度不一定为 len，返回值才是真正读取到的长度
    }
	
	// 跳过输入流中数据的n个字节
	public long skip(long n) throws IOException {

        long remaining = n;
        int nr;

        if (n <= 0) {
            return 0;
        }

        int size = (int)Math.min(MAX_SKIP_BUFFER_SIZE, remaining); 有一个最大跳过的字节数
        byte[] skipBuffer = new byte[size];
        while (remaining > 0) {
            nr = read(skipBuffer, 0, (int)Math.min(size, remaining)); 
            if (nr < 0) {
                break;
            }
            remaining -= nr;
        }

        return n - remaining;   返回跳过了多少字节
    }
	
	// 返回下一个方法调用能不受阻塞地从此读取（或者跳过）的估计字节数
	public int available() throws IOException {
        return 0;
    }
	
	// 关闭此输入流，并释放与其关联的所有资源
	public void close() throws IOException {}
	
	// 在此输出流中标记当前位置
	public synchronized void mark(int readlimit) {}
	
	
	 // 将此流重新定位到最后一次对此输入流调用 mark 方法时的位置
	public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
	
	
	// 测试此输入流是否支持 mark 和 reset 方法
	 public boolean markSupported() {
        return false;
    }

}
```



public int read(byte[] b) throws
参数：
b - 存储读入数据的缓冲区。

返回：
读入缓冲区的总字节数；如果因为已经到达流末尾而不再有数据可用，则返回 -1。

抛出：
- 如果不是因为流位于文件末尾而无法读取第一个字节；如果输入流已关闭；如果发生其他 I/O 错误。
- 如果 b 为 null。

1. 从输入流中读取一定数量的字节，并将其存储在缓冲区数组 b 中。以整数形式返回实际读取的字节数。在输入数据可用、检测到文件末尾或者抛出异常前，此方法一直阻塞。
2. 如果 b 的长度为 0，则不读取任何字节并返回 0；否则，尝试读取至少一个字节。如果因为流位于文件末尾而没有可用的字节，则返回值 -1；否则，至少读取一个字节并将其存储在 b 中。
3. 将读取的第一个字节存储在元素 b[0] 中，下一个存储在 b[1] 中，依次类推。读取的字节数最多等于 b 的长度。设k为实际读取的字节数；这些字节将存储在 b[0] 到 b[k-1] 的元素中，
不影响 b[k] 到 b[b.length-1] 的元素。

public int read(byte[] b, int off, int len) throws
参数：

b - 读入数据的缓冲区。

off - 数组 b 中将写入数据的初始偏移量。

len - 要读取的最大字节数。

返回：
读入缓冲区的总字节数；如果因为已到达流末尾而不再有数据可用，则返回 -1。

抛出：
- 如果不是因为位于文件末尾而无法读取第一个字节；如果输入流已关闭；如果发生其他 I/O 错误。
- 如果 b 为 null。
- 如果 off 为负，len 为负，或者 len 大于 b.length - off

1. 将输入流中最多 len 个数据字节读入 byte 数组。尝试读取 len 个字节，但读取的字节也可能小于该值。以整数形式返回实际读取的字节数。
2. 在输入数据可用、检测到流末尾或者抛出异常前，此方法一直阻塞。
3. 如果 len 为 0，则不读取任何字节并返回 0；否则，尝试读取至少一个字节。如果因为流位于文件末尾而没有可用的字节，则返回值 -1；否则，至少读取一个字节并将其存储在 b 中。
4. 将读取的第一个字节存储在元素 b[off] 中，下一个存储在 b[off+1] 中，依次类推。读取的字节数最多等于 len。设k为实际读取的字节数；这些字节将存储在 b[off] 到 b[off+k-1] 的元素中，
不影响 b[off+k] 到 b[off+len-1] 的元素。
5. 在任何情况下，b[0] 到 b[off] 的元素以及 b[off+len] 到 b[b.length-1] 的元素都不会受到影响。
6. 类 InputStream 的 read(b,off,len) 方法重复调用方法 read()。如果第一次这样的调用导致 IOException，则从对 read(b,off,len) 方法的调用中返回该异常。
如果对 read() 的任何后续调用导致 IOException，则捕获该异常并将其视为到达文件末尾；到达该点时读取的字节存储在 b 中，并返回发生异常之前读取的字节数。
在已读取输入数据 len 的请求数量、检测到文件结束标记、抛出异常前，此方法的默认实现将一直阻塞。建议子类提供此方法更为有效的实现。


问题：
java InputStream read(byte b[])为什么比read()效率要高？
1、read()是按字节读取输入流。
2、read(byte b[])是把输入流读到字节数组byte b[]。
看了一下read(byte b[])的源码，不也是循环调用read()这个方法呢？自己循环调用read()一个字节一个字节的读取，和read(byte b[])方法里循环调用read()一个字节一个字节的读取有何区别吗？


如果你看的是InputStream实现read(byte b[])的源码，的确是循环调用read()这个方法。但是InputStream是一个抽象类，InputStream实现的read(byte b[])方法只是它的默认实现，
很多InputStream的子类都会对read(byte b[])或者重写read(byte b[], int off, int len)，例如FileInputStream。看一下FileInputStream对read(byte b[])的实现源码，
你会发现它并不是循环调用read()，FileInputStream的read(byte b[])会比read()效率高很多。
所以一般情况下，InputStream的子类会重写read(byte b[])方法，所以read(byte b[])比read()效率要高。



参考：
https://blog.csdn.net/u010276761/article/details/52692962
http://www.51gjie.com/java/700.html



