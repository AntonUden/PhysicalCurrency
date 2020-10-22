package net.zeeraa.physicalcurrency.api.currency.item;

import org.bukkit.NamespacedKey;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;

public enum CurrencyItemTags {
	ITEM_CURRENCY_TYPE("currency-type"), CURRENCY_ITEM_NAME("currency-item-name");

	private String key;

	private CurrencyItemTags(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public NamespacedKey getNamespacedKey() {
		return new NamespacedKey(PhysicalCurrencyAPI.getPlugin(), key);
	}
}