package com.ls.io.nio_test;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/30 11:20
 */

import java.nio.*;


public class SliceBuffer {
    static public void main( String args[] ) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate( 10 );

        for (int i=0; i<buffer.capacity(); ++i) {
            buffer.put( (byte)i );
        }

        buffer.position( 3 );
        buffer.limit( 7 );

        ByteBuffer slice = buffer.slice();

        for (int i=0; i<slice.capacity(); ++i) {
            byte b = slice.get( i );
            b *= 11;
            slice.put( i, b );
        }

        buffer.position( 0 );
        buffer.limit( buffer.capacity() );

        while (buffer.remaining()>0) {
            System.out.println( buffer.get() );
        }
    }
}
