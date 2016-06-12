package com.folkcat.spider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TaobaoDBController {
	private String mUrl = "jdbc:mysql://localhost:3306/goods?"
			+ "user=root&password=6285720&useUnicode=true&characterEncoding=UTF8";
	Connection mConn = null;
	Statement mStatement=null;
	
	public TaobaoDBController() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			mConn= DriverManager.getConnection(mUrl);
			mStatement=mConn.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	public boolean insert(String goodsUrl,String imgUrl,String originPrice,String goodsTitle){
		if(goodsUrl==null|imgUrl==null|originPrice==null|goodsTitle==null){
			return false;
		}
		if(goodsUrl.equals("")|imgUrl.equals("")|originPrice.equals("")|goodsTitle.equals("")){
			return false;
		}
		int a=goodsUrl.indexOf('=');
		int b=goodsUrl.indexOf('&');
		String tmall_id=goodsUrl.substring(a+1, b);
		
		String sql = "insert into goods(goods_url,img_url,origin_price,goods_title,tmall_id) values('"+goodsUrl+"','"+imgUrl+"','"+originPrice+"','"+goodsTitle+"','"+tmall_id+"')";
		try {
			mStatement.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("插入数据出错啦");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

}
