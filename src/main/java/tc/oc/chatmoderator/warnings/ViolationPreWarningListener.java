package tc.oc.chatmoderator.warnings;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.events.ViolationAddEvent;

public class ViolationPreWarningListener implements Listener {

    private final @Nonnull PlayerManager manager;

    public ViolationPreWarningListener(@Nonnull PlayerManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerRecieveViolation(ViolationAddEvent event) {
        if (!event.getPlayer().isOnline()) {
            return;
        }

        Player player = (Player) event.getPlayer();
        WarningSendEvent warningEvent = new WarningSendEvent(player, event);
        Bukkit.getServer().getPluginManager().callEvent(warningEvent);

        PlayerViolationManager violations = this.manager.getViolationSet(event.getPlayer());

        if (!violations.isWarned() && warningEvent.getMessage() != null) {
            violations.sendWarning(warningEvent.getMessage());
            violations.setWarned(true);
        }
    }
}
