//package com.ls.lishuai.serialization.json;
//
//import com.google.gson.Gson;
//import com.ls.lishuai.serialization.model.UserVo;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GsonTest {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		UserVo src = new UserVo();
//		src.setName("Yaoming");
//		src.setAge(30);
//		src.setPhone(13789878978L);
//
//		UserVo f1 = new UserVo();
//		f1.setName("tmac");
//		f1.setAge(32);
//		f1.setPhone(138999898989L);
//		UserVo f2 = new UserVo();
//		f2.setName("liuwei");
//		f2.setAge(29);
//		f2.setPhone(138999899989L);
//
//		List<UserVo> friends = new ArrayList<UserVo>();
//		friends.add(f1);
//		friends.add(f2);
//		src.setFriends(friends);
//
//
//		Gson gson = new Gson();
//
//System.out.println(gson.toJson(src));
///**
//{"name":"Yaoming","age":30,"phone":13789878978,
//"friends":[{"name":"tmac","age":32,"phone":138999898989},
//{"name":"liuwei","age":29,"phone":138999899989}]}
//
// */
//	}
//
//}
