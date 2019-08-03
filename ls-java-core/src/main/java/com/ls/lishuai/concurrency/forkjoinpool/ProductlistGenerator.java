package com.ls.lishuai.concurrency.forkjoinpool;

import java.util.ArrayList;
import java.util.List;

public class ProductlistGenerator {

	public List<Product> genetor(int size) {
		List<Product> list = new ArrayList<Product>();
		for (int i=0;i <size;i++) {
			Product p = new Product();
			p.setName("product"+i);
			p.setPrice(10);
			list.add(p);
		}
		return list;
	}
}
