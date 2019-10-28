package com.fungames.rockpapersheetapi.controller;

import com.fungames.rockpapersheetapi.api.response.ApiResponse;
import com.fungames.rockpapersheetapi.api.response.GameResultApiResponse;
import com.fungames.rockpapersheetapi.api.response.RoomConnectApiApiResponse;
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
    public ApiResponse<RoomConnectApiApiResponse> connectToRoom(){
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
        roomService.setItem(roomId, userId, gameItem);
        return ApiResponse.of("OK");
    }

    @GetMapping(path = RoomRoute.RESULT)
    public ApiResponse<GameResultApiResponse> getResult(
            @PathVariable("id") UUID roomId,
            @RequestParam("userId") UUID userId
    ){
        return ApiResponse.of(roomService.getResult(roomId, userId));
    }
}
