package net.zeeraa.physicalcurrency.api.inventory;

import java.util.List;

import org.bukkit.inventory.Inventory;

import net.zeeraa.physicalcurrency.api.balance.Balance;
import net.zeeraa.physicalcurrency.api.currency.Currency;

public abstract class InventoryManager {
	public List<Balance> getBalance(Inventory inventory) {
		return this.getBalance(inventory, null);
	}

	public abstract List<Balance> getBalance(Inventory inventory, Currency currency);

	public List<CurrencyItemStack> getCurrencyItemStacks(Inventory inventory) {
		return this.getCurrencyItemStacks(inventory, null);
	}

	public abstract List<CurrencyItemStack> getCurrencyItemStacks(Inventory inventory, Currency currency);

	public boolean canWithdraw(Inventory inventory, double balance) {
		return this.canWithdraw(inventory, balance, null);
	}

	public abstract boolean canWithdraw(Inventory inventory, double balance, Currency currency);

	public boolean withdraw(Inventory inventory, double balance) {
		return this.withdraw(inventory, balance, null);
	}

	public abstract boolean withdraw(Inventory inventory, double balance, Currency currency);

	public abstract boolean canDeposit(Inventory inventory, double balance, Currency currency);

	public abstract boolean deposit(Inventory inventory, double balance, Currency currency);
}