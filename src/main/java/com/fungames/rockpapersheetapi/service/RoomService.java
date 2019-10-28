package com.fungames.rockpapersheetapi.service;

import com.fungames.rockpapersheetapi.RoomRepository;
import com.fungames.rockpapersheetapi.api.response.GameResultApiResponse;
import com.fungames.rockpapersheetapi.api.response.RoomConnectApiResponse;
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

    public void recreateRooms(){
        roomRepository.getRooms().clear();
    }

    public RoomConnectApiResponse connectToRoom() {
        RoomUserModel roomUserModel = new RoomUserModel();
        RoomModel roomModel = roomRepository.getEmptyRoom();
        roomModel.addUser(roomUserModel);

        RoomConnectApiResponse response = RoomConnectApiResponse.of(transformRoomModelToResponse.apply(roomModel));
        response.setUserId(roomUserModel.getId().toString());

        return response;
    }

    public RoomDataApiResponse getRoom(UUID roomId, UUID userId) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        if(modelOptional.isPresent()){
            if(modelOptional.get().hadAccess(userId)) {
                return transformRoomModelToResponse.apply(modelOptional.get());
            }
        }
        return RoomDataApiResponse.abandoned();
    }

    public void leaveRoom(UUID roomId, UUID userId) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        if(modelOptional.isPresent()){
            RoomModel roomModel = modelOptional.get();
            if(roomModel.hadAccess(userId)) {
                roomModel.getUser(userId).ifPresent(user -> user.setLastActivity(0));
            }
        }
    }

    public String setItem(UUID roomId, UUID userId, GameItem item) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        if(modelOptional.isPresent()){
            RoomModel roomModel = modelOptional.get();
            if(roomModel.hadAccess(userId)) {
                return roomModel.setUserAnswer(userId, item).toString();
            }
        }
        return "";
    }

    public GameResultApiResponse getResult(UUID roomId, UUID userId, UUID gameId) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        if(modelOptional.isPresent()){
            RoomModel roomModel = modelOptional.get();
            if(roomModel.hadAccess(userId) && !roomModel.isAbandoned()) {
                if(roomModel.getResult().getId().equals(gameId)) {
                    GameResultApiResponse response = new GameResultApiResponse();
                    response.setResultReady(roomModel.hasResult());
                    response.setEnemyItem(roomModel.getEnemyItem(userId));
                    response.setScore(roomModel.getScore().toString());
                    return response;
                } else {
                    return GameResultApiResponse.notReady();
                }
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
