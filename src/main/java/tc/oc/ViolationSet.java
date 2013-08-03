package tc.oc;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a set of violations for an individual player.
 */
public final class ViolationSet {
    @Nonnull
    private final Player player;
    @Nonnull
    private final HashMap<Class<? extends Violation>, Double> violationLevels;
    @Nonnull
    private final HashMap<Class<? extends Violation>, Set<Violation>> violations;

    private ViolationSet() {
        this.player = null;
        this.violationLevels = null;
        this.violations = null;
    }

    /**
     * Creates a new violation set.
     *
     * @param player The player.
     */
    public ViolationSet(@Nonnull final Player player) {
        this.player = Preconditions.checkNotNull(player, "Player");
        this.violationLevels = new HashMap<>();
        this.violations = new HashMap<>();
    }

    /**
     * Gets the violation level for the specified violation type.
     *
     * @param violationType The type of violation.
     * @return The violation level.
     */
    public double getViolationLevel(@Nonnull final Class<? extends Violation> violationType) {
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
    public void addViolation(@Nonnull final Violation violation) {
        Class<? extends Violation> type = Preconditions.checkNotNull(violation, "Violation").getClass();
        Set<Violation> violations = this.violations.get(type);
        if (violations == null) {
            violations = new HashSet<>();
        }
        violations.add(violation);
        this.violations.put(type, violations);
    }

    /**
     * Gets all violations.
     *
     * @return All violations.
     */
    @Nonnull
    public Set<Violation> getViolations() {
        HashSet<Violation> violations = new HashSet<>();
        for (Set<Violation> v : this.violations.values()) {
            for (Violation violation : v) {
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
    @Nonnull
    public Set<Violation> getViolations(@Nonnull final Class<? extends Violation> type) {
        Set<Violation> violations = this.violations.get(type);
        if (violations == null) {
            violations = new HashSet<>();
        }
        return violations;
    }
}
