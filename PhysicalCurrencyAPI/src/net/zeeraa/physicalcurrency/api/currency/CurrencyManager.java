package net.zeeraa.physicalcurrency.api.currency;

import java.util.Map;

import javax.annotation.Nullable;


public abstract class CurrencyManager {
	/**
	 * Get a map containing all currencies loaded
	 * <p>
	 * The key is the {@link Currency#getName()} in lower case
	 * 
	 * @return Map with {@link Currency}
	 */
	public abstract Map<String, Currency> getCurrencies();

	/**
	 * Get a {@link Currency} by its name
	 * 
	 * @param name The name of the currency
	 * @return Instance of the {@link Currency} or <code>null</code> if it was not
	 *         found
	 */
	@Nullable
	public Currency getCurrency(String name) {
		return getCurrencies().get(name.toLowerCase());
	}

	/**
	 * Check if a {@link Currency} exist by its name
	 * 
	 * @param name The name of the currency
	 * @return <code>true</code> it the currency exist
	 */
	public boolean currencyExist(String name) {
		return getCurrency(name) != null;
	}

	/**
	 * Load all the currencies
	 * <p>
	 * If there is already currencies loaded it will reload the data and remove
	 * currencies that no longer exists
	 * 
	 * @return <code>false</code> on failure
	 */
	public abstract boolean loadCurrencies();

	/**
	 * Save any modifications to the loaded currencies
	 * <p>
	 * Any changes in currencies.yml made after loading the currencies will be
	 * overwritten
	 * 
	 * @return <code>false</code> on failure
	 */
	public abstract boolean saveCurrencies();
}
