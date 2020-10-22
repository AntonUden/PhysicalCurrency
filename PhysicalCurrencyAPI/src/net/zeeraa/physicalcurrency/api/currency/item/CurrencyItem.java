package net.zeeraa.physicalcurrency.api.currency.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.json.JSONArray;
import org.json.JSONObject;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.currency.Currency;

public class CurrencyItem {
	private Material material;
	private Currency currency;

	private List<String> lore;

	private String name;
	private String displayName;
	private double currencyAmount;
	private int customModelData;

	public CurrencyItem(Material material, Currency currency, List<String> lore, String name, String displayName, double currencyAmount, int customModelData) {
		this.material = material;
		this.currency = currency;

		this.lore = lore;

		this.name = name;
		this.displayName = displayName;
		this.currencyAmount = currencyAmount;
		this.customModelData = customModelData;
	}

	public static CurrencyItem fromJson(JSONObject json) {
		String name = json.getString("name");
		String displayName = json.getString("display_name");
		String materialName = json.getString("material");
		Material material;
		try {
			material = Material.valueOf(materialName);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			PhysicalCurrencyAPI.getPlugin().getLogger().severe("Failed to read currency item " + name + ". " + materialName + " is not a valid material");
			throw e;
		}

		List<String> lore = new ArrayList<String>();

		JSONArray loreArray = json.getJSONArray("lore");

		for (int i = 0; i < loreArray.length(); i++) {
			lore.add(loreArray.getString(i));
		}

		String currencyName = json.getString("currency");

		Currency currency = PhysicalCurrencyAPI.getCurrencyManager().getCurrency(currencyName);

		if (currency == null) {
			PhysicalCurrencyAPI.getPlugin().getLogger().severe("Failed to read currency item " + name + ". The currency " + currencyName + " is not loaded");
			throw new NullPointerException();
		}

		double currencyAmount = json.getDouble("currency_amount");
		int customModelData = json.getInt("custom_model_data");

		return new CurrencyItem(material, currency, lore, name, displayName, currencyAmount, customModelData);
	}

	public static JSONObject toJson(CurrencyItem currencyItem) {
		JSONObject result = new JSONObject();

		JSONArray lore = new JSONArray();

		for (String s : currencyItem.getLore()) {
			lore.put(s);
		}

		result.put("name", currencyItem.getName());
		result.put("display_name", currencyItem.getDisplayName());
		result.put("material", currencyItem.getMaterial().name());
		result.put("lore", lore);
		result.put("currency", currencyItem.getCurrency().getName());
		result.put("currency_amount", currencyItem.getCurrencyAmount());
		result.put("custom_model_data", currencyItem.getCustomModelData());

		return result;
	}

	public void updateCurrencyItem(CurrencyItem newData) {
		this.setCurrencyAmount(newData.getCurrencyAmount());
		this.setCustomModelData(newData.getCustomModelData());
		this.setDisplayName(newData.getDisplayName());
		this.setLore(newData.getLore());
		this.setMaterial(newData.getMaterial());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CurrencyItem) {
			if (((CurrencyItem) obj).getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public JSONObject toJson() {
		return CurrencyItem.toJson(this);
	}

	public ItemStack getItem() {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(getDisplayName());
		meta.setLocalizedName(getDisplayName());
		meta.setCustomModelData(getCustomModelData());
		meta.setLore(getLore());

		meta.getPersistentDataContainer().set(CurrencyItemTags.ITEM_CURRENCY_TYPE.getNamespacedKey(), PersistentDataType.STRING, currency.getName());
		meta.getPersistentDataContainer().set(CurrencyItemTags.ITEM_CURRENCY_AMOUNT.getNamespacedKey(), PersistentDataType.DOUBLE, getCurrencyAmount());

		item.setItemMeta(meta);

		return item;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Currency getCurrency() {
		return currency;
	}

	public double getCurrencyAmount() {
		return currencyAmount;
	}

	public void setCurrencyAmount(double currencyAmount) {
		this.currencyAmount = currencyAmount;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getCustomModelData() {
		return customModelData;
	}

	public void setCustomModelData(int customModelData) {
		this.customModelData = customModelData;
	}
}