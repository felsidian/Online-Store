package com.gmail.hryhoriev75.onlineshop.model.entity;

public class Parameter extends Entity {

    private String name;
    private Type type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        TEXT, NUMBER, SET;

        public boolean isText() {
            return this == TEXT;
        }

        public boolean isNumber() {
            return this == NUMBER;
        }

        public boolean isSet() {
            return this == SET;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
