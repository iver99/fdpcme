package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;
import oracle.sysman.qatool.uifwk.webdriver.logging.EMTestLogger;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * @author shangwan
 */
public class SuiteLevelSetup
{
	/*
	 * below is the program to run a command line shell file in java with output in a log
	 * @param Argument[] as a array of arguments
	 * @param testLog as a test Logger where the output will be stored.
	 */
	public static String runCommand(String[] inpCmd, Logger testLog)
	{
		String result = "";
		// Time out limit for command execution, setting it to 5 mins
		// This value will be increased/decreased post discussion
		long timeOutAfter = 300;
		result = SuiteLevelSetup.runCommand(inpCmd, timeOutAfter, testLog);
		return result;
	}

	/*
	 * below is the program to run a command line shell file in java with output in a log
	 * @param Argument[] as a array of arguments
	 * @param testLog as a test Logger where the output will be stored.
	 */
	public static String runCommand(String[] inpCmd, long timeOutAfter, Logger testLog1)
	{
		final Logger testLog = testLog1;
		// testLog.info("<Method>: " +
		// Thread.currentThread().getStackTrace()[1].getMethodName() + "()");
		String s;
		String result = "";
		// convert to millisecsonds
		final long timeOutTime = timeOutAfter * 1000;
		try {
			String inputArray = Arrays.toString(inpCmd);
			testLog.info("#########################################################");
			testLog.log(Level.INFO, " #### Calling  runCommand with argumenents {0}", inputArray);
			// run a thread which will set a flag once it has slept
			// timeout period
			// waitFor 3 secs and check for stauts
			final long sleepFor = 3000;
			final long count = timeOutTime / sleepFor;
			final int[] returnValue = { -1 };
			// run a thread which will set a flag once it has slept for the
			// timeout
			// period
			final boolean[] flags = { true };
			new Thread() {
				@Override
				public void run()
				{
					int i = 0;
					while (flags[0] && i < count && returnValue[0] < 0) {
						try {
							testLog.log(Level.INFO, "Waiting for {0} milli seconds", sleepFor);
							Thread.sleep(sleepFor);
							i++;
						}
						catch (InterruptedException ex) {
							testLog.log(Level.INFO, "Interrupted Exception thrown {0}", ex);
						}
					}
					flags[0] = false;
				}
			}.start();
			// Invoke the shell script
			Process p = Runtime.getRuntime().exec(inpCmd);
			// execute the command and wait
			while (flags[0] && returnValue[0] < 0) {
				try {
					// Wait for the process to be closed
					returnValue[0] = p.waitFor();
					testLog.log(Level.INFO, "Output returnValue={0}", returnValue[0]);
					// process.destroy();
				}
				catch (InterruptedException ex) {
					testLog.log(Level.INFO, "Interrupted Exception thrown {0}", ex);
				}
			}
			// if the command timed out then log it
			if (returnValue[0] < 0) {
				testLog.info("The command did not complete before the timeout period expired (timeout:)");
				return null;
			}
			// Define a standard Output for command line out
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
			// define a string builder to capture stdOut to be returned as result
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = stdOut.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			result = builder.toString();
			// define a Standard Error for Error thrown
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			testLog.info("#########################################################");
			testLog.log(Level.INFO, "Standard output from runCommand:\n{0}", result);
			while ((s = stdOut.readLine()) != null) {
				testLog.info(s);
			}
			testLog.info("#########################################################");
			testLog.info("Standard error from runCommand (if any):\n");
			while ((s = stdError.readLine()) != null) {
				testLog.info(s);
			}
			stdOut.close();
			stdError.close();
		}
		catch (IOException e) {
			testLog.info("Exception while running runCommand");
			testLog.log(Level.INFO, "IO exception thrown {0}", e);
		}
		testLog.info("#########################################################");
		return result;
	}

	/*
	 * This method takes care of try-catch and sleep
	 * @param milliseconds, sleep time in milliseconds
	 * @param testLog for logging
	 */
	public static void sleep(int milliseconds, Logger testLog)
	{
		try {
			testLog.log(Level.INFO, "Sleeping for: {0} milliseconds", milliseconds);
			Thread.sleep(milliseconds);
			testLog.info("Done sleeping");
		}
		catch (InterruptedException e) {
			testLog.log(Level.INFO, "It was interrupted!\n{0}", e.getMessage());
		}
	}

	@AfterSuite
	public void afterSuite()
	{
		WebDriver webdriver = WebDriverUtils.initWebDriver(getClass().getName());
		webdriver.shutdownBrowser(true);
	}

	@BeforeSuite
	public void beforeSuite() throws IOException
	{
		Logger logger = EMTestLogger.getLogger(getClass().getName());
		testOHSWorkaround(logger);
	}

	public void testOHSWorkaround(Logger logger)
	{
		logger.info("Changing OHS's ssl.conf");
		String[] sslCommand = {
				"bash",
				"-c",
		"sed -i '/shmcb:/c\\SSLSessionCache \"none\"' /scratch/opt_ORCLemaas/InfrastructureSoftware/OHS1213/oracle_home/user_projects/domains/ohs_domain/config/fmwconfig/components/OHS/ohs1/ssl.conf" };
		SuiteLevelSetup.runCommand(sslCommand, logger);
		logger.info("Changing OHS's ssl.conf Done.......");
		logger.info("Stopping OHS");
		String[] stopCommand = {
				"bash",
				"-c",
		"echo welcome1 | /scratch/opt_ORCLemaas/InfrastructureSoftware/OHS1213/oracle_home/user_projects/domains/ohs_domain/bin/stopComponent.sh ohs1" };
		//  String [] stopCommand = {"echo", "welcome1", "|", "/scratch/opt_ORCLemaas/InfrastructureSoftware/OHS1213/oracle_home/user_projects/domains/ohs_domain/bin/stopComponent.sh", "ohs1"};
		SuiteLevelSetup.runCommand(stopCommand, logger);
		logger.info("Stopping OHS Done.......");
		logger.info("Starting OHS");
		String[] startCommand = {
				"bash",
				"-c",
		"echo welcome1 | /scratch/opt_ORCLemaas/InfrastructureSoftware/OHS1213/oracle_home/user_projects/domains/ohs_domain/bin/startComponent.sh ohs1" };
		SuiteLevelSetup.runCommand(startCommand, logger);
		logger.info("Starting OHS Done.......");
		SuiteLevelSetup.sleep(15000, logger); // 15 seconds
	}

}
