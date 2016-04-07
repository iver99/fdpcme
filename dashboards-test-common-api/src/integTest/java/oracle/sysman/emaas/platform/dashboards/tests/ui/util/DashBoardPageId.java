package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

public class DashBoardPageId
{
	/*
	 *
	 *The following variable define the all elements id of Dashboard page, including the home page, create page
	 *
	 *
	 *
	 * */
	public static final int Delaytime_long = 8000;
	public static final int Delaytime_short = 500;
	public static final String CreateDSButtonID = "cbtn";
	public static final String CreateDSOfDialogID = "createDsb";
	public static final String DashBoardNameBoxID = "dbsHNameIn";
	public static final String DashBoardDescBoxID = "dbsHDpIn";
	public static final String DashOKButtonID = "createDsb";
	public static final String DashSaveButtonID = "createDsb-2";
	//public static final String WidgetAddButtonID = "add-widget-button";
	public static final String OptionsID = "dashboardOptsBtn";
	public static final String DashBoardSaveID = "dashboard-screenshot";
	public static final String WidgetAddButton = "emcpdf_dsbopts_add";//"//div[@id='ojDialogWrapper-widgetDetailsDialog']/div[5]/button";////div[18]/div[5]/button/span[text()='Add']";
	public static final String DashboardEdit = "//*[@id='emcpdf_dsbopts_edit']";
	public static final String DashboardDuplicate = "//*[@id='emcpdf_dsbopts_duplicate']";
	public static final String DashboardDelete = "//*[@id='emcpdf_dsbopts_delete']";
	public static final String DashboardHome = "//*[@id='emcpdf_dsbopts_home']";
	public static final String DashboardFavorite = "//*[@id='emcpdf_dsbopts_favorites']";
	public static final String WidgetDialogCloseButtonID = "div[15]/div[3]/div/span";
	public static final String DashBoardID = "//div[@class='oj-panel dbs-summary-container']";// and @aria-dashboard='1127']";
	public static final String ElementID = "//*[@id=\"dtabhomesc\"]/div[1]";
	public static final String DeleteBtnID = "//*[@id=\"dtabhomesc\"]/div[1]/div[1]/div[2]/button";//div[@id='dtabhomesc']/div/div/div[2]/button";///html/body/div[2]/div/div/div/div/div[3]/div/div/div[2]/button";//"//button[@class='oj-button-half-chrome oj-sm-float-end oj-button oj-component oj-enabled oj-button-icon-only oj-component-initnode oj-default']/span[@class='oj-button-icon oj-start icon-delete-ena-16 oj-fwk-icon']";
	public static final String DeleteBtnID_Dialog = "/html/body/div[1]/div[2]/div/div[3]/button[1]";//"//div[@id='ojDialogWrapper-dbs_cfmDialog']/div[5]/button";
	public static final String OverviewCloseID = "overviewClose";
	public static final String DashBoardName = "//span[@id='builder-dbd-name-display-hover-area']";
	public static final String AddBtn = "widget-selector-okbtn";//
	public static final String closeBtnID = "/html/body/div[1]/div[2]/div/div[1]/div/span";///html/body/div[1]/div[2]/div/div[3]/div/span";//div[contains(@id,'ojDialogWrapper-ui-id') and @class='oj-dialog oj-component oj-draggable']/div[3]/div/span"; // ";";
	public static final String LinkID = "linksButton";
	public static final String DashBoardLinkID = "link=All Dashboards";
	public static final String DashboardTableID = "dtabhomesc";
	public static final String DeleteBtn_cfmDialog = "/html/body/div[1]/div[2]/div/div[3]/button[1]";
	public static final String DashboardSerachResult_panelID = "dtabhomesc";

	public static final String WidgetSearchInputID = "widget-search-input";
	public static final String WidgetSearchBtnID = "dbd-left-panel-header-search-btn";

