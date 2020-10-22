package net.zeeraa.physicalcurrency.api.player;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

public abstract class PlayerDataManager {
	public abstract Map<UUID, PlayerData> getLoadedPlayerData();

	@Nullable
	public abstract PlayerData getPlayerData(UUID uuid);

	public abstract void cleanCache();
	
	public abstract void clearCache();

	public abstract void clearCache(UUID uuid);

	public abstract boolean save(PlayerData playerData);

	public abstract File getPlayerDataFolder();

	public File getPlayerDataFile(UUID uuid) {
		return new File(getPlayerDataFolder().getPath() + File.separator + uuid.toString() + ".yml");
	}
}