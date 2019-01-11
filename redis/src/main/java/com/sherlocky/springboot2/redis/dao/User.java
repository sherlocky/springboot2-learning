package com.sherlocky.springboot2.redis.dao;

/**
 * @author: zhangcx
 * @date: 2019/1/11 17:08
 */
public class User {
    private Long id;
    private String name;

    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "id: " + this.id + ", name: " + this.name;
    }
}