	//OOB dashboards ID
	//OOB dashboards ID
	public static final String Application_Performance_Monitoring_CSS = ".dbs-summary-page-image[alt='Application Performance Monitoring']";
	public static final String Database_Health_Summary_CSS = ".dbs-summary-page-image[alt='Database Health Summary']";
	public static final String Host_Health_Summary_CSS = ".dbs-summary-page-image[alt='Host Health Summary']";
	public static final String Database_Performance_Analytics_CSS = ".dbs-summary-page-image[alt='Performance Analytics: Database']";
	public static final String Middleware_Performance_Analytics_CSS = ".dbs-summary-page-image[alt='Performance Analytics: Middleware']";
	//public static final String Middleware_Datasource_Performance_Analytics_ID = "//div[@aria-dashboard='19']";
	public static final String Database_Resource_Analytics_CSS = ".dbs-summary-page-image[alt='Resource Analytics: Database']";
	public static final String Middleware_Resource_Analytics_CSS = ".dbs-summary-page-image[alt='Resource Analytics: Middleware']";
	public static final String WebLogic_Health_Summary_CSS = ".dbs-summary-page-image[alt='WebLogic Health Summary']";
	public static final String Database_Configuration_and_Storage_By_Version_CSS = ".dbs-summary-page-image[alt='Database Configuration and Storage By Version']";
	public static final String Enterprise_OverView_CSS = ".dbs-summary-page-image[alt='Enterprise Overview']";
	public static final String Host_Inventory_By_Platform_CSS = ".dbs-summary-page-image[alt='Host Inventory By Platform']";
	public static final String Top_25_Databases_by_Resource_Consumption_CSS = ".dbs-summary-page-image[alt='Top 25 Databases by Resource Consumption']";
	public static final String Top_25_WebLogic_Servers_by_Heap_Usage_CSS = ".dbs-summary-page-image[alt='Top 25 WebLogic Servers by Heap Usage']";
	public static final String Top_25_WebLogic_Servers_by_Load_CSS = ".dbs-summary-page-image[alt='Top 25 WebLogic Servers by Load']";
	public static final String WebLogic_Servers_by_JDK_Version_CSS = ".dbs-summary-page-image[alt='WebLogic Servers by JDK Version']";
	public static final String Database_Operations_CSS = ".dbs-summary-page-image[alt='Database Operations']";
	public static final String Host_Operations_CSS = ".dbs-summary-page-image[alt='Host Operations']";
	public static final String Middleware_Operations_CSS = ".dbs-summary-page-image[alt='Middleware Operations']";

	public static final String SortDropListID = "//*[@id='ojChoiceId_sortcb_selected']";//ojChoiceId_sortcb";//ojChoiceId_sortcb_selected";//sortcb";
	public static final String Access_Date_ID = "/html/body/div/div/div/ul/li[2]/div";

