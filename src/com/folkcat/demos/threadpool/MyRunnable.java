package com.folkcat.demos.threadpool;

class MyRunnable implements Runnable {
	private String name;

	public MyRunnable(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			System.out.println(this.name + " is running.");
			Thread.sleep(400);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}