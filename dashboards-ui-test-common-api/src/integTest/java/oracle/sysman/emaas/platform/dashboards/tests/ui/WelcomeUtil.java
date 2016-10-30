package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class WelcomeUtil
{
	private WelcomeUtil() {
	  }

	/**
	 * Visit Log/Analyze/Search from Data Explorers dropdown in welcome
	 * 
	 * @param driver
	 * @param selection
	 * 				log | analyze | search
	 */
	public static void dataExplorers(WebDriver driver, String selection) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.dataExplorers(driver, selection);
	}

	/**
	 * Check if specific item in Learn More is existed in welcome.
	 * 
	 * @param driver
	 * @param itemName
	 * 		getStarted | videos | serviceOfferings
	 * @return
	 */
	public static boolean isLearnMoreItemExisted(WebDriver driver, String itemName)
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		return wu.isLearnMoreItemExisted(driver, itemName);
	}

	/**
	 * Check if specific service is existed in welcome
	 * 
	 * @param driver
	 * @param serviceName
	 * 			APM | LA | ITA | infraMonitoring | compliance | securityAnalytics | orchestration | dashboards | dataExplorers 
	 * @return
	 * @
	 */
	public static boolean isServiceExistedInWelcome(WebDriver driver, String serviceName) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		return wu.isServiceExistedInWelcome(driver, serviceName);
	}

	/**
	 * Visit "How to get started" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void learnMoreHow(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.learnMoreHow(driver);
	}

	/**
	 * Visit "Service Offerings" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void learnMoreServiceOffering(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.learnMoreServiceOffering(driver);
	}

	/**
	 * Visit "Videos" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void learnMoreVideo(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.learnMoreVideo(driver);
	}

	/**
	 * Visit "Application Performance Monitoring" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitAPM(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitAPM(driver);
	}
	
	/**
	 * Visit "Complliance Service" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitCompliance(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitCompliance(driver);
	}

	/**
	 * Visit "Dashboards" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitDashboards(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitDashboards(driver);
	}

	/**
	 * Visit "Infrustructure Monitoring" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitInfraMonitoring(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitInfraMonitoring(driver);
	}	
	
	/**
	 * Visit specific item in IT Analytics in welcome
	 * 
	 * @param driver
	 * @param selection
	 * 		default | performanceAnayticsDatabase | performanceAnalyticsMiddleware | 
	 * 		resourceAnalyticsDatabase | resourceAnalyticsMiddleware | resourceAnalyticsHost | 
	 *  	dataExplorerAnalyze | dataExplorer
	 * @
	 */
	public static void visitITA(WebDriver driver, String selection) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitITA(driver,selection);
	}

	/**
	 * Visit "Log Analytics" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitLA(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitLA(driver);
	}
	
	/**
	 * Visit "Security Monitoring and Analytics" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitSecurity(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitSecurity(driver);
	}

        /**
	 * Visit "Orchestration" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitOrchestration(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitOrchestration(driver);
	}

}
