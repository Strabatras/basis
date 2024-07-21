package ru.dedus.service;

import java.util.List;

public interface UserService {
    Long createUser(String userName);

    List<Long> createUser(List<String> userNameList);
}
