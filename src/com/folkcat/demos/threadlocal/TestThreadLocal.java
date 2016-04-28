package com.folkcat.demos.threadlocal;

public class TestThreadLocal {
	public static void main(String args[]){
		ThreadLocal<Boolean> booleanThreadLocal=new ThreadLocal<Boolean>();
		
		booleanThreadLocal.set(true);
		System.out.println("Thread#main booleanThreadLocal="+booleanThreadLocal.get());
		
		new Thread("Thread#1"){
			@Override
			public void run(){
				booleanThreadLocal.set(false);
				System.out.println("Thread#1 booleanThreadLocal="+booleanThreadLocal.get());
			}
		}.start();;
		
		new Thread("Thread#2"){
			@Override
			public void run(){
				System.out.println("Thread#1 booleanThreadLocal="+booleanThreadLocal.get());
			}
		}.start();
	}

}
