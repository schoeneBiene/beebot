package me.goodbee.beebot.commands;

import me.goodbee.beebot.CommandManager;
import me.goodbee.beebot.interfaces.Command;
import me.goodbee.beebot.util.Secrets;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.AttachedFile;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.DocFlavor;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class AIArtCommand implements Command {
    HttpClient httpClient = HttpClient.newHttpClient();
    Logger logger = LoggerFactory.getLogger("AIArtCommand");
    Map<String, String> models = new HashMap<>();

    public AIArtCommand(CommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Override
    public CommandData getCommandData() {
        models.put("stable-diffusion", "stabilityai/stable-diffusion-xl-base-1.0");
        models.put("pixel", "nerijs/pixel-art-xl");

        Map<String, SubcommandData> subcommands = new HashMap<>();

        for (Map.Entry<String, String> entry : models.entrySet()) {
            subcommands.put(entry.getKey(), new SubcommandData(entry.getKey(), "Generate an AI image using " + entry.getValue())
                    .addOption(OptionType.STRING, "prompt", "The prompt to use for the image.", true));

            logger.info("Registered subcommand " + entry.getKey());
        }

        SlashCommandData commandData = Commands.slash("ai-art", "Generate an AI image with Stable Diffusion.")
                .setContexts(InteractionContextType.ALL)
                .setIntegrationTypes(IntegrationType.ALL)
                .addSubcommands(subcommands.values());

        logger.debug(commandData.toString());

        return commandData;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) throws Exception {
        event.reply("Generating image...").queue();

        HttpRequest req = HttpRequest.newBuilder(
                URI.create("https://api-inference.huggingface.co/models/" + models.get(event.getSubcommandName())))
                .setHeader("Authorization", "Bearer " + Secrets.HF_TOKEN)
                .POST(HttpRequest.BodyPublishers.ofString(event.getOption("prompt").getAsString()))
                .timeout(Duration.ofMinutes(30))
                .build();

        HttpResponse res = httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream());


        InputStream inputStream = (InputStream) res.body();

        if(res.statusCode() == 503) {
            event.getHook().editOriginal("The model is being loaded, this may take several minutes.").queue();

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("inputs", event.getOption("prompt").getAsString());

            JSONObject jsonOptions = new JSONObject();
            jsonOptions.put("wait_for_model", true);

            jsonObject.put("options", jsonOptions);

            HttpRequest retryReq = HttpRequest.newBuilder(
                            URI.create("https://api-inference.huggingface.co/models/" + models.get(event.getSubcommandName())))
                    .setHeader("Authorization", "Bearer " + Secrets.HF_TOKEN)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                    .timeout(Duration.ofMinutes(30))
                    .build();

            inputStream = httpClient.send(retryReq, HttpResponse.BodyHandlers.ofInputStream()).body();
        }

        event.getHook().editOriginalAttachments(AttachedFile.fromData(inputStream, "image.jpeg")).queue();
        event.getHook().editOriginal("Here's your image!").queue();
    }
}
