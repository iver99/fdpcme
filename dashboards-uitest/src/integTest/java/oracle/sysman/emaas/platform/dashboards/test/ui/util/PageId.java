package oracle.sysman.emaas.platform.dashboards.test.ui.util;

public class PageId
{
	/*
	 *
	 *The following variable define the all elements id of Dashboard page, including the home page, create page
	 *
	 *
	 *
	 * */
	//home page
	//public static final String DashboardDisplayPanelID = "dtabhomesc";
	public static final String DashboardDisplayPanelCss = ".dbs-summaries-container";
	public static final String DashboardCss = "div#dtabhomesc div.oj-panel";
	public static final String ErrorPageSingOutBtnCss = "button[title='Sign Out']";
	public static final String OverviewCloseID = "overviewClose";
	public static final String WidgetSearchBtnID = "dbd-left-panel-header-search-btn";
	public static final String ADashboardTestByAriaLabel = "//*[@aria-label='ADashboard Test']";
	public static final String EnterpriseOverviewByAriaLabel = "//*[@aria-label='Enterprise Overview']";
	public static final String SortbyID = "oj-select-choice-mainContentsortcb";

	//OOB dashboards ID
	public static final String Application_Performance_Monitoring_ID = "//div[@aria-dashboard='14']";
	//public static final String Database_Health_Summary_ID = "//div[@aria-dashboard='11']";
	//public static final String Host_Health_Summary_ID = "//div[@aria-dashboard='13']";
	public static final String Database_Performance_Analytics_ID = "//div[@aria-dashboard='2']";
	public static final String Middleware_Performance_Analytics_ID = "//div[@aria-dashboard='4']";
	public static final String Database_Resource_Analytics_ID = "//div[@aria-dashboard='3']";
	public static final String Middleware_Resource_Analytics_ID = "//div[@aria-dashboard='18']";
	//public static final String WebLogic_Health_Summary_ID = "//div[@aria-dashboard='12']";
	public static final String Database_Configuration_and_Storage_By_Version_ID = "//div[@aria-dashboard='6']";
	public static final String Enterprise_OverView_ID = "//div[@aria-dashboard='1']";
	public static final String Host_Inventory_By_Platform_ID = "//div[@aria-dashboard='5']";
	public static final String Top_25_Databases_by_Resource_Consumption_ID = "//div[@aria-dashboard='8']";
	public static final String Top_25_WebLogic_Servers_by_Heap_Usage_ID = "//div[@aria-dashboard='9']";
	public static final String Top_25_WebLogic_Servers_by_Load_ID = "//div[@aria-dashboard='10']";
	public static final String WebLogic_Servers_by_JDK_Version_ID = "//div[@aria-dashboard='7']";
	public static final String Database_Operations_ID = "//div[@aria-dashboard='15']";
	public static final String Host_Operations_ID = "//div[@aria-dashboard='16']";
	public static final String Middleware_Operations_ID = "//div[@aria-dashboard='17']";

	//check box
	public static final String ITA_BoxID = "itaopt";

	//help id and about id
	public static final String MenuBtnID = "menubutton";
	public static final String SignOutID = "emcpdf_oba_logout";

	//Branding bar
	public static final String CompassIcon = "//*[@id='linksButton']";
	public static final String DashboardLink = "//*[@id='obbNavDsbHome']";

	//Branding bar link text
	public static final String BrandingBarLinkText_CS_ITA = "IT Analytics";
	public static final String BrandingBarLinkText_CS_APM = "APM";
	public static final String BrandingBarLinkText_CS_LA = "Log Analytics";
	public static final String BrandingBarLinkText_VA_ITA = "Analyze";
	public static final String BrandingBarLinkText_VA_TA = "Search";
	public static final String BrandingBarLinkText_VA_LA = "Log";
	public static final String BrandingBarLinkText_ADMIN_AGENT = "Agents";
	public static final String BrandingBarLinkText_ADMIN_ITA = "IT Analytics Administration";

	//builder page
	public static final String DashboardSetAddDashboardIcon_Css = ".dbd-icon-add";
	public static final String DateTimePick_Css = "[id^='dateTimePicker_']";
	public static final String TargetSelector_Css = ".df-targte-selector.df-targte-selector";
	public static final String DashboardSetOptions_Css = "#tabs-caret";
	public static final String DashboardSetOptionsEdit_CSS = "#dbs-edit [dashboardset-option='Edit']";
	public static final String RightDrawerEditSingleDBShare_CSS = ".dbd-right-panel-editdashboard-set-share .dbd-right-panel-editdashboard-innertitle.oj-collapsible-header";
	public static final String DashboardSetShare_Css = "#dashboardSetSharingShared";
	public static final String DashboardSetNotShare_Css = "#dashboardSetSharingNotShared";
	public static final String BuilderOptionsMenu_Css = ".df-toolbar .dashboardOptsBtn[title='Options']";
}
