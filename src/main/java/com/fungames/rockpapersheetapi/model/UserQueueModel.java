package com.fungames.rockpapersheetapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserQueueModel {
    private static final int ABANDONED_TIME = 2 * 60 * 60 * 1000;
    private static final int ACTIVE_TIME = 1500;

    private UUID id;
    private UUID roomId;
    private long timestamp;

    public UserQueueModel(){
        id = UUID.randomUUID();
        timestamp = System.currentTimeMillis();
    }

    public boolean isActive(){
        return System.currentTimeMillis() - timestamp > ACTIVE_TIME;
    }

    public boolean isUselessModel(){
        return System.currentTimeMillis() - timestamp > ABANDONED_TIME;
    }
}
