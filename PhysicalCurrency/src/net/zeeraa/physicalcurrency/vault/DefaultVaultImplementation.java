package net.zeeraa.physicalcurrency.vault;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.balance.Balance;

public class DefaultVaultImplementation implements Economy {

	@Override
	public boolean isEnabled() {
		if (PhysicalCurrencyAPI.getPlugin() != null) {
			return PhysicalCurrencyAPI.getPlugin().isEnabled();
		}

		return false;
	}

	@Override
	public String getName() {
		return "PhysicalCurrencies";
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	@Override
	public String format(double amount) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(amount);
	}

	@Override
	public String currencyNamePlural() {
		return "TODO";// TODO:
	}

	@Override
	public String currencyNameSingular() {
		return "TODO";// TODO:
	}

	@Override
	public boolean hasAccount(OfflinePlayer player) {
		return player.isOnline();
	}

	public boolean hasAccount(OfflinePlayer player, String worldName) {
		return hasAccount(player);
	}

	@Override
	public double getBalance(OfflinePlayer player) {
		if (player.isOnline()) {
			return Balance.getTotalVaultValue(PhysicalCurrencyAPI.getInventoryManager().getBalance(player.getPlayer().getInventory()));
		}
		return 0;
	}

	@Override
	public double getBalance(OfflinePlayer player, String world) {
		return getBalance(player);
	}

	@Override
	public boolean has(OfflinePlayer player, double amount) {
		if(player.isOnline()) {
			return Balance.getTotalVaultValue(PhysicalCurrencyAPI.getInventoryManager().getBalance(player.getPlayer().getInventory())) >= amount;
		}
		
		return false;
	}

	@Override
	public boolean has(OfflinePlayer player, String worldName, double amount) {
		return has(player, amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
		if (amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cant withdraw a negative amount");
		}
		
		if (!player.isOnline()) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cant withdraw from a player that is not online");
		}

		if (!PhysicalCurrencyAPI.getInventoryManager().canWithdraw(player.getPlayer().getInventory(), amount)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "The player does not have enough money");
		}

		if (PhysicalCurrencyAPI.getInventoryManager().withdraw(player.getPlayer().getInventory(), amount)) {
			return new EconomyResponse(amount, getBalance(player), ResponseType.SUCCESS, "");
		} else {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Could not withdraw money");
		}
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
		return withdrawPlayer(player, amount);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
		if (amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cant deposit a negative amount");
		}
		
		if (!player.isOnline()) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cant deposit to a player that is not online");
		}

		if(PhysicalCurrencyAPI.getInventoryManager().canDeposit(player.getPlayer().getInventory(), amount, PhysicalCurrencyAPI.getPlayerDataManager().getPlayerData(player.getUniqueId()).getPrimaryCurrency())) {
			if(PhysicalCurrencyAPI.getInventoryManager().deposit(player.getPlayer().getInventory(), amount, PhysicalCurrencyAPI.getPlayerDataManager().getPlayerData(player.getUniqueId()).getPrimaryCurrency())) {
				return new EconomyResponse(amount, getBalance(player), ResponseType.SUCCESS, "");
			}
		}
		return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cant deposit that amount to the player");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
		return depositPlayer(player, amount);
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
		return createPlayerAccount(player);
	}

	@Override
	public EconomyResponse createBank(String name, String player) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public EconomyResponse createBank(String name, OfflinePlayer player) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public EconomyResponse bankBalance(String name) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public EconomyResponse bankHas(String name, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public EconomyResponse isBankOwner(String name, String playerName) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public EconomyResponse isBankMember(String name, String playerName) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public EconomyResponse isBankMember(String name, OfflinePlayer player) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "PhysicalCurrencies does not support banks");
	}

	@Override
	public List<String> getBanks() {
		// This plugin does not support banks
		return new ArrayList<String>();
	}

	// Names are not supported

	@Override
	@Deprecated
	public boolean createPlayerAccount(String playerName) {
		return false;
	}

	@Override
	@Deprecated
	public boolean createPlayerAccount(String playerName, String worldName) {
		return false;
	}

	@Override
	@Deprecated
	public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Player names are not supported");
	}

	@Override
	@Deprecated
	public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Player names are not supported");
	}

	@Override
	@Deprecated
	public EconomyResponse depositPlayer(String playerName, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Player names are not supported");
	}

	@Override
	@Deprecated
	public EconomyResponse withdrawPlayer(String playerName, double amount) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Player names are not supported");
	}

	@Override
	@Deprecated
	public boolean has(String playerName, String worldName, double amount) {
		return false;
	}

	@Override
	@Deprecated
	public boolean has(String playerName, double amount) {
		return false;
	}

	@Override
	@Deprecated
	public double getBalance(String playerName, String world) {
		return 0;
	}

	@Override
	@Deprecated
	public double getBalance(String playerName) {
		return 0;
	}

	@Override
	@Deprecated
	public boolean hasAccount(String playerName, String worldName) {
		return false;
	}

	@Override
	@Deprecated
	public boolean hasAccount(String playerName) {
		return false;
	}
}