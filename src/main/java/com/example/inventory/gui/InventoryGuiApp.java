package com.example.inventory.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.function.Consumer;

/** Desktop client (JavaFX) for the inventory REST API. */
public class InventoryGuiApp extends Application {

    private final ApiClient api = new ApiClient();
    private final ObservableList<ProductRow> data = FXCollections.observableArrayList();
    private final TableView<ProductRow> table = new TableView<>(data);

    private final TextField nameField = new TextField();
    private final TextField descriptionField = new TextField();
    private final TextField quantityField = new TextField();
    private final TextField unitPriceField = new TextField();
    private final Label statusLabel = new Label();

    private Long selectedId;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        configureTable();

        VBox root = new VBox(10, table, buildForm(), buildButtons(), statusLabel);
        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 600, 500));
        stage.setTitle("Inventory");
        stage.show();

        refresh();
    }

    private void configureTable() {
        TableColumn<ProductRow, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<ProductRow, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ProductRow, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<ProductRow, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<ProductRow, BigDecimal> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        table.getColumns().addAll(idColumn, nameColumn, descriptionColumn, quantityColumn, priceColumn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, previous, selected) -> {
            if (selected != null) {
                selectedId = selected.getId();
                nameField.setText(selected.getName());
                descriptionField.setText(selected.getDescription());
                quantityField.setText(String.valueOf(selected.getQuantity()));
                unitPriceField.setText(selected.getUnitPrice().toString());
            }
        });
    }

    private GridPane buildForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Description:"), descriptionField);
        grid.addRow(2, new Label("Quantity:"), quantityField);
        grid.addRow(3, new Label("Unit price:"), unitPriceField);
        return grid;
    }

    private HBox buildButtons() {
        Button newButton = new Button("New");
        newButton.setOnAction(e -> clearForm());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> save());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> delete());

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refresh());

        return new HBox(10, newButton, saveButton, deleteButton, refreshButton);
    }

    private void clearForm() {
        selectedId = null;
        nameField.clear();
        descriptionField.clear();
        quantityField.clear();
        unitPriceField.clear();
        table.getSelectionModel().clearSelection();
    }

    private void save() {
        ProductRow product;
        try {
            product = new ProductRow();
            product.setName(nameField.getText());
            product.setDescription(descriptionField.getText());
            product.setQuantity(Integer.parseInt(quantityField.getText().trim()));
            product.setUnitPrice(new BigDecimal(unitPriceField.getText().trim()));
        } catch (NumberFormatException ex) {
            statusLabel.setText("Quantity and price must be numeric");
            return;
        }

        Long currentId = selectedId;
        Call<ProductRow> call = currentId == null
                ? () -> api.create(product)
                : () -> api.update(currentId, product);

        runInBackground(call, result -> {
            clearForm();
            refresh();
        });
    }

    private void delete() {
        if (selectedId == null) {
            statusLabel.setText("Select a product to delete");
            return;
        }
        Long id = selectedId;
        runInBackground(() -> {
            api.delete(id);
            return null;
        }, result -> {
            clearForm();
            refresh();
        });
    }

    private void refresh() {
        runInBackground(api::list, list -> {
            data.setAll(list);
            statusLabel.setText("Products loaded: " + list.size());
        });
    }

    private <T> void runInBackground(Call<T> call, Consumer<T> onSuccess) {
        Task<T> task = new Task<>() {
            @Override
            protected T call() throws Exception {
                return call.execute();
            }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> statusLabel.setText("Error: " + task.getException().getMessage()));
        new Thread(task).start();
    }

    @FunctionalInterface
    private interface Call<T> {
        T execute() throws Exception;
    }
}
