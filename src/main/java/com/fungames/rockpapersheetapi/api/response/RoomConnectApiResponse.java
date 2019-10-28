package com.fungames.rockpapersheetapi.api.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomConnectApiResponse extends RoomDataApiResponse {
    private String userId;

    public static RoomConnectApiResponse of(RoomDataApiResponse data) {
        RoomConnectApiResponse connect = new RoomConnectApiResponse();
        connect.isAbandoned = data.isAbandoned;
        connect.isReadyToStart = data.isReadyToStart;
        connect.roomId = data.roomId;
        return connect;
    }
}
