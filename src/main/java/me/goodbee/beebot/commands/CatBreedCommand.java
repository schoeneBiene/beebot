package me.goodbee.beebot.commands;

import me.goodbee.beebot.CommandManager;
import me.goodbee.beebot.interfaces.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicReference;

public class CatBreedCommand implements Command {

    HttpClient httpClient = HttpClient.newHttpClient();
    public CatBreedCommand(CommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("catbreed", "Get info about a cat breed")
                .addOption(OptionType.STRING, "breed", "The breed of the cat", true)
                .setIntegrationTypes(IntegrationType.ALL)
                .setContexts(InteractionContextType.ALL);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        HttpRequest req = HttpRequest.newBuilder(
                URI.create("https://api.thecatapi.com/v1/breeds/"
                )).build();

        HttpResponse res;

        try {
            res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            event.reply("Something went wrong!").queue();
            return;
        }

        JSONArray json = new JSONArray(res.body().toString());

        System.out.println(json.toString());

        JSONObject breed = null;

        for(int i = 0; i < json.length(); i++) {
            JSONObject obj = json.getJSONObject(i);
            if(obj.getString("name").equals(event.getOption("breed").getAsString())) {
                breed = obj;
                break;
            }
        }

        if(breed == null) {
            event.reply("Breed not found!").queue();
            return;
        }

        MessageEmbed embed = new EmbedBuilder()
                .setTitle(breed.getString("name"))
                .setDescription(
                        String.format(
                                "%s\n\nOrigin: %s\nLifespan: %s years\n\n[Wikipedia](%s)",
                                breed.getString("description"),
                                breed.getString("origin"),
                                breed.getString("life_span"),
                                breed.getString("wikipedia_url")
                        )
                ).setImage(String.format("https://cdn2.thecatapi.com/images/%s.jpg", breed.getString("reference_image_id")))
                .setUrl(breed.getString("wikipedia_url"))
                .build();

        event.replyEmbeds(embed).queue();
    }
}
