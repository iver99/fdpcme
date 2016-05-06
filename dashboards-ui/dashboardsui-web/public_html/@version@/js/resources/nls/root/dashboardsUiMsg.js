/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define({
//      "DBS_HOME_TITLE":"Dashboard Home - Oracle Enterprise Manager Cloud Service",
//      "DBS_BUILDER_TITLE":"{0} - Oracle Enterprise Manager Cloud Service",
      "DBS_HOME_CREATE_TYPE_DASHBOARDS":"Dashboard (a single dashboard)",
      "DBS_HOME_CREATE_TYPE_DASHBOARDSET":"Dashboard Set (multiple dashboards on tabs)",
      "DBS_HOME_DASHBOARDS_SET_CREATE_TYPE":"Type",
      "DBS_HOME_TITLE_HOME": "Home",
      "DBS_HOME_TITLE_DASHBOARDS": "Dashboards",
      "COMMON_BTN_OK": "OK",
      "COMMON_BTN_CANCEL": "Cancel",
      "COMMON_BTN_DISCARD": "Discard",
      "COMMON_BTN_SAVE": "Save",
      "COMMON_BTN_EDIT": "Edit",
      "COMMON_BTN_DELETE": "Delete",
      "COMMON_BTN_PRINT": "Print",
      "COMMON_BTN_ADD": "Add",
      "COMMON_BTN_SEARCH": "Search",
      "COMMON_BTN_SAVE_REMOVE":"Save and Remove",
      "COMMON_TEXT_YES": "Yes",
      "COMMON_TEXT_NO": "No",
      "COMMON_TEXT_CLOSE": "CLOSE",
      "COMMON_TEXT_CREATE": "Create",
      "COMMON_TEXT_ERROR": "Error",
      "COMMON_REQUIRE_ERROR": "{0} is required",
      "COMMON_SERVER_ERROR": "Error on accessing server",
      "COMMON_DASHBAORD_SAME_NAME_ERROR": "Name already exists.",
      "COMMON_DASHBAORD_SAME_NAME_ERROR_DETAIL": "Provide a unique name.",
      "COMMON_EDIT_DASHBOARD_DIALOG_TITLE": "Edit Dashboard Properties",
      "COMMON_DELETE_DASHBOARD_MSG": "Delete <b>{0}</b>? This action cannot be undone.",
      "COMMON_TEXT_SHARE": "Share",
      "COMMON_TEXT_SHARE_TITLE": "Share to public",
      "COMMON_TEXT_UNSHARE": "Stop Sharing",
      "COMMON_TEXT_UNSHARE_TITLE": "Stop sharing to public",
      "COMMON_TEXT_SHARE_CONFIRM_SUMMARY": "Dashboard shared.",
      "COMMON_TEXT_SHARE_CONFIRM_DETAIL": "This dashboard can now be viewed by others.",
      "COMMON_TEXT_UNSHARE_CONFIRM_SUMMARY": "Dashboard no longer shared.",
      "COMMON_TEXT_UNSHARE_CONFIRM_DETAIL": "This dashboard cannot be viewed by others.",
      "COMMON_SHARE_DAHBOARD_DIALOG_TILE": "Share Dashboard",
      "COMMON_SHARE_DAHBOARD_DIALOG_CONTENT": "Do you want to share <b>{0}</b>? This action will make the dashboard to be viewed by others.",
      "COMMON_UNSHARE_DAHBOARD_DIALOG_TILE": "Stop Sharing Dashboard",
      "COMMON_UNSHARE_DAHBOARD_DIALOG_CONTENT": "Do you want to stop sharing <b>{0}</b>?",
      "DBS_HOME_SEARCH_ARIA_LABEL": "Search for a Dashboard",
      "DBS_HOME_SEARCH_PLACE_HODE": "Search",
      "DBS_HOME_SEARCH_BTN_LABEL": "Search",
      "DBS_HOME_CLEAN_BTN_LABEL": "Clear",
      "DBS_HOME_CREATE_BTN_LABEL": "Create Dashboard",
      "DBS_HOME_CREATE_BTN_TT_TITLE": "How do I use this page?",
      "DBS_HOME_SELECT_SORT_BY_LABEL": "Sort by",
      "DBS_HOME_SELECT_SORT_ACCESS_DATE_ASC": "Last Accessed Ascending",
      "DBS_HOME_SELECT_SORT_ACCESS_DATE_DSC": "Last Accessed Descending",
      "DBS_HOME_SELECT_SORT_NAME_ASC": "Name Ascending",
      "DBS_HOME_SELECT_SORT_NAME_DSC": "Name Descending",
      "DBS_HOME_SELECT_SORT_CREATION_TIME_ASC": "Creation Date Ascending",
      "DBS_HOME_SELECT_SORT_CREATION_TIME_DSC": "Creation Date Descending",
      "DBS_HOME_SELECT_SORT_LAST_MODIFIED_ASC": "Last Modified Ascending",
      "DBS_HOME_SELECT_SORT_LAST_MODIFIED_DSC": "Last Modified Descending",
      "DBS_HOME_SELECT_SORT_LAST_OWNER_ASC": "Created By Ascending",
      "DBS_HOME_SELECT_SORT_LAST_OWNER_DSC": "Created By Descending",
      "DBS_HOME_SELECT_SORT_DEFAULT": "Default",
      "DBS_HOME_CREATE_BTN_TT_CONTENT": " Click to create a dashboard and add widgets of your choice.",
      "DBS_HOME_EXPLORE_BTN_LABEL": "Explore Data",
      "DBS_HOME_EXPLORE_BTN_TT": "Click to select a Visual Analyzer which allows you to explore data.",
      "DBS_HOME_CREATE_DLG_TITLE": "Create Dashboard",
      "DBS_HOME_CREATE_DLG_NAME": "Name",
      "DBS_HOME_CREATE_DLG_DES": "Description",
      "DBS_HOME_CREATE_DLG_TIME_RANGE": "Show time selector & refresh control",
      "DBS_HOME_CREATE_DLG_INVALID_NAME_SUM": "Name is invalid",
      "DBS_HOME_CREATE_DLG_INVALID_NAME": "You must enter a non empty name and its length should be less than 64 characters.",
      "DBS_HOME_CREATE_DLG_INVALID_DES_SUM": "Description is invalid",
      "DBS_HOME_CREATE_DLG_INVALID_DES": "You must enter a description with less than 256 characters.",
      "DBS_HOME_FILTER_TYPE_LABEL": "Type",
      "DBS_HOME_FILTER_TYPE_APP": "App",
      "DBS_HOME_FILTER_TYPE_DSB": "Dashboard",
      "DBS_HOME_FILTER_SERVICE_LABEL": "Cloud Services",
      "DBS_HOME_FILTER_SERVICE_LA": "Log Analytics",
      "DBS_HOME_FILTER_SERVICE_APM": "Application Performance Monitoring ",
      "DBS_HOME_FILTER_SERVICE_APM_ABBR": "APM",
      "DBS_HOME_FILTER_SERVICE_ITA": "IT Analytics",
      "DBS_HOME_FILTER_CREATOR_LABEL": "Created By",
      "DBS_HOME_FILTER_CREATOR_ORACLE": "Oracle",
      "DBS_HOME_FILTER_FAVORITES_LABEL": "Favorites",
      "DBS_HOME_FILTER_FAVORITES_MY": "My Favorites",
      "DBS_HOME_FILTER_CREATOR_SHARE": "Others (Shared)",
      "DBS_HOME_FILTER_CREATOR_ME": "Me",
      "DBS_HOME_VIEW_LABEL": "Choose one view",
      "DBS_HOME_VIEW_LIST_LABEL": "List View",
      "DBS_HOME_VIEW_GRID_LABEL": "Grid View",
      "DBS_HOME_VIEW_LIST_NAME": "Name",
      "DBS_HOME_VIEW_LIST_CREATOR": "Created By",
      "DBS_HOME_VIEW_LIST_CREATEDATE": "Creation Date",
      "DBS_HOME_VIEW_LIST_MODIFIEDDATE": "Last Modified",
      "DBS_HOME_COME_SOON_DLG_INFO": "Information",
      "DBS_HOME_COME_SOON_DLG_BODY": "Coming soon...",
      "DBS_HOME_CFM_DLG_DELETE_DSB": "Delete Dashboard",
      "DBS_HOME_CFM_DLG_DELETE_DSB_MSG": "Do you want to delete the selected dashboard '{0}'?",
      "DBS_HOME_CFM_DLG_DELETE_DSB_ERROR": "Error for deleting dashboard.",
      "DBS_HOME_WC_DLG_TILE": "Welcome, {0}!",
      "DBS_HOME_WC_DLG_BROWSE": "Browse",
      "DBS_HOME_WC_DLG_BROWSE_CONTENT": "through our rich gallery of analytics solutions.",
      "DBS_HOME_WC_DLG_BUILD": "Build",
      "DBS_HOME_WC_DLG_BUILD_CONTENT": "custom and personal dashboards using our out-of-the box widgets.",
      "DBS_HOME_WC_DLG_EXPLORE": "Explore",
      "DBS_HOME_WC_DLG_EXPLORE_CONTENT": "data, set visualization controls, and save widgets.",
      "DBS_HOME_WC_DLG_GOT_BTN": "Got it!",
      "DBS_HOME_DSB_PANEL_WIDGETS": "Widgets",
      "DBS_HOME_DSB_PAGE_SCREEN_SHOT": "Snapshot",
      "DBS_HOME_DSB_PAGE_DESCRIPTION": "Description",
      "DBS_HOME_DSB_PAGE_WIDGETS": "Widgets",    
      "DBS_HOME_DSB_PAGE_INFO_DESC": "Description: ",
      "DBS_HOME_DSB_PAGE_INFO_CREATE": "Created By: ",
      "DBS_HOME_DSB_PAGE_INFO_CDATE": "Creation Date: ",
      "DBS_HOME_DSB_PAGE_INFO_TYPE": "Type: ",
      "DBS_HOME_DSB_PAGE_INFO_TYPE_SYS": "System Dashboard",
      "DBS_HOME_DSB_PAGE_INFO_TYPE_USER": "User Dashboard",
      "DBS_HOME_DSB_PAGE_INFO_ISSYS": "System Dashboard: ",
      "DBS_HOME_DSB_PAGE_INFO_LABEL": "Dashboard Information",
      "DBS_HOME_DSB_PAGE_INFO_DELETE_LABEL": "Delete",
      "DBS_HOME_SHOWHIDE_DESCRIPTION":"Show dashboard description",
      "DBS_BUILDER_DASHBOARD_SET_DUPLICATED_DASHBOARD": "{0} is already a member of this dashboard set. Select another dashboard.",
      "DBS_BUILDER_DASHBOARD_SET_SHARE_SUCCESS": "The dashboard set and all of its memeber dashboards can now be viewed by others.",
      "DBS_BUILDER_DASHBOARD_SET_SHARE_ERROR": "The dashboard set cannot be viewed by others; individual dashboard members are still shared.",
      "DBS_BUILDER_DASHBOARD_SET_PRINT_MASK": "Loading Content",
      "DBS_BUILDER_LOADING": "Loading...",
      "DBS_BUILDER_NAME_EDIT": "Double click to edit name",
      "DBS_BUILDER_SAME_NAME_EXISTS_ERROR": "Dashboard with the same name exists already",
      "DBS_BUILDER_REQUIRE_NAME": "Name is required",
      "DBS_BUILDER_BTN_OPTIONS": "Options",
      "DBS_BUILDER_BTN_DUPLICATE": "Duplicate",
      "DBS_BUILDER_BTN_DUPLICATE_TITLE": "Duplicate dashboard",
      "DBS_BUILDER_DUPLICATE_DIALOG_TITLE": "Duplicate Dashboard",
      "DBS_BUILDER_BTN_EDIT_TITLE": "Edit dashboard",
       "DBS_BUILDER_EDIT_DSB_NAME": "Dashboard Name",
      "DBS_BUILDER_EDIT_SHOW_DSB_DESC": "Show dashboard description",
      "DBS_BUILDER_EDIT_SHOW_ENTITY_FILTER": "Show Entity filter",
      "DBS_BUILDER_EDIT_SHOW_TIME_FILTER": "Show Time Range filter",
      "DBS_BUILDER_BTN_DELETE_TITLE": "Delete dashboard",
      "DBS_BUILDER_BTN_FAVORITES_ADD": "Add Favorite",
      "DBS_BUILDER_BTN_FAVORITES_REMOVE": "Remove Favorite",
      "DBS_BUILDER_BTN_HOME_SET": "Set as Home",
      "DBS_BUILDER_BTN_HOME_REMOVE": "Remove as Home",
      "DBS_BUILDER_SET_AS_HOME_DIALOG_TITLE": "Set as Home",
      "DBS_BUILDER_BTN_ADD_WIDGET": "Add widgets",
      "DBS_BUILDER_BTN_ADD": "Add",
      "DBS_BUILDER_BTN_ADD_TEXT": "Add Text",
      "DBS_BUILDER_BTN_TOGGLE_MODE": "Toggle Mode",
//      "DBS_BUILDER_TEXT_WIDGET_SAMPLE": "Sample Text",
//      "DBS_BUILDER_TEXT_WIDGET_LINK_DIALOG_TITLE": "Add Hyperlink",
//      "DBS_BUILDER_TEXT_WIDGET_RENAME": "Rename",
//      "DBS_BUILDER_TEXT_WIDGET_DELETE": "Delete",
      "TEXT_LENGTH_ERROR_MSG": "Error: The Content is too long!",
//      "DBS_BUILDER_BTN_ADD_TEXT_TITLE": "Add Text Widget",
//      "DBS_BUILDER_BTN_ADD_HINT_TITLE": "How do I use this page?",
//      "DBS_BUILDER_BTN_ADD_HINT_DETAIL": "Click the + button and add new widgets to your dashboard",
//      "DBS_BUILDER_BTN_ADD_HINT_TEXT_LINE2_1": "Click ",
//      "DBS_BUILDER_BTN_ADD_HINT_ADD_LINK": "Options->Add",
//      "DBS_BUILDER_BTN_ADD_HINT_TEXT_LINE2_2": " to get started",
      "DBS_BUILDER_CONFIRM_LEAVE_DIALOG_TITLE": "Unsaved Changes",
      "DBS_BUILDER_CONFIRM_LEAVE_DIALOG_CONTENT": "You have unsaved changes on this dashboard. Do you want to save your changes?",
      "DBS_BUILDER_BTN_ADD_HINT_TITLE": "Let's get started!",
      "DBS_BUILDER_BTN_ADD_HINT_TEXT_LINE1": "Drag and drop content from the Edit Page panel on the right to build your dashboard.",
      "DBS_BUILDER_BTN_ADD_HINT_TEXT_LINE2_1": "Click ",
      "DBS_BUILDER_BTN_ADD_HINT_ADD_LINK": "Options->Add",
      "DBS_BUILDER_BTN_ADD_HINT_TEXT_LINE2_2": " to get started",
      "DBS_BUILDER_BTN_SAVE_DASHBOARD": "Save Dashboard",
      "DBS_BUILDER_SETTINGS": "Settings",
      "DBS_BUILDER_FILTERS": "FILTERS",
      "DBS_BUILDER_INCLUDE_TIME_CONTROL": "Include time control (range and refresh)",
      "DBS_BUILDER_VIEW": "VIEW",
      "DBS_BUILDER_VIEW_DESC": "Configure builder view like widget height, etc.",
      "DBS_BUILDER_OTHERS": "OTHERS",
      "DBS_BUILDER_OTHERS_DESC": "Configure builder miscellaneous",
//      "DBS_BUILDER_LEFT_PANEL_TITLE": "Dashboard",
      "DBS_BUILDER_LEFT_PANEL_BTN_HIDE_LABEL": "Hide",
      "DBS_BUILDER_LEFT_PANEL_BTN_SHOW_LABEL": "Show",
      "DBS_BUILDER_LEFT_PANEL_BTN_HELP_LABEL": "Help",
      "DBS_BUILDER_LEFT_PANEL_CONTENT_LABEL": "Content",
      "DBS_BUILDER_LEFT_PANEL_SEARCH_PLACEHODE": "Search",
      "DBS_BUILDER_LEFT_PANEL_SEARCH_PLACEHODE_CONTENT": "Search for content",
      "DBS_BUILDER_LEFT_PANEL_SEARCH_ARIA_LABEL": "Search for a widget",
      "DBS_BUILDER_LEFT_PANEL_SEARCH_BTN_LABEL": "Search",
      "DBS_BUILDER_LEFT_PANEL_CONTAINER_LABEL": "Container",
      "DBS_BUILDER_LEFT_PANEL_CONTAINER_POP_VALUE1": "219",
      "DBS_BUILDER_LEFT_PANEL_CONTAINER_POP_LABEL1": "Total Processes",
      "DBS_BUILDER_LEFT_PANEL_CONTAINER_POP_LABEL2": "CPU Status",
      "DBS_BUILDER_LEFT_PANEL_CONTAINER_POP_LABEL3": "Memory Used",
      "DBS_BUILDER_LEFT_PANEL_CONTAINER_POP_LABEL4": "Disk Used",
      "DBS_BUILDER_LEFT_PANEL_CONTAINER_POP_DESC": "Group one or more content terms into a single container",
      "DBS_BUILDER_LEFT_PANEL_Edit_Page": "Edit Page",
//      "DBS_BUILDER_LEFT_PANEL_WIDGET_BTN_LABEL": "Go To Data Explorer",
//      "DBS_BUILDER_LEFT_PANEL_ADD_TEXT": "Text",
//      "DBS_BUILDER_LEFT_PANEL_ADD_TEXT_HELPER": "Sample Text",
//      "DBS_BUILDER_LEFT_PANEL_ADD_LINK": "Link",
//      "DBS_BUILDER_LEFT_PANEL_ADD_LINK_HELPER": "Link to other dashboard",
      "DBS_BUILDER_LEFT_PANEL_WIDGET_PAGES": "Pages",
//      "DBS_BUILDER_LEFT_PANEL_SHOW_TIME_SELECTOR": "Show Time Selector",
//      "DBS_BUILDER_LEFT_PANEL_SHOW_REFRESH": "Show Refresh Control",
      "DBS_BUILDER_ADD_WIDGET_DLG_TITLE": "Add Widgets",
      "DBS_BUILDER_ADD_WIDGET_DLG_WIDGET_GROUP": "Widget Group",
      "DBS_BUILDER_ADD_WIDGET_DLG_CREATE_WIDGET": "Create Widget",
      "DBS_BUILDER_ADD_WIDGET_DLG_SEARCH_PLACEHOLDER": " Enter criteria to search for a widget.",
      "DBS_BUILDER_ADD_WIDGET_DLG_DBL_CLICK": "Double-click to add a widget, or click a widget to view details and add.",
      "DBS_BUILDER_WIDGET_DETAIL_TITLE": "Widget Details",
      "DBS_BUILDER_WIDGET_DETAIL_NAME": "Name",
      "DBS_BUILDER_WIDGET_DETAIL_DESC": "Description",
      "DBS_BUILDER_WIDGET_DETAIL_QUERY_STRING": "Query String",
      "DBS_BUILDER_WIDGET_DETAIL_OWNER": "Owner",
      "DBS_BUILDER_WIDGET_DETAIL_CREATION_DATE": "Creation Date",
      "DBS_BUILDER_CREATE_WIDGET_TITLE": "Create New Widget",
      "DBS_BUILDER_CREATE_WIDGET_HEADER": "Create Widget (for integration dev only)",
      "DBS_BUILDER_CREATE_WIDGET_PROVIDER_NAME": "Provider Name",
      "DBS_BUILDER_CREATE_WIDGET_VERSION": "Version",
      "DBS_BUILDER_CREATE_WIDGET_ASSET_ROOT": "Asset Root",
      "DBS_BUILDER_CREATE_WIDGET_NAME": "Name",
      "DBS_BUILDER_CREATE_WIDGET_DESC": "Description",
      "DBS_BUILDER_CREATE_WIDGET_QUR_STR": "Query String",
      "DBS_BUILDER_CREATE_WIDGET_CAT": "Category",
      "DBS_BUILDER_CREATE_WIDGET_INFO_ABS_1ST": "Enter the ",
      "DBS_BUILDER_CREATE_WIDGET_INFO_ABS_2ND": "FULL ABSOLUTE PATH",
      "DBS_BUILDER_CREATE_WIDGET_INFO_ABS_3RD": " for below URLs in this category. E.g. http://slc04pxi.us.oracle.com:7001/widgets/sample/sampleWidget.html",
      "DBS_BUILDER_CREATE_WIDGET_INFO_REL_1ST": "Enter the ",
      "DBS_BUILDER_CREATE_WIDGET_INFO_REL_2ND": "RELATIVE PATH",
      "DBS_BUILDER_CREATE_WIDGET_INFO_REL_3RD": " for below URLs in this category. E.g. /sample/sampleWidget.html",
      "DBS_BUILDER_CREATE_WIDGET_KOC_NAME": "KOC Name",
      "DBS_BUILDER_CREATE_WIDGET_VM_URL": "View Model URL",
      "DBS_BUILDER_CREATE_WIDGET_TMPL_URL": "Template URL",
      "DBS_BUILDER_CREATE_WIDGET_ICN_URL": "ICON URL",
      "DBS_BUILDER_CREATE_WIDGET_HISGRM_URL": "Histogram URL",
      "DBS_BUILDER_CHANGE_URL_TITLE": "Change URL",
      "DBS_BUILDER_TILE_ACTIONS": "Configure widget",
      "DBS_BUILDER_TILE_EDIT":"Open in Data Explorer",
      "DBS_BUILDER_TILE_REFRESH": "Refresh",
      "DBS_BUILDER_TILE_REMOVE": "Remove",
      "DBS_BUILDER_TILE_RESIZE": "Resize",
      "DBS_BUILDER_TILE_WIDER": "Wider",
      "DBS_BUILDER_TILE_NARROWER": "Narrower",
      "DBS_BUILDER_TILE_TALLER": "Taller",
      "DBS_BUILDER_TILE_SHORTER": "Shorter",
      "DBS_BUILDER_TILE_MAXIMIZE": "Maximize",
      "DBS_BUILDER_TILE_RESTORE": "Restore",
      "DBS_BUILDER_TILE_SHOW":"Show title",
      "DBS_BUILDER_TILE_HIDE":"Hide title",
      "DBS_BUILDER_TILE_CFG": "Edit",
      "DBS_BUILDER_ALL_TARGETS": "All Targets",
      "DBS_BUILDER_TARGETS_SELECTED": "Targets Selected",
      "DBS_BUILDER_AUTOREFRESH_REFRESH":"Auto-refresh",
      "DBS_BUILDER_AUTOREFRESH_OFF":"Off",
      "DBS_BUILDER_AUTOREFRESH_ON":"On (Every 5 Minutes)",
      "DBS_BUILDER_AUTOREFRESH_NONE":"None",
      "DBS_BUILDER_AUTOREFRESH_15SEC":"15 Seconds",
      "DBS_BUILDER_AUTOREFRESH_30SEC":"30 Seconds",
      "DBS_BUILDER_AUTOREFRESH_1MIN":"1 Minute",
      "DBS_BUILDER_AUTOREFRESH_15MIN":"15 Minutes",
      "DBS_BUILDER_MSG_CHANGES_SAVED": "Changes on the dashboard have been saved successfully.",
      "DBS_BUILDER_MSG_SET_AS_HOME_SUCC": "Dashboard is now set as the Home page.",
      "DBS_BUILDER_MSG_SET_AS_HOME_FAIL": "Error occurred when setting the dashboard as the Home page. Check console log or server log for details.",
      "DBS_BUILDER_MSG_SET_AS_HOME_CONFIRM": "Another dashboard has been set as the Home page already. Continue to set this dashboard as the Home page?",
      "DBS_BUILDER_MSG_ADD_FAVORITE_SUCC": "Dashboard added to Favorites.",
      "DBS_BUILDER_MSG_ADD_FAVORITE_FAIL": "Error occurred when adding the dashboard to Favorites. Check console log or server log for details.",
      "DBS_BUILDER_MSG_REMOVE_FAVORITE_SUCC": "Dashboard removed from Favorites.",
      "DBS_BUILDER_MSG_REMOVE_FAVORITE_FAIL": "Error occurred when removing the dashboard from Favorites. Check console log or server log for details.",
      "DBS_BUILDER_MSG_REMOVE_AS_HOME_SUCC": "Dashboard is no longer set as the Home page.",
      "DBS_BUILDER_MSG_REMOVE_AS_HOME_FAIL": "Error occurred when removing the dashboard as the Home page. Check console log or server log for details.",
      "DBS_BUILDER_MSG_ERROR_IN_SAVING": "Error occurred when saving the dashboard. Check console log or server log for details.",
      "DBS_BUILDER_MSG_ERROR_IN_DUPLICATING": "Error occurred when duplicating the dashboard. Check console log or server log for details.",
      "DBS_BUILDER_MSG_ERROR_NAME_DUPLICATED_SUMMARY": "Dashboard name already exists.",
      "DBS_BUILDER_MSG_ERROR_NAME_DUPLICATED_DETAIL": "Provide a unique name.",
      
      
      "TEXT_WIDGET_IFRAME_HINT":"Please change the URL and click \"Change\" to apply: ",
      "TEXT_WIDGET_IFRAME_CONFIGURATION":"Configuration",
      "TEXT_WIDGET_SUBSCRIBER_HINT":"You will see received message and what I will respond",
      "LABEL_WIDGET_IFRAME_CHANGE":"Change",
      "TEXT_WIDGET_PUBLISHER_HINT":"Please write your message and click \"Publish\" to send",
      "LABEL_WIDGET_PUBLISHER_PUBLISH":"Publish",
      
      "DBS_ERROR_PAGE_TITLE":"Error",
      "DBS_ERROR_PAGE_NOT_FOUND_MSG":"Sorry, the page you have requested either doesn't exist or you do not have access to it.",
      "DBS_ERROR_DASHBOARD_ID_NOT_FOUND_MSG":"Sorry, you must specify a valid dashboard id to open a dashboard.",
      "DBS_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG":"Sorry, the page you have requested either doesn't exist or you do not have access to it. Reason: \"No service is subscribed\"",
      "DBS_ERROR_ORA_EMSAAS_USERNAME_AND_TENANTNAME_INVALID": "Error: failed to retrieve user or tenant.",
      "DBS_ERROR_HOME_PAGE_NOT_FOUND_MSG":"The dashboard which you have set as Home page no longer exists or you don't have access to it.",
      "DBS_ERROR_TEXT_CLICK": "Click",
      "DBS_ERROR_TEXT_HERE": "here",
      "DBS_ERROR_TEXT_GO_HOME_PAGE": "to go to the default Home page",
      "DBS_ERROR_URL": "Requested URL is: ",
      "DBS_ERROR_BTN_SIGN_OUT": "Sign Out",
      
      "SERVICE_NAME_APM":"Application Performance Monitoring",
      "SERVICE_NAME_ITAnalytics":"IT Analytics",
      "SERVICE_NAME_LogAnalytics":"Log Analytics",

      // common messages for error page are put here: add __PLUS_SERVICE to support url pattern '&service=APM|ITAnalytics|LogAnalytics'
      "COMMON_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG__PLUS_SERVICE":"You currently don't have access to this page because you're not subscribed to {0}.",
      "COMMON_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG":"You currently don't have access to this page because you're not subscribed to the service being accessed.",
      "COMMON_ERROR_PAGE_NOT_FOUND_MSG":"Sorry, the page you've requested is not available.",
      "COMMON_ERROR_PAGE_NO_ACCESS_NO_PERMISSION_MSG__PLUS_SERVICE":"You don't have permissions to access to this page. Contact your {0} Administrator.",
      "COMMON_ERROR_PAGE_NO_ACCESS_NO_PERMISSION_MSG":"You don't have permissions to access to this page. Contact your Administrator.",
      "COMMON_ERROR_PAGE_NO_ACCESS_NO_SUBS_MSG__PLUS_SERVICE":"You currently don't have access to this page because you're not subscribed to {0}.",
      "COMMON_ERROR_PAGE_NO_ACCESS_NO_SUBS_MSG":"You currently don't have access to this page because you're not subscribed to the service being accessed.",

      "LANDING_HOME_WINDOW_TITLE": "Landing Home",
      "LANDING_HOME_WELCOME_SLOGAN": "Welcome to Oracle Management Cloud",
      "LANDING_HOME_APM": "Application Performance Monitoring",
      "LANDING_HOME_APM_DESC": "Rapidly identify, response, and resolve your software roadblocks",
      "LANDING_HOME_LA": "Log Analytics",
      "LANDING_HOME_LA_DESC": "Topology aware log exploration and analytics for modern applications and infrastructure",
      "LANDING_HOME_ITA": "IT Analytics",
      "LANDING_HOME_ITA_DESC": "Operational big data intelligence for modern IT",
      "LANDING_HOME_ITA_DB_PERFORMANCE": "Performance Analytics - Database",
      "LANDING_HOME_SELECT": "Select",
      "LANDING_HOME_ITA_DB_RESOURCE": "Resource Analytics - Database",
      "LANDING_HOME_ITA_MIDDLEWARE_PERFORMANCE": "Performance Analytics - Middleware",
      "LANDING_HOME_ITA_MIDDLEWARE_RESOURCE": "Resource Analytics - Middleware",
      "LANDING_HOME_DASHBOARDS": "Dashboards",
      "LANDING_HOME_DASHBOARDS_DESC": "Build custom dashboards using out-of-the-box widgets or your own visualization of data",
      "LANDING_HOME_DATA_EXPLORERS": "Data Explorers",
      "LANDING_HOME_DATA_EXPLORERS_DESC": "Search, analyze, and visualize data",
      "LANDING_HOME_DATA_EXPLORER": "Data Explorer",
      
      "LANDING_HOME_LEARN_MORE": "Learn More",
      "LANDING_HOME_GET_STARTED_LINK": "How to get started",
      "LANDING_HOME_VIDEOS_LINK": "Videos",
      "LANDING_HOME_COMMUNITY_LINK": "Service Offerings",
      
      "DBS_BUILDER_EDIT_WIDGET_LINK": "Link",
      "DBS_BUILDER_EDIT_WIDGET_LINK_DESC": "Link to other dashboard",
      "DBS_BUILDER_EDIT_WIDGET_LINK_DIALOG_TITLE": "Edit widget link",
      "DBS_BUILDER_EDIT_WIDGET_LINK_DIALOG_NAME_LABEL": "Name",
      "DBS_BUILDER_EDIT_WIDGET_LINK_DIALOG_URL_LABEL": "Url",
      "DBS_BUILDER_EDIT_WIDGET_LINK_NAME_REQUIRED": "Name is required",
      "DBS_BUILDER_EDIT_WIDGET_LINK_URL_REQUIRED": "URL is required",
      "DBS_BUILDER_EDIT_WIDGET_LINK_NAME_VALIDATE_ERROR": "Enter a non-empty name and its length should be less than 4000 characters.",
      "DBS_BUILDER_EDIT_WIDGET_LINK_URL_LENGTH_VALIDATE_ERROR": "Enter a non-empty URL and its length should be less than 4000 characters.",
      "DBS_BUILDER_EDIT_WIDGET_LINK_URL_VALIDATE_ERROR": "The format of URL is incorrect.",
      
      //dashoard set message
      "DBSSET_BUILDER_DASHBOARDSET":"Dashboard Set",
      "DBSSET_BUILDER_EDIT_DASHBOARDSET":"Edit Dashboard Set",
      "DBSSET_BUILDER_DELETE_WARNING":"<b>Note:</b> Deleting the dashboard set does not delete any of the individual dashboards you created and included in the set",
      "DBSSET_BUILDER_DASHBOARDSET_OPTIONS":"Dashboard Set options",
      "DBSSET_BUILDER_REMOVE_DASHBOARD":"Remove dashboard",
      "DBSSET_BUILDER_DELETE_DASHBOARDSET":"Delete Dashboardset",
      "DBSSET_BUILDER_SELECT_TIP":"Select a dashboard to display on this tab.",
      "DBSSET_BUILDER_NAME_DESCRIPTION":"Name and Description",
      "DBSSET_BUILDER_VISIBILITY":"Dashboard Visibility",
      "DBSSET_BUILDER_SHARE_SETTINGS":"Share Settings",
      "DBSSET_BUILDER_NOT_SHARED":"Not Shared (visible only to me)",
      "DBSSET_BUILDER_SHARED":"Shared with others",
      "DBSSET_BUILDER_DEFAULT_REFRESH":"Default Auto-refresh Value",
      "DBSSET_BUILDER_REFRESH_TIPS":"Specify whether this dashboard refreshes automatically by default for other users.User changes to this setting are saved",
      "DBSSET_BUILDER_Delete_TIPS":"You have unsaved changes in this dashboard.Select <b>Save and Remove </b> to save this dashboard before removing it from the dashboard set.Select <b> Remove </b> to remove the dashboard from the set and discard all changes"
});

