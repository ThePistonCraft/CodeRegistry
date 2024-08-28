package de.eincode.coderegistry.command;

import de.eincode.coderegistry.annotation.RegisterCommand;
import de.eincode.coderegistry.registry.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

@RegisterCommand(
        name = "TestCommand",
        permission = "eincode.testcommand",
        description = "Test Description, permission, descriprion, and aliases is optional",
        aliases = "tc"
)
public class TestCommand extends CommandBase {

    public TestCommand(Plugin plugin, String noPermissionMessage) {
        super(plugin, "§cYou have don't permissions to do that.");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("TestCommand");
        return false;
    }
}
