package ru.dedus.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.dedus.common.UserCreationEvent;
import ru.dedus.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public Long createUser(String userName) {
        // logic to create a user and store it in a database
        Long primaryKey = ThreadLocalRandom.current().nextLong(1, 1000);

        this.eventPublisher.publishEvent(new UserCreationEvent(userName, primaryKey));

        return primaryKey;
    }

    public List<Long> createUser(List<String> userNameList) {
        List<Long> resultIds = new ArrayList<>();

        for (String userName : userNameList) {
            resultIds.add(createUser(userName));
        }
        return resultIds;
    }
}
