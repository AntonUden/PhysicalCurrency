package net.zeeraa.physicalcurrency.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import net.zeeraa.physicalcurrency.PhysicalCurrency;
import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.currency.Currency;
import net.zeeraa.physicalcurrency.api.player.PlayerData;
import net.zeeraa.physicalcurrency.api.player.PlayerDataManager;

public class DefaultPlayerDataManager extends PlayerDataManager {
	private Map<UUID, PlayerData> cache = new HashMap<UUID, PlayerData>();

	@Override
	public Map<UUID, PlayerData> getLoadedPlayerData() {
		return cache;
	}

	@Override
	public PlayerData getPlayerData(UUID uuid) {
		if (cache.containsKey(uuid)) {
			return cache.get(uuid);
		}

		File playerDataFile = getPlayerDataFile(uuid);

		PlayerData playerData = null;

		if (playerDataFile.exists()) {
			YamlConfiguration dataFile = YamlConfiguration.loadConfiguration(playerDataFile);

			if (dataFile.contains("primary-currency")) {
				Currency primaryCurrency = PhysicalCurrencyAPI.getCurrencyManager().getCurrency(dataFile.getString("primary-currency"));

				boolean save = false;
				if (primaryCurrency == null) {
					primaryCurrency = PhysicalCurrencyAPI.getCurrencyManager().getDefaultCurrency();
					PhysicalCurrency.getInstance().getLogger().warning("Could not find currency " + dataFile.getString("primary-currency") + " in player data file " + playerDataFile.getPath() + ". Using system default " + primaryCurrency.getName() + " instead");

					save = true;
				}

				playerData = new PlayerData(uuid, primaryCurrency);

				cache.put(uuid, playerData);

				if (save) {
					playerData.save();
				}

				return playerData;
			}
		}

		playerData = new PlayerData(uuid, PhysicalCurrencyAPI.getCurrencyManager().getDefaultCurrency());
		playerData.save();

		return playerData;
	}

	@Override
	public void clearCache() {
		if (PhysicalCurrencyAPI.isDebugMode()) {
			PhysicalCurrency.getInstance().getLogger().info("Removing cached data for all players. Reason: clearCache()");
		}
		cache.clear();
	}

	@Override
	public void clearCache(UUID uuid) {
		if (PhysicalCurrencyAPI.isDebugMode()) {
			PhysicalCurrency.getInstance().getLogger().info("Removing cached data for " + uuid + ". Reason: clearCache(UUID)");
		}
		cache.remove(uuid);
	}

	@Override
	public File getPlayerDataFolder() {
		return PhysicalCurrency.getInstance().getPlayerDataFolder();
	}

	@Override
	public boolean save(PlayerData playerData) {
		YamlConfiguration data = new YamlConfiguration();

		data.set("primary-currency", playerData.getPrimaryCurrency().getName());

		try {
			data.save(getPlayerDataFile(playerData.getUuid()));
			return true;
		} catch (IOException e) {
			PhysicalCurrency.getInstance().getLogger().warning("Failed to save player data for " + playerData.getUuid());
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void cleanCache() {
		List<UUID> toBeRemoved = new ArrayList<UUID>();
		for (UUID uuid : cache.keySet()) {
			Player player = Bukkit.getServer().getPlayer(uuid);

			if (player != null) {
				if (player.isOnline()) {
					continue;
				}
			}

			toBeRemoved.add(uuid);
		}

		for (UUID uuid : toBeRemoved) {
			if (PhysicalCurrencyAPI.isDebugMode()) {
				PhysicalCurrency.getInstance().getLogger().info("Removing cached data for " + uuid + ". Reason: cleanCache()");
			}
			cache.remove(uuid);
		}
	}
}