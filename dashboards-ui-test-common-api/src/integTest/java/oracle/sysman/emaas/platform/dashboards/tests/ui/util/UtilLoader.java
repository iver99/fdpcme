/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * Load API implementation
 *
 * @author miao
 */
public final class UtilLoader<T extends IUiTestCommonAPI>
{
	/**
	 * Return concrete utility class according to detected version
	 * <p>
	 * e.g. IBrandingBarUtil -> BrandingBarUtil_Vesion -> BrandingBarUtil_<version>
	 *
	 * @param utilInterface
	 *            of Utility Interface
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T loadUtil(WebDriver wdriver, Class<T> utilInterface)
	{
		if (utilInterface == null) {
			throw new IllegalArgumentException("null Util interface!");
		}

		if (!utilInterface.isInterface()) {
			throw new IllegalArgumentException("concrete Util class is not allowed:" + utilInterface);
		}

		//determine api version to call
		String apiVersion = getApiVersion(wdriver, utilInterface);

		//load util implementation according to api version
		String interfaceName = utilInterface.getSimpleName();
		String packageName = utilInterface.getPackage().getName();
		String utilClassName = packageName.substring(0, packageName.length() - 5) + ".impl." + interfaceName.substring(1) + "_"
				+ apiVersion;
		try {
			Class<T> utilClass = (Class<T>) Class.forName(utilClassName);
			T utilObj = utilClass.newInstance();
			return utilObj;
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException("Util class is not found: " + utilClassName);
		}
		catch (InstantiationException e) {
			throw new RuntimeException("Failed to initialize Util class: " + utilClassName, e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException("Illegal access to Util class: " + utilClassName, e);
		}
	}

	@SuppressWarnings("unchecked")
	private String getApiVersion(WebDriver wdriver, Class<T> utilInterface)
	{
		String interfaceName = utilInterface.getSimpleName();
		String packageName = utilInterface.getPackage().getName();
		String utilVersionClassName = packageName.substring(0, packageName.length() - 5) + ".impl." + interfaceName.substring(1)
				+ "_Version";
		try {
			Class<T> utilClass = (Class<T>) Class.forName(utilVersionClassName);
			T utilObj = utilClass.newInstance();
			return utilObj.getApiVersion(wdriver);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException("Util Version class is not found: " + utilVersionClassName);
		}
		catch (InstantiationException e) {
			throw new RuntimeException("Failed to initialize Util Version class: " + utilVersionClassName, e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException("Illegal access to Util Version class: " + utilVersionClassName, e);
		}

	}
}
