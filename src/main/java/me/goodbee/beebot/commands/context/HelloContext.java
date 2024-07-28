package me.goodbee.beebot.commands.context;

import me.goodbee.beebot.CommandManager;
import me.goodbee.beebot.interfaces.ContextCommand;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class HelloContext implements ContextCommand {

    public HelloContext(CommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Override
    public CommandData getCommandData() {
        return Commands.context(Command.Type.MESSAGE, "Hello Context!")
                .setContexts(InteractionContextType.ALL)
                .setIntegrationTypes(IntegrationType.ALL);
    }

    @Override
    public void execute(@NotNull MessageContextInteractionEvent event) throws Exception {
        event.reply("Hello Context Interactions! :wave:").setEphemeral(true).queue();
    }
}
