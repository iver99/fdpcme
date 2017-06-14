package oracle.sysman.emaas.platform.dashboards.test.util;

public class PageId
{
	private PageId() {
	  }

	/*
	 *
	 *The following variable define the all elements id of Dashboard page, including the home page, create page
	 *
	 *
	 *
	 * */
	//home page
	public static final String DASHBOARDDISPLAYPANELID = "dtabhomesc";
	public static final String DASHBOARDCSS = "div#dtabhomesc div.oj-panel";
	public static final String ERRORPAGESINGOUTBTNCSS = "button[title='Sign Out']";
	public static final String OVERVIEWCLOSEID = "overviewClose";
	public static final String WIDGETSEARCHBTNID = "dbd-left-panel-header-search-btn";

	//OOB dashboards ID
	public static final String APPLICATION_PERFORMANCE_MONITORING_ID = "//div[@aria-dashboard='14']";
	public static final String DATABASE_HEALTH_SUMMARY_ID = "//div[@aria-dashboard='11']";
	public static final String HOST_HEALTH_SUMMARY_ID = "//div[@aria-dashboard='13']";
	public static final String DATABASE_PERFORMANCE_ANALYTICS_ID = "//div[@aria-dashboard='2']";
	public static final String MIDDLEWARE_PERFORMANCE_ANALYTICS_ID = "//div[@aria-dashboard='4']";
	public static final String DATABASE_RESOURCE_ANALYTICS_ID = "//div[@aria-dashboard='3']";
	public static final String MIDDLEWARE_RESOURCE_ANALYTICS_ID = "//div[@aria-dashboard='18']";
	public static final String WEBLOGIC_HEALTH_SUMMARY_ID = "//div[@aria-dashboard='12']";
	public static final String DATABASE_CONFIGURATION_AND_STORAGE_BY_VERSION_ID = "//div[@aria-dashboard='6']";
	public static final String ENTERPRISE_OVERVIEW_ID = "//div[@aria-dashboard='1']";
	public static final String HOST_INVENTORY_BY_PLATFORM_ID = "//div[@aria-dashboard='5']";
	public static final String TOP_25_DATABASES_BY_RESOURCE_CONSUMPTION_ID = "//div[@aria-dashboard='8']";
	public static final String TOP_25_WEBLOGIC_SERVERS_BY_HEAP_USAGE_ID = "//div[@aria-dashboard='9']";
	public static final String TOP_25_WEBLOGIC_SERVERS_BY_LOAD_ID = "//div[@aria-dashboard='10']";
	public static final String WEBLOGIC_SERVERS_BY_JDK_VERSION_ID = "//div[@aria-dashboard='7']";
	public static final String DATABASE_OPERATIONS_ID = "//div[@aria-dashboard='15']";
	public static final String HOST_OPERATIONS_ID = "//div[@aria-dashboard='16']";
	public static final String MIDDLEWARE_OPERATIONS_ID = "//div[@aria-dashboard='17']";

	//dashboard page button
	public static final String DASHBOARDEDITBUTTON = "//button[@title='Edit Settings']";
	public static final String DASHBOARDNAME = ".//*[@id='dbd-edit-settings-container']/span[2]";
	public static final String DASHBOARDNAME_CSS = ".edit-setting-link.edit-dsb-link";
	public static final String DASHBOARDFILTERS = "//span[contains(text(),'Dashboard Filters')]";
	public static final String DASHBOARDENTITIES = "//label[text()='Entities']";
	
	public static final String ENABLEGCTIMERANGE = "//*[@id='enableGCTimeRange']";
	public static final String ENABLETIMERANGE = "//*[@id='enableTimeRange']";
	public static final String ENABLEENTITYFILTER = "//*[@id='enableEntityFilter']";
	public static final String ENABLEGCENTITYFILTER = "//*[@id='enableGCEntities']";
	public static final String DISABLEENTITYFILTER = "//*[@id='disableEntityFilter']";
	public static final String ENTITYBUTTON = "//*[@id='emcta_tgtSel0_dropDown']";
	public static final String SELECTBUTTON = "//*[@id='emcta_tgtSel0_ok']";
	public static final String CANCELBUTTON = "//*[@id='emcta_tgtSel0_cancel']";
	
	//check box
	public static final String ITA_BOXID = "itaopt";

	//help id and about id
	public static final String MENUBTNID = "menubutton";
	public static final String SIGNOUTID = "emcpdf_oba_logout";

