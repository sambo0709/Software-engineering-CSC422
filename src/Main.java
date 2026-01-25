/*
I certify that this Java file I am submitting is all my own work.
Confirming it is not copied from any source.
Signed: Samuel Boye
Date: 01/18/2026
Class: CSC 422
File Name: Main.java
Assignment: Assignment 2 Part 2
Description: Pet Database Program with file load/save and error handling.
*/

import java.util.Scanner;

public class Main {

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next(); // discard bad token
            System.out.print("Please enter a valid number: ");
        }
        return sc.nextInt();
    }

    private static void printMenu() {
        System.out.println("Pet Database Program.");
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("1) View all pets");
        System.out.println("2) Add new pets");
        System.out.println("3) Remove a pet");
        System.out.println("4) Exit program");
        System.out.print("Your choice: ");
    }

    private static void addPetsFlow(Scanner sc, PetDatabase db) {
        System.out.println();
        sc.nextLine(); // consume leftover newline from menu choice

        int before = db.size();

        while (true) {
            System.out.print("add pet (name, age): ");
            String line = sc.nextLine().trim();

            if (line.equalsIgnoreCase("done")) break;
            if (line.isEmpty()) continue;

            if (db.isFull()) {
                System.out.println("Error: Database is full.");
                break;
            }

            String[] parts = line.split("\\s+");

            // Must be EXACTLY 2 values: name age
            if (parts.length != 2) {
                System.out.println("Error: " + line + " is not a valid input.");
                continue;
            }

            String name = parts[0];
            String ageToken = parts[1];

            int age;
            try {
                age = Integer.parseInt(ageToken);
            } catch (NumberFormatException e) {
                System.out.println("Error: " + ageToken + " is not a valid age.");
                continue;
            }

            if (!db.isValidAge(age)) {
                System.out.println("Error: " + age + " is not a valid age.");
                continue;
            }

            db.addPet(name, age);
        }

        int added = db.size() - before;
        System.out.println(added + " pets added.");
        System.out.println();
    }

    private static void removePetFlow(Scanner sc, PetDatabase db) {
        System.out.println();
        db.printAll();
        System.out.println();

        System.out.print("Enter the pet ID to remove: ");
        int id = readInt(sc);
        sc.nextLine(); // consume newline

        Pet removed = db.removePet(id);
        if (removed == null) {
            System.out.println("Error: ID " + id + " does not exist.");
        } else {
            System.out.println(removed.getName() + " " + removed.getAge() + " is removed.");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        final String FILE_NAME = "pets.txt";

        Scanner sc = new Scanner(System.in);
        PetDatabase db = new PetDatabase();

        // Milestone 3: load at startup
        db.loadFromFile(FILE_NAME);

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
                case 3 -> removePetFlow(sc, db);
                case 4 -> {
                    // Milestone 3: save on exit
                    db.saveToFile(FILE_NAME);
                    System.out.println();
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                }
                default -> {
                    System.out.println("Please choose 1-4.");
                    System.out.println();
                }
            }
        }
    }
}
