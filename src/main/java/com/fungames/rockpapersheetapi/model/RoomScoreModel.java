package com.fungames.rockpapersheetapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoomScoreModel {
    private UUID id;
    private int user1;
    private int user2;

    @Override
    public String toString() {
        return user1 + " / " + user2;
    }
}
