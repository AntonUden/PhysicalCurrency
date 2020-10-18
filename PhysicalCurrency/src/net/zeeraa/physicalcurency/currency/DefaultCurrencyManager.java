package net.zeeraa.physicalcurency.currency;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import net.zeeraa.physicalcurency.PhysicalCurrency;
import net.zeeraa.physicalcurrency.api.currency.Currency;
import net.zeeraa.physicalcurrency.api.currency.CurrencyManager;

public class DefaultCurrencyManager extends CurrencyManager {
	private Map<String, Currency> currencies;

	public DefaultCurrencyManager() {
		currencies = new HashMap<>();
	}

	@Override
	public Map<String, Currency> getCurrencies() {
		return currencies;
	}

	@Override
	public boolean loadCurrencies() {
		try {
			JSONArray jsonCurrencies = new JSONArray(FileUtils.readFileToString(PhysicalCurrency.getInstance().getCurrenciesFile(), Charset.defaultCharset()));

			Map<String, Currency> newCurrenciesMap = new HashMap<String, Currency>();

			for (int i = 0; i < jsonCurrencies.length(); i++) {
				JSONObject jsonCurrency = jsonCurrencies.getJSONObject(i);

				Currency currencyToAdd;

				if (getCurrency(jsonCurrency.getString("name")) != null) {
					currencyToAdd = getCurrency(jsonCurrency.getString("name"));

					currencyToAdd.setDisplayNamePlural(jsonCurrency.getString("display_name_plural"));
					currencyToAdd.setDisplayNameSingular(jsonCurrency.getString("display_name_singular"));
					currencyToAdd.setVaultValue(jsonCurrency.getDouble("vault_value"));
				} else {
					currencyToAdd = Currency.fromJson(jsonCurrency);
				}

				if (PhysicalCurrency.getInstance().isDebugMode()) {
					System.out.println("---------------------------------");
					System.out.println(currencyToAdd);
					System.out.println(currencyToAdd.getDebugData());
				}

				newCurrenciesMap.put(currencyToAdd.getName().toLowerCase(), currencyToAdd);
			}

			currencies = newCurrenciesMap;

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveCurrencies() {
		try {
			JSONArray jsonResult = new JSONArray();

			for (Currency currency : currencies.values()) {
				jsonResult.put(currency.toJson());
			}

			FileUtils.write(PhysicalCurrency.getInstance().getCurrenciesFile(), jsonResult.toString(4), Charset.defaultCharset(), false);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}