package com.folkcat.spider;

public class Test {
	public static void main(String args[]){
		String str="hello my name is donghaifeng";
		int a=str.indexOf("my");
		int b=str.indexOf("hai");
		System.out.println(str.subSequence(a, b));
	}
}
