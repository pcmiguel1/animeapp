package com.pcmiguel.anime;

public class Category {

    private String name;
    private int num;

    public Category(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }
}
