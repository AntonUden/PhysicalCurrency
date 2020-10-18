package net.zeeraa.physicalcurrency.api.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.zeeraa.physicalcurrency.api.currency.Currency;

public class PlayerData {
	private UUID uuid;
	private Currency primaryCurrency;

	public PlayerData(UUID uuid, Currency primaryCurrency) {
		this.uuid = uuid;
		this.primaryCurrency = primaryCurrency;
	}

	public UUID getUuid() {
		return uuid;
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getServer().getOfflinePlayer(uuid);
	}

	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(uuid);
	}

	public Currency getPrimaryCurrency() {
		return primaryCurrency;
	}

	public void setPrimaryCurrency(Currency primaryCurrency) {
		this.primaryCurrency = primaryCurrency;
	}
}