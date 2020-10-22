package net.zeeraa.physicalcurrency.physicalcurrency.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.balance.Balance;
import net.zeeraa.physicalcurrency.api.currency.Currency;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItem;
import net.zeeraa.physicalcurrency.api.inventory.CurrencyItemStack;
import net.zeeraa.physicalcurrency.api.inventory.InventoryManager;

public class DefaultInventoryManager extends InventoryManager {
	@Override
	public List<CurrencyItemStack> getCurrencyItemStacks(Inventory inventory, Currency currency) {
		List<CurrencyItemStack> result = new ArrayList<CurrencyItemStack>();

		for (int i = 0; i < inventory.getSize(); i++) {
			ItemStack item = inventory.getItem(i);

			if (item == null) {
				continue;
			}

			// System.out.println(item);
			// System.out.println("PhysicalCurrencyAPI.getCurrencyItemManager().isCurrencyItem(item):
			// " + PhysicalCurrencyAPI.getCurrencyItemManager().isCurrencyItem(item));

			if (PhysicalCurrencyAPI.getCurrencyItemManager().isCurrencyItem(item)) {
				// System.out.println(item.toString() + " is a currency item");

				CurrencyItem ci = PhysicalCurrencyAPI.getCurrencyItemManager().getCurrencyItem(item);

				if (ci != null) {
					if (currency != null) {
						if (!currency.equals(ci.getCurrency())) {
							continue;
						}
					}

					result.add(new CurrencyItemStack(ci, item));
				}
			}
		}

		return result;
	}

	@Override
	public List<Balance> getBalance(Inventory inventory, Currency currency) {
		Map<Currency, Double> amount = new HashMap<Currency, Double>();

		List<CurrencyItemStack> items = getCurrencyItemStacks(inventory, currency);

		for (CurrencyItemStack item : items) {
			double stackValue = item.getCurrencyItem().getCurrencyAmount() * ((double) item.getItemStack().getAmount());

			if (!amount.containsKey(item.getCurrencyItem().getCurrency())) {

				amount.put(item.getCurrencyItem().getCurrency(), stackValue);
			} else {
				amount.put(item.getCurrencyItem().getCurrency(), amount.get(item.getCurrencyItem().getCurrency()) + stackValue);
			}
		}

		List<Balance> result = new ArrayList<Balance>();

		for (Currency c : amount.keySet()) {
			result.add(new Balance(c, amount.get(c)));
		}

		return result;
	}

	@Override
	public boolean canWithdraw(Inventory inventory, double balance, Currency currency) {
		List<Balance> pBalance = this.getBalance(inventory, currency);
		return Balance.getTotalVaultValue(pBalance) >= balance;
	}

	@Override
	public boolean withdraw(Inventory inventory, double balance, Currency currency) {
		System.out.println("DefaultInventoryManager.withdraw()");
		List<CurrencyItemStack> stacks = this.getCurrencyItemStacks(inventory, currency);
		System.out.println("stacks " + stacks.size());
		double balanceLeft = balance;
		double balanceWithdrawn = 0;

		Collections.sort(stacks, Comparator.comparingDouble(CurrencyItemStack::getCurrencyVaultValue).reversed());

		for (int i = 0; i < stacks.size(); i++) {
			System.out.println("Scanning stack: " + i);
			CurrencyItemStack cis = stacks.get(i);
			int newStackSize = cis.getItemStack().getAmount();

			System.out.println("Size: " + cis.getItemStack().getAmount());

			while (newStackSize > 0 && balanceLeft > 0) {
				if (balanceLeft < cis.getCurrencyVaultValue()) {
					break;
				}

				newStackSize--;
				balanceLeft -= cis.getCurrencyVaultValue();

				balanceWithdrawn += cis.getCurrencyVaultValue();
			}

			if (newStackSize > 0) {
				cis.getItemStack().setAmount(newStackSize);
				System.out.println("Setting size to " + newStackSize);
			} else {
				inventory.removeItem(cis.getItemStack());
				System.out.println("Removing the stack");
			}

			System.out.println("balanceLeft: " + balanceLeft);
			System.out.println("i == stacks.size() - 1: " + (i == stacks.size() - 1));
			
			if (balanceLeft > 0 && (i == stacks.size() - 1)) {
				// if a player has a 5 dollar item and 4 dollar is withdrawn withdraw the 5
				// dollar item instead
				int stackSize = cis.getItemStack().getAmount();

				if (stackSize > 0) {
					cis.getItemStack().setAmount(stackSize - 1);
				} else {
					inventory.remove(cis.getItemStack());
				}

				balanceLeft -= cis.getCurrencyVaultValue();
				balanceWithdrawn += cis.getCurrencyVaultValue();
			}
		}

		System.out.println("At final stage");
		System.out.println("Withdraw balance " + balance + " resulted in a total of " + balanceWithdrawn + " withdrawn");
		System.out.println("Final balanceLeft: " + balanceLeft);
		if (PhysicalCurrencyAPI.isDebugMode()) {
			PhysicalCurrencyAPI.getPlugin().getLogger().info("Withdraw balance " + balance + " resulted in a total of " + balanceWithdrawn + " withdrawn");
		}

		return balanceLeft <= 0;
	}

	@Override
	public boolean canDeposit(Inventory inventory, double balance, Currency currency) {
		List<Balance> pBalance = this.getBalance(inventory, currency);
		return false;
	}

	@Override
	public boolean deposit(Inventory inventory, double balance, Currency currency) {
		List<Balance> pBalance = this.getBalance(inventory, currency);
		return false;
	}
}