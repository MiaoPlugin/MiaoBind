package pw.yumc.MiaoBind.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.MiaoBind.runnable.CheckArmor;
import pw.yumc.MiaoBind.runnable.UpdateInventory;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.bukkit.P;

/**
 * @author MiaoWoo
 */
public class PlayerListener implements Listener {
    private Config config = P.getInjectConfig();
    private Sound sound;

    public PlayerListener() {
        try {
            sound = Sound.valueOf("ITEM_SHIELD_BREAK");
        } catch (IllegalArgumentException e) {
            sound = Sound.valueOf("ITEM_BREAK");
        }
        Bukkit.getPluginManager().registerEvents(this, P.instance);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPreCommand(PlayerCommandPreprocessEvent e) {
        if (config.CheckArmorCommand.contains(e.getMessage().toLowerCase())) {
            new CheckArmor(e.getPlayer()).runTaskLater(P.instance, 2);
        }
    }

    @EventHandler
    public void onPlayerHeldItem(PlayerItemHeldEvent event) {
        PlayerInventory inv = event.getPlayer().getInventory();
        ItemStack pre = inv.getItem(event.getPreviousSlot());
        if (ItemKit.isInvalidItem(pre)) {
            inv.setItem(event.getPreviousSlot(), null);
        }
        ItemStack newi = inv.getItem(event.getNewSlot());
        if (ItemKit.isInvalidItem(newi)) {
            inv.setItem(event.getNewSlot(), null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) { return; }
        switch (ItemKit.getItemType(item)) {
            case MiaoTimeBind:
                if (ItemKit.isInvalidItem(item)) {
                    item.setType(Material.AIR);
                }
                break;
            case BIND_ON_USE:
                ItemKit.bindItem(event.getPlayer(), item);
                break;
            case BIND_ON_EQUIP:
                if (ItemKit.ArmorKit.isEquipable(item)) {
                    new CheckArmor(event.getPlayer()).runTaskAsynchronously(P.instance);
                }
                break;
            default:
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        if (!player.isValid()) { return; }
        final Item item = event.getItemDrop();
        final ItemStack itemStack = item.getItemStack();
        Log.d("[PlayerDropItemEvent] 玩家 %s 丢弃物品 %s", player.getName(), itemStack);
        if (config.PreventDrop) {
            if (ItemKit.isBind(itemStack) && ItemKit.isBoundPlayer(player, itemStack)) {
                Log.d("[PlayerDropItemEvent] 物品 %s 检测到已绑定玩家 %s", itemStack, player.getName());
                item.setPickupDelay(2 * 20);
                event.setCancelled(true);
                new UpdateInventory(player).runTask(P.instance);
            }
        } else if (config.DeleteOnDrop) {
            Log.d("[PlayerDropItemEvent] 玩家 %s 丢弃物品且物品被直接销毁!", player.getName());
            player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
            event.getItemDrop().remove();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemPickup(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        final Item item = event.getItem();
        final ItemStack itemStack = item.getItemStack();
        switch (ItemKit.getItemType(itemStack)) {
            case MiaoBind:
            case MiaoTimeBind:
                if (!ItemKit.isBoundPlayer(player, itemStack) && !player.isOp()) {
                    event.setCancelled(true);
                    item.setPickupDelay(200);
                    return;
                }
                if (ItemKit.isInvalidItem(itemStack)) {
                    itemStack.setType(Material.AIR);
                }
                break;
            case BIND_ON_PICKUP:
                ItemKit.bindItem(player, itemStack);
                break;
            default:
        }
        if (player.getItemOnCursor() != null && ItemKit.isBind(player.getItemOnCursor())) {
            item.setPickupDelay(40);
            event.setCancelled(true);
        }
    }
}
