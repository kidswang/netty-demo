package com.waiwaiwai.demo.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Account {
    private Integer id;
    private Long balance;
}
