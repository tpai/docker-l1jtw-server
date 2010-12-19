/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package l1j.server.server;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.model.monitor.L1PcMonitor;

public class GeneralThreadPool {
	private static Logger _log = Logger.getLogger(GeneralThreadPool.class
			.getName());

	private static GeneralThreadPool _instance;

	private static final int SCHEDULED_CORE_POOL_SIZE = 10;

	private Executor _executor; // 通用的ExecutorService
	private ScheduledExecutorService _scheduler; // 通用的ScheduledExecutorService
	private ScheduledExecutorService _pcScheduler; // 監測完家專用的ScheduledExecutorService
	// L1J一応在默認狀態，地圖：4什麼也不做，籌委會是一個被佔領的第二次運行時間大約為 6毫秒(AutoUpdate:約6ms,ExpMonitor:極小)
	private final int _pcSchedulerPoolSize = 1 + Config.MAX_ONLINE_USERS / 20; // 每 20 人增加一個 PoolSize

	public static GeneralThreadPool getInstance() {
		if (_instance == null) {
			_instance = new GeneralThreadPool();
		}
		return _instance;
	}

	private GeneralThreadPool() {
		if (Config.THREAD_P_TYPE_GENERAL == 1) {
			_executor = Executors
					.newFixedThreadPool(Config.THREAD_P_SIZE_GENERAL);
		} else if (Config.THREAD_P_TYPE_GENERAL == 2) {
			_executor = Executors.newCachedThreadPool();
		} else {
			_executor = null;
		}
		_scheduler = Executors
				.newScheduledThreadPool(SCHEDULED_CORE_POOL_SIZE,
						new PriorityThreadFactory("GerenalSTPool",
								Thread.NORM_PRIORITY));
		_pcScheduler = Executors.newScheduledThreadPool(_pcSchedulerPoolSize,
				new PriorityThreadFactory("PcMonitorSTPool",
						Thread.NORM_PRIORITY));
	}

	public void execute(Runnable r) {
		if (_executor == null) {
			Thread t = new Thread(r);
			t.start();
		} else {
			_executor.execute(r);
		}
	}

	public void execute(Thread t) {
		t.start();
	}

	public ScheduledFuture<?> schedule(Runnable r, long delay) {
		try {
			if (delay <= 0) {
				_executor.execute(r);
				return null;
			}
			return _scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null;
		}
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable r,
			long initialDelay, long period) {
		return _scheduler.scheduleAtFixedRate(r, initialDelay, period,
				TimeUnit.MILLISECONDS);
	}

	public ScheduledFuture<?> pcSchedule(L1PcMonitor r, long delay) {
		try {
			if (delay <= 0) {
				_executor.execute(r);
				return null;
			}
			return _pcScheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null;
		}
	}

	public ScheduledFuture<?> pcScheduleAtFixedRate(L1PcMonitor r,
			long initialDelay, long period) {
		return _pcScheduler.scheduleAtFixedRate(r, initialDelay, period,
				TimeUnit.MILLISECONDS);
	}

	// ThreadPoolManager 借用
	private class PriorityThreadFactory implements ThreadFactory {
		private final int _prio;

		private final String _name;

		private final AtomicInteger _threadNumber = new AtomicInteger(1);

		private final ThreadGroup _group;

		public PriorityThreadFactory(String name, int prio) {
			_prio = prio;
			_name = name;
			_group = new ThreadGroup(_name);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		public Thread newThread(Runnable r) {
			Thread t = new Thread(_group, r);
			t.setName(_name + "-" + _threadNumber.getAndIncrement());
			t.setPriority(_prio);
			return t;
		}

		public ThreadGroup getGroup() {
			return _group;
		}
	}
}
