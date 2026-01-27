package com.github.lucbf;

public enum ManaType{
    FIRE(0),
    ICE(1),
    ANY(2);
    private final int val;

    ManaType(int i) {
        this.val = i;
    }

    public int getVal() {
        return val;
    }
}