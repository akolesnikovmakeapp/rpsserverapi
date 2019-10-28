package com.fungames.rockpapersheetapi.api.response;

import com.fungames.rockpapersheetapi.model.GameItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResultApiResponse {
    private boolean isResultReady;
    private boolean isFailed;
    private GameItem enemyItem;
    private String score;

    public static GameResultApiResponse failed(){
        GameResultApiResponse response = new GameResultApiResponse();
        response.setFailed(true);
        return response;
    }
}
