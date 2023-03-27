package com.example.phonebook.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import lombok.With;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@With
public class Response {
    ResponseStatus status;
    String message;
    Object data;
    public static Response ok(Object data){
        return new Response(ResponseStatus.ok,null,data);
    }

    public static Response fail(){
        return new Response(ResponseStatus.fail,null,null);
    }

}
