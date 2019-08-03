package com.ls.lishuai.jdk8;//package ls.jdk8;
//
//import java.util.Arrays;
//
//
///**
// * https://blog.csdn.net/u014470581/article/details/54944384
// *
// *
// *
// */
//public class T {
//
//	public static void main(String[] args) {
//		Arrays.asList( "a", "b", "d" ).forEach( e -> System.out.println(e) );
//
//		Arrays.asList( "a", "b", "d" ).forEach( ( String e ) -> System.out.println( e ) );
//
//		Arrays.asList( "a", "b", "d" ).forEach( e -> {
//			System.out.print( e );
//			System.out.print( e );
//		} );
//		String separator = ",";
//		Arrays.asList( "a", "b", "d" ).forEach(
//				( String e ) -> System.out.print( e + separator ) );
//
//		final String separator1 = ",";
//		Arrays.asList( "a", "b", "d" ).forEach(
//				( String e ) -> System.out.print( e + separator1 ) );
//
//		Arrays.asList( "a", "b", "d" ).sort( ( e1, e2 ) -> e1.compareTo( e2 ) );
//
//		Arrays.asList( "a", "b", "d" ).sort( ( e1, e2 ) -> {
//			int result = e1.compareTo( e2 );
//			return result;
//		} );
//
//
//
//
//
//	}
//
//}
