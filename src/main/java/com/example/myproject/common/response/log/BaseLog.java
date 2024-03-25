package com.example.myproject.common.response.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;

public class BaseLog {
    protected static final ObjectMapper mapper = new ObjectMapper();
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BaseLog(){
    }
}
