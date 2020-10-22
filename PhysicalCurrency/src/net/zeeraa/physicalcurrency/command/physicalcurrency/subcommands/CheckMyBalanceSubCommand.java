package net.zeeraa.physicalcurrency.command.physicalcurrency.subcommands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.balance.Balance;
import net.zeeraa.zcommandlib.command.ZSubCommand;
import net.zeeraa.zcommandlib.command.utils.AllowedSenders;

public class CheckMyBalanceSubCommand extends ZSubCommand {
	public CheckMyBalanceSubCommand() {
		super("checkmybalance");

		setPermission("physicalcurrency.command.physicalcurrency.checkmybalance");
		setPermissionDefaultValue(PermissionDefault.OP);

		setAllowedSenders(AllowedSenders.PLAYERS);

		setDescription("Check your balance");
		setHelpString("/physicalcurrency checkmybalance" + ChatColor.AQUA + " Check your balance. Used for testing");

		setEmptyTabMode(true);
		setFilterAutocomplete(true);
		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player player = (Player) sender;

		List<Balance> balanceList = PhysicalCurrencyAPI.getInventoryManager().getBalance(player.getInventory());

		sender.sendMessage(ChatColor.GOLD + "You have " + ChatColor.AQUA + balanceList.size() + ChatColor.GOLD + " types of currencies");
		for (Balance balance : balanceList) {
			sender.sendMessage(ChatColor.GOLD + "Currency: " + ChatColor.AQUA + balance.getCurrency().getName() + ChatColor.GOLD + ". Amount " + ChatColor.AQUA + balance.getAmount() + ChatColor.GOLD + ". Vault value: " + ChatColor.AQUA + balance.getVaultVaule());
		}
		sender.sendMessage(ChatColor.GOLD + "Total vault value: " + ChatColor.AQUA + Balance.getTotalVaultValue(balanceList));

		return true;
	}
}