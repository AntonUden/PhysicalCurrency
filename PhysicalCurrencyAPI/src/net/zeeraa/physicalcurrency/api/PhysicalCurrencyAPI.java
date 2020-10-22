package net.zeeraa.physicalcurrency.api;

import org.bukkit.plugin.Plugin;

import net.zeeraa.physicalcurrency.api.apiimplementation.APIImplementation;
import net.zeeraa.physicalcurrency.api.currency.CurrencyManager;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItemManager;
import net.zeeraa.physicalcurrency.api.inventory.InventoryManager;
import net.zeeraa.physicalcurrency.api.player.PlayerDataManager;

public class PhysicalCurrencyAPI {
	public static CurrencyManager getCurrencyManager() {
		return APIImplementation.getImplementation().getCurrencyManager();
	}

	public static CurrencyItemManager getCurrencyItemManager() {
		return APIImplementation.getImplementation().getCurrencyItemManager();
	}

	public static Plugin getPlugin() {
		return APIImplementation.getImplementation().getPlugin();
	}

	public static boolean isDebugMode() {
		return APIImplementation.getImplementation().isDebugMode();
	}

	public static InventoryManager getInventoryManager() {
		return APIImplementation.getImplementation().getInventoryManager();
	}
	
	public static PlayerDataManager getPlayerDataManager() {
		return APIImplementation.getImplementation().getPlayerDataManager();
	}
}