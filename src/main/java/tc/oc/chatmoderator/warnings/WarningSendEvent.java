package tc.oc.chatmoderator.warnings;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.google.common.base.Preconditions;

public class WarningSendEvent extends Event {

    private @Nullable String message;
    private final Player player;

    public WarningSendEvent(final Player player) {
        this(player, null);
    }

    public WarningSendEvent(final Player player, String message) {
        this.player = Preconditions.checkNotNull(player, "player cannot be null");
        this.message = message;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getMessage() {
        return this.message;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return WarningSendEvent.handlers;
    }
}
