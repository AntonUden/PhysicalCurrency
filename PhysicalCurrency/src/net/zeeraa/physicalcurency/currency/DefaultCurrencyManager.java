package net.zeeraa.physicalcurency.currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

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
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(PhysicalCurrency.getInstance().getCurrenciesFile());

		if (!cfg.contains("primary-currency") || !cfg.contains("currencies")) {
			PhysicalCurrency.getInstance().getLogger().severe("currencies.yml does not contain a valid currency configuration");
			return false;
		}

		ConfigurationSection cfgCurrencies = cfg.getConfigurationSection("currencies");

		List<Currency> readCurrencies = new ArrayList<Currency>();

		for (String cName : cfgCurrencies.getKeys(false)) {
			ConfigurationSection cfgCurrency = cfgCurrencies.getConfigurationSection(cName);

			String displayNameSingular = cfgCurrency.getString("display-name-singular");
			String displayNamePlural = cfgCurrency.getString("display-name-plural");

			double vaultValue = cfgCurrency.getDouble("vault-value");

			Currency currency = new Currency(cName, displayNameSingular, displayNamePlural, vaultValue);

			readCurrencies.add(currency);
		}

		Map<String, Currency> newCurrenciesMap = new HashMap<String, Currency>();

		if (PhysicalCurrency.getInstance().isDebugMode()) {
			System.out.println("Currency map: " + currencies);
		}

		for (Currency currency : readCurrencies) {
			if (PhysicalCurrency.getInstance().isDebugMode()) {
				System.out.println("---------------------------------");
				System.out.println(currency);
				System.out.println(currency.getDebugData());
			}

			Currency currencyToAdd;

			if (currencies.containsKey(currency.getName().toLowerCase())) {
				// Update existing
				currencyToAdd = currencies.get(currency.getName().toLowerCase());

				currencyToAdd.setDisplayNamePlural(currency.getDisplayNamePlural());
				currencyToAdd.setDisplayNameSingular(currency.getDisplayNameSingular());

				currencyToAdd.setVaultValue(currency.getVaultValue());
			} else {
				// Create new
				currencyToAdd = currency;
			}

			newCurrenciesMap.put(currency.getName().toLowerCase(), currencyToAdd);

			currencies = newCurrenciesMap;
		}

		return true;
	}

	@Override
	public boolean saveCurrencies() {
		// TODO:
		return false;
	}
}