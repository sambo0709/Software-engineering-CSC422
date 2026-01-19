/*
I certify that this Java file I am submitting is all my own work.
None of it is copied from any source or any person.
Signed: Samuel Boye
Date: 01/18/2026
Class: CSC 422
File Name: PetDatabase.java
Assignment: Assignment 1 Part 2
Description: Stores pets and supports add/view/search/update/remove.
*/

import java.util.ArrayList;
import java.util.List;

public class PetDatabase {
    private final ArrayList<Pet> pets = new ArrayList<>();

    public void addPet(String name, int age) {
        pets.add(new Pet(name, age));
    }

    public int size() {
        return pets.size();
    }

    public Pet get(int index) {
        return pets.get(index);
    }

    public void updatePet(int index, String newName, int newAge) {
        Pet p = pets.get(index);
        p.setName(newName);
        p.setAge(newAge);
    }

    public Pet removePet(int index) {
        return pets.remove(index);
    }

    public List<Integer> searchByName(String name) {
        ArrayList<Integer> matches = new ArrayList<>();
        String target = name.trim().toLowerCase();
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getName().equalsIgnoreCase(target)) {
                matches.add(i);
            }
        }
        return matches;
    }

    public List<Integer> searchByAge(int age) {
        ArrayList<Integer> matches = new ArrayList<>();
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getAge() == age) {
                matches.add(i);
            }
        }
        return matches;
    }

    // Table printing for: view all, search name, search age
    // Widths: ID=3, NAME=10, AGE=4
    public void printTable(List<Integer> indices) {
        System.out.println("+----------------------+");
        System.out.printf("| %-3s| %-10s| %-4s|%n", "ID", "NAME", "AGE");
        System.out.println("+----------------------+");

        for (int idx : indices) {
            Pet p = pets.get(idx);
            System.out.printf("| %-3d| %-10s| %-4d|%n", idx, p.getName(), p.getAge());
        }

        System.out.println("+----------------------+");
        System.out.println(indices.size() + " rows in set.");
    }

    public void printAll() {
        ArrayList<Integer> all = new ArrayList<>();
        for (int i = 0; i < pets.size(); i++) all.add(i);
        printTable(all);
    }
}
