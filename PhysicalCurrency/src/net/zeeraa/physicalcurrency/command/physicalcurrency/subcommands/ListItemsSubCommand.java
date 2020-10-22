package net.zeeraa.physicalcurrency.command.physicalcurrency.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItem;
import net.zeeraa.zcommandlib.command.ZSubCommand;
import net.zeeraa.zcommandlib.command.utils.AllowedSenders;

public class ListItemsSubCommand extends ZSubCommand {
	public ListItemsSubCommand() {
		super("listitems");

		setPermission("physicalcurrency.command.physicalcurrency.listitems");
		setPermissionDefaultValue(PermissionDefault.OP);

		setAllowedSenders(AllowedSenders.ALL);

		setDescription("Show all items");

		setEmptyTabMode(true);
		setFilterAutocomplete(true);
		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "" + PhysicalCurrencyAPI.getCurrencyItemManager().getAll().size() + " currency items loaded");
		for (CurrencyItem ci : PhysicalCurrencyAPI.getCurrencyItemManager().getAll()) {
			sender.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.AQUA + ci.getName() + ChatColor.GOLD + ". Display name: " + ChatColor.AQUA + ci.getDisplayName() + ChatColor.GOLD + ". Currency amount: " + ChatColor.AQUA + ci.getCurrencyAmount() + ChatColor.GOLD + ". Currency: " + ChatColor.AQUA + ci.getCurrency().getName() + ChatColor.GOLD + ". Material: " + ChatColor.AQUA + ci.getMaterial().name() + ChatColor.GOLD + ". Model data: " + ChatColor.AQUA + ci.getCustomModelData());
		}
		return true;
	}
}