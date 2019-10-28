package com.fungames.rockpapersheetapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoomAnswerModel {
    private UUID id;
    private GameItem answerUser1;
    private GameItem answerUser2;

    public RoomAnswerModel() {
        id = UUID.randomUUID();
    }

    public static RoomAnswerModel of(RoomAnswerModel model){
        RoomAnswerModel ram = new RoomAnswerModel();
        ram.setAnswerUser1(model.getAnswerUser1());
        ram.setAnswerUser2(model.getAnswerUser2());
        return ram;
    }
}
