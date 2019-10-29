package com.fungames.rockpapersheetapi.repository;

import com.fungames.rockpapersheetapi.model.RoomModel;
import com.fungames.rockpapersheetapi.model.UserQueueModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class RoomQueueRepository {
    @Getter(AccessLevel.PUBLIC)
    private List<UserQueueModel> users = new ArrayList<>();

    public void clearQueue(){
        users.stream().filter(UserQueueModel::isUselessModel).collect(Collectors.toList()).forEach(users::remove);
    }

    public UUID addUser(){
        clearQueue();

        UserQueueModel queueModel = new UserQueueModel();
        users.add(queueModel);
        return queueModel.getId();
    }

    public void removeUser(UUID userId){
        for (UserQueueModel model : users) {
            if(userId.equals(model.getId())) {
                users.remove(model);
                break;
            }
        }
    }

    public void actualizeUser(UUID uuid){
        Optional<UserQueueModel> oModel = findQueueByUserId(uuid);
        oModel.ifPresent(userQueueModel -> userQueueModel.setTimestamp(System.currentTimeMillis()));
    }

    public Optional<UserQueueModel> findQueueByUserId(UUID userId){
        for (UserQueueModel model : users) {
            if(userId.equals(model.getId())) return Optional.of(model);
        }
        return Optional.empty();
    }

    public UserQueueModel[] getPairOfUsers(){
        UserQueueModel[] users = new UserQueueModel[2];
        int i = 0;
        for (UserQueueModel model : this.users) {
            if (model.isActive() && model.getRoomId() == null)
                users[i] = model;
            else
                return null;
            if (++i > 1) break;
        }
        return users;
    }
}
