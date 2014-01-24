package tc.oc.chatmoderator.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.google.common.base.Preconditions;

import tc.oc.chatmoderator.PlayerViolationManager;

public class ScoreUpdateEvent extends Event {

    private PlayerViolationManager violations;
    private double oldScore;
    private double newScore;

    public ScoreUpdateEvent(PlayerViolationManager violations, double oldScore) {
        this.violations = Preconditions.checkNotNull(violations);
        this.oldScore = oldScore;
        this.newScore = this.violations.getScore();
    }

    public PlayerViolationManager getViolations() {
        return this.violations;
    }

    public double getOldScore() {
        return this.oldScore;
    }

    public double getNewScore() {
        return this.newScore;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return ScoreUpdateEvent.handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return ScoreUpdateEvent.handlers;
    }
}
