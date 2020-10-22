package net.zeeraa.physicalcurrency.command.physicalcurrency.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.currency.Currency;
import net.zeeraa.zcommandlib.command.ZSubCommand;
import net.zeeraa.zcommandlib.command.utils.AllowedSenders;

public class ListCurrenciesSubCommand extends ZSubCommand {
	public ListCurrenciesSubCommand() {
		super("listcurrencies");

		setPermission("physicalcurrency.command.physicalcurrency.listcurrencies");
		setPermissionDefaultValue(PermissionDefault.OP);

		setAllowedSenders(AllowedSenders.ALL);

		setDescription("Show all currencies");

		setEmptyTabMode(true);
		setFilterAutocomplete(true);
		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "" + PhysicalCurrencyAPI.getCurrencyManager().getCurrencies().size() + " currencies loaded");
		for (Currency currency : PhysicalCurrencyAPI.getCurrencyManager().getCurrencies().values()) {
			sender.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.AQUA + currency.getName() + ChatColor.GOLD + ", Singular: " + ChatColor.AQUA + currency.getDisplayNameSingular() + ChatColor.GOLD + ", Plural: " + currency.getDisplayNamePlural() + ChatColor.GOLD + ". Vault value: " + ChatColor.AQUA + currency.getVaultValue());
		}
		return true;
	}
}