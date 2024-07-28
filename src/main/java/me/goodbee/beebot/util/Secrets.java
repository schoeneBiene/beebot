package me.goodbee.beebot.util;

import io.github.cdimascio.dotenv.Dotenv;

public class Secrets {
    public static Dotenv dotenv = Dotenv.load();

    public static String HF_TOKEN = dotenv.get("HF_TOKEN");
    public static String DISCORD_TOKEN = dotenv.get("DISCORD_TOKEN");
    public static String CAT_API_KEY = dotenv.get("CAT_API_KEY");
    public static String WOLFRAM_APP_ID_SHORT = dotenv.get("WOLFRAM_APP_ID_SHORT");
}
