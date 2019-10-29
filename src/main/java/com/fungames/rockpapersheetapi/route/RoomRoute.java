package com.fungames.rockpapersheetapi.route;

public class RoomRoute {
    public static final String ROOT = BaseRoute.ROOT + "room/";

    public static final String ROOM_USER = ROOT + "user/";
    public static final String BY_ID = ROOT + "{id}/";
    public static final String RESULT = BY_ID + "result/";
}
