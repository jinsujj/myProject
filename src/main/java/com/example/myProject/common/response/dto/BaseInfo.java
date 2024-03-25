package com.example.myproject.common.response.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseInfo {
    protected transient ObjectMapper mapper = new ObjectMapper();
}
