package com.fungames.rockpapersheetapi.api.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomConnectApiApiResponse extends RoomDataApiResponse {
    private String userId;

    public static RoomConnectApiApiResponse of(RoomDataApiResponse data) {
        RoomConnectApiApiResponse connect = new RoomConnectApiApiResponse();
        connect.isAbandoned = data.isAbandoned;
        connect.isReadyToStart = data.isReadyToStart;
        connect.roomId = data.roomId;
        return connect;
    }
}
