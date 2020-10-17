package net.zeeraa.physicalcurrency.api.currency.item;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.currency.Currency;

public abstract class CurrencyItemManager {
	public abstract Map<Currency, List<CurrencyItem>> getCurrencyItems();

	/**
	 * Load all the currency items
	 * <p>
	 * If there is already currencies loaded it will reload the data and remove
	 * currency items that no longer exists
	 * 
	 * @return <code>false</code> on failure
	 */
	public abstract boolean loadCurrencyItems();

	/**
	 * Save any modifications to the loaded currency items
	 * <p>
	 * Any changes in currency-items.yml made after loading the currencies will be
	 * overwritten
	 * 
	 * @return <code>false</code> on failure
	 */
	public abstract boolean saveCurrencyItems();
	
	@Nullable
	public List<CurrencyItem> getCurrencyItems(Currency currency) {
		if(getCurrencyItems().containsKey(currency)) {
			return getCurrencyItems().get(currency);
		}
		return null;
	}
	
	public boolean isCurrencyItem(ItemStack item) {
		return isCurrencyItem(item);
	}
	
	public boolean isCurrencyItem(ItemMeta meta) {
		return meta.getPersistentDataContainer().has(CurrencyItemTags.ITEM_CURRENCY_AMOUNT.getNamespacedKey(), PersistentDataType.STRING);
	}
	
	public CurrencyItem getCurrencyItem(ItemStack item) {
		return getCurrencyItem(item.getItemMeta());
	}
	
	public CurrencyItem getCurrencyItem(ItemMeta meta) {
		if(isCurrencyItem(meta)) {
			Currency currency = PhysicalCurrencyAPI.getCurrencyManager().getCurrency(meta.getPersistentDataContainer().get(CurrencyItemTags.ITEM_CURRENCY_TYPE.getNamespacedKey(), PersistentDataType.STRING));
			
			if(currency != null) {
				double value = meta.getPersistentDataContainer().get(CurrencyItemTags.ITEM_CURRENCY_AMOUNT.getNamespacedKey(), PersistentDataType.DOUBLE);
				
				List<CurrencyItem> currencyItems = getCurrencyItems(currency);
				
				if(currency != null) {
					for(CurrencyItem ci : currencyItems) {
						if(ci.getCurrencyAmount() == value) {
							return ci;
						}
					}
				}
			}
		}
		return null;
	}
}