package com.fungames.rockpapersheetapi.controller;

import com.fungames.rockpapersheetapi.api.response.ApiResponse;
import com.fungames.rockpapersheetapi.api.response.GameResultApiResponse;
import com.fungames.rockpapersheetapi.api.response.RoomConnectApiResponse;
import com.fungames.rockpapersheetapi.api.response.RoomDataApiResponse;
import com.fungames.rockpapersheetapi.model.GameItem;
import com.fungames.rockpapersheetapi.route.RoomRoute;
import com.fungames.rockpapersheetapi.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RoomController {
    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @GetMapping(path = RoomRoute.ROOT)
    public ApiResponse<RoomConnectApiResponse> connectToRoom(){
        return ApiResponse.of(roomService.connectToRoom());
    }

    @GetMapping(path = RoomRoute.BY_ID)
    public ApiResponse<RoomDataApiResponse> getRoom(
            @PathVariable("id") UUID roomId,
            @RequestParam("userId") UUID userId
    ) {
        return ApiResponse.of(roomService.getRoom(roomId, userId));
    }

    @PutMapping(path = RoomRoute.BY_ID)
    public ApiResponse<String> setItem(
            @PathVariable("id") UUID roomId,
            @RequestParam("userId") UUID userId,
            @RequestParam("item") GameItem gameItem
    ){
        return ApiResponse.of(roomService.setItem(roomId, userId, gameItem));
    }

    @GetMapping(path = RoomRoute.RESULT)
    public ApiResponse<GameResultApiResponse> getResult(
            @PathVariable("id") UUID roomId,
            @RequestParam("userId") UUID userId,
            @RequestParam("gameId") UUID gameId
    ){
        return ApiResponse.of(roomService.getResult(roomId, userId, gameId));
    }

    @DeleteMapping(path = RoomRoute.ROOT)
    public ApiResponse<String> recreateRooms(){
        roomService.recreateRooms();
        return ApiResponse.of("OK");
    }
}
