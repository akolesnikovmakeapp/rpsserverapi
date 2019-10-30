package com.fungames.rockpapersheetapi.api.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomDataApiResponse {
    protected String roomId;
    protected boolean isAbandoned;
    protected boolean isReadyToNextRound;

    public static RoomDataApiResponse abandoned(){
        RoomDataApiResponse response = new RoomDataApiResponse();
        response.setAbandoned(true);
        response.setReadyToNextRound(false);
        return response;
    }
}
