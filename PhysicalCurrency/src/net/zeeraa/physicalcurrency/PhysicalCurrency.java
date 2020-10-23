package net.zeeraa.physicalcurrency;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.milkbowl.vault.economy.Economy;
import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.apiimplementation.APIImplementation;
import net.zeeraa.physicalcurrency.api.currency.CurrencyManager;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItemManager;
import net.zeeraa.physicalcurrency.api.inventory.InventoryManager;
import net.zeeraa.physicalcurrency.api.player.PlayerDataManager;
import net.zeeraa.physicalcurrency.apiimplementation.DefaultAPIImplementation;
import net.zeeraa.physicalcurrency.command.physicalcurrency.PhysicalCurrencyCommand;
import net.zeeraa.physicalcurrency.currency.DefaultCurrencyManager;
import net.zeeraa.physicalcurrency.currency.item.DefaultCurrencyItemManager;
import net.zeeraa.physicalcurrency.physicalcurrency.inventory.DefaultInventoryManager;
import net.zeeraa.physicalcurrency.player.DefaultPlayerDataManager;
import net.zeeraa.physicalcurrency.vault.DefaultVaultImplementation;
import net.zeeraa.zcommandlib.command.registrator.ZCommandRegistrator;

public class PhysicalCurrency extends JavaPlugin implements Listener {
	private static PhysicalCurrency instance;

	private File currenciesFile;
	private File currencyItemsFile;
	private File playerDataFolder;

	private CurrencyManager currencyManager;
	private CurrencyItemManager currencyItemManager;
	private InventoryManager inventoryManager;
	private PlayerDataManager playerDataManager;

	private BukkitTask cleanCacheTask;

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

	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	public PlayerDataManager getPlayerDataManager() {
		return playerDataManager;
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

	public File getPlayerDataFolder() {
		return playerDataFolder;
	}

	@Override
	public void onEnable() {
		PhysicalCurrency.instance = this;

		cleanCacheTask = null;

		// -=-=-= SETUP DATA FILES =-=-=-
		saveDefaultConfig();

		currenciesFile = new File(getDataFolder().getPath() + File.separator + "currencies.json");
		currencyItemsFile = new File(getDataFolder().getPath() + File.separator + "items.json");
		playerDataFolder = new File(getDataFolder().getPath() + File.separator + "playerdata");

		try {
			if (!currenciesFile.exists()) {
				FileUtils.forceMkdirParent(currenciesFile);
				URL url = this.getClass().getClassLoader().getResource("currencies.json");
				InputStream in = url.openStream();
				Files.copy(in, currenciesFile.toPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe("Failed to create currencies.json at: " + currenciesFile.getPath());
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		try {
			if (!currencyItemsFile.exists()) {
				FileUtils.forceMkdirParent(currencyItemsFile);
				getLogger().info("Creating default items.json at: " + currencyItemsFile.getPath());
				URL url = this.getClass().getClassLoader().getResource("items.json");
				InputStream in = url.openStream();
				Files.copy(in, currencyItemsFile.toPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe("Failed to create items.json at: " + currencyItemsFile.getPath());
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		try {
			FileUtils.forceMkdirParent(playerDataFolder);
			FileUtils.forceMkdir(playerDataFolder);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe("Failed to create playerdata folder at: " + playerDataFolder.getPath());
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		debugMode = getConfig().getBoolean("debug-mode");

		// -=-=-= INIT API =-=-=-
		APIImplementation.setImplementation(new DefaultAPIImplementation());

		// -=-=-= CURRENCY MANAGERS =-=-=-
		currencyManager = new DefaultCurrencyManager();
		currencyItemManager = new DefaultCurrencyItemManager();
		playerDataManager = new DefaultPlayerDataManager();
		inventoryManager = new DefaultInventoryManager();

		currencyManager.loadCurrencies();
		currencyItemManager.loadCurrencyItems();

		ZCommandRegistrator.registerCommand(this, new PhysicalCurrencyCommand());

		cleanCacheTask = new BukkitRunnable() {
			@Override
			public void run() {
				playerDataManager.cleanCache();
			}
		}.runTaskTimer(this, 2000L, 2000L);

		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			Bukkit.getServicesManager().register(Economy.class, new DefaultVaultImplementation(), this, ServicePriority.Normal);
		}
	}

	@Override
	public void onDisable() {
		if (cleanCacheTask != null) {
			cleanCacheTask.cancel();
		}

		HandlerList.unregisterAll((Plugin) this);
		Bukkit.getScheduler().cancelTasks(this);

		if (playerDataManager != null) {
			playerDataManager.clearCache();
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		PhysicalCurrencyAPI.getPlayerDataManager().getPlayerData(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		PhysicalCurrencyAPI.getPlayerDataManager().clearCache(e.getPlayer().getUniqueId());
	}
}
