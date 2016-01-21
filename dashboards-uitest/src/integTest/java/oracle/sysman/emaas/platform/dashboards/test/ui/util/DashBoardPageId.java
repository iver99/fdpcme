package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.qatool.uifwk.webdriver.*;

public class DashBoardPageId {
	/*
	 * 
	 *The following variable define the all elements id of Dashboard page, including the home page, create page
	 *
	 *
	 *
	 * */
	public static final int Delaytime_long = 8000;
	public static final int Delaytime_short = 500;
	public static final String CreateDSButtonID="cbtn";
	public static final String CreateDSOfDialogID="createDsb";
	public static final String DashBoardNameBoxID = "dbsHNameIn";
	public static final String DashBoardDescBoxID = "dbsHDpIn";
	public static final String DashOKButtonID = "createDsb";
	public static final String DashSaveButtonID = "createDsb-2";
	//public static final String WidgetAddButtonID = "add-widget-button";
	public static final String OptionsID = "dashboardOptsBtn";
	public static final String DashBoardSaveID = "dashboard-screenshot";
	public static final String WidgetAddButton = "emcpdf_dsbopts_add";//"//div[@id='ojDialogWrapper-widgetDetailsDialog']/div[5]/button";////div[18]/div[5]/button/span[text()='Add']";
	public static final String DashboardEdit="//*[@id='emcpdf_dsbopts_edit']";
	public static final String DashboardDuplicate="//*[@id='emcpdf_dsbopts_duplicate']";
	public static final String DashboardDelete="//*[@id='emcpdf_dsbopts_delete']";
	public static final String WidgetDialogCloseButtonID = "div[15]/div[3]/div/span";
	public static final String DashBoardID = "//div[@class='oj-panel dbs-summary-container']";// and @aria-dashboard='1127']";
	public static final String ElementID = "//*[@id=\"dtabhomesc\"]/div[1]";
	public static final String DeleteBtnID = "//*[@id=\"dtabhomesc\"]/div[1]/div[1]/div[2]/button";//div[@id='dtabhomesc']/div/div/div[2]/button";///html/body/div[2]/div/div/div/div/div[3]/div/div/div[2]/button";//"//button[@class='oj-button-half-chrome oj-sm-float-end oj-button oj-component oj-enabled oj-button-icon-only oj-component-initnode oj-default']/span[@class='oj-button-icon oj-start icon-delete-ena-16 oj-fwk-icon']";
	public static final String DeleteBtnID_Dialog = "//div[@id='ojDialogWrapper-dbs_cfmDialog']/div[5]/button";
	public static final String OverviewCloseID = "overviewClose";
	public static final String DashBoardName = "//span[@id='builder-dbd-name-display-hover-area']";
	public static final String AddBtn = "widget-selector-okbtn";//
	public static final String closeBtnID = "/html/body/div[1]/div[2]/div/div[3]/div/span";//div[contains(@id,'ojDialogWrapper-ui-id') and @class='oj-dialog oj-component oj-draggable']/div[3]/div/span"; // ";";
	public static final String LinkID = "linksButton";
	public static final String DashBoardLinkID = "link=All Dashboards";
	public static final String DashboardTableID = "dtabhomesc";
	
	//OOB dashboards ID
	public static final String Application_Performance_Monitoring_ID = "//div[@aria-dashboard='14']";
	public static final String Database_Health_Summary_ID = "//div[@aria-dashboard='11']";
	public static final String Host_Health_Summary_ID = "//div[@aria-dashboard='13']";
	public static final String Database_Performance_Analytics_ID = "//div[@aria-dashboard='2']";	
	public static final String Middleware_Performance_Analytics_ID = "//div[@aria-dashboard='4']";
	//public static final String Middleware_Datasource_Performance_Analytics_ID = "//div[@aria-dashboard='19']";
	public static final String Database_Resource_Analytics_ID = "//div[@aria-dashboard='3']";
	public static final String Middleware_Resource_Analytics_ID = "//div[@aria-dashboard='18']";
	public static final String WebLogic_Health_Summary_ID = "//div[@aria-dashboard='12']";	
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
		
	public static final String SortDropListID = "//*[@id='ojChoiceId_sortcb_selected']";//ojChoiceId_sortcb";//ojChoiceId_sortcb_selected";//sortcb";
	public static final String Access_Date_ID = "/html/body/div/div/div/ul/li[2]/div";
	
	
	//search
	public static final String SearchDSBoxID = "sinput";
	
