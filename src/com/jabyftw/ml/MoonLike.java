package com.jabyftw.ml;

import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Rafael
 */
public class MoonLike extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private boolean equipOnJoin;
    private ItemStack head, chest, pants, boots;

    @Override
    public void onEnable() {
        config = getConfig();
        config.addDefault("config.damageTickDelay", 10);
        config.addDefault("config.playerVacumDamage", 2.0D);
        config.addDefault("config.equipOnJoin", true);
        config.addDefault("armor.head", "GLASS");
        config.addDefault("armor.chest", "CHAINMAIL_CHESTPLATE");
        config.addDefault("armor.pants", "IRON_LEGGINGS");
        config.addDefault("armor.boots", "IRON_BOOTS");
        config.options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        equipOnJoin = config.getBoolean("config.equipOnJoin");
        head = new ItemStack(Material.valueOf(config.getString("armor.head").toUpperCase()), 1);
        chest = new ItemStack(Material.valueOf(config.getString("armor.chest").toUpperCase()), 1);
        pants = new ItemStack(Material.valueOf(config.getString("armor.pants").toUpperCase()), 1);
        boots = new ItemStack(Material.valueOf(config.getString("armor.boots").toUpperCase()), 1);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {

            @Override
            public void run() {
                for (Player p : getServer().getOnlinePlayers()) {
                    if (p.getEquipment().getHelmet() != null) {
                        if (!p.getEquipment().getHelmet().getType().equals(head.getType())) {
                            p.damage(config.getDouble("config.playerVacumDamage"));
                        }
                    } else {
                        p.damage(config.getDouble("config.playerVacumDamage"));
                    }
                }
            }
        }, config.getInt("config.damageTickDelay"), config.getInt("config.damageTickDelay"));
        getLogger().log(Level.INFO, "Enabled.");
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getLogger().log(Level.INFO, "Disabled.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (equipOnJoin) {
            p.getEquipment().setHelmet(head);
            p.getEquipment().setChestplate(chest);
            p.getEquipment().setLeggings(pants);
            p.getEquipment().setBoots(boots);
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (equipOnJoin) {
            p.getEquipment().setHelmet(head);
            p.getEquipment().setChestplate(chest);
            p.getEquipment().setLeggings(pants);
            p.getEquipment().setBoots(boots);
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        checkPlayer(e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        checkPlayer(e.getPlayer());
    }

    private void checkPlayer(Player player) {
        player.removePotionEffect(PotionEffectType.JUMP);
    }
}
