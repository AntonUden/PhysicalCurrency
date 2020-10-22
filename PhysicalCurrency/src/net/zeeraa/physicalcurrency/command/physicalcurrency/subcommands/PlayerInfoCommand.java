package net.zeeraa.physicalcurrency.command.physicalcurrency.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.physicalcurrency.api.PhysicalCurrencyAPI;
import net.zeeraa.physicalcurrency.api.player.PlayerData;
import net.zeeraa.zcommandlib.command.ZSubCommand;
import net.zeeraa.zcommandlib.command.utils.AllowedSenders;

public class PlayerInfoCommand extends ZSubCommand {
	public PlayerInfoCommand() {
		super("playerinfo");

		setPermission("physicalcurrency.command.physicalcurrency.playerinfo");
		setPermissionDefaultValue(PermissionDefault.OP);

		setAllowedSenders(AllowedSenders.ALL);

		setDescription("Get player info");
		setHelpString("/physicalcurrency playerinfo [Player]" + ChatColor.AQUA + " Get player info");

		setEmptyTabMode(false);
		setFilterAutocomplete(true);
		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player player = null;

		if (args.length == 0) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				sender.sendMessage(ChatColor.RED + "Please provide a player");
				return false;
			}
		} else {
			player = Bukkit.getServer().getPlayer(args[0]);

			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Could not find player " + args[0]);
				return false;
			}
		}

		PlayerData pd = PhysicalCurrencyAPI.getPlayerDataManager().getPlayerData(player.getUniqueId());

		sender.sendMessage(ChatColor.AQUA + player.getName() + "s" + ChatColor.GOLD + " data:");
		sender.sendMessage(ChatColor.GOLD + "UUID: " + ChatColor.AQUA + pd.getUuid());
		sender.sendMessage(ChatColor.GOLD + "Primary currency: " + ChatColor.AQUA + pd.getPrimaryCurrency().getName());

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<String>();

		if (args.length == 0 || args.length == 1) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				result.add(player.getName());
			}
		}
		return result;
	}
}