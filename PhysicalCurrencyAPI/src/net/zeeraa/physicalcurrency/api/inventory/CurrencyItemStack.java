package net.zeeraa.physicalcurrency.api.inventory;

import org.bukkit.inventory.ItemStack;

import net.zeeraa.physicalcurrency.api.currency.Currency;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItem;

public class CurrencyItemStack {
	private CurrencyItem currencyItem;
	private ItemStack itemStack;

	public CurrencyItemStack(CurrencyItem currencyItem, ItemStack itemStack) {
		this.currencyItem = currencyItem;
		this.itemStack = itemStack;
	}

	public CurrencyItem getCurrencyItem() {
		return currencyItem;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public Currency getCurrency() {
		return currencyItem.getCurrency();
	}
	
	public double getCurrencyVaultValue() {
		return currencyItem.getCurrency().getVaultValue();
	}
}