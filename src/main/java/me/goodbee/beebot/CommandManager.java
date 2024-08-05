package me.goodbee.beebot;

import me.goodbee.beebot.interfaces.Command;
import me.goodbee.beebot.interfaces.ContextCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    Logger logger = LoggerFactory.getLogger("CommandManager");
    Map<String, Command> commands = new HashMap<>();
    Map<String, ContextCommand> contextCommands = new HashMap<>();

    JDA jda;

    public CommandManager(JDA jda) {
        this.jda = jda;
    }

    /**
     * Adds a slash command to the CommandManager
     * @param command The {@link me.goodbee.beebot.interfaces.Command} to add
     */
    public void registerCommand(Command command) {
        logger.info("Registering command: " + command.getCommandData().getName());
        commands.put(command.getCommandData().getName(), command);
    }

    /**
     * Adds a context command to the CommandManager
     * @param command The {@link me.goodbee.beebot.interfaces.ContextCommand} to add
     */
    public void registerCommand(ContextCommand command) {
        logger.info("Registering context command: " + command.getCommandData().getName());
        contextCommands.put(command.getCommandData().getName(), command);
    }

    /**
     * Registers all added commands
     */
    public void pushCommands() {
        ArrayList<CommandData> data = new ArrayList<>();

        for (Command command : commands.values()) {
            data.add(command.getCommandData());
        }

        for (ContextCommand command : contextCommands.values()) {
            data.add(command.getCommandData());
        }

        jda.updateCommands().addCommands(data).queue();
    }

    /**
     * Executes a command
     * @param event The event of the slash command
     */
    public void executeCommand(SlashCommandInteractionEvent event) {
        try {
        commands.get(event.getName()).execute(event);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            if(!event.isAcknowledged()) {
                event.reply(String.format(":x: An error occurred while executing the command: %s", e.getMessage())).queue();
            } else {
                event.getHook().editOriginal(String.format(":x: An error occurred while executing the command: %s", e.getMessage())).queue();
            }
        }
    }

    /**
     * Executes a command
     * @param event The event of the slash command
     */
    public void executeCommand(MessageContextInteractionEvent event) {
        try {
            contextCommands.get(event.getName()).execute(event);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            if(!event.isAcknowledged()) {
                event.reply(String.format(":x: An error occurred while executing the command: %s", e.getMessage())).queue();
            } else {
                event.getHook().editOriginal(String.format(":x: An error occurred while executing the command: %s", e.getMessage())).queue();
            }
        }
    }
}
