[java中Base64转码与解码（加密与解密）原理与使用](https://blog.csdn.net/chenleixing/article/details/46543901)
[常见的编码概述](http://www.cnblogs.com/infosec-hoary/archive/2012/02/28/2371385.html)


base64编码（把3个字节的数据变成4个字节，比原来的长1/3，不够的补=， A--Z,a--z,0--9,+,/使用者64个字符，
此外还会出现= 和/）

应用：
1、加密数据，安全网络传输

```java
public class Base {

     public static void main(String[] args) {
          final String text = "Base64";
          System.out.println( text.length() );
        final String encoded = Base64
            .getEncoder()
            .encodeToString( text.getBytes( StandardCharsets.UTF_8 ) );
        System.out.println( text.length() );
        System.out.println( encoded.length() );

        final String decoded = new String(
            Base64.getDecoder().decode( encoded ),
            StandardCharsets.UTF_8 );
        System.out.println( decoded );

     }

}

```