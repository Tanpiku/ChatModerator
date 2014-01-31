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
    public static final double MAX_PLAYER_SCORE = 300;
    public static final double MIN_PLAYER_SCORE = 0;

    public ScoreUpdateListener(PlayerManager manager) {
        this.manager = Preconditions.checkNotNull(manager, "manager");
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onViolationAdd(ViolationAddEvent event) {
        PlayerViolationManager violations = this.manager.getViolationSet(event.getViolation().getPlayer());

        if (violations == null) {
            return;
        }

        double score = violations.getScore();
        double originalScore = score;

        Set<Violation> violationsOfType = violations.getViolationsForType(event.getViolation().getClass());

        for (Violation violation : violationsOfType) {
            // Skip useless violations (some of these are necessary...)
            if (violation.getLevel() == 0) {
                continue;
            }

            if (violation.equals(event.getViolation())) {
                // Add the score of this violation raw
                score += Math.max(0, violation.getLevel());
            } else {
                // Add the average of the scores scaled down by time of all violations of this type
                long secondsSince = new Duration(violation.getTime(), Instant.now()).getStandardSeconds();
                secondsSince = secondsSince == 0 ? 1 : secondsSince;

                int messagesSince = violations.getMessagesSinceLastViolation() > 0 ? 1 : violations.getMessagesSinceLastViolation();

                double scaledViolationLevel = violation.getLevel() / (secondsSince * (messagesSince == 0 ? 1 : messagesSince));
                score += (scaledViolationLevel *= 1.5d);

                violations.setScore(Math.min(Math.max(score, 0), MAX_PLAYER_SCORE));
            }
        }

        // Add up a bit since more violations means more likely that the player is offending
        score += violations.getAllViolations().size();

        violations.setScore(Math.min(Math.max(score, 0), MAX_PLAYER_SCORE));
        Bukkit.getPluginManager().callEvent(new ScoreUpdateEvent(violations, originalScore));
    }

    @EventHandler
    public void onChannelMessageEvent(ChannelMessageEvent event) {
        // Cool down
        PlayerViolationManager violations = this.manager.getViolationSet(event.getSender());

        if (violations.getViolationsForHashCode(event.hashCode()).size() == 0) {
            violations.incrementMessagesSinceLastViolation();

            // If no violations were added, we decrement the score of the player
            double originalScore = violations.getScore();
            double newScore = originalScore;

            if (violations.getMessagesSinceLastViolation() < 3) {
                double scaledSinceLast = Math.max((Math.pow(violations.getMessagesSinceLastViolation(), 2)*3), 0);
                if (event.getMessage().length() < 7) {
                    scaledSinceLast /= 2;
                }

                newScore -= scaledSinceLast;
            } else {
                newScore -= Math.max(Math.pow(violations.getMessagesSinceLastViolation(), 2) * violations.getMessagesSinceLastViolation(), 0);
            }

            violations.setScore(Math.max(newScore, 0));

            if (violations.getScore() != originalScore) {
                Bukkit.getPluginManager().callEvent(new ScoreUpdateEvent(violations, originalScore));
            }
        }
    }
}
