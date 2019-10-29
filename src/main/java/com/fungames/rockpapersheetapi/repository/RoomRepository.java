package com.fungames.rockpapersheetapi.repository;

import com.fungames.rockpapersheetapi.model.RoomModel;
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
public class RoomRepository {
    @Getter(AccessLevel.PUBLIC)
    private List<RoomModel> rooms = new ArrayList<>();

    public Optional<RoomModel> findRoomById(UUID id){
        for (RoomModel model : rooms) {
            if(model.getId().equals(id)) {
                return Optional.of(model);
            }
        }
        return Optional.empty();
    }

    private void clearRooms(){
        rooms.stream().filter(RoomModel::isAbandoned).collect(Collectors.toList()).forEach(room -> rooms.remove(room));
    }

    public RoomModel getEmptyRoom(){
        clearRooms();
        return addRoom();
    }

    private RoomModel addRoom(){
        RoomModel model = new RoomModel();
        rooms.add(model);
        return model;
    }
}
