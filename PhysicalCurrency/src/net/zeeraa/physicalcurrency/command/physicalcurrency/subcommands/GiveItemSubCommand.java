package net.zeeraa.physicalcurrency.command.physicalcurrency.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.currency.Currency;
import net.zeeraa.physicalcurrency.api.currency.item.CurrencyItem;
import net.zeeraa.zcommandlib.command.ZSubCommand;
import net.zeeraa.zcommandlib.command.utils.AllowedSenders;

public class GiveItemSubCommand extends ZSubCommand {
	public GiveItemSubCommand() {
		super("giveitem");

		setPermission("physicalcurrency.command.physicalcurrency.giveitem");
		setPermissionDefaultValue(PermissionDefault.OP);

		setAllowedSenders(AllowedSenders.ALL);

		setDescription("Give a currency item to a player");
		setHelpString("/physicalcurrency giveitem <Currency> <Item> [Player]" + ChatColor.AQUA + " Give currency items to a player");

		setEmptyTabMode(false);
		setFilterAutocomplete(true);
		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player player = null;

		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Invalid usage. See " + ChatColor.AQUA + "/physicalcurrency giveitem help" + ChatColor.RED + " for help");
			return false;
		}

		Currency currency = PhysicalCurrencyAPI.getCurrencyManager().getCurrency(args[0]);

		if (currency == null) {
			sender.sendMessage(ChatColor.RED + "Could not find currency: " + args[0]);
			return false;
		}

		CurrencyItem currencyItem = PhysicalCurrencyAPI.getCurrencyItemManager().getCurrencyItem(currency, args[1]);

		if (currencyItem == null) {
			sender.sendMessage(ChatColor.RED + "Could not find currency item: " + args[0] + " in currency: " + currency.getName());
			return false;
		}

		if (args.length >= 3) {
			player = Bukkit.getServer().getPlayer(args[2]);

			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Could not find player " + args[2]);
				return false;
			}
		} else {
			if (sender instanceof Player) {
				if (args.length == 2) {
					player = (Player) sender;
				}
			} else {
				if (args.length == 2) {
					sender.sendMessage(ChatColor.RED + "Please provide a player");
					return false;
				}
			}
		}

		if (player == null) {
			sender.sendMessage(ChatColor.DARK_RED + "Error: No target");
			return false;
		}

		ItemStack item = currencyItem.getItem();

		player.getInventory().addItem(item);

		sender.sendMessage(ChatColor.GREEN + "Success");

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<String>();

		if (args.length == 0 || args.length == 1) {
			for (Currency currency : PhysicalCurrencyAPI.getCurrencyManager().getCurrencies().values()) {
				result.add(currency.getName());
			}
		} else if (args.length == 2) {
			Currency currency = PhysicalCurrencyAPI.getCurrencyManager().getCurrency(args[0]);

			if (currency != null) {
				for (CurrencyItem ci : PhysicalCurrencyAPI.getCurrencyItemManager().getCurrencyItems(currency)) {
					result.add(ci.getName());
				}
			}
		} else if (args.length == 3) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				result.add(player.getName());
			}
		}

		return result;
	}
}