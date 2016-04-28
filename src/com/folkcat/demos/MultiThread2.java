//通过实现Runnable接口达到多线程目的

package com.folkcat.demos;

public class MultiThread2 implements Runnable {
	private String name;
	public MultiThread2() {
		// TODO Auto-generated constructor stub
	}
	public MultiThread2(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}
	
	
	
	public void  run(){
		for (int i = 0; i < 5; i++) {
            System.out.println(name + "运行     " + i);
        }
		
	}
	
	
	public static void main(String[] args) {
	MultiThread2 h1=new MultiThread2("线程A");
        Thread demo= new Thread(h1);
        MultiThread2 h2=new MultiThread2("线程Ｂ");
        Thread demo1=new Thread(h2);
        demo.start();
        demo1.start();
    }

}
