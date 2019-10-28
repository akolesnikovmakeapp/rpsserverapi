package com.fungames.rockpapersheetapi.service;

import com.fungames.rockpapersheetapi.RoomRepository;
import com.fungames.rockpapersheetapi.api.response.GameResultApiResponse;
import com.fungames.rockpapersheetapi.api.response.RoomConnectApiApiResponse;
import com.fungames.rockpapersheetapi.api.response.RoomDataApiResponse;
import com.fungames.rockpapersheetapi.model.GameItem;
import com.fungames.rockpapersheetapi.model.RoomModel;
import com.fungames.rockpapersheetapi.model.RoomUserModel;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class RoomService {
    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    public RoomConnectApiApiResponse connectToRoom() {
        RoomUserModel roomUserModel = new RoomUserModel();
        RoomModel roomModel = roomRepository.getEmptyRoom();
        roomModel.addUser(roomUserModel);

        System.out.println("ID " + roomModel.getId().toString());
        System.out.println("USER1 NN " + (roomModel.getUser1() != null));
        System.out.println("USER2 NN " + (roomModel.getUser2() != null));

        RoomConnectApiApiResponse response = RoomConnectApiApiResponse.of(transformRoomModelToResponse.apply(roomModel));
        response.setUserId(roomUserModel.getId().toString());

        return response;
    }

    public RoomDataApiResponse getRoom(UUID roomId, UUID userId) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        System.out.println("HERE RESULT");
        if(modelOptional.isPresent()){
            System.out.println("HERE1 RESULT");
            if(modelOptional.get().hadAccess(userId)) {
                System.out.println("HERE2 RESULT");
                return transformRoomModelToResponse.apply(modelOptional.get());
            }
        }
        return RoomDataApiResponse.abandoned();
    }

    public void setItem(UUID roomId, UUID userId, GameItem item) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        if(modelOptional.isPresent()){
            RoomModel roomModel = modelOptional.get();
            if(roomModel.hadAccess(userId)) {
                roomModel.setUserAnswer(userId, item);
            }
        }
    }

    public GameResultApiResponse getResult(UUID roomId, UUID userId) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        if(modelOptional.isPresent()){
            RoomModel roomModel = modelOptional.get();
            if(roomModel.hadAccess(userId)) {
                GameResultApiResponse response = new GameResultApiResponse();
                response.setResultReady(roomModel.hasResult());
                response.setEnemyItem(roomModel.getEnemyItem(userId));
                response.setScore(roomModel.getScore().toString());
                return response;
            }
        }
        return GameResultApiResponse.failed();
    }

    private Function<RoomModel, RoomDataApiResponse> transformRoomModelToResponse = roomModel -> {
        RoomDataApiResponse response = new RoomDataApiResponse();
        response.setRoomId(roomModel.getId().toString());
        response.setAbandoned(roomModel.isAbandoned());
        response.setReadyToStart(roomModel.isReadyToStart());
        return response;
    };
}
