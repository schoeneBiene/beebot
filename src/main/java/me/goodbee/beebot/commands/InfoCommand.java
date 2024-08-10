package me.goodbee.beebot.commands;

import me.goodbee.beebot.CommandManager;
import me.goodbee.beebot.interfaces.Command;
import me.goodbee.beebot.util.Emoji;
import me.goodbee.beebot.util.GitInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class InfoCommand implements Command {
    public InfoCommand(CommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("info", "Get info about the bot")
                .setContexts(InteractionContextType.ALL)
                .setIntegrationTypes(IntegrationType.ALL);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) throws Exception {
        String message = String.format(
                "%s Version: %s  •  %s [%s](<https://github.com/schoeneBiene/beebot/commit/%s>) (%s %s)\n" +
                "%s Gateway Ping: %sms",

                Emoji.VERSION,
                GitInfo.VERSION,
                Emoji.COMMIT,
                GitInfo.COMMIT_ABBREV,
                GitInfo.COMMIT_FULL,
                Emoji.BRANCH,
                GitInfo.BRANCH,
                Emoji.PINGPONG,
                event.getJDA().getGatewayPing()
        );
        event.reply(message).queue();
    }
}
