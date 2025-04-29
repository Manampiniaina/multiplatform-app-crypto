package com.crypto.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Wrapper {
    
    private ObjectMapper objectMapper ; 

    public Wrapper() {
        setObjectMapper(new ObjectMapper());
    }

    public String enJSON(Object object) throws Exception{

        String jsonString = null ;
        try {

            jsonString = objectMapper.writeValueAsString(object);

        } catch (Exception e) {
            e.printStackTrace();
            throw e ;
        }
        return jsonString ;
    }
}
