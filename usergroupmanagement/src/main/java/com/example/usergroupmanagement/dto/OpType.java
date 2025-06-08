package com.example.usergroupmanagement.dto;

public enum OpType {
        ADD(1),
        REMOVE(2);


        private final int value;

        OpType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static OpType fromValue(int value) {
            for (OpType type : OpType.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid value: " + value);
        }
        public static OpType fromString(String name) {
            for (OpType type : OpType.values()) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid name: " + name);
        }
        public static OpType fromStringOrNull(String name) {
            for (OpType type : OpType.values()) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return null;
        }
        public static OpType fromValueOrNull(int value) {
            for (OpType type : OpType.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            return null;
        }
    }
