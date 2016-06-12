package com.folkcat.test;

public class Test {
	public static void main(String args[]){
		byte bytesArray[]=new byte[1024];
		bytesArray[0]=20;
		bytesArray[1]=21;
		int size=0;
		for(;;){
			if(bytesArray[size]==0){
				break;
			}
			size++;
		}
		System.out.println("Size:"+size);
	}
}
