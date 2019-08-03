///**
// *
// */
//package com.ls.li.poi;
//
//import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.ss.usermodel.Row;
//import org.junit.Test;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author lishuai
// * @data 2016-11-7 下午5:38:24
// */
//
//public class TestPOI {
//
//
//
//	@Test
//    public void testWriteExcel() throws IOException{
//		//String[] headers={"姓名","性别","年龄","身高","住址","联系电话"};
//		 List<Map<String, String>> listMap= new ArrayList<Map<String, String>>();
//		  List<String> list=new ArrayList<String>();
//		  list.add("姓名");
//		  list.add("性别");
//		  list.add("年龄");
//		  list.add("身高");
//		  list.add("住址");
//		  list.add("联系电话");
//
//		  Map<String, String> map2=new HashMap<String, String>();
//		  map2.put("姓名", "张三");
//		  map2.put("性别", "男");
//		  map2.put("年龄", "13333333333333333333333333333333333333333333333333333333333333333333333333333333333");
//		  map2.put("身高", "57");
//		  map2.put("住址", "啊啊啊啊");
//		  map2.put("联系电话", "123456");
//		  listMap.add(map2);
//
//		  Map<String, String> map1=new HashMap<String, String>();
//		  map1.put("姓名", "王五");
//		  map1.put("性别", "女");
//		  map1.put("年龄", "23");
//		  map1.put("身高", "09");
//		  map1.put("住址", "发广告");
//		  map1.put("联系电话", "099765");
//		  listMap.add(map1);
//		// 声明一个工作薄
//	      HSSFWorkbook workbook = new HSSFWorkbook();
//	      // 生成一个表格
//	      HSSFSheet sheet = workbook.createSheet("测试1");
//	   // 生成一个样式
//	      HSSFCellStyle style = workbook.createCellStyle();
//	      // 设置这些样式
//	      style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//	      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//	      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//	      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//	      style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//	      style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//	      style.setAlignment(HSSFCellStyle.ALIGN_FILL);
//
//	      //产生表格标题行
//	      HSSFRow row = sheet.createRow(0);
//	      for (int i = 0; i < list.size(); i++) {
//	         HSSFCell cell = row.createCell(i);
//	         HSSFRichTextString text = new HSSFRichTextString(list.get(i));
//	         cell.setCellValue(text);
//	      }
//
//	      //数据输出行
//	      for(int j=0;j<listMap.size();j++){
//	    	  HSSFRow r = sheet.createRow(j+1);
//	    	  for(int k = 0; k < list.size(); k++){
//	    		  HSSFCell cell = r.createCell(k);
//	    		  Map<String, String> map = listMap.get(j);
//	    		  String value = map.get(list.get(k));
//	    		  HSSFRichTextString text = new HSSFRichTextString(value);
//	 	         cell.setCellValue(text);
//	    	  }
//	      }
//
//
//	      OutputStream out = new FileOutputStream("d://b.xls");
//
//	      workbook.write(out);
//
//	      out.close();
//
//
//
//	}
//
//
//
//	@Test
//    public void testReadExcel() throws FileNotFoundException, IOException{
//
//		 FileInputStream fileInputStream = new FileInputStream(new File("d:\\p2p案例.xls"));
//         HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
//
//
//
//
//         List<String> list=new ArrayList<String>();
//
//         List<Map<String, String>> listMap= new ArrayList<Map<String, String>>();
//
//         for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//
//             HSSFSheet sheet = workbook.getSheetAt(i);
//             int firstRowNum = sheet.getFirstRowNum();
//             int lastRowNum = sheet.getLastRowNum();
//             //获取表头数据，放到一个list中
//             Row firstRow = sheet.getRow(firstRowNum);
//             short firstCellNum = firstRow.getFirstCellNum();
//             short lastCellNum = firstRow.getLastCellNum();
//             for(int j=firstCellNum;j<lastCellNum;j++){
//            	 list.add(firstRow.getCell(j).getStringCellValue());
//             }
//             System.out.println(list.toString());
//
//             //遍历除表头的行的数据放到map中
//             for (int k=firstRowNum+1;k<=lastRowNum;k++) {
//            	 StringBuilder  sb =new StringBuilder();
//            	 Map<String, String> map=new HashMap<String, String>();
//            	 Row row = sheet.getRow(k);
//            	 //每一行数据对应一个map，其中的key为表头对应的列，value是每个单元格的内容
//            	 for(short j=firstCellNum;j<lastCellNum;j++){
//
//            		 String v ="";
//            		 if(row.getCell(j).getCellType()==0){
//            			 double value = row.getCell(j).getNumericCellValue();
//            			 v=Double.toString(value);
//            		 }else{
//            			 v = row.getCell(j).getStringCellValue().trim();
//
//            		 }
//
//            		 sb.append(v.toString()+"|");
//            		 map.put(list.get(j), v);
//            	 }
//            	 listMap.add(map);
//            	 System.out.println(sb.toString());
//             }
//             System.out.println();
//         }
//
//
//
//      // 声明一个工作薄
//	      HSSFWorkbook workbook1 = new HSSFWorkbook();
//	      // 生成一个表格
//	      HSSFSheet sheet = workbook1.createSheet("测试1");
//
//
//	      //产生表格标题行
//	      HSSFRow row = sheet.createRow(0);
//	      for (int i = 0; i < list.size(); i++) {
//	         HSSFCell cell = row.createCell(i);
//	         HSSFRichTextString text = new HSSFRichTextString(list.get(i));
//	         cell.setCellValue(text);
//	      }
//
//	      //数据输出行
//	      for(int j=0;j<listMap.size();j++){
//	    	  HSSFRow r = sheet.createRow(j+1);
//	    	  for(int k = 0; k < list.size(); k++){
//	    		  HSSFCell cell = r.createCell(k);
//	    		  Map<String, String> map = listMap.get(j);
//	    		  String value = map.get(list.get(k));
//	    		  HSSFRichTextString text = new HSSFRichTextString(value);
//	 	         cell.setCellValue(text);
//	    	  }
//	      }
//
//
//	      OutputStream out = new FileOutputStream("d://c.xls");
//
//	      workbook.write(out);
//
//	      out.close();
//
//
//
//    }
//
//}
