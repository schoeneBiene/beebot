package me.goodbee.beebot.commands;

import me.goodbee.beebot.CommandManager;
import me.goodbee.beebot.interfaces.Command;
import me.goodbee.beebot.util.Secrets;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class WolframAlphaCommand implements Command {
    HttpClient httpClient = HttpClient.newHttpClient();

    public WolframAlphaCommand(CommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("wolfram", "a")
                .addSubcommands(
                        new SubcommandData("short", "Short answers")
                                .addOption(OptionType.STRING, "query", "The query", true)
                )
                .setContexts(InteractionContextType.ALL)
                .setIntegrationTypes(IntegrationType.ALL);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) throws URISyntaxException, IOException, InterruptedException {
        if(Objects.equals(event.getSubcommandName(), "short")) {
            HttpRequest req = HttpRequest.newBuilder(
                    URI.create(String.format(
                            "http://api.wolframalpha.com/v1/result?appid=%s&i=%s",
                            Secrets.WOLFRAM_APP_ID_SHORT,
                            URLEncoder.encode(event.getOption("query").getAsString(), StandardCharsets.UTF_8)
                    ))
            ).build();

            HttpResponse res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

            event.reply(String.format("**%s**\n\n%s", event.getOption("query").getAsString(), (String) res.body())).queue();
        }
    }
}
