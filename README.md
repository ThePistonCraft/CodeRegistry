# CodeRegistry Plugin

## Overview

The **CodeRegistry** plugin is a Paper plugin that provides a framework for automatically registering commands without define the Commands in the plugin.yml and event listeners in Minecraft plugins. It utilizes custom annotations to identify and register commands and listeners from specified classes within your plugin package. This reduces boilerplate code and simplifies the registration process.

## Features

- **Automatic Command Registration**: Register commands by simply annotating your command classes.
- **Automatic Listener Registration**: Register event listeners by annotating your listener classes.
- **Simple Configuration**: Minimal setup required—just specify your package name, and everything is handled automatically.

## Installation

1. **Clone the Repository**: Clone the repository or download the package.

   ```bash
   git clone https://github.com/ThePistonCraft/CodeRegistry.git
   ```

2. **Compile the Plugin**: Use Maven or your preferred build tool to compile the plugin.

   ```bash
   mvn clean install
   ```

3. **Place the Plugin in Your Server's Plugin Folder**: Copy the generated `.jar` file to your Minecraft server's `plugins` directory.

4. **Run Your Server**: Start or restart your Minecraft server to load the plugin.

## Usage

### 1. Define Commands

To create a command, simply extend the `CommandBase` class and annotate your class with `@RegisterCommand`. The `CommandBase` constructor requires the plugin instance and a no-permission message.

Example:

```java
@RegisterCommand(
    name = "TestCommand",
    permission = "eincode.testcommand",
    description = "Test Description",
    aliases = {"tc"}
)
public class TestCommand extends CommandBase {

    public TestCommand(Plugin plugin, String noPermissionMessage) {
        super(plugin, "§cYou don't have permission to do that.");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("TestCommand executed");
        return true;
    }
}
```

### 2. Define Event Listeners

To create an event listener, implement the `Listener` interface and annotate your class with `@RegisterListener`.

Example:

```java
@RegisterListener
public class TestListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Hey! Welcome to this server.");
    }
}
```

### 3. Register Commands and Listeners

In your plugin's `onEnable` method, create a `Registry` instance and call the `initialize()` method. This method will automatically scan the specified package and register all commands and listeners.

Example:

```java
@Override
public void onEnable() {
    new Registry(this, "de.eincode.coderegistry").initialize();
}
```

If you're using the older methods `registerCommandsFromClasses()` and `registerListenersFromClasses()`, please note that they are now marked as deprecated and will be removed in a future version. It's recommended to switch to `initialize()`.

```java
@Deprecated(forRemoval = true)
public void registerCommandsFromClasses() {
    // Old command registration logic
}

@Deprecated(forRemoval = true)
public void registerListenersFromClasses() {
    // Old listener registration logic
}
```

## Compatibility

- **Tested Version**: This plugin has been tested with **Minecraft 1.21**.
- **Lower Versions**: The plugin may work with lower versions of Minecraft, but bugs may occur. Please report any issues you encounter on those versions.

## Customization

- **Annotations**: Modify the `@RegisterCommand` and `@RegisterListener` annotations as needed to suit your plugin's requirements.
- **Package Name**: Ensure that the base package name provided to the `Registry` constructor matches the package where your commands and listeners are located.

## Requirements

- **Minecraft Server**: Compatible with Bukkit/Spigot/Paper servers.
- **Java**: Ensure you are using Java 8 or later.
- **Paper API**: This plugin is built against the Paper API.

## Maven Repository

To use the CodeRegistry plugin in your own Maven projects, you need to add the Maven repository and dependency to your `pom.xml`:

### 1. Add Maven Repository

Add the following repository section to your `pom.xml` to include the GitHub Maven repository:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/ThePistonCraft/CodeRegistry</url>
    </repository>
</repositories>
```

### 2. Add Dependency

Add the following dependency to your `pom.xml` to include the CodeRegistry plugin:

```xml
<dependencies>
    <dependency>
        <groupId>de.eincode</groupId>
        <artifactId>coderegistry</artifactId>
        <version>1.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 3. Authentication

If the repository is private, you need to authenticate by adding your GitHub credentials to Maven's settings. Create or update the `settings.xml` file in your Maven configuration directory (`~/.m2/` or `C:\Users\<YourUsername>\.m2\`) with the following:

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>your-github-username</username>
            <password>your-github-token</password>
        </server>
    </servers>
</settings>
```

Replace `your-github-username` with your GitHub username and `your-github-token` with your GitHub Personal Access Token.

## Bug Reports

If you encounter any bugs or issues while using the CodeRegistry plugin, please feel free to:

1. **Open an Issue**: Report the bug by opening an issue on the [GitHub repository](https://github.com/ThePistonCraft/CodeRegistry/issues).
2. **Contact Me on Discord**: You can also reach out to me directly on Discord. My username is `einCode_`.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributions

Contributions are welcome! Please fork this repository, make your changes, and submit a pull request.
