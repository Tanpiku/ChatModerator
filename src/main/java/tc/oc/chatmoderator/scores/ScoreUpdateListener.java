package tc.oc.chatmoderator.scores;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.google.common.base.Preconditions;

import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.events.ViolationAddEvent;

public class ScoreUpdateListener implements Listener {

    private final PlayerManager manager;
    
    public ScoreUpdateListener(PlayerManager manager) {
        this.manager = Preconditions.checkNotNull(manager, "manager");
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onViolationAdd(ViolationAddEvent event) {
        PlayerViolationManager violations = this.manager.getViolationSet(event.getViolation().getPlayer());
        violations.setScore(-1);
    }
}
