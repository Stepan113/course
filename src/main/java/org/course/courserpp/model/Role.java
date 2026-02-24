package org.course.courserpp.model;

import lombok.Getter;

@Getter
public enum Role {

    GUARANTOR("Поручитель"),
    EXECUTOR("Исполнитель");

    private final String name;

    Role(String name) {
        this.name = name;
    }

}
