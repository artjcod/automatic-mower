package org.mowit.setting;

import java.util.HashMap;

/**
 * This Factory will get and register all the Setting Loader. If no setting
 * loader was found , the default one will be used
 * 
 * @author snaceur
 * @version V1.0
 */
public final class SettingLoaderFactory {

	/**
	 * Singleton no instantiation
	 */
	private SettingLoaderFactory() {
	}

	/**
	 * loader list
	 */
	private static HashMap<String, SettingInterface> settingLoaders = new HashMap<>();

	public static void registerLoader(String name, SettingInterface settingInterface) {
		settingLoaders.put(name, settingInterface);
	}

	/**
	 * get the loader by name
	 * 
	 * @param name
	 * @return instance of SettingInterace
	 */
	public static SettingInterface getSettingLoader(String name) {
		return settingLoaders.get(name) != null ? settingLoaders.get(name)
				: settingLoaders.get(DefaultSettingLoader.class.getSimpleName());
	}

}
