package ru.dedus.common;

import lombok.Getter;

@Getter
public class UserCreationEvent {
    private final String username;
    private final Long id;

    public UserCreationEvent(String username, Long id) {
        this.username = username;
        this.id = id;
    }
}
