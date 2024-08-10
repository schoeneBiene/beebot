package me.goodbee.beebot;

import me.goodbee.beebot.commands.*;
import me.goodbee.beebot.commands.context.HelloContext;
import me.goodbee.beebot.commands.context.ImageCaptionContext;
import me.goodbee.beebot.commands.context.SummarizeContext;
import me.goodbee.beebot.listeners.ContextMenuInteractionListener;
import me.goodbee.beebot.listeners.ReadyListener;
import me.goodbee.beebot.listeners.SlashCommandInteractionListener;
import me.goodbee.beebot.util.GitInfo;
import me.goodbee.beebot.util.Secrets;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        String token = Secrets.DISCORD_TOKEN;
        Logger logger = LoggerFactory.getLogger("Main");

        GitInfo.init();

        JDABuilder builder = JDABuilder.createDefault(token);

        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        // Add event listeners
        builder.addEventListeners(new ReadyListener());

        JDA jda = builder.build();

        jda.awaitReady();

        // Register commands
        logger.info("Registering commands");

        CommandManager commandManager = new CommandManager(jda);

        new HelloWorldCommand(commandManager);
        new CatCommand(commandManager);
        new CatBreedCommand(commandManager);
        new WolframAlphaCommand(commandManager);
        new AIArtCommand(commandManager);
        new ErrorCommand(commandManager);
        new InfoCommand(commandManager);

        new HelloContext(commandManager);
        new SummarizeContext(commandManager);
        new ImageCaptionContext(commandManager);

        commandManager.pushCommands();
        jda.addEventListener(new SlashCommandInteractionListener(commandManager));
        jda.addEventListener(new ContextMenuInteractionListener(commandManager));
        logger.info("Commands registered!");
    }
}