	//Branding Bar links
	//Home Link
	public static final String HomeLinkID = "/html/body/div[*]/header/div/div[1]/div[3]/div/div/div[1]/div[2]/div[1]/a";
	//dashboard home link
	public static final String DashBoardHomeLinkID = "/html/body/div[*]/header/div/div[1]/div[3]/div/div/div[1]/div[2]/div[2]/a";
	//My Favorites link
	public static final String MyFavoritesLinkID = "/html/body/div[*]/header/div/div[1]/div[3]/div/div/div[1]/div[2]/div[3]/a";
	//IT Analytics link
	public static final String ITALinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[2]/div[2]/div[2]/a";
	//Log Analytics link
	public static final String LALinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[2]/div[2]/div[3]/a";
	//APM link
	public static final String APMLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[2]/div[2]/div[4]/a";
	//Log link
	public static final String LOGLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[2]/a";
	//AWR Analytics link
	//public static final String AWRALinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[3]/a";
	//Analyze link
	public static final String AnalyzeLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[3]/a";
	//Search link
	public static final String SearchLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[4]/a";
	//Agents link
	public static final String AgentsLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[4]/div[2]/div[2]/a";
	//IT Analytics Administration link
	public static final String ITA_Admin_LinkID ="/html/body/div[*]/header/div/div/div[3]/div/div/div[4]/div[2]/div[3]/a";
	
	//check box
	public static final String ITA_BoxID = "itaopt";
	public static final String LA_BoxID = "laopt";
	public static final String APM_BoxID = "apmopt";
	public static final String Oracle_BoxID = "oracleopt";
	public static final String Other_BoxID = "otheropt";
	public static final String Share_BoxID = "shareopt";
	
	//id of dashboard name /html/body/div[2]/div/div/div/div/div/span/h1 /html/body/div[2]/div/div/div/div[2]/div/span/span,/html/body/div[3]/div/div/div/div/div/span/h1;/html/body/div[2]/div/div/div/div[2]/div/span/span
	public static final String DashboardNameID = "/html/body/div[*]/div/div/div/div/div/span/h1";
	public static final String MDashboardNameID = "/html/body/div[3]/div/div/div/div/div/span/h1";
	//id of dashboard desc
	public static final String DashboardDescID = "/html/body/div[*]/div/div/div/div[2]/div/span/span";
	public static final String MDashboardDescID = "/html/body/div[3]/div/div/div/div[2]/div/span/span";
	
	//remove dashboard/html/body/div[3]/div/div/div/div/div[2]/div[3]/div/div[2]/div[2]/button
	public static final String InfoBtnID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[2]/div/div[2]/div[2]/button";//"/html/body/div[*]/div/div[1]/div/div/div[2]/div[2]/div/div[2]/div[2]/button";///html/body/div[*]/div/div/div/div/div[2]/div[3]/div/div[2]/div[2]/button";
	public static final String RmBtnID = "/html/body/div[1]/div/div/div[1]/div/div/div[2]/button";//"/html/body/div/div/div/div/div/div/div[2]/button";
	
	//Explore Data
	public static final String ExploreDataBtnID = "exploreDataBtn";
	public static final String ExploreDataMenu_Log_ID = "ui-id-3";
	public static final String ExploreDataMenu_Analyze_ID = "ui-id-4";
	public static final String ExploreDataMenu_Search_ID = "ui-id-5";
	
	//help id and about id
	public static final String MenuBtnID = "menubutton";
	public static final String HelpID = "emcpdf_oba_help";
	public static final String AboutID = "emcpdf_oba_about";
	public static final String SignOutID = "emcpdf_oba_logout";
	public static final String HelpContentID = "get_started";//task
	public static final String AboutContentID = "/html/body/div[1]/div[2]/div/div[4]/div[2]/div[2]/p[2]";
	public static final String AboutCloseID = "okButton";
	
	//edit dashboard
	public static final String TimeRangeID = "/html/body/div[2]/div[2]/div/div/div[1]/div[1]/div/div/button";
	public static final String NameEditID = "builder-dbd-name-editor-btn";
	public static final String DescEditID = "builder-dbd-description-editor-btn";
	public static final String NameInputID = "builder-dbd-name-input";
	public static final String DescInputID = "builder-dbd-description-input";
	public static final String NameEditOKID = "builder-dbd-name-ok";
	public static final String DescEditOKID = "builder-dbd-description-ok";
	
