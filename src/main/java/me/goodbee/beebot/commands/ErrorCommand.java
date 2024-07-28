package me.goodbee.beebot.commands;

import me.goodbee.beebot.CommandManager;
import me.goodbee.beebot.interfaces.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class ErrorCommand implements Command {
    public ErrorCommand(CommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("error-test", "Generates an error")
                .setContexts(InteractionContextType.ALL)
                .setIntegrationTypes(IntegrationType.ALL);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) throws Exception {
        throw new Exception("Test exception");
    }
}
