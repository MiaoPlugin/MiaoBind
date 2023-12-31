package pw.yumc.MiaoBind.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import pw.yumc.YumCore.annotation.NotProguard;

/**
 * @author MiaoWoo
 */
@NotProguard
public class BindItemEvent extends PlayerEvent implements Cancellable {
    private ItemStack itemStack;
    private String cancelReason = "未知";
    private boolean cancelled;

    public BindItemEvent(Player player, ItemStack itemStack) {
        super(player);
        this.setItemStack(itemStack);
        this.cancelled = false;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
