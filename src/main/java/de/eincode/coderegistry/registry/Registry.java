package de.eincode.coderegistry.registry;

import de.eincode.coderegistry.annotation.RegisterCommand;
import de.eincode.coderegistry.annotation.RegisterListener;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Registry {
    private final Plugin plugin;
    private final String basePackage;

    public Registry(Plugin plugin, String basePackage) {
        this.plugin = plugin;
        this.basePackage = basePackage;
    }

    @SneakyThrows
    public void initialize() {
        final Iterable<Class<?>> classes = getPluginClasses(basePackage);

        for (Class<?> clazz : classes) {
            if (CommandBase.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(RegisterCommand.class)) {
                final RegisterCommand registerCommand = clazz.getAnnotation(RegisterCommand.class);
                final String commandName = registerCommand.name();
                final String[] aliases = registerCommand.aliases();
                final String description = registerCommand.description();

                final Constructor<?> constructor = clazz.getConstructor(Plugin.class);
                final CommandBase command = (CommandBase) constructor.newInstance(plugin);

                this.registerCommand(commandName, aliases, description, command);
            }

            if (Listener.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(RegisterListener.class)) {
                Constructor<?> constructor;
                Listener listener;

                try {
                    constructor = clazz.getConstructor(Plugin.class);
                    listener = (Listener) constructor.newInstance(plugin);
                } catch (NoSuchMethodException e) {
                    constructor = clazz.getConstructor();
                    listener = (Listener) constructor.newInstance();
                }

                this.registerListener(listener);
            }
        }
    }


    @Deprecated(forRemoval = true)
    @SneakyThrows
    public void registerCommandsFromClasses() {
        final Iterable<Class<?>> classes = getPluginClasses(basePackage);

        for (Class<?> clazz : classes) {
            if (CommandBase.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(RegisterCommand.class)) {
                final RegisterCommand registerCommand = clazz.getAnnotation(RegisterCommand.class);
                final String commandName = registerCommand.name();
                final String[] aliases = registerCommand.aliases();
                final String description = registerCommand.description();

                final Constructor<?> constructor = clazz.getConstructor(Plugin.class);
                final CommandBase command = (CommandBase) constructor.newInstance(plugin);

                this.registerCommand(commandName, aliases, description, command);
            }
        }
    }

    @SneakyThrows
    private void registerCommand(String name, String[] aliases, String description, CommandExecutor executor) {
        final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        final SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());

        final Command command = new Command(name, description, "", List.of(aliases)) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
                return executor.onCommand(sender, this, alias, args);
            }

            @Override
            public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
                if (executor instanceof TabCompleter tabCompleter) {
                    return tabCompleter.onTabComplete(sender, this, alias, args);
                }
                return new ArrayList<>();
            }
        };

        commandMap.register(name, command);
    }

    @Deprecated(forRemoval = true)
    @SneakyThrows
    public void registerListenersFromClasses() {
        final Iterable<Class<?>> classes = getPluginClasses(basePackage);

        for (Class<?> clazz : classes) {
            if (Listener.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(RegisterListener.class)) {
                Constructor<?> constructor;
                Listener listener;

                try {
                    constructor = clazz.getConstructor(Plugin.class);
                    listener = (Listener) constructor.newInstance(plugin);
                } catch (NoSuchMethodException e) {
                    constructor = clazz.getConstructor();
                    listener = (Listener) constructor.newInstance();
                }

                this.registerListener(listener);
            }
        }
    }

    private void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @SneakyThrows
    private Iterable<Class<?>> getPluginClasses(String basePackage) {
        final List<Class<?>> classes = new ArrayList<>();
        final File pluginFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        final JarFile jarFile = new JarFile(pluginFile);
        final Enumeration<JarEntry> entries = jarFile.entries();
        final URLClassLoader classLoader = (URLClassLoader) plugin.getClass().getClassLoader();
        final String basePackagePath = basePackage.replace('.', '/');

        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String name = entry.getName();

            if (name.endsWith(".class") && !entry.isDirectory() && name.startsWith(basePackagePath)) {
                final String className = name.replace('/', '.').replace(".class", "");
                final Class<?> clazz = classLoader.loadClass(className);
                classes.add(clazz);
            }
        }
        return classes;
    }
}