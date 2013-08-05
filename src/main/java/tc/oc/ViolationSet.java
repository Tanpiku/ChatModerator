package tc.oc;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;

import java.util.*;

/**
 * Represents a set of violations for an individual player.
 */
public final class ViolationSet {
    private final OfflinePlayer player;
    private final Map<Class<? extends Violation>, Double> violationLevels = new HashMap<>();
    private final Map<Class<? extends Violation>, Set<Violation>> violations = new HashMap<>();

    private ViolationSet() {
        this.player = null;
    }

    /**
     * Creates a new violation set.
     *
     * @param player The player.
     */
    public ViolationSet(final OfflinePlayer player) {
        this.player = Preconditions.checkNotNull(player, "Player");
    }

    /**
     * Gets the violation level for the specified violation type.
     *
     * @param violationType The type of violation.
     * @return The violation level.
     */
    public double getViolationLevel(final Class<? extends Violation> violationType) {
        Double level = this.violationLevels.get(Preconditions.checkNotNull(violationType));
        if (level != null) {
            return level;
        } else {
            return 0.0D;
        }
    }

    /**
     * Adds a violation.
     *
     * @param violation The violation to be added.
     */
    public void addViolation(final Violation violation) {
        Class<? extends Violation> type = Preconditions.checkNotNull(violation, "Violation").getClass();
        Set<Violation> violations = this.violations.get(type);
        if (violations == null) {
            violations = new HashSet<>();
        }
        violations.add(violation);
        this.violations.put(type, violations);
        this.violationLevels.put(type, violation.getLevel());
    }

    /**
     * Gets all violations.
     *
     * @return All violations.
     */
    public Set<Violation> getViolations() {
        Set<Violation> violations = new HashSet<>();
        for (Set<Violation> v : this.violations.values()) {
            for (Violation violation : v) {
                violations.add(violation);
            }
        }
        return Collections.unmodifiableSet(violations);
    }

    /**
     * Gets the violations of the specified type.
     *
     * @param type The type of violation to get.
     * @return The violations of the specified type.
     */
    public Set<Violation> getViolations(final Class<? extends Violation> type) {
        Set<Violation> violations = this.violations.get(type);
        if (violations == null) {
            violations = new HashSet<>();
        }
        return Collections.unmodifiableSet(violations);
    }
}