	//Branding bar link text
	public static final String BRANDINGBARLINKTEXT_CS_ITA = "IT Analytics";
	public static final String BRANDINGBARLINKTEXT_CS_APM = "APM";
	public static final String BrandingBarLinkText_CS_LA = "Log Analytics";
	public static final String BRANDINGBARLINKTEXT_VA_ITA = "Analyze";
	public static final String BRANDINGBARLINKTEXT_VA_TA = "Search";
	public static final String BRANDINGBARLINKTEXT_VA_LA = "Log";
	public static final String BRANDINGBARLINKTEXT_ADMIN_AGENT = "Agents";
	public static final String BRANDINGBARLINKTEXT_ADMIN_ITA = "IT Analytics Administration";
	//=======
	//	public static final String HelpContentID = "get_started";//task
	//	public static final String AboutContentID = "/html/body/div[1]/div[2]/div/div[2]/div[2]/div[2]/p[2]";//"/html/body/div[1]/div[2]/div/div[4]/div[2]/div[2]/p[2]";
	//	public static final String AboutCloseID = "okButton";
	//
	//	//edit dashboard
	//	public static final String TimeRangeID = "/html/body/div[2]/div[2]/div/div/div[1]/div[1]/div/div/button";
	//	public static final String NameEditID = "builder-dbd-name-editor-btn";
	//	public static final String DescEditID = "builder-dbd-description-editor-btn";
	//	public static final String NameInputID = "builder-dbd-name-input";
	//	public static final String DescInputID = "builder-dbd-description-input";
	//	public static final String NameEditOKID = "builder-dbd-name-ok";
	//	public static final String DescEditOKID = "builder-dbd-description-ok";
	//
	//	//refresh //*[@id='ojChoiceId_autoRefreshSelect_selected
	//	public static final String AutoRefreshID = "//*[@id='ojChoiceId_autoRefreshSelect_selected']";
	//	public static final String AutoRefreshBy_15_Secs_ID = "/html/body/div[*]/div/div/ul/li[2]/div";//"/html/body/div[1]/div/div/ul/li[2]/div";//oj-listbox-result-label-2";
	//	public static final String AutoRefreshBy_30_Secs_ID = "/html/body/div[*]/div/div/ul/li[3]/div";//"/html/body/div[1]/div/div/ul/li[3]/div";//oj-listbox-result-label-3";
	//	public static final String AutoRefreshBy_1_Min_ID = "/html/body/div[*]/div/div/ul/li[4]/div";//"/html/body/div[1]/div/div/ul/li[4]/div";//oj-listbox-result-label-4";
	//	public static final String AutoRefreshBy_15_Mins_ID = "/html/body/div[*]/div/div/ul/li[5]/div";//"/html/body/div[1]/div/div/ul/li[5]/div";//oj-listbox-result-label-5";
	//
	//	//tile operation
	//	public static final String TileTitle = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[*]/h2";//"/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[*]/h2";
	//	public static final String ConfigTileID = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[1]/div/div[2]/button";// "/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[1]/div/div[2]/button";
	//	public static final String OpenTileID = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[1]/div/div[1]/button";
	//	public static final String HideTileID = "/html/body/div[1]/div/ul/li[1]/a/span[2]";
	//	public static final String WiderTileID = "/html/body/div[1]/div/ul/li[3]/a/span[2]";
	//	public static final String NarrowerTileID = "/html/body/div[1]/div/ul/li[4]/a/span[2]";
	//	public static final String TallerTileID = "/html/body/div[1]/div/ul/li[5]/a/span[2]";
	//	public static final String ShorterTileID = "/html/body/div[1]/div/ul/li[6]/a/span[2]";
	//	public static final String MaximizeTileID = "/html/body/div[1]/div/ul/li[7]/a/span[2]";
	//	public static final String RestoreTileID = "/html/body/div[1]/div/ul/li[8]/a/span[2]";
	//	public static final String RemoveTileID = "/html/body/div[1]/div/ul/li[10]/a/span[2]";
	//
	//	//time picker
	//	public static final String TimePickerID = "/html/body/div[*]/div[2]/div[1]/div[1]/div/div[2]/div/span/span";//"/html/body/div[*]/div[2]/div[2]/div[2]/div[1]/div[2]/div/button";//"/html/body/div[3]/div[2]/div[2]/div[2]/div[1]/div[2]/div/button";///html/body/div[*]/div[2]/div/div/div[1]/div[1]/div/div/button";//"
	//	public static final String CustomDateTimeID = "/html/body/div[1]/div/div/div[1]/div/div[1]/div/a[11]";
	//	public static final String ApplyBtnID = "applyButton";
	//	public static final String CancelBtnID = "cancelButton";
	//	public static final String DateID1 = "/html/body/div[1]/div/div[2]/div/div/div/div[2]/div/div/div/div[1]/table/tbody/tr[4]/td[2]/a";//"/html/body/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[1]/table/tbody/tr[4]/td[2]/a";
	//	public static final String DateID2 = "/html/body/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[2]/table/tbody/tr[4]/td[3]/a";
	//
	//	//check external link
	//	public static final String ExternalLink = "/html/body/div[*]/header/div/div[1]/div[1]/div[1]/div[3]/span";
	//	public static final String ExternalTargetLinkID = "/html/body/div[4]/div[3]/div/div/div[7]/div/div[2]/div[2]/div[1]/div/div[1]/span";
	//
	//	//grid view and list view id
	//	public static final String GridViewID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[1]/label";
	//	public static final String ListViewID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[2]/label";
	//
	//	public static final String DashBoardListViewDashBoardID = "/html/body/div[2]/div/div[1]/div/div/div[2]/div[2]/table/tbody/tr/td[2]/a";
	//	public static final String DashBoardInfoID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[2]/table/tbody/tr/td[5]/button";
	//	public static final String DashBoardDeleteID = "/html/body/div[1]/div/div/div[1]/div/div/div/button";
	//	public static final String LV_DeleteBtnID_Dialog = "/html/body/div[1]/div[2]/div/div[3]/button[1]";//"/html/body/div[1]/div[2]/div/div[5]/button[1]";
	//
	//	public static final String WelcomeID = "/html/body/div[2]/div/div[1]";//div[@class='welcome-slogan']";//"";
	//
	//	//welcome page verify
	//	public static final String Welcome_APMLinkID = "/html/body/div[2]/div/div[2]/ul/li[1]/a/div/div[2]/div[1]";
	//	public static final String Welcome_LALinkID = "/html/body/div[2]/div/div[2]/ul/li[2]/a/div/div[2]/div[1]";
	//	public static final String Welcome_ITALinkID = "/html/body/div[2]/div/div[2]/ul/li[3]/div/div/div[2]/div[1]";
	//	public static final String Welcome_DashboardsLinkID = "/html/body/div[2]/div/div[2]/ul/li[4]/a/div/div[2]/div[1]";
	//	public static final String Welcome_DataExp = "/html/body/div[2]/div/div[2]/ul/li[5]/div/div/div[2]/div[1]";
	//	public static final String Welcome_ITA_SelectID = "ojChoiceId_ITA_options_selected";
	//	public static final String Welcome_DataExp_SelectID = "ojChoiceId_autogen1_selected";
	//	public static final String Welcome_LearnMore_getStarted = "//a[contains(text(),'How to get started')]";
	//	public static final String Welcome_LearnMore_Videos = "//a[contains(text(),'Videos')]";
	//	public static final String Welcome_LearnMore_ServiceOffering = "//a[contains(text(),'Service Offerings')]";
	//	public static final String Welcome_ITA_PADatabase = "/html/body/div[1]/div/div/ul/li[2]/div";//"oj-listbox-result-label-8";//ITA Select Item : Performance Analytics - Database
	//	public static final String Welcome_ITA_PAMiddleware = "/html/body/div[1]/div/div/ul/li[3]/div";//"oj-listbox-result-label-9";//ITA Select Item : Performance Analytics - Middleware
	//	public static final String Welcome_ITA_RADatabase = "/html/body/div[1]/div/div/ul/li[4]/div";//"oj-listbox-result-label-10";//ITA Select Item : Resource Analytics - Database
	//	public static final String Welcome_ITA_RAMiddleware = "/html/body/div[1]/div/div/ul/li[5]/div";//"oj-listbox-result-label-11";//ITA Select Item : Resource Analytics - Middleware
	//	public static final String Welcome_ITA_DEAnalyze = "/html/body/div[1]/div/div/ul/li[6]/div";//"oj-listbox-result-label-12";//ITA Select Item : Data Explorer - Analyze
	//	public static final String Welcome_ITA_DE = "/html/body/div[1]/div/div/ul/li[7]/div";//"oj-listbox-result-label-13";//ITA Select Item : Data Explorer
	//	public static final String Welcome_DataExp_Log = "/html/body/div[1]/div/div/ul/li[2]/div";//"oj-listbox-result-label-4";//Data Explorers Select Item : Log
	//	public static final String Welcome_DataExp_Analyze = "/html/body/div[1]/div/div/ul/li[3]/div";//"oj-listbox-result-label-5";//Data Explorers Select Item : Analyze
	//	public static final String Welcome_DataExp_Search = "/html/body/div[1]/div/div/ul/li[4]/div";//"oj-listbox-result-label-6";//Data Explorers Select Item : Search
	//
	//	//Sharing and stoping dashbaord
	//	public static final String option = "dashboardOptsBtn";
	//	public static final String dashboardshare = "emcpdf_dsbopts_share";//"//*[@id='ui-id-5']/span[2]";
	//	public static final String stopshare_btn = "emcpdf_dsbopts_share";//"//*[@id='ui-id-5']/span[2]";
	//>>>>>>> 160c9f7961de810ed40fc62d0e8080a544e5730e
}
