package tc.oc.chatmoderator;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.joda.time.Instant;
import tc.oc.chatmoderator.events.ViolationAddEvent;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.*;

import java.util.*;

/**
 * Represents a set of violations for an individual player.
 */
public final class PlayerViolationManager {
    private final OfflinePlayer player;
    private final Map<Class<? extends Violation>, ViolationSet> violations = new HashMap<>();
    private Instant lastMessageTime;

    /**
     * Creates a new violation set.
     *
     * @param player The player.
     */
    public PlayerViolationManager(final OfflinePlayer player) {
        this.player = Preconditions.checkNotNull(player, "Player");
        
        setUpViolations();
    }

    /**
     * Helper method to add all violations to the violation map (this is where their values are declared)
     * Add more of these as necessary...
     */
    private void setUpViolations() {
        // TODO: add proper default levels to all ViolationSet(s).

        Plugin plugin = Bukkit.getPluginManager().getPlugin("ChatModerator");
        FileConfiguration pluginConfig = plugin.getConfig();

        Preconditions.checkArgument(plugin.isEnabled(), "plugin not enabled");

        this.violations.put(DuplicateMessageViolation.class, new ViolationSet(pluginConfig.getDouble("filters.duplicate-messages.default-level", 1)));
        this.violations.put(ServerIPViolation.class, new ViolationSet(pluginConfig.getDouble("filters.server-ip.default-level", 1)));
        this.violations.put(RepeatedCharactersViolation.class, new ViolationSet(pluginConfig.getDouble("filters.repeated-characters.default-level", 1)));
        this.violations.put(ProfanityViolation.class, new ViolationSet(pluginConfig.getDouble("filters.profanity.default-level", 1)));
        this.violations.put(AllCapsViolation.class, new ViolationSet(pluginConfig.getDouble("filters.all-caps.default-level", 1)));
    }
    
    /**
     * Gets the violation level for the specified violation type.
     *
     * @param violationType The type of violation.
     * @return The violation level.
     */
    public double getViolationLevel(final Class<? extends Violation> violationType) {
        double level = this.violations.get(Preconditions.checkNotNull(violationType)).getLevel();

        return level;
    }

    /**
     * Adds a violation, and call the ViolationAddEvent for use elsewhere
     *
     * @param violation The violation to be added.
     */
    public void addViolation(final Violation violation) {
        Class<? extends Violation> type = Preconditions.checkNotNull(violation, "Violation").getClass();
        Set<Violation> violations = this.violations.get(type).getViolations();

        violations.add(violation);
        
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("ChatModerator"), new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().getPluginManager().callEvent(new ViolationAddEvent(violation.getPlayer(), violation));
            }
        });
    }

    /**
     * Gets all violations.
     *
     * @return All violations.
     */
    public Set<Violation> getAllViolations() {
        Set<Violation> violations = new HashSet<>();
        for (ViolationSet violationSet : this.violations.values()) {
            for (Violation violation : violationSet.getViolations()) {
                violations.add(violation);
            }
        }
        return violations;
    }

    /**
     * Gets the violations of the specified type.
     *
     * @param type The type of violation to get.
     * @return The violations of the specified type.
     */
    public Set<Violation> getViolationsForType(final Class<? extends Violation> type) {
        Set<Violation> violations = this.violations.get(type).getViolations();

        return Collections.unmodifiableSet(violations);
    }

    /**
     * Gets all violations that occurred at the specified time.
     *
     * @param time The {@link org.joda.time.Instant} to search for.
     * @return The violations that happened at the specified time.
     */
    public Set<Violation> getViolationsForTime(Instant time) {
        Set<Violation> violations = new HashSet<Violation>();

        for(Violation v : this.getAllViolations()) {
            if(v.getTime().equals(time)) {
                violations.add(v);
            }
        }

        return violations;
    }

    public void setLastMessageTime(Instant time) {
        this.lastMessageTime = Preconditions.checkNotNull(time);
    }

    public Instant getLastMessageTime() {
        return this.lastMessageTime;
    }

    /**
     * Stores a level and a set of violations per Violation type.
     */
    public class ViolationSet {
        /**
         * The level that the violation is worth
         */
        private double level;
        
        /**
         * The list of violations of this specific type
         */
        private final Set<Violation> violations = new HashSet<>();

        /**
         * Represents the callable constructor of ViolationSet
         * @param level The level for that violation
         */
        public ViolationSet(double level) {
            this.level = level;
        }
        
        /**
         * Gets the level assigned to this particular type of violation
         * @return The level
         */
        public double getLevel() {
            return this.level;
        }
        
        /**
         * Gets the violations associated with this type
         * @return The violations
         */
        public Set<Violation> getViolations() {
            return this.violations;
        }
    }
}
