package me.goodbee.beebot.util;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Util class to access the environment variables
 */
public class Secrets {
    public static Dotenv dotenv = Dotenv.load();

    /**
     * Hugging Face API Key
     */
    public static String HF_TOKEN = dotenv.get("HF_TOKEN");
    /**
     * Discord Bot Token
     */
    public static String DISCORD_TOKEN = dotenv.get("DISCORD_TOKEN");
    /**
     * CatAPI API Key
     */
    public static String CAT_API_KEY = dotenv.get("CAT_API_KEY");
    /**
     * Wolfram Short Answers App ID
     */
    public static String WOLFRAM_APP_ID_SHORT = dotenv.get("WOLFRAM_APP_ID_SHORT");
}
