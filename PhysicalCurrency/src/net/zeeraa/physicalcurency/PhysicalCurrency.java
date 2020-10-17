package net.zeeraa.physicalcurency;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.zeeraa.physicalcurency.currency.DefaultCurrencyManager;
import net.zeeraa.physicalcurency.currency.item.DefaultCurrencyItemManager;
import net.zeeraa.physicalcurrency.api.currency.CurrencyManager;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItemManager;

public class PhysicalCurrency extends JavaPlugin {
	private static PhysicalCurrency instance;

	private File currenciesFile;
	private File currencyItemsFile;

	private CurrencyManager currencyManager;
	private CurrencyItemManager currencyItemManager;

	private boolean debugMode;

	public static PhysicalCurrency getInstance() {
		return instance;
	}

	public CurrencyManager getCurrencyManager() {
		return currencyManager;
	}

	public CurrencyItemManager getCurrencyItemManager() {
		return currencyItemManager;
	}

	@Override
	public void onEnable() {
		PhysicalCurrency.instance = this;

		// -=-=-= SETUP DATA FILES =-=-=-
		saveDefaultConfig();

		currenciesFile = new File(getDataFolder().getPath() + File.separator + "currencies.yml");
		currencyItemsFile = new File(getDataFolder().getPath() + File.separator + "currency-items.yml");

		try {
			if (!currenciesFile.exists()) {
				getLogger().info("Creating default currencies.yml at: " + currenciesFile.getPath());
				InputStream in = getClass().getClassLoader().getResourceAsStream("currencies.yml");
				Files.copy(in, currenciesFile.toPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe("Failed to create currencies.yml at: " + currenciesFile.getPath());
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		try {
			if (!currenciesFile.exists()) {
				getLogger().info("Creating default currency-items.yml at: " + currenciesFile.getPath());
				InputStream in = getClass().getClassLoader().getResourceAsStream("currency-items.yml");
				Files.copy(in, currenciesFile.toPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe("Failed to create currency-items.yml at: " + currenciesFile.getPath());
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		debugMode = getConfig().getBoolean("debug-mode");
		
		// -=-=-= CURRENCY MANAGER =-=-=-
		currencyManager = new DefaultCurrencyManager();
	currencyItemManager = new DefaultCurrencyItemManager();
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll((Plugin) this);
		Bukkit.getScheduler().cancelTasks(this);
	}

	public File getCurrenciesFile() {
		return currenciesFile;
	}

	public File getCurrencyItemsFile() {
		return currencyItemsFile;
	}

	public boolean isDebugMode() {
		return debugMode;
	}
}