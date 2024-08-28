package de.eincode.coderegistry;

import de.eincode.coderegistry.registry.Registry;
import org.bukkit.plugin.java.JavaPlugin;

public final class CodeRegistry extends JavaPlugin {

    @Override
    public void onEnable() {
        final Registry registry = new Registry(this, "de.eincode.coderegistry");
        registry.registerCommandsFromClasses();
        registry.registerListenersFromClasses();
    }

}