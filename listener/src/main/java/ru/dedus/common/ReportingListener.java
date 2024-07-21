package ru.dedus.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReportingListener {

    private static final Logger logger = LoggerFactory.getLogger(ReportingListener.class);

    @EventListener({UserCreationEvent.class})
    public void reportUserCreation(UserCreationEvent event) {
        logger.atInfo()
                .setMessage("Increment counter as new user was created: " + event.getUsername())
                .log();
    }

    @EventListener(UserCreationEvent.class)
    public void syncUserToExternalSystem(UserCreationEvent event) {
        logger.atInfo()
                .setMessage("Informing other systems about new user: " + event.getUsername())
                .log();
    }
}
