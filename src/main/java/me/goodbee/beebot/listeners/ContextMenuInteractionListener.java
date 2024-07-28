package me.goodbee.beebot.listeners;

import me.goodbee.beebot.CommandManager;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ContextMenuInteractionListener extends ListenerAdapter {
    CommandManager commandManager;

    public ContextMenuInteractionListener(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event) {
        commandManager.executeCommand(event);
    }
}
