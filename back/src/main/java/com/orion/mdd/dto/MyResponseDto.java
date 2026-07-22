package com.orion.mdd.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * class used to normalize the response to the client
 * MyResponseDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyResponseDto {

    /**
     * the message to send to the client
     */
    private String message;
    
    /**
     *  the code (a string)
     */ 
    private String code;

    /**
     * the data a map string object
     */
    private Map<String, Object> data;

}
