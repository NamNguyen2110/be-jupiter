package com.jupiter.common.base;

import com.jupiter.common.utils.StatusCodeUtils;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

    private String code;
    private String message;
    private Object responseMessage;
    private Long timestamp;
    private T data;

    public static ApiResponse<Object> ok(Object obj) {
        return ApiResponse.<Object>builder()
                .code(StatusCodeUtils.OK)
                .message("success")
                .timestamp(System.currentTimeMillis())
                .data(obj)
                .build();
    }

}
