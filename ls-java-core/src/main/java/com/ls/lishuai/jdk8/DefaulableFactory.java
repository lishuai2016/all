package com.ls.lishuai.jdk8;//package ls.jdk8;
//
//import java.util.function.Supplier;
//
///**
// * @Author: lishuai
// * @CreateDate: 2018/8/3 11:59
// */
//private interface DefaulableFactory {
//    // Interfaces now allow static methods
//    static Defaulable create( Supplier< Defaulable > supplier ) {
//        return supplier.get();
//    }
//    public static void main( String[] args ) {
//        Defaulable defaulable = DefaulableFactory.create( DefaultableImpl::new );
//        System.out.println( defaulable.notRequired() );
//
//        defaulable = DefaulableFactory.create( OverridableImpl::new );
//        System.out.println( defaulable.notRequired() );
//    }
//
//}
//
