package net.zeeraa.physicalcurrency.api.apiimplementation;

import org.bukkit.plugin.Plugin;

import net.zeeraa.physicalcurrency.api.currency.CurrencyManager;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItemManager;

public abstract class APIImplementation {
	private static APIImplementation instance;
	
	/**
	 * Set the api implementation
	 * @param instance The instance of the {@link APIImplementation}
	 */
	public static void setImplementation(APIImplementation instance) {
		APIImplementation.instance = instance;
	}
	
	/**
	 * Get the api implementation for the plugin
	 * @return {@link APIImplementation} instance from the main plugin
	 */
	public static APIImplementation getImplementation() {
		return instance;
	}
	
	
	/**
	 * Get the instance of the {@link CurrencyManager}
	 * @return {@link CurrencyManager} instance
	 */
	public abstract CurrencyManager getCurrencyManager();
	
	public abstract CurrencyItemManager getCurrencyItemManager();
	
	public abstract Plugin getPlugin();

	public abstract boolean isDebugMode();
}
