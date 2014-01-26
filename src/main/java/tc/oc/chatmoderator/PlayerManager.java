package tc.oc.chatmoderator;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager for players. Handles the addition of violations and modifications of violation levels.
 */
public final class PlayerManager {
    private final Map<OfflinePlayer, PlayerViolationManager> violationSets = new HashMap<>();
    private final ChatModeratorPlugin plugin;

    /**
     * Creates a new player manager.
     *
     * @param plugin The plugin instance.
     */
    PlayerManager(final ChatModeratorPlugin plugin) {
        Preconditions.checkArgument(Preconditions.checkNotNull(plugin, "Plugin").isEnabled(), "Plugin not loaded.");
        this.plugin = plugin;
    }

    /**
     * Gets the violations for the specified player.
     *
     * @param player The player.
     * @return The violations for the specified player.
     */
    public @Nullable PlayerViolationManager getViolationSet(final OfflinePlayer player) {
        PlayerViolationManager violations = this.violationSets.get(Preconditions.checkNotNull(player, "Player"));
        if (violations == null) {
            violations = new PlayerViolationManager(player);
            this.violationSets.put(player, violations);
        }
        return violations;
    }
}
