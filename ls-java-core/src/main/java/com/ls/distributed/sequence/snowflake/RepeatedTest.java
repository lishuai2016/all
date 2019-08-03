//package com.ls.distributed.sequence.snowflake;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class RepeatedTest {
//
//	/**
//	 * 重复性测试
//	 */
//	@Test
//	public void testRepeated() {
//		Set<Long> set = new HashSet<Long>();
//		int maxTimes = 10000 * 10;
//		Sequence sequence = new Sequence(0, 0);
//		for (int i = 0; i < maxTimes; i++) {
//			set.add(sequence.nextId());
//		}
//		Assert.assertEquals(maxTimes, set.size());
//	}
//
//}
