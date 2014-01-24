package tc.oc.chatmoderator.scores;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.joda.time.Duration;
import org.joda.time.Instant;

import com.github.rmsy.channels.event.ChannelMessageEvent;
import com.google.common.base.Preconditions;

import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.events.ScoreUpdateEvent;
import tc.oc.chatmoderator.events.ViolationAddEvent;
import tc.oc.chatmoderator.violations.Violation;

public class ScoreUpdateListener implements Listener {

    private final PlayerManager manager;
    
    public ScoreUpdateListener(PlayerManager manager) {
        this.manager = Preconditions.checkNotNull(manager, "manager");
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onViolationAdd(ViolationAddEvent event) {
        PlayerViolationManager violations = this.manager.getViolationSet(event.getViolation().getPlayer());

        if (violations == null) {
            return;
        }

        double score = 0;
        double originalScore = violations.getScore();

        Set<Violation> violationsOfType = violations.getViolationsForType(event.getViolation().getClass());

        for (Violation violation : violationsOfType) {
            if (violation.equals(event.getViolation())) {
                // Add the score of this violation raw
                score += violation.getLevel();
            } else {
                // Add the average of the scores scaled down by time of all violations of this type
                Duration timeSinceViolation = new Duration(violation.getTime(), Instant.now());
                score += violation.getLevel() / timeSinceViolation.toStandardSeconds().getSeconds();
            }
        }

        // Add up a bit since more violations means more likely that the player is offending
        score += violations.getAllViolations().size();

        violations.setScore(score);
        Bukkit.getPluginManager().callEvent(new ScoreUpdateEvent(violations, originalScore));
    }

    public void onChannelMessageEvent(ChannelMessageEvent event) {
        PlayerViolationManager violations = this.manager.getViolationSet(event.getSender());

        if (violations.getViolationsForHashCode(event.hashCode()).size() == 0) {
            // If no violations were added, we decrement the score of the player
            violations.setScore(violations.getScore() - 10);
        }
    }
}
