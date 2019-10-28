package com.fungames.rockpapersheetapi.api.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomDataApiResponse {
    protected String roomId;
    protected boolean isReadyToStart;
    protected boolean isAbandoned;

    public static RoomDataApiResponse abandoned(){
        RoomDataApiResponse response = new RoomDataApiResponse();
        response.setReadyToStart(false);
        response.setAbandoned(true);
        return response;
    }
}
