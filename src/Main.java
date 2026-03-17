/*
I certify that this Java file I am submitting is all my own work.
None of it is copied from any source or any person.
Signed: Samuel Boye
Date: 01/18/2026
Class: CSC 422
File Name: Main.java
Assignment: Assignment 2 Part 2
Description: Pet Database Program with JavaFX GUI, file load/save, search, and error handling.
*/

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class Main extends Application {

    private static final String FILE_NAME = "pets.txt";
    private static final int MAX_PETS = 5;

    // Master pet list — source of truth for the UI
    private final ArrayList<String> petNames = new ArrayList<>();
    private final ArrayList<Integer> petAges  = new ArrayList<>();

    private final ObservableList<PetRow> petRows = FXCollections.observableArrayList();

    private Label countLabel;
    private Label avgLabel;
    private Label statusLabel;

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        loadFromFile(FILE_NAME);
        refreshRows();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0d1117;");
        root.setTop(buildHeader());
        root.setCenter(buildCenter());
        root.setBottom(buildFooter());

        Scene scene = new Scene(root, 780, 640);
        scene.setFill(Color.web("#0d1117"));

        stage.setTitle("Pet Database · CSC 422");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.show();
    }

    @Override
    public void stop() {
        saveToFile(FILE_NAME);
    }

    // ── File I/O ──────────────────────────────────────────────────────────────

    private void loadFromFile(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null && petNames.size() < MAX_PETS) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                if (parts.length != 2) continue;
                try {
                    int age = Integer.parseInt(parts[1]);
                    if (age >= 1 && age <= 20) {
                        petNames.add(parts[0]);
                        petAges.add(age);
                    }
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException ignored) {}
    }

    private void saveToFile(String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            for (int i = 0; i < petNames.size(); i++) {
                out.println(petNames.get(i) + " " + petAges.get(i));
            }
        } catch (IOException ignored) {}
    }

    // ── Refresh table ─────────────────────────────────────────────────────────

    private void refreshRows() {
        petRows.clear();
        for (int i = 0; i < petNames.size(); i++) {
            petRows.add(new PetRow(i, petNames.get(i), petAges.get(i)));
        }
        if (countLabel != null) countLabel.setText(String.valueOf(petNames.size()));
        if (avgLabel != null) {
            if (petAges.isEmpty()) {
                avgLabel.setText("—");
            } else {
                double avg = petAges.stream().mapToInt(Integer::intValue).average().orElse(0);
                avgLabel.setText(String.format("%.1f", avg));
            }
        }
        if (statusLabel != null) {
            statusLabel.setText(petNames.size() + " record" + (petNames.size() != 1 ? "s" : ""));
        }
    }

    // ── Header ────────────────────────────────────────────────────────────────

    private HBox buildHeader() {
        Label paw = new Label("🐾");
        paw.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-background-color: #3fb950;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 6 8 6 8;"
        );

        Label title = new Label("Pet Database");
        title.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #e6edf3;");

        Label sub = new Label("CSC 422 · Assignment 2 Part 2");
        sub.setStyle("-fx-font-size: 11px; -fx-text-fill: #8b949e;");

        VBox titleBox = new VBox(2, title, sub);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label badge = new Label("● LIVE");
        badge.setStyle(
                "-fx-background-color: #1a4a24;" +
                        "-fx-text-fill: #3fb950;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-family: 'Courier New';" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 3 10 3 10;" +
                        "-fx-border-color: #2ea043;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-width: 1;"
        );

        HBox header = new HBox(12, paw, titleBox, badge);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setStyle(
                "-fx-background-color: #161b22;" +
                        "-fx-border-color: #30363d;" +
                        "-fx-border-width: 0 0 1 0;"
        );
        HBox.setHgrow(titleBox, Priority.ALWAYS);
        return header;
    }

    // ── Center ────────────────────────────────────────────────────────────────

    private VBox buildCenter() {
        VBox center = new VBox(12);
        center.setPadding(new Insets(16, 20, 8, 20));
        center.setStyle("-fx-background-color: #0d1117;");
        center.getChildren().addAll(buildStatCards(), buildTableSection(), buildAddSection());
        return center;
    }

    // ── Stat Cards ────────────────────────────────────────────────────────────

    private HBox buildStatCards() {
        countLabel = new Label("0");
        countLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #58a6ff; -fx-font-family: 'Courier New';");
        Label countSub = new Label("of 5 slots used");
        countSub.setStyle("-fx-font-size: 11px; -fx-text-fill: #8b949e;");
        VBox countCard = statCard("TOTAL PETS", countLabel, countSub);

        avgLabel = new Label("—");
        avgLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #bc8cff; -fx-font-family: 'Courier New';");
        Label avgSub = new Label("valid range: 1–20 yrs");
        avgSub.setStyle("-fx-font-size: 11px; -fx-text-fill: #8b949e;");
        VBox avgCard = statCard("AVERAGE AGE", avgLabel, avgSub);

        HBox row = new HBox(12, countCard, avgCard);
        HBox.setHgrow(countCard, Priority.ALWAYS);
        HBox.setHgrow(avgCard, Priority.ALWAYS);
        countCard.setMaxWidth(Double.MAX_VALUE);
        avgCard.setMaxWidth(Double.MAX_VALUE);
        return row;
    }

    private VBox statCard(String labelText, Label valueLabel, Label subLabel) {
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #8b949e; -fx-font-weight: bold;");
        VBox card = new VBox(4, lbl, valueLabel, subLabel);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setStyle(
                "-fx-background-color: #161b22;" +
                        "-fx-border-color: #30363d;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-width: 1;"
        );
        return card;
    }

    // ── Table Section ─────────────────────────────────────────────────────────

    private VBox buildTableSection() {
        Label dot = new Label("●");
        dot.setStyle("-fx-text-fill: #3fb950; -fx-font-size: 10px;");
        Label hdr = new Label("Pet Records");
        hdr.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 12px; -fx-font-weight: bold;");
        HBox secHeader = new HBox(6, dot, hdr);
        secHeader.setPadding(new Insets(8, 12, 8, 12));
        secHeader.setStyle("-fx-background-color: #1c2332; -fx-border-color: #30363d; -fx-border-width: 0 0 1 0;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name…");
        styleInput(searchField);

        Button clearBtn = styledButton("Clear");
        clearBtn.setOnAction(e -> searchField.clear());

        HBox searchRow = new HBox(8, searchField, clearBtn);
        searchRow.setPadding(new Insets(8, 12, 8, 12));
        searchRow.setAlignment(Pos.CENTER_LEFT);
        searchRow.setStyle("-fx-background-color: #161b22; -fx-border-color: #30363d; -fx-border-width: 0 0 1 0;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        TableView<PetRow> table = buildTable(searchField);

        statusLabel = new Label("0 records");
        statusLabel.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 12px; -fx-font-family: 'Courier New';");

        Button exportTxt = styledButton("⬇ Export pets.txt");
        exportTxt.setOnAction(e -> exportTxt());
        Button exportCsv = styledButton("⬇ Export .csv");
        exportCsv.setOnAction(e -> exportCsv());

        Region spacer = new Region();
        HBox exportRow = new HBox(8, statusLabel, spacer, exportTxt, exportCsv);
        exportRow.setPadding(new Insets(8, 12, 8, 12));
        exportRow.setAlignment(Pos.CENTER_LEFT);
        exportRow.setStyle("-fx-background-color: #161b22; -fx-border-color: #30363d; -fx-border-width: 1 0 0 0;");
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox section = new VBox(0, secHeader, searchRow, table, exportRow);
        section.setStyle(
                "-fx-background-color: #161b22;" +
                        "-fx-border-color: #30363d;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-width: 1;"
        );
        VBox.setVgrow(table, Priority.ALWAYS);
        return section;
    }

    @SuppressWarnings("unchecked")
    private TableView<PetRow> buildTable(TextField searchField) {
        FilteredList<PetRow> filtered = new FilteredList<>(petRows, p -> true);
        searchField.textProperty().addListener((obs, old, val) ->
                filtered.setPredicate(row ->
                        val == null || val.isEmpty() ||
                                row.getName().toLowerCase().contains(val.toLowerCase())
                )
        );

        // ID column
        TableColumn<PetRow, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(55);
        idCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setGraphic(null); return; }
                Label l = new Label(String.valueOf(val));
                l.setStyle("-fx-background-color: #1c2332; -fx-text-fill: #8b949e; -fx-font-family: 'Courier New'; -fx-font-size: 11px; -fx-padding: 2 6 2 6; -fx-border-color: #30363d; -fx-border-radius: 4; -fx-background-radius: 4; -fx-border-width: 1;");
                setGraphic(l); setText(null);
            }
        });

        // Name column
        TableColumn<PetRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(220);
        nameCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setText(null); return; }
                setText(val);
                setStyle("-fx-text-fill: #58a6ff; -fx-font-weight: bold; -fx-font-family: 'Courier New'; -fx-font-size: 13px;");
            }
        });

        // Age column
        TableColumn<PetRow, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setPrefWidth(110);
        ageCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setGraphic(null); return; }
                Label l = new Label(val + " yr" + (val != 1 ? "s" : ""));
                l.setStyle("-fx-background-color: #1c2a1c; -fx-text-fill: #3fb950; -fx-font-family: 'Courier New'; -fx-font-size: 11px; -fx-padding: 2 8 2 8; -fx-background-radius: 20;");
                setGraphic(l); setText(null);
            }
        });

        // Remove column
        TableColumn<PetRow, Void> removeCol = new TableColumn<>("");
        removeCol.setPrefWidth(60);
        removeCol.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("✕");
            {
                String base = "-fx-background-color: transparent; -fx-text-fill: #8b949e; -fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 2 6 2 6; -fx-border-radius: 4; -fx-background-radius: 4;";
                String hover = "-fx-background-color: #2d1b1b; -fx-text-fill: #f85149; -fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 2 6 2 6; -fx-border-radius: 4; -fx-background-radius: 4;";
                btn.setStyle(base);
                btn.setOnMouseEntered(e -> btn.setStyle(hover));
                btn.setOnMouseExited(e -> btn.setStyle(base));
                btn.setOnAction(e -> {
                    PetRow row = getTableView().getItems().get(getIndex());
                    int realId = row.getId();
                    petNames.remove(realId);
                    petAges.remove(realId);
                    refreshRows();
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableView<PetRow> table = new TableView<>(filtered);
        table.getColumns().addAll(idCol, nameCol, ageCol, removeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(38);
        table.setPrefHeight(200);
        table.setStyle("-fx-background-color: #161b22; -fx-border-width: 0; -fx-control-inner-background: #161b22; -fx-control-inner-background-alt: #161b22;");
        Label placeholder = new Label("No pets in database. Add one below.");
        placeholder.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 13px; -fx-font-style: italic;");
        table.setPlaceholder(placeholder);
        return table;
    }

    // ── Add Section ───────────────────────────────────────────────────────────

    private VBox buildAddSection() {
        Label dot = new Label("●");
        dot.setStyle("-fx-text-fill: #58a6ff; -fx-font-size: 10px;");
        Label hdr = new Label("Add New Pet");
        hdr.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 12px; -fx-font-weight: bold;");
        HBox secHeader = new HBox(6, dot, hdr);
        secHeader.setPadding(new Insets(8, 12, 8, 12));
        secHeader.setStyle("-fx-background-color: #1c2332; -fx-border-color: #30363d; -fx-border-width: 0 0 1 0;");

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Buddy");
        styleInput(nameField);

        TextField ageField = new TextField();
        ageField.setPromptText("e.g. 3");
        ageField.setMaxWidth(130);
        styleInput(ageField);

        Button addBtn = new Button("+ Add Pet");
        addBtn.setStyle("-fx-background-color: #3fb950; -fx-text-fill: #0d1117; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 6; -fx-padding: 6 14 6 14; -fx-cursor: hand;");
        addBtn.setOnMouseEntered(e -> addBtn.setStyle("-fx-background-color: #46c957; -fx-text-fill: #0d1117; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 6; -fx-padding: 6 14 6 14; -fx-cursor: hand;"));
        addBtn.setOnMouseExited(e -> addBtn.setStyle("-fx-background-color: #3fb950; -fx-text-fill: #0d1117; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 6; -fx-padding: 6 14 6 14; -fx-cursor: hand;"));

        Label nameLabel = fieldLabel("PET NAME");
        Label ageLabel  = fieldLabel("AGE (1–20)");
        VBox nameBox = new VBox(4, nameLabel, nameField);
        VBox ageBox  = new VBox(4, ageLabel, ageField);
        HBox.setHgrow(nameBox, Priority.ALWAYS);

        HBox inputRow = new HBox(8, nameBox, ageBox, addBtn);
        inputRow.setAlignment(Pos.BOTTOM_LEFT);
        inputRow.setPadding(new Insets(12, 12, 4, 12));

        Label errLabel = new Label("");
        errLabel.setStyle("-fx-text-fill: #f85149; -fx-font-size: 12px; -fx-font-family: 'Courier New'; -fx-padding: 0 12 8 12;");
        errLabel.setMinHeight(22);

        Runnable doAdd = () -> {
            String name   = nameField.getText().trim();
            String ageRaw = ageField.getText().trim();
            errLabel.setText("");

            if (name.isEmpty())        { errLabel.setText("Error: pet name cannot be empty."); return; }
            if (name.contains(" "))    { errLabel.setText("Error: " + name + " is not a valid input."); return; }
            if (ageRaw.isEmpty())      { errLabel.setText("Error: age is required."); return; }

            int age;
            try { age = Integer.parseInt(ageRaw); }
            catch (NumberFormatException ex) { errLabel.setText("Error: " + ageRaw + " is not a valid age."); return; }

            if (age < 1 || age > 20)   { errLabel.setText("Error: " + age + " is not a valid age."); return; }
            if (petNames.size() >= MAX_PETS) { errLabel.setText("Error: Database is full."); return; }

            petNames.add(name);
            petAges.add(age);
            nameField.clear();
            ageField.clear();
            nameField.requestFocus();
            refreshRows();
        };

        addBtn.setOnAction(e -> doAdd.run());
        ageField.setOnAction(e -> doAdd.run());
        nameField.setOnAction(e -> ageField.requestFocus());

        VBox section = new VBox(0, secHeader, inputRow, errLabel);
        section.setStyle("-fx-background-color: #161b22; -fx-border-color: #30363d; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 1;");
        return section;
    }

    // ── Footer ────────────────────────────────────────────────────────────────

    private HBox buildFooter() {
        Label lbl = new Label("Data auto-saved to pets.txt on exit  ·  Press Enter to submit fields");
        lbl.setStyle("-fx-text-fill: #484f58; -fx-font-size: 11px; -fx-font-family: 'Courier New';");
        HBox footer = new HBox(lbl);
        footer.setPadding(new Insets(8, 20, 12, 20));
        footer.setStyle("-fx-background-color: #0d1117;");
        return footer;
    }

    // ── Export ────────────────────────────────────────────────────────────────

    private void exportTxt() {
        saveToFile("pets_export.txt");
        showInfo("Exported pets_export.txt (" + petNames.size() + " records)");
    }

    private void exportCsv() {
        try (PrintWriter out = new PrintWriter(new FileWriter("pets_export.csv"))) {
            out.println("id,name,age");
            for (int i = 0; i < petNames.size(); i++) {
                out.println(i + "," + petNames.get(i) + "," + petAges.get(i));
            }
        } catch (IOException ex) {
            showInfo("Error writing CSV."); return;
        }
        showInfo("Exported pets_export.csv (" + petNames.size() + " records)");
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.setTitle("Pet Database");
        a.showAndWait();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void styleInput(TextField f) {
        f.setStyle("-fx-background-color: #0d1117; -fx-text-fill: #e6edf3; -fx-prompt-text-fill: #484f58; -fx-border-color: #30363d; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-padding: 5 8 5 8;");
    }

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 10px; -fx-font-weight: bold;");
        return l;
    }

    private Button styledButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #1c2332; -fx-text-fill: #8b949e; -fx-font-size: 12px; -fx-background-radius: 6; -fx-border-color: #30363d; -fx-border-radius: 6; -fx-border-width: 1; -fx-padding: 4 10 4 10; -fx-cursor: hand;");
        return b;
    }

    // ── PetRow model ──────────────────────────────────────────────────────────

    public static class PetRow {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty  name;
        private final SimpleIntegerProperty age;

        public PetRow(int id, String name, int age) {
            this.id   = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.age  = new SimpleIntegerProperty(age);
        }

        public int    getId()   { return id.get();   }
        public String getName() { return name.get(); }
        public int    getAge()  { return age.get();  }

        public IntegerProperty idProperty()   { return id;   }
        public StringProperty  nameProperty() { return name; }
        public IntegerProperty ageProperty()  { return age;  }
    }
}