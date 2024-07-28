package me.goodbee.beebot.commands.context;

import me.goodbee.beebot.CommandManager;
import me.goodbee.beebot.interfaces.ContextCommand;
import me.goodbee.beebot.util.Secrets;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SummarizeContext implements ContextCommand {
    HttpClient httpClient = HttpClient.newHttpClient();
    public SummarizeContext(CommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Override
    public CommandData getCommandData() {
        return Commands.context(Command.Type.MESSAGE, "Summarize")
                .setContexts(InteractionContextType.ALL)
                .setIntegrationTypes(IntegrationType.ALL);
    }

    @Override
    public void execute(@NotNull MessageContextInteractionEvent event) throws Exception {
        event.reply(":hourglass_flowing_sand: Summarizing...").queue();

        HttpRequest req = HttpRequest.newBuilder(
                URI.create("https://api-inference.huggingface.co/models/facebook/bart-large-cnn")
        ).setHeader("Authorization", "Bearer " + Secrets.HF_TOKEN)
                .setHeader("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(event.getTarget().getContentRaw()))
                .build();

        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

        if(res.statusCode() == 503) {
            JSONObject json = new JSONObject(res.body());

            event.getHook().editOriginal(String.format(":x: The model is loading, estimated time to load is %s, please try again later.", json.getString("estimated_time"))).queue();
        }

        System.out.println(res.body().toString());
        JSONArray jsonArray = new JSONArray(res.body().toString());

        JSONObject json = jsonArray.getJSONObject(0);

        event.getHook().editOriginal(String.format(
                "Summary of https://discord.com/channels/%s/%s/%s:\n\n%s\n\n-# Powered by [Facebook BART Large](<https://huggingface.co/facebook/bart-large-cnn>)",
                event.getGuild().getId(),
                event.getChannel().getId(),
                event.getTarget().getId(),
                json.getString("summary_text")
        )).queue();
    }
}
