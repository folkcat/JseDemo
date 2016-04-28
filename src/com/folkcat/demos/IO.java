package com.folkcat.demos;

import java.io.File;

public class IO {
	public static void main(String args[]){
		File f=new File("D:\\1");
	        try{
	            f.mkdir();
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	

}
