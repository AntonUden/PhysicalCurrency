package net.zeeraa.physicalcurency.currency.item;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import net.zeeraa.physicalcurency.PhysicalCurrency;
import net.zeeraa.physicalcurrency.api.currency.Currency;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItem;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItemManager;

public class DefaultCurrencyItemManager extends CurrencyItemManager {
	private Map<Currency, List<CurrencyItem>> currencyItems;

	public DefaultCurrencyItemManager() {
		currencyItems = new HashMap<Currency, List<CurrencyItem>>();
	}

	@Override
	public Map<Currency, List<CurrencyItem>> getCurrencyItems() {
		return currencyItems;
	}

	@Override
	public boolean loadCurrencyItems() {
		return false;
	}

	@Override
	public boolean saveCurrencyItems() {
		try {
			JSONArray result = new JSONArray();

			for (List<CurrencyItem> currencyList : currencyItems.values()) {
				for (CurrencyItem currencyItem : currencyList) {
					result.put(currencyItem.toJson());
				}
			}

			FileUtils.write(PhysicalCurrency.getInstance().getCurrencyItemsFile(), result.toString(4), Charset.defaultCharset(), false);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}