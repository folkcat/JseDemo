
//通过继承Thread类达到多线程目的

package com.folkcat.demos;

public class MultiThread1 extends Thread{
	 public MultiThread1() {
		// TODO Auto-generated constructor stub
	}
		 
    
 
    public MultiThread1(String name) {
        this.name = name;
    }
 
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(name + "运行     " + i);
        }
    }
 
    public static void main(String[] args) {
    	MultiThread1 h1=new MultiThread1("A");
    	MultiThread1 h2=new MultiThread1("B");
        h1.start();
        h2.start();
    }
 
    private String name;
}
	

