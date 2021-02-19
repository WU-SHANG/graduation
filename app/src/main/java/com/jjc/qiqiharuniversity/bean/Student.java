package com.jjc.qiqiharuniversity.bean;

/**
 * Author jiajingchao
 * Created on 2021/1/3
 * Description:
 */
public class Student {
    /**
     * 学号 主键
     */
    private int sno;
    private String password;
    private String name;
    private String sex;
    /**
     * 学院
     */
    private String college;

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sno=" + sno +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", college='" + college + '\'' +
                '}';
    }
}