	public static final String BrandingBarDashboardLinkLocator = "//*[@id='links_menu']//*[contains(@data-bind, 'dashboardLinkLabel')]";
	// filter check boxes
	public static final String FilterApmLocator = "//*[@id='apmopt']";
	public static final String FilterItaLocator = "//*[@id='itaopt']";
	public static final String FilterLaLocator = "//*[@id='laopt']";
	public static final String FilterOracleLocator = "//*[@id='oracleopt']";
	public static final String FilterShareLocator = "//*[@id='shareopt']";
	public static final String FilterMeLocator = "//*[@id='otheropt']";
	public static final String FilterFavoriteLocator = "//*[@id='myfavorites']";
	// sort by
	public static final String SortBySelectLocator = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'dsb-sortcb')]//*[contains(@class, 'oj-select-chosen')]";
	public static final String SortByDefaultLocator = "/html/body/div/div/div/ul/li[1]/div";
	public static final String SortByNameASCLocator = "/html/body/div/div/div/ul/li[2]/div";
	public static final String SortByNameDSCLocator = "/html/body/div/div/div/ul/li[3]/div";
	public static final String SortByCreatedByASCLocator = "/html/body/div/div/div/ul/li[4]/div";
	public static final String SortByCreatedByDSCLocator = "/html/body/div/div/div/ul/li[5]/div";
	public static final String SortByCreateDateASCLocator = "/html/body/div/div/div/ul/li[6]/div";
	public static final String SortByCreateDateDSCLocator = "/html/body/div/div/div/ul/li[7]/div";
	public static final String SortByLastModifiedASCLocator = "/html/body/div/div/div/ul/li[8]/div";
	public static final String SortByLastModifiedDSCLocator = "/html/body/div/div/div/ul/li[9]/div";
	public static final String SortByLastAccessASCLocator = "/html/body/div/div/div/ul/li[10]/div";
	public static final String SortByLastAccessDSCLocator = "/html/body/div/div/div/ul/li[11]/div";
	//search
	//public static final String SearchDSBoxID = "sinput";

	public static final String SearchDashboardInputLocator = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'dbs-sinput')]";
	public static final String SearchDashboardSearchBtnLocator = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'dbs-search-icon')]";

	//Dahobrd home Views
	public static final String DashboardsGridViewLocator = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'icon-gridview-16')]";
	public static final String DashboardsListViewLocator = "//div[not(contains(@style,'display:none'))]//*[contains(@class, 'icon-listview-16')]";

	//Dashboard
	public static final String DashboardLocator = "//*[contains(@class, 'dbs-dsbnameele') and text() = '_name_']";//"//[contains(@class, 'dbs-dsbnameele')]//text()[. = '_name_']";//*[contains(@class, 'icon-listview-16')]";
	public static final String OOBDashboardLocator = "//*[contains(@class, 'dbs-dsbsystem') and text() = '_name_']";

	//Branding Bar links
	//Home Link
	//public static final String HomeLinkID = "/html/body/div[*]/header/div/div[1]/div[3]/div/div/div[1]/div[2]/div[1]/a";
	public static final String HomeLinkID = "//a[contains(text(),'Home')]";

	//dashboard home link
	//public static final String DashBoardHomeLinkID = "/html/body/div[*]/header/div/div[1]/div[3]/div/div/div[1]/div[2]/div[2]/a";
	public static final String DashBoardHomeLinkID = "//a[contains(text(),'Dashboards')]";

	//My Favorites link
	//public static final String MyFavoritesLinkID = "/html/body/div[*]/header/div/div[1]/div[3]/div/div/div[1]/div[2]/div[3]/a";
	public static final String MyFavoritesLinkID = "//a[contains(text(),'My Favorites')]";
	//IT Analytics link
	//public static final String ITALinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[2]/div[2]/div[2]/a";
	public static final String ITALinkID = "//a[contains(text(),'IT Analytics')]";
	//Log Analytics link
	//public static final String LALinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[2]/div[2]/div[3]/a";
	public static final String LALinkID = "//a[contains(text(),'Log Analytics')]";
	//APM link
	//public static final String APMLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[2]/div[2]/div[4]/a";
	public static final String APMLinkID = "//a[contains(text(),'APM')]";
	//Log link
	//public static final String LOGLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[2]/a";
	public static final String LOGLinkID = "//a[contains(text(),'Log')]";

	//AWR Analytics link
	//public static final String AWRALinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[3]/a";
	//Analyze link
	//public static final String AnalyzeLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[3]/a";
	public static final String AnalyzeLinkID = "//a[contains(text(),'Analyze')]";
	//Search link
	//public static final String SearchLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[3]/div[2]/div[4]/a";
	public static final String SearchLinkID = "//a[contains(text(),'Search')]";
	//Agents link
	//public static final String AgentsLinkID = "/html/body/div[*]/header/div/div/div[3]/div/div/div[4]/div[2]/div[3]/a";
	public static final String AgentsLinkID = "//a[contains(text(),'Agents')]";
	//IT Analytics Administration link
	//public static final String ITA_Admin_LinkID ="/html/body/div[*]/header/div/div/div[3]/div/div/div[4]/div[2]/div[4]/a";
	public static final String ITA_Admin_LinkID = "//a[contains(text(),'IT Analytics Administration')]";

	//check box
	public static final String ITA_BoxID = "itaopt";
	public static final String LA_BoxID = "laopt";
	public static final String APM_BoxID = "apmopt";
	public static final String Oracle_BoxID = "oracleopt";
	public static final String Other_BoxID = "otheropt";
	public static final String Share_BoxID = "shareopt";
	public static final String Favorite_BoxID = "myfavorites";

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
	public static final String ExploreDataMenu_Log = "/html/body/div[1]/div/ul/li[1]/a";//"ui-id-3";
	public static final String ExploreDataMenu_Analyze = "/html/body/div[1]/div/ul/li[2]/a";//"ui-id-4";
	public static final String ExploreDataMenu_Search = "/html/body/div[1]/div/ul/li[3]/a";//"ui-id-5";

	//help id and about id
	public static final String MenuBtnID = "menubutton";
	public static final String HelpID = "emcpdf_oba_help";
	public static final String AboutID = "emcpdf_oba_about";
	public static final String SignOutID = "emcpdf_oba_logout";
	public static final String HelpContentID = "get_started";//task
	public static final String AboutContentID = "/html/body/div[1]/div[2]/div/div[2]/div[2]/div[2]/p[2]";//"/html/body/div[1]/div[2]/div/div[4]/div[2]/div[2]/p[2]";
	public static final String AboutCloseID = "okButton";

	//edit dashboard
	public static final String TimeRangeID = "/html/body/div[2]/div[2]/div/div/div[1]/div[1]/div/div/button";
	public static final String NameEditID = "builder-dbd-name-editor-btn";
	public static final String DescEditID = "builder-dbd-description-editor-btn";
	public static final String NameInputID = "builder-dbd-name-input";
	public static final String DescInputID = "builder-dbd-description-input";
	public static final String NameEditOKID = "builder-dbd-name-ok";
	public static final String DescEditOKID = "builder-dbd-description-ok";

	//dashboard builder tile edit area
	public static final String BuilderTilesEditArea = "//div[contains(@class, 'tiles-wrapper')]";
	public static final String BuilderTileTitleLocator = "//h2[contains(@class, 'dbd-tile-title') and @data-tile-title='%s']";
	public static final String BuilderTileConfigLocator = "following-sibling::*//button[contains(@class, 'dbd-tile-action')]";
	public static final String BuilderTileDataExploreLocator = "following-sibling::*//button[contains(@class, 'dbd-data-explore')]";
	public static final String BuilderTileShowLocator = "//ul[not(contains(@style,'display:none'))]/li[@data-option='showhide-title']/a[@data-show-hide-title='show']";
	public static final String BuilderTileHideLocator = "//ul[not(contains(@style,'display:none'))]/li[@data-option='showhide-title']/a[@data-show-hide-title='hide']";

	//dashboard builder options
	public static final String BuilderOptionsMenuLocator = "//button[contains(@class, 'dashboardOptsBtn')]";
	public static final String BuilderOptionsAutoRefreshLocator = "id=emcpdf_dsbopts_refresh";
	public static final String BuilderOptionsAutoRefreshOffLocator = "//li[@data-singledb-option='Off']/a";
	public static final String BuilderOptionsAutoRefreshOn5MinLocator = "//li[@data-singledb-option=\"On (Every 5 Minutes)\"]/a";
	public static final String BuilderOptionsDeleteLocator = "id=emcpdf_dsbopts_delete";
	public static final String BuilderOptionsDeleteMenuLocator = "//a[@id='emcpdf_dsbopts_delete']";

	//dashboard delete dialog
	public static final String BuilderDeleteDialogLocator = "id=ojDialogWrapper-dbs_cfmDialog";
	public static final String BuilderDeleteDialogDeleteBtnLocator = "//button[@data-delete-dialog='Delete']";

	// auto refresh menu items
	public static final String BuilderAutoRefreshOffSelectedLocator = "//li[@data-singledb-option='Off']/a/span[contains(@class, 'fa-check')]";
	public static final String BuilderAutoRefreshOn5MinSelectedLocator = "//li[@data-singledb-option='On (Every 5 Minutes)']/a/span[contains(@class, 'fa-check')]";

	// for R1.6, following ids are not used any more
	//refresh //*[@id='ojChoiceId_autoRefreshSelect_selected
	public static final String AutoRefreshID = "//*[@id='ojChoiceId_autoRefreshSelect_selected']";
	public static final String AutoRefreshBy_15_Secs_ID = "/html/body/div[*]/div/div/ul/li[2]/div";//"/html/body/div[1]/div/div/ul/li[2]/div";//oj-listbox-result-label-2";
	public static final String AutoRefreshBy_30_Secs_ID = "/html/body/div[*]/div/div/ul/li[3]/div";//"/html/body/div[1]/div/div/ul/li[3]/div";//oj-listbox-result-label-3";
	public static final String AutoRefreshBy_1_Min_ID = "/html/body/div[*]/div/div/ul/li[4]/div";//"/html/body/div[1]/div/div/ul/li[4]/div";//oj-listbox-result-label-4";
	public static final String AutoRefreshBy_15_Mins_ID = "/html/body/div[*]/div/div/ul/li[5]/div";//"/html/body/div[1]/div/div/ul/li[5]/div";//oj-listbox-result-label-5";

	//tile operation
	public static final String TileTitle = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[*]/h2";//"/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[*]/h2";
	public static final String ConfigTileID = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[1]/div/div[2]/button";// "/html/body/div[*]/div[2]/div[2]/div[*]/div/div[2]/div[*]/div/div[1]/div/div[2]/button";
	public static final String OpenTileID = "/html/body/div[*]/div[2]/div[1]/div[*]/div/div[2]/div/div/div[1]/div/div[1]/button";
	public static final String HideTileID = "/html/body/div[1]/div/ul/li[1]/a/span[2]";
	public static final String WiderTileID = "/html/body/div[1]/div/ul/li[3]/a/span[2]";
	public static final String NarrowerTileID = "/html/body/div[1]/div/ul/li[4]/a/span[2]";
	public static final String TallerTileID = "/html/body/div[1]/div/ul/li[5]/a/span[2]";
	public static final String ShorterTileID = "/html/body/div[1]/div/ul/li[6]/a/span[2]";
	public static final String MaximizeTileID = "/html/body/div[1]/div/ul/li[7]/a/span[2]";
	public static final String RestoreTileID = "/html/body/div[1]/div/ul/li[8]/a/span[2]";
	public static final String RemoveTileID = "/html/body/div[1]/div/ul/li[10]/a/span[2]";

	//tile css locators -> css=<locator>
    public static final String WidgetTitleCSS = ".dbd-widget";
    public static final String TileTitleCSS = "h2.dbd-tile-title";
    public static final String ConfigTileCSS = "button[id^=actionButton]";
    public static final String WiderTileCSS = "li[data-option=wider]";
    public static final String NarrowerTileCSS = "li[data-option=narrower]";
    public static final String ShorterTileCSS = "li[data-option=shorter]";
    public static final String TallerTileCSS = "li[data-option=taller]";
    public static final String RemoveTileCSS = "li[data-option=remove]";

	//time picker
	public static final String TimePickerID = "/html/body/div[*]/div[2]/div[1]/div[1]/div/div[2]/div/span/span";//"/html/body/div[*]/div[2]/div[2]/div[2]/div[1]/div[2]/div/button";//"/html/body/div[3]/div[2]/div[2]/div[2]/div[1]/div[2]/div/button";///html/body/div[*]/div[2]/div/div/div[1]/div[1]/div/div/button";//"
	public static final String CustomDateTimeID = "/html/body/div[1]/div/div/div[1]/div/div[1]/div/a[11]";
	public static final String ApplyBtnID = "applyButton";
	public static final String CancelBtnID = "cancelButton";
	public static final String DateID1 = "/html/body/div[1]/div/div[2]/div/div/div/div[2]/div/div/div/div[1]/table/tbody/tr[4]/td[2]/a";//"/html/body/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[1]/table/tbody/tr[4]/td[2]/a";
	public static final String DateID2 = "/html/body/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[2]/table/tbody/tr[4]/td[3]/a";

	//check external link
	public static final String ExternalLink = "/html/body/div[*]/header/div/div[1]/div[1]/div[1]/div[3]/span";
	public static final String ExternalTargetLinkID = "/html/body/div[4]/div[3]/div/div/div[7]/div/div[2]/div[2]/div[1]/div/div[1]/span";

	//grid view and list view id
	public static final String GridViewID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[1]/label";
	public static final String ListViewID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[2]/div[3]/span[2]/label";

	public static final String DashBoardListViewDashBoardID = "/html/body/div[2]/div/div[1]/div/div/div[2]/div[2]/table/tbody/tr/td[2]/a";
	public static final String DashBoardInfoID = "/html/body/div[*]/div/div[1]/div/div/div[2]/div[2]/table/tbody/tr/td[5]/button";
	public static final String DashBoardDeleteID = "/html/body/div[1]/div/div/div[1]/div/div/div/button";
	public static final String LV_DeleteBtnID_Dialog = "/html/body/div[1]/div[2]/div/div[3]/button[1]";//"/html/body/div[1]/div[2]/div/div[5]/button[1]";

	public static final String WelcomeID = "/html/body/div[2]/div/div[1]";//div[@class='welcome-slogan']";//"";

	//welcome page verify
	public static final String Welcome_APMLinkCSS = "APM_wrapper"; //".landing-home-box-content-head[data-bind='text: APM']";
	public static final String Welcome_LALinkCSS = "LA_wrapper"; //".landing-home-box-content-head[data-bind='text: LA']";
	public static final String Welcome_ITALinkID = "ITA_wrapper"; //".landing-home-box-content-head[data-bind='text: ITA']";
	public static final String Welcome_DashboardsLinkID = "dashboards_wrapper"; //"/html/body/div[2]/div/div[2]/ul/li[4]/a/div/div[2]/div[1]";
	public static final String Welcome_DataExp = "data_explore_wrapper"; //"/html/body/div[2]/div/div[2]/ul/li[5]/div/div/div[2]/div[1]";
	public static final String Welcome_ITA_SelectID = "ITA_options"; //"ojChoiceId_ITA_options_selected";
	public static final String Welcome_DataExp_SelectID = "dataExp_options";//"dataExp_Select"; //"ojChoiceId_autogen1_selected";
	public static final String Welcome_LearnMore_getStarted = "getStarted"; //"//a[contains(text(),'How to get started')]";
	public static final String Welcome_LearnMore_Videos = "videos"; //"//a[contains(text(),'Videos')]";
	public static final String Welcome_LearnMore_ServiceOffering = "service_offerings"; //"//a[contains(text(),'Service Offerings')]";
	public static final String Welcome_ITA_PADatabase = "ITA_DB_perf"; //"/html/body/div[1]/div/div/ul/li[2]/div";//"oj-listbox-result-label-8";//ITA Select Item : Performance Analytics - Database
	public static final String Welcome_ITA_PAMiddleware = "ITA_mw_perf"; //"/html/body/div[1]/div/div/ul/li[3]/div";//"oj-listbox-result-label-9";//ITA Select Item : Performance Analytics - Middleware
	public static final String Welcome_ITA_RADatabase = "ITA_DB_resource"; //"/html/body/div[1]/div/div/ul/li[4]/div";//"oj-listbox-result-label-10";//ITA Select Item : Resource Analytics - Database
	public static final String Welcome_ITA_RAMiddleware = "ITA_mw_resource"; //"/html/body/div[1]/div/div/ul/li[5]/div";//"oj-listbox-result-label-11";//ITA Select Item : Resource Analytics - Middleware
	public static final String Welcome_ITA_DEAnalyze = "ITA_Analyze"; //"/html/body/div[1]/div/div/ul/li[6]/div";//"oj-listbox-result-label-12";//ITA Select Item : Data Explorer - Analyze
	public static final String Welcome_ITA_DE = "ITA_Search"; //"/html/body/div[1]/div/div/ul/li[7]/div";//"oj-listbox-result-label-13";//ITA Select Item : Data Explorer
	public static final String Welcome_DataExp_Log = "dataExp_Log"; //"/html/body/div[1]/div/div/ul/li[2]/div";//"oj-listbox-result-label-4";//Data Explorers Select Item : Log
	public static final String Welcome_DataExp_Analyze = "dataExp_Analyze"; //"/html/body/div[1]/div/div/ul/li[3]/div";//"oj-listbox-result-label-5";//Data Explorers Select Item : Analyze
	public static final String Welcome_DataExp_Search = "dataExp_Search"; //"/html/body/div[1]/div/div/ul/li[4]/div";//"oj-listbox-result-label-6";//Data Explorers Select Item : Search

	//Sharing and stoping dashboard
	public static final String option = "dashboardOptsBtn";
	public static final String dashboardshare = "emcpdf_dsbopts_share";//"//*[@id='ui-id-5']/span[2]";
	public static final String stopshare_btn = "emcpdf_dsbopts_share";//"//*[@id='ui-id-5']/span[2]";

    public static final String TILE_WIDER = "wider";
    public static final String TILE_NARROWER = "narrower";
    public static final String TILE_TALLER = "taller";
    public static final String TILE_SHORTER = "shorter";



}
