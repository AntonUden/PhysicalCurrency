package net.zeeraa.physicalcurrency.api.currency.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.currency.Currency;

public class CurrencyItem {
	private Material material;
	private Currency currency;

	private List<String> lore;

	private String name;
	private double currencyAmount;
	private int customModelData;

	public CurrencyItem(Material material, Currency currency, List<String> lore, String name, double currencyAmount, int customModelData) {
		this.material = material;
		this.currency = currency;

		this.lore = lore;

		this.name = name;
		this.currencyAmount = currencyAmount;
		this.customModelData = customModelData;
	}

	public ItemStack getItem() {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		meta.setLocalizedName(name);
		meta.setCustomModelData(customModelData);
		meta.setLore(lore);

		meta.getPersistentDataContainer().set(CurrencyItemTags.ITEM_CURRENCY_TYPE.getNamespacedKey(), PersistentDataType.STRING, currency.getName());
		meta.getPersistentDataContainer().set(CurrencyItemTags.ITEM_CURRENCY_AMOUNT.getNamespacedKey(), PersistentDataType.DOUBLE, currencyAmount);

		item.setItemMeta(meta);

		return item;
	}

	public Material getMaterial() {
		return material;
	}

	public Currency getCurrency() {
		return currency;
	}

	public double getCurrencyAmount() {
		return currencyAmount;
	}

	public List<String> getLore() {
		return lore;
	}

	public String getName() {
		return name;
	}

	public int getCustomModelData() {
		return customModelData;
	}
}