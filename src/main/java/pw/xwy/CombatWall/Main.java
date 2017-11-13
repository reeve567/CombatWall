package pw.xwy.CombatWall;

import me.qiooip.notorious.Notorious;
import me.qiooip.notorious.handlers.CombatTagHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin implements Listener {

	private CombatTagHandler combatTagHandler = null;
	private HashMap<Player, Boolean> players = new HashMap<Player, Boolean>();


	@Override
	public void onEnable() {

		if (!combatTagSetup()) {
			setEnabled(false);
			System.out.println("Missing Core.");
		} else {
			getServer().getPluginManager().registerEvents(this, this);
		}
	}


	@EventHandler
	public void onMove(final PlayerMoveEvent e) {
		final double[] Tx = {e.getFrom().getX()};
		final double[] Tz = {e.getFrom().getZ()};
		int border = 2800;
		if (combatTagHandler.isCombatTagged(e.getPlayer()) && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("world")) {
			if ((-51.5 < Tz[0] && Tz[0] < 51.5) && (-51.5 < Tx[0] && Tx[0] < 51.5)) {

				double changeX =  e.getFrom().getX()-e.getTo().getX();
				double changeZ =  e.getFrom().getZ()-e.getTo().getZ();

				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot go into spawn with a combat tag!"));
				Location loc = e.getPlayer().getLocation();
				if (changeZ > 0) {
					loc.setZ(loc.getZ() + 3);
					e.getPlayer().teleport(loc);
					System.out.println("1");
				} else if (changeZ < 0) {
					loc.setZ(loc.getZ() - 3);
					e.getPlayer().teleport(loc);
					System.out.println("2");
				}
				if (changeX > 0) {
					loc.setX(loc.getX() + 3);
					e.getPlayer().teleport(loc);
					System.out.println("3");
				} else if (changeX < 0) {
					loc.setX(loc.getX() - 3);
					e.getPlayer().teleport(loc);
					System.out.println("4");
				}
				/*
				for (int i = 0; i < 40; i++) {
					Tx[0] = e.getPlayer().getLocation().getX();
					Tz[0] = e.getPlayer().getLocation().getZ();
					if ((-51.5 < Tz[0] && Tz[0] < 51.5) && (-51.5 < Tx[0] && Tx[0] < 51.5)) {
						loc = e.getPlayer().getLocation();
						if (Tz[0] < 51.5) {
							loc.setZ(loc.getZ() + 1);
							e.getPlayer().teleport(loc);
						} else if (Tz[0] > -51.5) {
							loc.setZ(loc.getZ() - 1);
							e.getPlayer().teleport(loc);
						}
						if (Tx[0] < 51.5) {
							loc.setX(loc.getX() + 1);
							e.getPlayer().teleport(loc);
						} else if (Tx[0] > -51.5) {
							loc.setX(loc.getX() - 1);
							e.getPlayer().teleport(loc);
						}
					}
				}*/
			}

		}
		if ((-border > Tz[0] || border < Tz[0]) || (-border > Tx[0] || border < Tx[0]) && e.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
			Location loc = new Location(e.getPlayer().getLocation().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ());

			loc.setYaw(e.getTo().getYaw());
			loc.setPitch(e.getTo().getPitch());
			loc.setDirection(e.getTo().getDirection());
			if (Tz[0] > border) {
				loc.setZ(loc.getBlockZ() - 2);
				e.getPlayer().teleport(loc);
			} else if (Tz[0] < -border) {
				loc.setZ(loc.getBlockZ() + 2);
				e.getPlayer().teleport(loc);
			}
			if (Tx[0] > border) {
				loc.setX(loc.getBlockX() - 2);
				e.getPlayer().teleport(loc);
			} else if (Tx[0] < -border) {
				loc.setX(loc.getBlockX() + 2);
				e.getPlayer().teleport(loc);
			}
		}
	}

	@EventHandler
	public void enderPearlLand(PlayerTeleportEvent e) {
		if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			if (!e.getPlayer().getWorld().getName().equalsIgnoreCase("world_the_end")) {
				int x = e.getTo().getBlockX();
				int z = e.getTo().getBlockZ();
				if ((-51 < z && z < 51) && (-51 < x && x < 51)) {
					Player player = e.getPlayer();
					if (combatTagHandler.isCombatTagged(player)) {
						e.setCancelled(true);
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot go into spawn with a combat tag!"));
					}
				} else if ((-3000 > z || 3000 < z) || (-3000 > x || 3000 < x)) {
					e.setCancelled(true);
				}
			}
		}
	}

	private boolean combatTagSetup() {
		if (getServer().getPluginManager().getPlugin("Notorious") == null) {
			return false;
		}
		Plugin not = getServer().getPluginManager().getPlugin("Notorious");
		if (!not.isEnabled()) {
			return false;
		}
		Notorious notorious = Notorious.getInstance();
		combatTagHandler = notorious.getCombatTagHandler();
		return true;

	}
}
