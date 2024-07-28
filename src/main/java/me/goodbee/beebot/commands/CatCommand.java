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
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class CatCommand implements Command {
    HttpClient httpClient = HttpClient.newHttpClient();

    public CatCommand(CommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("cat", "Get a cat")
                .setContexts(InteractionContextType.ALL)
                .setIntegrationTypes(IntegrationType.ALL)
                .addOption(OptionType.STRING, "breed", "The breed of the cat", false);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        HttpRequest.Builder reqBuilder  = HttpRequest.newBuilder(
                URI.create("https://api.thecatapi.com/v1/images/search")
        )
                .header("x-api-key", Secrets.CAT_API_KEY);

        if(event.getOption("breed") != null) {
            reqBuilder.uri(URI.create("https://api.thecatapi.com/v1/images/search?breed_ids=" + Objects.requireNonNull(event.getOption("breed")).getAsString()));
        }

        HttpRequest req = reqBuilder.build();

        HttpResponse res;

        try {
            res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            event.reply("Something went wrong").queue();
            return;
        }

        JSONArray json = new JSONArray(res.body().toString());
        event.reply(json.getJSONObject(0).getString("url")).queue();
    }
}