	//refresh //*[@id='ojChoiceId_autoRefreshSelect_selected
	public static final String AutoRefreshID = "//*[@id='ojChoiceId_autoRefreshSelect_selected']";
	public static final String AutoRefreshBy_15_Secs_ID = "/html/body/div[*]/div/div/ul/li[2]/div";//"/html/body/div[1]/div/div/ul/li[2]/div";//oj-listbox-result-label-2";	
	public static final String AutoRefreshBy_30_Secs_ID = "/html/body/div[*]/div/div/ul/li[3]/div";//"/html/body/div[1]/div/div/ul/li[3]/div";//oj-listbox-result-label-3";
	public static final String AutoRefreshBy_1_Min_ID = "/html/body/div[*]/div/div/ul/li[4]/div";//"/html/body/div[1]/div/div/ul/li[4]/div";//oj-listbox-result-label-4";
	public static final String AutoRefreshBy_15_Mins_ID = "/html/body/div[*]/div/div/ul/li[5]/div";//"/html/body/div[1]/div/div/ul/li[5]/div";//oj-listbox-result-label-5";
	
	//tile operation
	public static final String TileTitle = "/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[*]/h2";
	public static final String ConfigTileID = "/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[1]/div/div[2]/button";
	public static final String OpenTileID = "/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[1]/div/div[1]/button";
	public static final String HideTileID = "/html/body/div[1]/div/ul/li[1]/a/span[2]";
	public static final String RemoveTileID = "/html/body/div[1]/div/ul/li[2]/a/span[2]";
	public static final String WiderTileID = "/html/body/div[1]/div/ul/li[4]/a/span[2]";
	public static final String NarrowerTileID = "/html/body/div[1]/div/ul/li[5]/a/span[2]";
	public static final String TallerTileID = "/html/body/div[1]/div/ul/li[6]/a/span[2]";
	public static final String ShorterTileID = "/html/body/div[1]/div/ul/li[7]/a/span[2]";
	public static final String MaximizeTileID = "/html/body/div[1]/div/ul/li[8]/a/span[2]";
	public static final String RestoreTileID = "/html/body/div[1]/div/ul/li[9]/a/span[2]";
	
	//time picker
	public static final String TimePickerID = "/html/body/div[*]/div[2]/div[2]/div[2]/div[1]/div[2]/div/button";//"/html/body/div[3]/div[2]/div[2]/div[2]/div[1]/div[2]/div/button";///html/body/div[*]/div[2]/div/div/div[1]/div[1]/div/div/button";//"
	public static final String CustomDateTimeID = "/html/body/div[1]/div/div/div[1]/div/div[1]/div/a[11]";
	public static final String ApplyBtnID = "applyButton";
	public static final String CancelBtnID = "cancelButton";
	public static final String DateID1 = "/html/body/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[1]/table/tbody/tr[4]/td[2]/a";
	public static final String DateID2 = "/html/body/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[2]/table/tbody/tr[4]/td[3]/a";

	
	//check external link
	public static final String ExternalLink = "/html/body/div[*]/header/div/div[1]/div[1]/div[1]/div[3]/span";
	public static final String ExternalTargetLinkID = "/html/body/div[4]/div[3]/div/div/div[7]/div/div[2]/div[2]/div[1]/div/div[1]/span";
	
	//grid view and list view id             /html/body/div[2]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[2]/label/span[1]
	public static final String GridViewID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[1]/label";
	public static final String ListViewID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[2]/label";
	
	public static final String DashBoardListViewDashBoardID = "/html/body/div[2]/div/div[1]/div/div/div[2]/div[2]/table/tbody/tr/td[2]/a";
	public static final String DashBoardInfoID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[2]/table/tbody/tr/td[5]/button";
	public static final String DashBoardDeleteID = "/html/body/div[1]/div/div/div[1]/div/div/div/button";
	public static final String LV_DeleteBtnID_Dialog = "/html/body/div[1]/div[2]/div/div[5]/button[1]";
	
	public static final String WelcomeID = "/html/body/div[2]/div/div[1]";//div[@class='welcome-slogan']";//"";                                                                                     

	//welcome page verify
	public static final String Welcome_APMLinkID = "/html/body/div[2]/div/div[2]/ul/li[1]/a/div/div[2]/div[1]";
	public static final String Welcome_LALinkID = "/html/body/div[2]/div/div[2]/ul/li[2]/a/div/div[2]/div[1]";
	public static final String Welcome_ITALinkID ="/html/body/div[2]/div/div[2]/ul/li[3]/div/div/div[2]/div[1]";
	public static final String Welcome_DataExp = "/html/body/div[2]/div/div[2]/ul/li[5]/div/div/div[2]/div[1]";
   
       //Sharing&stooping dashbaord
        public static final String option="dashboardOptsBtn";
	public static final String dashboardshare="emcpdf_dsbopts_share";
	public static final String stopshare_btn="//a[@id='emcpdf_dsbopts_share']/span[2]";
}
