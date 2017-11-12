package pw.xwy.CombatWall;

import me.qiooip.notorious.Notorious;
import me.qiooip.notorious.handlers.CombatTagHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	private CombatTagHandler combatTagHandler = null;
	
	@Override
	public void onEnable() {
		
		if (!combatTagSetup()) {
			setEnabled(false);
			System.out.println("Missing Core.");
		}
		else {
			getServer().getPluginManager().registerEvents(this,this);
		}
	}
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		int Tx = e.getTo().getBlockX();
		int Tz = e.getTo().getBlockZ();
		int border = 2800;
		if (combatTagHandler.isCombatTagged(e.getPlayer()) && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("world")) {
			int Fx = e.getFrom().getBlockX();
			int Fz = e.getFrom().getBlockZ();
			
			if (((Fx - Tx) < 0 || (Fx -Tx) > 0) || (Fz - Tz < 0 || Fx - Tz > 0)) {
				if ((-51 < Tz && Tz < 51) && (-51 < Tx && Tx <51)) {
					Location loc = new Location(e.getTo().getWorld(),e.getPlayer().getLocation().getBlockX(),e.getPlayer().getLocation().getBlockY(),e.getPlayer().getLocation().getBlockZ());
					loc.setYaw(e.getTo().getYaw());
					loc.setPitch(e.getTo().getPitch());
					loc.setDirection(e.getTo().getDirection());
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou cannot go into spawn with a combat tag!"));
					if (Tz < 0) {
						loc.setZ(loc.getBlockZ() - 2);
						e.getPlayer().teleport(loc);
					}
					else if (Tz > 0) {
						loc.setZ(loc.getBlockZ() + 2);
						e.getPlayer().teleport(loc);
					}
					if (Tx < 0) {
						loc.setX(loc.getBlockX()-2);
						e.getPlayer().teleport(loc);
					}
					else if (Tx > 0) {
						loc.setX(loc.getBlockX()+2);
						e.getPlayer().teleport(loc);
					}
				}
			}
			
		}
		if ((-border > Tz || border < Tz) || (-border > Tx || border < Tx) && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("world")) {
			Location loc = new Location(e.getPlayer().getLocation().getWorld(),e.getTo().getBlockX(),e.getTo().getBlockY(),e.getTo().getBlockZ());
			
			loc.setYaw(e.getTo().getYaw());
			loc.setPitch(e.getTo().getPitch());
			loc.setDirection(e.getTo().getDirection());
			if (Tz > border) {
				loc.setZ(loc.getBlockZ() - 2);
				e.getPlayer().teleport(loc);
			}
			else if (Tz < -border) {
				loc.setZ(loc.getBlockZ() + 2);
				e.getPlayer().teleport(loc);
			}
			if (Tx > border) {
				loc.setX(loc.getBlockX()-2);
				e.getPlayer().teleport(loc);
			}
			else if (Tx < -border) {
				loc.setX(loc.getBlockX()+2);
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
						player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou cannot go into spawn with a combat tag!"));
					}
				}
				else if ((-3000 > z || 3000 < z) || (-3000 > x || 3000 < x)) {
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
