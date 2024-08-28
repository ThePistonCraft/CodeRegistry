package de.eincode.coderegistry.listener;

import de.eincode.coderegistry.annotation.RegisterListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RegisterListener
public class TestListener implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Hey! Welcome on this Server.");
    }
}
