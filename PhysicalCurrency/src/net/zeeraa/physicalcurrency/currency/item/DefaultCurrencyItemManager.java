package net.zeeraa.physicalcurrency.currency.item;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import net.zeeraa.physicalcurrency.PhysicalCurrency;
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
		JSONArray jsonArray;
		Map<Currency, List<CurrencyItem>> newCurrencyItems = new HashMap<Currency, List<CurrencyItem>>();

		try {
			jsonArray = new JSONArray(FileUtils.readFileToString(PhysicalCurrency.getInstance().getCurrencyItemsFile(), Charset.defaultCharset()));

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);

				CurrencyItem currencyItem = CurrencyItem.fromJson(json);

				if (!newCurrencyItems.containsKey(currencyItem.getCurrency())) {
					newCurrencyItems.put(currencyItem.getCurrency(), new ArrayList<CurrencyItem>());
				}

				if (currencyItems.containsKey(currencyItem.getCurrency())) {
					CurrencyItem oldCurrencyItem = getCurrencyItem(currencyItem.getCurrency(), currencyItem.getName());

					if (oldCurrencyItem != null) {
						oldCurrencyItem.updateCurrencyItem(currencyItem);

						// keep the old one with the updated data
						currencyItem = oldCurrencyItem;
					}
				}
				newCurrencyItems.get(currencyItem.getCurrency()).add(currencyItem);
			}

			currencyItems = newCurrencyItems;

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

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