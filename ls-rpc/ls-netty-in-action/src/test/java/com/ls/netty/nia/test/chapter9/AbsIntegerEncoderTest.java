//package com.ls.netty.nia.test.chapter9;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.embedded.EmbeddedChannel;
//import org.junit.Test;
//
//
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
///**
// * Listing 9.4 Testing the AbsIntegerEncoder
// *
// * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
// */
//public class AbsIntegerEncoderTest {
//    @Test
//    public void testEncoded() {
//        ByteBuf buf = Unpooled.buffer();
//        for (int i = 1; i < 10; i++) {
//            buf.writeInt(i * -1);
//        }
//
//        EmbeddedChannel channel = new EmbeddedChannel(
//            new AbsIntegerEncoder());
//        assertTrue(channel.writeOutbound(buf));
//        assertTrue(channel.finish());
//
//        // read bytes
//        for (int i = 1; i < 10; i++) {
////            assertEquals(i, channel.readOutbound());
//        }
//        assertNull(channel.readOutbound());
//    }
//}
