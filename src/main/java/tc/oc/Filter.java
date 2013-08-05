package tc.oc;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;

/**
 * Represents a filter that filters chat messages, dispatching violations if necessary.
 */
public abstract class Filter {
    protected final ChatModeratorPlugin plugin;

    private Filter() {
        this.plugin = null;
    }

    protected Filter(final ChatModeratorPlugin plugin) {
        Preconditions.checkArgument(Preconditions.checkNotNull(plugin, "Plugin").isEnabled(), "Plugin not loaded.");
        this.plugin = plugin;
    }

    /**
     * Filters a message. When this happens, violations are dispatched if necessary, and listeners of the violations can
     * modify the message.
     *
     * @param message The message that should be instead sent. This may be a modified message, the unchanged message, or
     *                <code>null</code>, if the message is to be cancelled.
     * @param player  The player that sent the message.
     */
    @Nullable
    public abstract String filter(String message, final OfflinePlayer player);
}
