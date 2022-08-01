package org.pjj.transcation.entity;

/**
 * @author PengJiaJun
 * @Date 2022/08/01 18:58
 */
public class User {
    private Integer id;
    private String name;
    private Integer money;

    public User() {
    }

    public User(Integer id, String name, Integer money) {
        this.id = id;
        this.name = name;
        this.money = money;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}
