/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author guochen
 */
public class DashboardManagerTestMockup
{
	private DashboardManagerTestMockup() {
	  }

	private static final Logger logger = LogManager.getLogger(DashboardManagerTestMockup.class);
	
	public static void executeRepeatedly(int threadNum, final int repeatsPerThread, final Runnable r,
			final Runnable postThreadRun, final Runnable postAllRun) throws InterruptedException
	{
		ExecutorService exec = Executors.newFixedThreadPool(threadNum);
		final double[] threadAverageDuration = new double[threadNum];
		for (int i = 0; i < threadNum; i++) {
			final int threadIndex = i;
			exec.execute(new Runnable() {
				@Override
				public void run()
				{
					try {
						long start = System.currentTimeMillis();
						for (int j = 0; j < repeatsPerThread; j++) {
							r.run();
							
						}
						long totalDuration = System.currentTimeMillis() - start;
						threadAverageDuration[threadIndex] = totalDuration / repeatsPerThread;
						
						if (postThreadRun != null) {
							postThreadRun.run();
						}
					}
					catch (SecurityException e) {
						logger.info("context",e);
					}
					catch (IllegalArgumentException e) {
						logger.info("context",e);
					}
				}
			});
		}
		exec.shutdown();
		exec.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		double average = 0;
		for (int i = 0; i < threadNum; i++) {
			average += threadAverageDuration[i];
		}
		average /= threadNum;
		
		if (postAllRun != null) {
			postAllRun.run();
		}
	}
}
