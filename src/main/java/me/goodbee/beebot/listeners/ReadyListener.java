package me.goodbee.beebot.listeners;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LoggerFactory.getLogger("ReadyListener").info("Logged in as " + event.getJDA().getSelfUser().getName());
    }
}
