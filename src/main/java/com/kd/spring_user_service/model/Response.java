package com.kd.spring_user_service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private String status;
    private String message;
    private String timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Response(){
        this.timestamp = LocalDateTime.now().toString();
    }

    public Response(String status, String message){
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
    }

    public Response(String status, String message, T data){
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>("200", "success", data);
    }

    public static <T> Response<T> success(String message,T data) {
        return new Response<>("200", message, data);
    }

    public static Response<Void> success() {
        return new Response<>("200", "success");
    }
    public static Response<Void> success(String message) {
        return new Response<>("200", message);
    }

    public static Response<Void> failure(String message) {
        return new Response<>("500", message);
    }

    public static Response<Void> badRequest(String message) {
        return new Response<>("400", message);
    }

    public static Response<Void> unauthorized() {
        return new Response<>("401", "unauthorized");
    }
    public static Response<Void> unauthorized(String message) {
        return new Response<>("401", message);
    }

    public static Response<Void> notFund(String message) {
        return new Response<>("404", message);
    }

    public static Response<Void> forbidden(String message) {
        return new Response<>("403", message);
    }

    public static Response error(String message) {
        return new Response<>("400", message);
    }
}