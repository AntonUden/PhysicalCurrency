package net.zeeraa.physicalcurrency.apiimplementation;

import org.bukkit.plugin.Plugin;

import net.zeeraa.physicalcurrency.PhysicalCurrency;
import net.zeeraa.physicalcurrency.api.apiimplementation.APIImplementation;
import net.zeeraa.physicalcurrency.api.currency.CurrencyManager;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItemManager;
import net.zeeraa.physicalcurrency.api.inventory.InventoryManager;
import net.zeeraa.physicalcurrency.api.player.PlayerDataManager;

public class DefaultAPIImplementation extends APIImplementation {
	public DefaultAPIImplementation() {
		APIImplementation.setImplementation(this);
	}

	@Override
	public CurrencyManager getCurrencyManager() {
		return PhysicalCurrency.getInstance().getCurrencyManager();
	}

	@Override
	public CurrencyItemManager getCurrencyItemManager() {
		return PhysicalCurrency.getInstance().getCurrencyItemManager();
	}

	@Override
	public Plugin getPlugin() {
		return PhysicalCurrency.getInstance();
	}

	@Override
	public boolean isDebugMode() {
		return PhysicalCurrency.getInstance().isDebugMode();
	}

	@Override
	public InventoryManager getInventoryManager() {
		return PhysicalCurrency.getInstance().getInventoryManager();
	}

	@Override
	public PlayerDataManager getPlayerDataManager() {
		return PhysicalCurrency.getInstance().getPlayerDataManager();
	}
}