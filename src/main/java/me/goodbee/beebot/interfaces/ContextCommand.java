package me.goodbee.beebot.interfaces;

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public interface ContextCommand {
    CommandData getCommandData();

    void execute(@NotNull MessageContextInteractionEvent event) throws Exception;
}
