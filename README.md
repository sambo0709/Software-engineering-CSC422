# Pet Database — CSC 422

**Assignment 2 Part 2 | Samuel Boye | 01/18/2026**

A JavaFX desktop application for managing a small pet database. Supports adding, removing, and searching pets with persistent file storage.

---

## Features

- Dark-themed JavaFX GUI
- Add pets with name and age validation
- Remove pets by clicking the ✕ button
- Live search/filter by name
- Stat cards showing total pets and average age
- Auto-saves to `pets.txt` on exit, auto-loads on startup
- Export to `pets.txt` or `pets.csv`

---

## Project Structure

```
src/
├── Main.java          # JavaFX GUI and application entry point
├── PetDatabase.java   # Database logic (add, remove, load, save)
└── Pet.java           # Pet model class (name, age)
pets.txt               # Auto-generated data file (created on first run)
```

---

## Requirements

- Java 25 (Temurin recommended)
- JavaFX SDK 21.0.2
- IntelliJ IDEA

---

## Setup

### 1. Install JavaFX

Download JavaFX SDK 21.0.2 from [gluonhq.com](https://gluonhq.com/products/javafx/) and unzip it to:

```
/Users/<yourname>/javafx-sdk-21.0.2/
```

Or run in Terminal:

```bash
curl -L https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2_osx-aarch64_bin-sdk.zip -o ~/javafx.zip && unzip ~/javafx.zip -d ~/
```

### 2. Add JavaFX to IntelliJ

1. Open **File → Project Structure** (`⌘ ;`)
2. Go to **Libraries → + → Java**
3. Navigate to `/Users/<yourname>/javafx-sdk-21.0.2/lib` and select it
4. Click **OK → Apply → OK**

### 3. Add VM Options

1. Click the **Main** dropdown (top right) → **Edit Configurations**
2. Click **Modify options → Add VM options**
3. Paste:

```
--module-path /Users/<yourname>/javafx-sdk-21.0.2/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base
```

4. Click **OK**

### 4. Run

Hit the green **▶ Run** button in IntelliJ.

---

## Usage

| Action | How |
|--------|-----|
| Add a pet | Enter name and age in the form, press **+ Add Pet** or hit Enter |
| Remove a pet | Click the **✕** button on any row |
| Search | Type in the search bar to filter by name, press Escape to clear |
| Export | Click **⬇ Export pets.txt** or **⬇ Export .csv** |

### Validation Rules

- Name must be a single word (no spaces)
- Age must be between 1 and 20
- Maximum 5 pets in the database at once

---

## Data File

`pets.txt` is created automatically in the project root. Each line contains a pet name and age:

```
Whiskers 4
Rex 7
Buddy 3
```

The file is loaded on startup and saved on exit.

---

## Notes

- Warnings about `sun.misc.Unsafe` and restricted methods are harmless — they come from JavaFX 21 running on Java 25 and do not affect functionality.
- The `_tmp_shadow.txt` file is a temporary file used internally and can be ignored.
