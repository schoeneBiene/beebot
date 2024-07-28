package me.goodbee.beebot.interfaces;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

public interface Command {
    CommandData getCommandData();

    void execute(@NotNull SlashCommandInteractionEvent event) throws Exception;
}
