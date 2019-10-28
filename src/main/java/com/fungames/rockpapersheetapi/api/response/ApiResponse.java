package com.fungames.rockpapersheetapi.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private T result;

    public static <T> ApiResponse<T> of(T result){
        ApiResponse<T> response = new ApiResponse<>();
        response.setResult(result);
        return response;
    }
}
