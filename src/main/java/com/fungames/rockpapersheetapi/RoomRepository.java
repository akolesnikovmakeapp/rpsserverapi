package com.fungames.rockpapersheetapi;

import com.fungames.rockpapersheetapi.model.RoomModel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@NoArgsConstructor
public class RoomRepository {
    private List<RoomModel> rooms = new ArrayList<>();

    public Optional<RoomModel> findRoomById(UUID id){
        for (RoomModel model : rooms) {
            if(model.getId().equals(id)) {
                return Optional.of(model);
            }
        }
        return Optional.empty();
    }

    public RoomModel getEmptyRoom(){
        for (RoomModel model : rooms) {
            if(model.isWaitingToUsers()) {
                System.out.println("HERE1");
                return model;
            }
        }

        for (RoomModel model : rooms) {
            if(model.isEmptyRoom()) {
                System.out.println("HERE2");
                return model;
            }
        }

        for (RoomModel model : rooms) {
            if(model.isAbandoned()) {
                System.out.println("HERE3");
                model.clearRoom();
                return model;
            }
        }

        System.out.println("HERE4");
        return addRoom();
    }

    private RoomModel addRoom(){
        RoomModel model = new RoomModel();
        rooms.add(model);
        return model;
    }
}
