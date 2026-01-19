/*
I certify that this Java file I am submitting is all my own work.
Confirming it is not copied from any source.
Signed: Samuel Boye
Date: 01/18/2026
Class: CSC 422
File Name: Main.java
Assignment: Assignment 1 Part 2
Description: Console UI for Pet Database Program with 3 releases.
*/

import java.util.List;
import java.util.Scanner;

public class Main {

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next(); // discard
            System.out.print("Please enter a valid number: ");
        }
        return sc.nextInt();
    }

    private static void confirmsSingleWordName(String name) {
        // Assignment says you can assume single word; no hard validation required.
        // This method exists only to keep intent clear.
    }

    private static void addPetsFlow(Scanner sc, PetDatabase db) {
        System.out.println();
        sc.nextLine(); // consume leftover newline

        int before = db.size();

        while (true) {
            System.out.print("add pet (name, age): ");
            String line = sc.nextLine().trim();

            if (line.equalsIgnoreCase("done")) break;
            if (line.isEmpty()) continue;

            // Expect: "Name Age"
            String[] parts = line.split("\\s+");
            if (parts.length < 2) {
                // error handling optional; keep simple
                continue;
            }

            String name = parts[0];
            confirmsSingleWordName(name);

            int age;
            try {
                age = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                continue;
            }

            db.addPet(name, age);
        }

        int added = db.size() - before;
        System.out.println(added + " pets added.");
        System.out.println();
    }

    private static void updatePetFlow(Scanner sc, PetDatabase db) {
        System.out.println();
        db.printAll();
        System.out.println();

        System.out.print("Enter the pet ID you want to update: ");
        int id = readInt(sc);
        sc.nextLine(); // consume newline

        if (id < 0 || id >= db.size()) {
            System.out.println("Invalid ID.");
            return;
        }

        System.out.println("Enter new name and new age:");
        String line = sc.nextLine().trim();
        String[] parts = line.split("\\s+");
        if (parts.length < 2) explainedReturn();

        String newName = parts[0];
        int newAge;
        try {
            newAge = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid age.");
            return;
        }

        String oldName = db.get(id).getName();
        int oldAge = db.get(id).getAge();

        db.updatePet(id, newName, newAge);

        System.out.printf("%s %d changed to %s %d.%n", oldName, oldAge, newName, newAge);
        System.out.println();
    }

    private static void explainedReturn() {
        // helper to keep code short; shouldn't happen in normal use
        System.out.println("Invalid input.");
        throw new IllegalArgumentException();
    }

    private static void removePetFlow(Scanner sc, PetDatabase db) {
        System.out.println();
        System.out.print("Enter the pet ID to remove: ");
        int id = readInt(sc);
        sc.nextLine(); // consume newline

        if (id < 0 || id >= db.size()) {
            System.out.println("Invalid ID.");
            return;
        }

        Pet removed = db.removePet(id);
        System.out.println(removed.getName() + " is removed.");
        System.out.println();
    }

    private static void searchByNameFlow(Scanner sc, PetDatabase db) {
        System.out.println();
        sc.nextLine(); // consume leftover newline
        System.out.print("Enter a name to search: ");
        String name = sc.nextLine().trim();

        List<Integer> matches = db.searchByName(name);
        System.out.println();
        db.printTable(matches);
        System.out.println();
    }

    private static void searchByAgeFlow(Scanner sc, PetDatabase db) {
        System.out.println();
        System.out.print("Enter age to search: ");
        int age = readInt(sc);
        sc.nextLine(); // consume newline

        List<Integer> matches = db.searchByAge(age);
        System.out.println();
        db.printTable(matches);
        System.out.println();
    }

    private static void printMenu() {
        System.out.println("Pet Database Program.");
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("1) View all pets");
        System.out.println("2) Add more pets");
        System.out.println("3) Update an existing pet");
        System.out.println("4) Remove an existing pet");
        System.out.println("5) Search pets by name");
        System.out.println("6) Search pets by age");
        System.out.println("7) Exit program");
        System.out.print("Your choice: ");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PetDatabase db = new PetDatabase();

        while (true) {
            printMenu();
            int choice = readInt(sc);

            switch (choice) {
                case 1 -> {
                    System.out.println();
                    db.printAll();
                    System.out.println();
                }
                case 2 -> addPetsFlow(sc, db);
                case 3 -> updatePetFlow(sc, db);
                case 4 -> removePetFlow(sc, db);
                case 5 -> searchByNameFlow(sc, db);
                case 6 -> searchByAgeFlow(sc, db);
                case 7 -> {
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                }
                default -> {
                    System.out.println("Please choose 1-7.");
                    System.out.println();
                }
            }
        }
    }
}
