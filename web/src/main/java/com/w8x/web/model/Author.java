package com.w8x.web.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Bean;

@Data
@ToString
public class Author {
    private String name;
    private String email;
    private String username;
}