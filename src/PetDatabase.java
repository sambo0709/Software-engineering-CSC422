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

    public Pet get(int id) {
        return pets.get(id);
    }

    public void updatePet(int id, String newName, int newAge) {
        Pet p = pets.get(id);
        p.setName(newName);
        p.setAge(newAge);
    }

    public Pet removePet(int id) {
        return pets.remove(id);
    }

    // V2 feature: search by name (case-insensitive), returns matching IDs
    public List<Integer> searchByName(String name) {
        ArrayList<Integer> matches = new ArrayList<>();
        String target = name.trim();
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getName().equalsIgnoreCase(target)) {
                matches.add(i);
            }
        }
        return matches;
    }

    // V2 feature: search by age, returns matching IDs
    public List<Integer> searchByAge(int age) {
        ArrayList<Integer> matches = new ArrayList<>();
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getAge() == age) {
                matches.add(i);
            }
        }
        return matches;
    }

    public void printAll() {
        ArrayList<Integer> all = new ArrayList<>();
        for (int i = 0; i < pets.size(); i++) all.add(i);
        printTable(all);
    }

    public void printTable(List<Integer> ids) {
        System.out.println("+----------------------+");
        System.out.println("| ID | NAME      | AGE |");
        System.out.println("+----------------------+");

        for (int id : ids) {
            Pet p = pets.get(id);
            System.out.printf("| %2d | %-9s | %3d |%n", id, truncate(p.getName(), 9), p.getAge());
        }

        System.out.println("+----------------------+");
        System.out.println(ids.size() + " rows in set.");
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max);
    }
}
