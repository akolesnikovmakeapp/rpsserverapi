package com.fungames.rockpapersheetapi.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoomUserModel {
    private static final long MAX_ACTIVITY_DELAY = 60 * 1000; // 60 sec

    @Setter(AccessLevel.PRIVATE)
    private UUID id;
    private int score;
    private long lastActivity;

    public RoomUserModel(){
        id = UUID.randomUUID();
        lastActivity = System.currentTimeMillis();
    }

    public boolean isActive(){
        long currentTimeMillis = System.currentTimeMillis();
        if(currentTimeMillis - lastActivity <= MAX_ACTIVITY_DELAY) return true;
        return false;
    }
}
