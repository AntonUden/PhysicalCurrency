package net.zeeraa.physicalcurrency.api.currency;

import org.json.JSONObject;

public class Currency {
	private String name;
	private String displayNameSingular;
	private String displayNamePlural;

	private double vaultValue;

	public Currency(String name, String displayNameSingular, String displayNamePlural, double vaultValue) {
		this.name = name;
		this.displayNameSingular = displayNameSingular;
		this.displayNamePlural = displayNamePlural;
		this.vaultValue = vaultValue;
	}

	public static Currency fromJson(JSONObject json) {
		String name = json.getString("name");
		String displayNameSingular = json.getString("display_name_singular");
		String displayNamePlural = json.getString("display_name_plural");

		double vaultValue = json.getDouble("vault_value");

		return new Currency(name, displayNameSingular, displayNamePlural, vaultValue);
	}

	public static JSONObject toJson(Currency currency) {
		JSONObject result = new JSONObject();

		result.put("name", currency.getName());
		result.put("display_name_singular", currency.getDisplayNameSingular());
		result.put("display_name_plural", currency.getDisplayNamePlural());
		result.put("vault_value", currency.getVaultValue());

		return result;
	}

	public JSONObject toJson() {
		return Currency.toJson(this);
	}

	/**
	 * Get the internal name for the currency
	 * 
	 * @return The internal currency name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the display name of the currency in singular
	 * 
	 * @return The currency display name in singular
	 */
	public String getDisplayNameSingular() {
		return displayNameSingular;
	}

	/**
	 * Set the new singular display name of the currency
	 * 
	 * @param displayNameSingular The new singular display name
	 */
	public void setDisplayNameSingular(String displayNameSingular) {
		this.displayNameSingular = displayNameSingular;
	}

	/**
	 * Get the display name of the currency in plural
	 * 
	 * @return The currency display name in plural
	 */
	public String getDisplayNamePlural() {
		return displayNamePlural;
	}

	/**
	 * Set the new plural display name of the currency
	 * 
	 * @param displayNamePlural The new plural display name
	 */
	public void setDisplayNamePlural(String displayNamePlural) {
		this.displayNamePlural = displayNamePlural;
	}

	/**
	 * Get the display name for a certain amount of the currency
	 * 
	 * @param amount The amount
	 * @return the singular or plural display name depending on the amount
	 */
	public String getDisplayName(double amount) {
		return amount == 1 ? getDisplayNameSingular() : getDisplayNamePlural();
	}

	/**
	 * Get the vault value of the currency
	 * 
	 * @return vault value
	 */
	public double getVaultValue() {
		return vaultValue;
	}

	/**
	 * Set the vault value of the currency
	 * 
	 * @param vaultValue The new vault value
	 */
	public void setVaultValue(double vaultValue) {
		this.vaultValue = vaultValue;
	}

	/**
	 * Check if this {@link Currency} is equal to another {@link Currency} or string
	 * <p>
	 * If a string is passed this will compare {@link Currency#getName()} and The
	 * string using {@link String#equalsIgnoreCase(String)}
	 * 
	 * @param obj An instance of {@link Currency} or a string to compare with
	 * 
	 * @return True if this {@link Currency} and the object is matching
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Currency) {
				return this.getName().equalsIgnoreCase(((Currency) obj).getName()) && this.getDisplayNameSingular().equalsIgnoreCase(((Currency) obj).getDisplayNameSingular()) && this.getDisplayNamePlural().equalsIgnoreCase(((Currency) obj).getDisplayNamePlural());
			}

			if (obj instanceof String) {
				return this.getName().equalsIgnoreCase((String) obj);
			}
		}

		return false;
	}

	/**
	 * Get a string with info used to debug currencies
	 * 
	 * @return String with debug info
	 */
	public String getDebugData() {
		return "name: " + name + " | displayNameSingular: " + displayNameSingular + " | displayNamePlural: " + displayNamePlural + " |  vaultValue: " + vaultValue;
	}
}