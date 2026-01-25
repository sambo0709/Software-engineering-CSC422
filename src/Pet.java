/*
I certify that this Java file I am submitting is all my own work.
None of it is copied from any source or any person.
Signed: Samuel Boye
Date: 01/18/2026
Class: CSC 422
File Name: Pet.java
Assignment: Assignment 2 Part 2
Description: Pet model class (name and age).
*/

public class Pet {

    private String name;
    private int age;

    public Pet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
}
