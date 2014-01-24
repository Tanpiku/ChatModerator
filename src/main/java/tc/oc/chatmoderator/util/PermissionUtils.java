package tc.oc.chatmoderator.util;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import tc.oc.chatmoderator.filters.Filter;

public class PermissionUtils {

    public static boolean isPlayerExempt(OfflinePlayer player, Class<? extends Filter> filter) {
        @Nullable Player onlinePlayer = Bukkit.getPlayer(player.getName());

        if (onlinePlayer == null) {
            return false;
        }
        
        return onlinePlayer.hasPermission(ChatModeratorUtil.permissions.get(filter));
    }

    public static boolean isPlayerExempt(OfflinePlayer player, Permission permission) {
        @Nullable Player onlinePlayer = Bukkit.getPlayer(player.getName());

        if (onlinePlayer == null) {
            return false;
        }
        
        return onlinePlayer.hasPermission(permission);
    }
    
}
