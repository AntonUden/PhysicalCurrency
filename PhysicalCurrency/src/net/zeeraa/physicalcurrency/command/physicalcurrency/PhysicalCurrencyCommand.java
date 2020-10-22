package net.zeeraa.physicalcurrency.command.physicalcurrency;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.physicalcurrency.command.physicalcurrency.subcommands.CheckMyBalanceSubCommand;
import net.zeeraa.physicalcurrency.command.physicalcurrency.subcommands.GiveItemSubCommand;
import net.zeeraa.physicalcurrency.command.physicalcurrency.subcommands.ListCurrenciesSubCommand;
import net.zeeraa.physicalcurrency.command.physicalcurrency.subcommands.ListItemsSubCommand;
import net.zeeraa.physicalcurrency.command.physicalcurrency.subcommands.PlayerInfoCommand;
import net.zeeraa.zcommandlib.command.ZCommand;
import net.zeeraa.zcommandlib.command.utils.AllowedSenders;

public class PhysicalCurrencyCommand extends ZCommand {

	public PhysicalCurrencyCommand() {
		super("physicalcurrency");

		setPermission("physicalcurrency.command.physicalcurrency");
		setPermissionDefaultValue(PermissionDefault.OP);
		setAllowedSenders(AllowedSenders.ALL);

		setDescription("Parent command for the physical currency plugin");
		setHelpString("/physicalcurrency");

		addSubCommand(new ListCurrenciesSubCommand());
		addSubCommand(new ListItemsSubCommand());
		addSubCommand(new GiveItemSubCommand());
		addSubCommand(new PlayerInfoCommand());
		addSubCommand(new CheckMyBalanceSubCommand());

		addHelpSubCommand();

		setEmptyTabMode(true);
		setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "Use " + ChatColor.AQUA + "/physicalcurrency help " + ChatColor.GOLD + "for help");
		return true;
	}
}