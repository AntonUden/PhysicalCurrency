package net.zeeraa.physicalcurrency.api.balance;

import java.util.ArrayList;
import java.util.List;

import net.zeeraa.physicalcurrency.api.currency.Currency;

public class Balance {
	private Currency currency;
	private double amount;

	public Balance(Currency currency, double amount) {
		this.currency = currency;
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public double getAmount() {
		return amount;
	}

	/**
	 * Set the amount
	 * <p>
	 * This wont modify the players balance and is only used while trying to sum up
	 * the balance
	 * 
	 * @param amount The new amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getVaultVaule() {
		return currency.getVaultValue() * amount;
	}

	public static double getTotalVaultValue(Balance... balance) {
		List<Balance> bt = new ArrayList<Balance>();

		for (Balance b : balance) {
			bt.add(b);
		}

		return Balance.getTotalVaultValue(bt);
	}

	public static double getTotalVaultValue(List<Balance> balance) {
		double amount = 0;

		for (Balance b : balance) {
			amount += b.getVaultVaule();
		}

		return amount;
	}
}