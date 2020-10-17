package net.zeeraa.physicalcurency.apiimplementation;

import org.bukkit.plugin.Plugin;

import net.zeeraa.physicalcurency.PhysicalCurrency;
import net.zeeraa.physicalcurrency.api.apiimplementation.APIImplementation;
import net.zeeraa.physicalcurrency.api.currency.CurrencyManager;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItemManager;

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
}