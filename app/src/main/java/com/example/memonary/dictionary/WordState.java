package com.example.memonary.dictionary;

public enum WordState {
    NOT_SAVED(0), DAY1(1), DAY2(2), DAY4(4), DAY8(8), DAY15(15), LEARNED(100);

    public int value;

    WordState(int value) {
        this.value = value;
    }

    public WordState next() {
        return values()[this.ordinal()+1];
    }
}
