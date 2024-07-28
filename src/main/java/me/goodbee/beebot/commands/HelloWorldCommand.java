package me.goodbee.beebot.commands;

import me.goodbee.beebot.CommandManager;
import me.goodbee.beebot.interfaces.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

public class HelloWorldCommand implements Command {
    public HelloWorldCommand(CommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("hello", "Hello World!")
                .setContexts(InteractionContextType.ALL)
                .setIntegrationTypes(IntegrationType.ALL);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.reply("Hello World!").setEphemeral(true).queue();
    }
}
