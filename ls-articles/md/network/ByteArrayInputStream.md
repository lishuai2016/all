---
title: 字节数组流源码解析（ByteArrayInputStream）
categories: 
- IO
tags:
---

```java
package java.io;

/**
 * A <code>ByteArrayInputStream</code> contains
 * an internal buffer that contains bytes that
 * may be read from the stream. An internal
 * counter keeps track of the next byte to
 * be supplied by the <code>read</code> method.
 * <p>
 * Closing a <tt>ByteArrayInputStream</tt> has no effect. The methods in
 * this class can be called after the stream has been closed without
 * generating an <tt>IOException</tt>.
字节数组流中包含一个内部的buffer，这个流可以从这个buffer中读取数据


 */
public class ByteArrayInputStream extends InputStream {

    /**
     * An array of bytes that was provided
     * by the creator of the stream. Elements <code>buf[0]</code>
     * through <code>buf[count-1]</code> are the
     * only bytes that can ever be read from the
     * stream;  element <code>buf[pos]</code> is
     * the next byte to be read.
     */
	 构建这个流需要传入这个buf数组对象，使得这个流的可读数据为buf[0]到buf[count-1]，下一个可读的为>buf[pos]
    protected byte buf[];

    /**
     * The index of the next character to read from the input stream buffer.
     * This value should always be nonnegative
     * and not larger than the value of <code>count</code>.
     * The next byte to be read from the input stream buffer
     * will be <code>buf[pos]</code>.
     */
    protected int pos;  下一个可读的位置

    /**
     * The currently marked position in the stream.
     * ByteArrayInputStream objects are marked at position zero by
     * default when constructed.  They may be marked at another
     * position within the buffer by the <code>mark()</code> method.
     * The current buffer position is set to this point by the
     * <code>reset()</code> method.
     * <p>
     * If no mark has been set, then the value of mark is the offset
     * passed to the constructor (or 0 if the offset was not supplied).
     *
     * @since   JDK1.1
     */
    protected int mark = 0;   流的标记位置，默认值为0，也可以通过构造函数传入offset设置为Mark的初始位置

    /**
     * The index one greater than the last valid character in the input
     * stream buffer.
     * This value should always be nonnegative
     * and not larger than the length of <code>buf</code>.
     * It  is one greater than the position of
     * the last byte within <code>buf</code> that
     * can ever be read  from the input stream buffer.
     */
    protected int count;   最大可用字节数组的索引，比最后一个可用字节索引大1，即0 <= count <= buf.length

    /**
     * Creates a <code>ByteArrayInputStream</code>
     * so that it  uses <code>buf</code> as its
     * buffer array.
     * The buffer array is not copied.
     * The initial value of <code>pos</code>
     * is <code>0</code> and the initial value
     * of  <code>count</code> is the length of
     * <code>buf</code>.
     *
     * @param   buf   the input buffer.
     */
    public ByteArrayInputStream(byte buf[]) {   初始化对象，即整个字节数组都可用
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }

    /**
     * Creates <code>ByteArrayInputStream</code>
     * that uses <code>buf</code> as its
     * buffer array. The initial value of <code>pos</code>
     * is <code>offset</code> and the initial value
     * of <code>count</code> is the minimum of <code>offset+length</code>
     * and <code>buf.length</code>.
     * The buffer array is not copied. The buffer's mark is
     * set to the specified offset.
     *
     * @param   buf      the input buffer.
     * @param   offset   the offset in the buffer of the first byte to read.
     * @param   length   the maximum number of bytes to read from the buffer.
     */
    public ByteArrayInputStream(byte buf[], int offset, int length) {  初始化流对象，从offset位置开始的字节才可以使用，并且把Mark位置设置为偏移位置处
        this.buf = buf;
        this.pos = offset;   
        this.count = Math.min(offset + length, buf.length);   可用的最大索引不能超过字节数组的长度
        this.mark = offset;
    }

    /**
     * Reads the next byte of data from this input stream. The value
     * byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>. If no byte is available
     * because the end of the stream has been reached, the value
     * <code>-1</code> is returned.
     * <p>
     * This <code>read</code> method
     * cannot block.
     *
     * @return  the next byte of data, or <code>-1</code> if the end of the
     *          stream has been reached.
     */
    public synchronized int read() {    读取一个字节，转化成整数返回。该方法加上同步操作
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }

    /**
     * Reads up to <code>len</code> bytes of data into an array of bytes
     * from this input stream.
     * If <code>pos</code> equals <code>count</code>,
     * then <code>-1</code> is returned to indicate
     * end of file. Otherwise, the  number <code>k</code>
     * of bytes read is equal to the smaller of
     * <code>len</code> and <code>count-pos</code>.
     * If <code>k</code> is positive, then bytes
     * <code>buf[pos]</code> through <code>buf[pos+k-1]</code>
     * are copied into <code>b[off]</code>  through
     * <code>b[off+k-1]</code> in the manner performed
     * by <code>System.arraycopy</code>. The
     * value <code>k</code> is added into <code>pos</code>
     * and <code>k</code> is returned.
     * <p>
     * This <code>read</code> method cannot block.
     *
     * @param   b     the buffer into which the data is read.
     * @param   off   the start offset in the destination array <code>b</code>
     * @param   len   the maximum number of bytes read.
     * @return  the total number of bytes read into the buffer, or
     *          <code>-1</code> if there is no more data because the end of
     *          the stream has been reached.
     * @exception  NullPointerException If <code>b</code> is <code>null</code>.
     * @exception  IndexOutOfBoundsException If <code>off</code> is negative,
     * <code>len</code> is negative, or <code>len</code> is greater than
     * <code>b.length - off</code>
     */
    public synchronized int read(byte b[], int off, int len) {    从输入流中读取，最大len个字节放到b数组中，并且从b数组的off位置处开始放
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {   边界判断，使得off+len 不超过字节数组b的长度
            throw new IndexOutOfBoundsException();
        }

        if (pos >= count) {   读到流的尾部了，返回-1
            return -1;
        }

        int avail = count - pos;  流中的可读的字节数
        if (len > avail) {
            len = avail;          如果流中剩余的字节数比用户要读的字节数小，那么用户只能读取还剩下的字节数
        }
        if (len <= 0) {          如果用户没有读取字节，直接返回0
            return 0; 
        }
        System.arraycopy(buf, pos, b, off, len);    底层的native方法，把buf中从pos位置开始的数据复制到b数组从off开始出，复制的长度为len
        pos += len;
        return len;   返回从流中读取的字节数
    }

    /**
     * Skips <code>n</code> bytes of input from this input stream. Fewer
     * bytes might be skipped if the end of the input stream is reached.
     * The actual number <code>k</code>
     * of bytes to be skipped is equal to the smaller
     * of <code>n</code> and  <code>count-pos</code>.
     * The value <code>k</code> is added into <code>pos</code>
     * and <code>k</code> is returned.
     *
     * @param   n   the number of bytes to be skipped.
     * @return  the actual number of bytes skipped.
     */
    public synchronized long skip(long n) {
        long k = count - pos;      流中可以跳过的最大字节数
        if (n < k) {				通过比较流中可以跳过的最大字节数和用户想跳过的字节数，取两者中的最小值，并且必须满足大于0
            k = n < 0 ? 0 : n;
        }

        pos += k;      
        return k;  返回真实跳过了流中的字节数
    }

    /**
     * Returns the number of remaining bytes that can be read (or skipped over)
     * from this input stream.
     * <p>
     * The value returned is <code>count&nbsp;- pos</code>,
     * which is the number of bytes remaining to be read from the input buffer.
     *
     * @return  the number of remaining bytes that can be read (or skipped
     *          over) from this input stream without blocking.
     */
    public synchronized int available() {     返回流中可用的字节数
        return count - pos;
    }

    /**
     * Tests if this <code>InputStream</code> supports mark/reset. The
     * <code>markSupported</code> method of <code>ByteArrayInputStream</code>
     * always returns <code>true</code>.
     *
     * @since   JDK1.1
     */
    public boolean markSupported() {    是否开始mark/reset功能，这个字节数组流支持，返回true
        return true;
    }

    /**
     * Set the current marked position in the stream.
     * ByteArrayInputStream objects are marked at position zero by
     * default when constructed.  They may be marked at another
     * position within the buffer by this method.
     * <p>
     * If no mark has been set, then the value of the mark is the
     * offset passed to the constructor (or 0 if the offset was not
     * supplied).
     *
     * <p> Note: The <code>readAheadLimit</code> for this class
     *  has no meaning.
     *
     * @since   JDK1.1
     */
    public void mark(int readAheadLimit) {   把Mark标记位置设置为当前可读位置
        mark = pos;
    }

    /**
     * Resets the buffer to the marked position.  The marked position
     * is 0 unless another position was marked or an offset was specified
     * in the constructor.
     */
    public synchronized void reset() {      把可读位置设置为之前的Mark位置，这样可以重复读取有个流中的数据
        pos = mark;
    }

    /**
     * Closing a <tt>ByteArrayInputStream</tt> has no effect. The methods in
     * this class can be called after the stream has been closed without
     * generating an <tt>IOException</tt>.
     * <p>
     */
    public void close() throws IOException {   关闭流依旧没有实现，所有调用这个方法没做任何事情
    }

}

```

