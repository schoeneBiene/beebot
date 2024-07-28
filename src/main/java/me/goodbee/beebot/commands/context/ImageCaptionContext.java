package me.goodbee.beebot.commands.context;

import me.goodbee.beebot.CommandManager;
import me.goodbee.beebot.interfaces.ContextCommand;
import me.goodbee.beebot.util.Secrets;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ImageCaptionContext implements ContextCommand {
    HttpClient httpClient = HttpClient.newHttpClient();

    public ImageCaptionContext(CommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Override
    public CommandData getCommandData() {
        return Commands.context(Command.Type.MESSAGE, "Caption Image")
                .setContexts(InteractionContextType.ALL)
                .setIntegrationTypes(IntegrationType.ALL);
    }

    @Override
    public void execute(@NotNull MessageContextInteractionEvent event) throws Exception {
        if(event.getTarget().getAttachments().isEmpty()) {
            event.reply("Message has no attachments").queue();
            return;
        }

        Message.Attachment attachment = event.getTarget().getAttachments().get(0);

        if(!attachment.isImage()) {
            event.reply("Attachment is not an image").queue();
            return;
        }

        event.reply("Fetching image...").queue();

        HttpRequest getImageReq = HttpRequest.newBuilder(
                URI.create(attachment.getUrl()))
                .build();

        HttpResponse getImageRes = httpClient.send(getImageReq, HttpResponse.BodyHandlers.ofInputStream());

        InputStream inputStream = (InputStream) getImageRes.body();

        HttpRequest req = HttpRequest.newBuilder(
                URI.create("https://api-inference.huggingface.co/models/Salesforce/blip-image-captioning-large"))
                .setHeader("Authorization", "Bearer " + Secrets.HF_TOKEN)
                .POST(HttpRequest.BodyPublishers.ofByteArray(inputStream.readAllBytes()))
                .build();

        event.getHook().editOriginal("Processing image...").queue();
        HttpResponse res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

        System.out.println(res.body().toString());

        JSONArray jsonArray = new JSONArray(res.body().toString());

        String caption = jsonArray.getJSONObject(0).getString("generated_text");

        event.getHook().editOriginal(String.format(
                "Image caption for https://discord.com/channels/%s/%s/%s:\n\n%s\n\n-# Powered by [BLIP Captioning](<https://huggingface.co/Salesforce/blip-image-captioning-large>)",
                event.getGuild().getId(),
                event.getChannel().getId(),
                event.getTarget().getId(),
                caption
        )).queue();
    }
}
