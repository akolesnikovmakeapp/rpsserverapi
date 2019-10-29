package com.fungames.rockpapersheetapi.service;

import com.fungames.rockpapersheetapi.model.UserQueueModel;
import com.fungames.rockpapersheetapi.repository.RoomQueueRepository;
import com.fungames.rockpapersheetapi.repository.RoomRepository;
import com.fungames.rockpapersheetapi.api.response.GameResultApiResponse;
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
    private RoomQueueRepository roomQueueRepository;

    public RoomService(RoomRepository roomRepository, RoomQueueRepository roomQueueRepository){
        this.roomRepository = roomRepository;
        this.roomQueueRepository = roomQueueRepository;
    }

    public UUID regToQueue() {
        return roomQueueRepository.addUser();
    }

    public UUID connectToRoom(UUID userId) {
        roomQueueRepository.actualizeUser(userId);

        Optional<UserQueueModel> oModel = roomQueueRepository.findQueueByUserId(userId);
        if(oModel.isPresent() && oModel.get().getRoomId() != null) {
            roomQueueRepository.removeUser(userId);
            return oModel.get().getRoomId();
        } else {
            UserQueueModel[] pair = roomQueueRepository.getPairOfUsers();
            if(pair == null) return null;

            RoomModel roomModel = roomRepository.getEmptyRoom();
            for (UserQueueModel model : pair) {
                roomModel.addUser(new RoomUserModel(model.getId()));
                model.setRoomId(roomModel.getId());
            }

            roomQueueRepository.removeUser(userId);
            return roomModel.getId();
        }
    }

    public RoomDataApiResponse getRoom(UUID roomId, UUID userId) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        if(modelOptional.isPresent()){
            if(modelOptional.get().hasAccess(userId)) {
                return transformRoomModelToResponse.apply(modelOptional.get());
            }
        }
        return RoomDataApiResponse.abandoned();
    }

    public void leaveRoom(UUID roomId, UUID userId) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        if(modelOptional.isPresent()){
            RoomModel roomModel = modelOptional.get();
            if(roomModel.hasAccess(userId)) {
                roomModel.getUser(userId).ifPresent(user -> user.setLastActivity(0));
            }
        }
    }

    public String setItem(UUID roomId, UUID userId, GameItem item) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        if(modelOptional.isPresent()){
            RoomModel roomModel = modelOptional.get();
            if(roomModel.hasAccess(userId)) {
                return roomModel.setUserAnswer(userId, item).toString();
            }
        }
        return "NOT_FOUND";
    }

    public GameResultApiResponse getResult(UUID roomId, UUID userId, UUID gameId) {
        Optional<RoomModel> modelOptional = roomRepository.findRoomById(roomId);
        if(modelOptional.isPresent()){
            RoomModel roomModel = modelOptional.get();
            if(roomModel.hasAccess(userId) && !roomModel.isAbandoned()) {
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
        return response;
    };
}
