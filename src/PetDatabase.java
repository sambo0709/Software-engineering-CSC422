/*
I certify that this Java file I am submitting is all my own work.
None of it is copied from any source or any person.
Signed: Samuel Boye
Date: 01/18/2026
Class: CSC 422
File Name: PetDatabase.java
Assignment: Assignment 2 Part 2
Description: Stores up to 5 pets, validates age, supports load/save, view, add, remove.
*/

import java.io.*;
import java.util.ArrayList;

public class PetDatabase {

    private static final int MAX_PETS = 5;
    private final ArrayList<Pet> pets = new ArrayList<>();

    public int size() {
        return pets.size();
    }

    public boolean isFull() {
        return pets.size() >= MAX_PETS;
    }

    public boolean isValidAge(int age) {
        return age >= 1 && age <= 20;
    }

    public void addPet(String name, int age) {
        if (!isFull()) {
            pets.add(new Pet(name, age));
        }
    }

    // Returns removed pet if valid, otherwise null
    public Pet removePet(int id) {
        if (id < 0 || id >= pets.size()) return null;
        return pets.remove(id);
    }

    public void printAll() {
        System.out.println("+----------------------+");
        System.out.println("| ID | NAME      | AGE |");
        System.out.println("+----------------------+");

        for (int i = 0; i < pets.size(); i++) {
            Pet p = pets.get(i);
            System.out.printf("| %2d | %-9s | %3d |%n",
                    i, truncate(p.getName(), 9), p.getAge());
        }

        System.out.println("+----------------------+");
        System.out.println(pets.size() + " rows in set.");
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max);
    }

    // Milestone 3: Load from file at startup
    public void loadFromFile(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null && !isFull()) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");
                if (parts.length != 2) continue;

                String name = parts[0];
                String ageToken = parts[1];

                int age;
                try {
                    age = Integer.parseInt(ageToken);
                } catch (NumberFormatException e) {
                    continue;
                }

                if (!isValidAge(age)) continue;

                addPet(name, age);
            }
        } catch (IOException e) {
            // Optional: ignore or print a simple message
            // System.out.println("Error reading file.");
        }
    }

    // Milestone 3: Save to file on exit
    public void saveToFile(String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            for (Pet p : pets) {
                out.println(p.getName() + " " + p.getAge());
            }
        } catch (IOException e) {
            // Optional: ignore or print a simple message
            // System.out.println("Error writing file.");
        }
    }
}
