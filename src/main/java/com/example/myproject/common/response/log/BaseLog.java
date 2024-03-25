package com.example.myproject.common.response.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;

public class BaseLog {
    protected transient ObjectMapper mapper = new ObjectMapper();
    protected transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BaseLog(){
    }
}
