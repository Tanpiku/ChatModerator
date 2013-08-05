package tc.oc;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager for players. Handles the addition of violations and modifications of violation levels.
 */
public final class PlayerManager {
    private final Map<OfflinePlayer, ViolationSet> violationSets = new HashMap<>();
    private final ChatModeratorPlugin plugin;

    private PlayerManager() {
        this.plugin = null;
    }

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
    @Nullable
    public ViolationSet getViolationSet(final OfflinePlayer player) {
        ViolationSet violations = this.violationSets.get(Preconditions.checkNotNull(player, "Player"));
        if (violations == null) {
            violations = new ViolationSet(player);
            this.violationSets.put(player, violations);
        }
        return violations;
    }
}
