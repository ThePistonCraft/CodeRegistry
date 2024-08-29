package de.eincode.coderegistry.registry;

import de.eincode.coderegistry.annotation.RegisterCommand;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase implements CommandExecutor, TabCompleter {

    protected final Plugin plugin;
    protected final String noPermissionMessage;

    public CommandBase(Plugin plugin, String noPermissionMessage) {
        this.plugin = plugin;
        this.noPermissionMessage = noPermissionMessage;
    }

    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        final RegisterCommand registerCommand = this.getClass().getAnnotation(RegisterCommand.class);

        if (sender instanceof final Player player) {
            if (registerCommand != null) {
                if (!registerCommand.permission().isEmpty() && !sender.hasPermission(registerCommand.permission())) {
                    player.sendMessage(this.noPermissionMessage);
                    return false;
                }

                this.execute(sender, args);
            }
            return true;
        }
        return this.execute(sender, args);
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return new ArrayList<>();
    }
}