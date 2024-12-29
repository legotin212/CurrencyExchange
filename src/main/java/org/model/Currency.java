package org.model;

import lombok.Getter;

@Getter
public class Currency {
    private final int id;
    private final String name;
    private final String code;
    private final String sign;

    public Currency(int id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }
}
