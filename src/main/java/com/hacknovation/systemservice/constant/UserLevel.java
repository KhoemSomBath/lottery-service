package com.hacknovation.systemservice.constant;

import java.util.Objects;

public enum UserLevel {
    SUPPER(1, "SUPPER"),
    ADMIN(2, "ADMIN"),
    STAFF(3, "STAFF"),
    SENIOR(4, "SENIOR"),
    MASTER(5, "MASTER"),
    AGENT(6, "AGENT"),
    MEMBER(7, "MEMBER");

    private final String name;
    private int level;

    UserLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

    private String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public static UserLevel getByName(String name) {
        if (Objects.equals(name, SUPPER.name()))
            return SUPPER;
        if (Objects.equals(name, ADMIN.name()))
            return ADMIN;
        if (Objects.equals(name, STAFF.name()))
            return STAFF;
        if (Objects.equals(name, SENIOR.name()))
            return SENIOR;
        if (Objects.equals(name, MASTER.name()))
            return MASTER;
        if (Objects.equals(name, AGENT.name()))
            return AGENT;
        return MEMBER;
    }

    public boolean isCheck(UserLevel level) {
        return this.level < level.getLevel();
    }
}
