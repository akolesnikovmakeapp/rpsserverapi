package com.fungames.rockpapersheetapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoomGameModel {
    private UUID id;
    private GameItem answerUser1;
    private GameItem answerUser2;

    public RoomGameModel() {
        id = UUID.randomUUID();
    }

    public static RoomGameModel of(RoomGameModel model){
        RoomGameModel ram = new RoomGameModel();
        ram.setId(model.getId());
        ram.setAnswerUser1(model.getAnswerUser1());
        ram.setAnswerUser2(model.getAnswerUser2());
        return ram;
    }
}
