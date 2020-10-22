package net.zeeraa.physicalcurrency.physicalcurrency.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.balance.Balance;
import net.zeeraa.physicalcurrency.api.currency.Currency;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItem;
import net.zeeraa.physicalcurrency.api.inventory.CurrencyItemStack;
import net.zeeraa.physicalcurrency.api.inventory.InventoryManager;

public class DefaultInventoryManager extends InventoryManager {
	public static final int[] IGNORED_PLAYER_SLOTS = { 36, 37, 38, 39, 40 };

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

					result.add(new CurrencyItemStack(ci, item, inventory));
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
		List<CurrencyItemStack> stacks = this.getCurrencyItemStacks(inventory, currency);

		double balanceLeft = balance;
		double balanceWithdrawn = 0;

		Collections.sort(stacks, Comparator.comparingDouble(CurrencyItemStack::getSingleItemVaultValue).reversed());

		for (int i = 0; i < stacks.size(); i++) {
			CurrencyItemStack cis = stacks.get(i);
			int newStackSize = cis.getItemStack().getAmount();

			while (newStackSize > 0 && balanceLeft > 0) {
				if (balanceLeft < cis.getSingleItemVaultValue()) {
					break;
				}

				// System.out.println("stack size: " + newStackSize);
				// System.out.println(cis.getCurrencyItem().getName() + " " +
				// cis.getSingleItemVaultValue() + " " + cis.getCurrency().getName());

				newStackSize--;

				balanceLeft -= cis.getSingleItemVaultValue();

				balanceWithdrawn += cis.getSingleItemVaultValue();

				// System.out.println("newStackSize: " + newStackSize + " balanceLeft: " +
				// balanceLeft + " balanceWithdrawn: " + balanceWithdrawn);
			}

			if (newStackSize > 0) {
				cis.getItemStack().setAmount(newStackSize);
			} else {
				inventory.removeItem(cis.getItemStack());
			}

			if (balanceLeft > 0 && (i == stacks.size() - 1)) {
				int stackSize = cis.getItemStack().getAmount();

				if (stackSize > 0) {
					cis.getItemStack().setAmount(stackSize - 1);
				} else {
					cis.getInventory().remove(cis.getItemStack());
				}

				balanceLeft -= cis.getSingleItemVaultValue();
				balanceWithdrawn += cis.getSingleItemVaultValue();
			}
		}

		if (PhysicalCurrencyAPI.isDebugMode()) {
			PhysicalCurrencyAPI.getPlugin().getLogger().info("Withdraw balance " + balance + " resulted in a total of " + balanceWithdrawn + " withdrawn");
		}

		return balanceLeft <= 0;
	}

	private Map<CurrencyItem, Integer> getAmountToAdd(double balance, Currency currency) {
		List<CurrencyItem> playerCurrencyItems = PhysicalCurrencyAPI.getCurrencyItemManager().getCurrencyItems(currency);
		Map<CurrencyItem, Integer> amountToAdd = new HashMap<CurrencyItem, Integer>();

		Collections.sort(playerCurrencyItems, Comparator.comparingDouble(CurrencyItem::getVaultValue).reversed());

		double amountLeft = balance;

		for (CurrencyItem ci : playerCurrencyItems) {
			double vaultValue = ci.getVaultValue();

			int failsafeBecauseMyCodeIsBad = 1000000;

			while (amountLeft >= vaultValue) {
				if (amountToAdd.containsKey(ci)) {
					amountToAdd.put(ci, amountToAdd.get(ci) + 1);
				} else {
					amountToAdd.put(ci, (int) 1);
				}
				amountLeft -= vaultValue;

				failsafeBecauseMyCodeIsBad--;
				if (failsafeBecauseMyCodeIsBad <= 0) {
					PhysicalCurrencyAPI.getPlugin().getLogger().severe("Got stuck in while loop during deposit or deposit check. Check you config and validate that the vault value of your currency is not lower than 1");
					return null;
				}
			}
		}
		return amountToAdd;
	}

	private boolean canAddAmount(Inventory inventory, Map<CurrencyItem, Integer> amount) {
		int avaliableSlots = 0;

		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) == null) {
				// System.out.println("Slot: " + i + " is empty");
				boolean shouldCountSlot = true;

				if (inventory instanceof PlayerInventory) {
					for (int j = 0; j < IGNORED_PLAYER_SLOTS.length; j++) {
						// System.out.println("IGNORED_PLAYER_SLOTS[j] == i : " +
						// (IGNORED_PLAYER_SLOTS[j] == i) + " j: " + j + " i: " + i + "
						// IGNORED_PLAYER_SLOTS[j]: " + IGNORED_PLAYER_SLOTS[j]);
						if (IGNORED_PLAYER_SLOTS[j] == i) {
							shouldCountSlot = false;
							continue;
						}
					}
				}

				if (shouldCountSlot) {
					avaliableSlots++;
				}
			}
		}

		// System.out.println("Initial avaliableSlots: " + avaliableSlots);

		for (CurrencyItem ci : amount.keySet()) {
			double itemCount = amount.get(ci);

			int stacks = (int) Math.ceil(((double) itemCount) / ((double) ci.getMaterial().getMaxStackSize()));

			// System.out.println("raw stack size: " + ((double) itemCount) / ((double)
			// ci.getMaterial().getMaxStackSize()));
			// System.out.println("stacks: " + stacks + " for " + ci.getName());

			avaliableSlots -= stacks;
		}

		// System.out.println("Final avaliableSlots: " + avaliableSlots);

		return avaliableSlots >= 0;
	}

	@Override
	public boolean canDeposit(Inventory inventory, double balance, Currency currency) {
		return canAddAmount(inventory, getAmountToAdd(balance, currency));
	}

	@Override
	public boolean deposit(Inventory inventory, double balance, Currency currency) {
		Map<CurrencyItem, Integer> amountToAdd = getAmountToAdd(balance, currency);

		if (canAddAmount(inventory, amountToAdd)) {
			for (CurrencyItem ci : amountToAdd.keySet()) {
				int itemCount = amountToAdd.get(ci);
				int stackSize = ci.getMaterial().getMaxStackSize();

				while (itemCount > 0) {
					int addSize = itemCount;

					if (addSize > stackSize) {
						addSize = stackSize;
					}

					ItemStack stack = ci.getItem();
					stack.setAmount(addSize);

					inventory.addItem(stack);

					itemCount -= addSize;
				}
			}

			return true;
		}

		return false;
	}
}