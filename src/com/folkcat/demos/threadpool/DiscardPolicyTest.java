package com.folkcat.demos.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DiscardPolicyTest {
	public static void main(String[] args) throws Exception {

		// 创建线程池。线程池的"最大池大小"和"核心池大小"都为1(THREADS_SIZE)，"线程池"的阻塞队列容量为1(CAPACITY)。
		ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 20, 0,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(500));
		// 设置线程池的拒绝策略为"丢弃"
		// pool.setRejectedExecutionHandler(new
		// ThreadPoolExecutor.DiscardPolicy());

		// 新建10个任务，并将它们添加到线程池中。
		for (int i = 0; i < 100; i++) {
			Runnable myrun = new MyRunnable("task-" + i);
			pool.execute(myrun);
		}
		// 关闭线程池
		pool.shutdown();
	}
}