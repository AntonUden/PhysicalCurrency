package net.zeeraa.physicalcurrency.currency;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import net.zeeraa.physicalcurrency.PhysicalCurrency;
import net.zeeraa.physicalcurrency.api.currency.Currency;
import net.zeeraa.physicalcurrency.api.currency.CurrencyManager;

public class DefaultCurrencyManager extends CurrencyManager {
	private Map<String, Currency> currencies;

	private Currency defaultCurrency;

	public DefaultCurrencyManager() {
		currencies = new HashMap<>();
		defaultCurrency = null;
	}

	@Override
	public Map<String, Currency> getCurrencies() {
		return currencies;
	}

	@Override
	public boolean loadCurrencies() {
		try {
			JSONObject json = new JSONObject(FileUtils.readFileToString(PhysicalCurrency.getInstance().getCurrenciesFile(), Charset.defaultCharset()));

			JSONArray jsonCurrencies = json.getJSONArray("currencies");

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

			String defaultCurrencyName = json.getString("default_currency");
			Currency newDefaultCurrency = getCurrency(defaultCurrencyName);

			if (newDefaultCurrency == null) {
				if (currencies.size() == 0) {
					PhysicalCurrency.getInstance().getLogger().warning("Default currency " + defaultCurrency + " is not loaded");
					PhysicalCurrency.getInstance().getLogger().warning("No other currencies configured. Could not set default currency");
					return false;
				} else {
					// There should be a better way of doing this but this one should work without
					// throwing any errors
					for (Currency currency : currencies.values()) {
						defaultCurrency = currency;
						PhysicalCurrency.getInstance().getLogger().warning("Using " + currency.getName() + " as the default currency since the configured default " + defaultCurrencyName + " was not found");
						break;
					}
				}
			} else {
				defaultCurrency = newDefaultCurrency;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveCurrencies() {
		try {
			JSONObject jsonData = new JSONObject();
			JSONArray currenciesArray = new JSONArray();

			jsonData.put("default-currency", defaultCurrency.getName());

			for (Currency currency : currencies.values()) {
				currenciesArray.put(currency.toJson());
			}

			jsonData.put("currencies", currenciesArray);

			FileUtils.write(PhysicalCurrency.getInstance().getCurrenciesFile(), jsonData.toString(4), Charset.defaultCharset(), false);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Currency getDefaultCurrency() {
		return defaultCurrency;
	}
}