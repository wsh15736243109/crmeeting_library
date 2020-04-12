package com.cr.pn.Utils.runable;

import android.annotation.SuppressLint;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务线程池.
 * 复杂的业务逻辑用业务线程池处理.
 * @author zy
 *
 */
public class StandardThreadExecutor extends ThreadPoolExecutor {

	public static final int DEFAULT_MIN_THREADS = 30;
	public static final int DEFAULT_MAX_THREADS = 200;
	public static final int DEFAULT_MAX_IDLE_TIME = 2000; // 1 minutes

	protected AtomicInteger submittedTasksCount; // 正在处理的任务数
	private int maxSubmittedTaskCount; // 最大允许同时处理的任务数

	public StandardThreadExecutor() {
		this(DEFAULT_MIN_THREADS, DEFAULT_MAX_THREADS);
	}

	public StandardThreadExecutor(int coreThread, int maxThreads) {
		this(coreThread, maxThreads, maxThreads);
	}

	public StandardThreadExecutor(int coreThread, int maxThreads, long keepAliveTime, TimeUnit unit) {
		this(coreThread, maxThreads, keepAliveTime, unit, maxThreads);
	}

	public StandardThreadExecutor(int coreThreads, int maxThreads, int queueCapacity) {
		this(coreThreads, maxThreads, queueCapacity, Executors.defaultThreadFactory());
	}

	public StandardThreadExecutor(int coreThreads, int maxThreads, int queueCapacity, ThreadFactory threadFactory) {
		this(coreThreads, maxThreads, DEFAULT_MAX_IDLE_TIME, TimeUnit.MILLISECONDS, queueCapacity, threadFactory);
	}

	public StandardThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit,
			int queueCapacity) {
		this(coreThreads, maxThreads, keepAliveTime, unit, queueCapacity, Executors.defaultThreadFactory());
	}

	public StandardThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit, int queueCapacity,
			ThreadFactory threadFactory) {
		this(coreThreads, maxThreads, keepAliveTime, unit, queueCapacity, threadFactory, new AbortPolicy());
	}

	public StandardThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit, int queueCapacity,
			ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(coreThreads, maxThreads, keepAliveTime, unit, new ExecutorQueue(), threadFactory, handler);
		((ExecutorQueue) getQueue()).setStandardThreadExecutor(this);

		submittedTasksCount = new AtomicInteger(0);
		// 最大并发任务限制： 队列buffer数 + 最大线程数
		maxSubmittedTaskCount = queueCapacity;
	}

	public boolean executeRunnable(Runnable command) {
		int count = submittedTasksCount.getAndSet(getActiveCount());
		// 依赖的LinkedTransferQueue没有长度限制，因此这里进行控制
		if (count >= maxSubmittedTaskCount-1) {
			return false;
		}
		super.execute(command);
		return true;
	}

	/**
	 * 返回提交的任务数.
	 * @return
	 */
	public int getSubmittedTasksCount() {
		return this.submittedTasksCount.get();
	}

	/**
	 * 返回最大任务数.
	 * @return
	 */
	public int getMaxSubmittedTaskCount() {
		return maxSubmittedTaskCount;
	}

}

/**
 * LinkedTransferQueue 能保证更高性能，相比与LinkedBlockingQueue有明显提升
 * <p/>
 * 
 * <pre>
 * 		1) 不过LinkedTransferQueue的缺点是没有队列长度控制，需要在外层协助控制
 * </pre>
 *
 * @author maijunsheng
 */
@SuppressLint("NewApi")
class ExecutorQueue extends LinkedTransferQueue<Runnable> {
	private static final long serialVersionUID = -265236426751004839L;
	
	StandardThreadExecutor threadPoolExecutor;

	public ExecutorQueue() {
		super();
	}

	public void setStandardThreadExecutor(StandardThreadExecutor threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;
	}

	// 注：代码来源于 tomcat
	public boolean force(Runnable o) {
		if (threadPoolExecutor.isShutdown()) {
			throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
		}
		// forces the item onto the queue, to be used if the task is rejected
		return super.offer(o);
	}

	// 注：tomcat的代码进行一些小变更
	public boolean offer(Runnable o) {
		int poolSize = threadPoolExecutor.getPoolSize();

		// 当线程池中的线程等于最大线程数时.
		if (poolSize == threadPoolExecutor.getMaximumPoolSize()) {
			return super.offer(o);
		}
		
		if (threadPoolExecutor.getSubmittedTasksCount() <= poolSize) {
			return super.offer(o);
		}
		// if we have less threads than maximum force creation of a new
		// thread
		if (poolSize < threadPoolExecutor.getMaximumPoolSize()) {
			return false;
		}
		// if we reached here, we need to add it to the queue
		return super.offer(o);
	}
}
