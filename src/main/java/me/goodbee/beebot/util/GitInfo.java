package me.goodbee.beebot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GitInfo {
    private static final Logger logger = LoggerFactory.getLogger("GitInfo");

    public static String BRANCH = "unset";
    public static String COMMIT_FULL = "unset";
    public static String COMMIT_ABBREV = "unset";
    public static String VERSION = "unset";

    public static void init() {
        logger.info("Reading git properties");

        Properties properties = new Properties();

        try (InputStream is = GitInfo.class.getClassLoader()
                .getResourceAsStream("git.properties")) {
            properties.load(is);
        }
        catch (IOException io) {
            logger.error("An error occurred while loading git info: ", io);

            BRANCH = "error";
            COMMIT_FULL = "error";
            COMMIT_ABBREV = "error";
            VERSION = "error";
        }

        BRANCH = properties.getProperty("git.branch");
        COMMIT_FULL = properties.getProperty("git.commit.id.full");
        COMMIT_ABBREV = properties.getProperty("git.commit.id.abbrev");
        VERSION = properties.getProperty("git.build.version");
    }
}
