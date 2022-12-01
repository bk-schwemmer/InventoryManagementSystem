package controller;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Class displays and controls Main Menu scene and all features therein
 *
 * @author Brian Schwemmer
 */
public class MainMenu implements Initializable {

    public TableView<Part> partTable;
    public TableView<Product> productTable;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partPriceCol;
    public TableColumn partStockCol;
    public TableColumn productIDCol;
    public TableColumn productNameCol;
    public TableColumn productStockCol;
    public TableColumn productPriceCol;
    public Label partUpdates;
    public Label productUpdates;
    public TextField productSearch;
    public TextField partSearch;

    // private data members
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Sets display settings for parts and products table views in the scene
     * @param url unused parameter
     * @param resourceBundle unused parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partTable.setItems(Inventory.getAllParts());
        productTable.setItems(Inventory.getAllProducts());

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    // Part Methods

    /**
     * Filters or highlights parts based on input in search field
     * @param actionEvent action from button click
     */
    public void onPartSearch(ActionEvent actionEvent) {
        partUpdates.setText("");
        String partialName = partSearch.getText();
        partSearch.clear();
        partTable.getSelectionModel().clearSelection();

        // Empty search box shows all parts
        if (partialName == "") {
            partTable.setItems(Inventory.getAllParts());
            return;
        }

        ObservableList<Part> foundParts = Inventory.lookupPart(partialName);

        if (foundParts == null) {
            try {
                int searchID = Integer.parseInt(partialName);
                Part matchingPart = Inventory.lookupPart(searchID);
                if (matchingPart == null) {
                    partUpdates.setText("No matching parts found");
                    partTable.setItems(Inventory.getAllParts());
                    return;
                } else {
                    partTable.getSelectionModel().select(matchingPart);
                }
            }
            catch (NumberFormatException e) {
                partUpdates.setText("No matching parts found");
            }

        } else {
            partTable.setItems(foundParts);
        }
    }

    /**
     * Opens Add Part scene to allow creation of a new inventory part
     * @param actionEvent action from button click
     * @throws IOException if fxml file is incorrect or missing
     */
    public void onAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        root.setStyle("-fx-font-family: 'Arial';");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene addPartScene = new Scene(root, 800, 900);
        stage.setTitle("Add Part");
        stage.setScene(addPartScene);
        stage.show();
    }

    /**
     * Opens Modify Part scene to allow adjustment to selected part
     * @param actionEvent action from button click
     * @throws IOException if fxml file is incorrect or missing
     */
    public void onModifyPart(ActionEvent actionEvent) throws IOException {
        if (partTable.getSelectionModel().getSelectedItem() == null) {
            partUpdates.setText("No Part Selected. Please select a part to modify it.");
        } else {
            ModifyPart.setSelectedPart(partTable.getSelectionModel().getSelectedItem());
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            root.setStyle("-fx-font-family: 'Arial';");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene modifyPartScene = new Scene(root, 800, 900);
            stage.setTitle("Modify Part");
            stage.setScene(modifyPartScene);
            stage.show();
        }
    }

    /**
     * Deletes selected part after verifying with pop-up window.
     * If no part is selected, error message is displayed on screen
     * @param actionEvent action from button click
     */
    public void onDeletePart(ActionEvent actionEvent) {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            partUpdates.setText("No item was selected");
            return;
        }

        // Verification box to double-check before deleting
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this part?");
        deleteAlert.setTitle("Parts");
        deleteAlert.setHeaderText("Delete");
        deleteAlert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Arial';");

        Optional<ButtonType> result = deleteAlert.showAndWait();

        if (result.get() == ButtonType.OK) {
            boolean deleted = Inventory.deletePart(selectedPart);
            partTable.getSelectionModel().clearSelection();

            if (!deleted)
                partUpdates.setText("There was no matching part found in inventory");
        }
    }


    // Product Methods

    /**
     * Filters or highlights products based on input in search field
     * @param actionEvent action from button click
     */
    public void onProductSearch(ActionEvent actionEvent) {
        // Clear notification label
        partUpdates.setText("");

        String partialName = productSearch.getText();

        // Clear the search field and any selection in the table
        productSearch.clear();
        productTable.getSelectionModel().clearSelection();

        if (Objects.equals(partialName, "")) {
            productTable.setItems(Inventory.getAllProducts());
            return;
        }

        ObservableList<Product> foundProducts = Inventory.lookupProduct(partialName);

        // Can't find match with text? Try searching PartID
        if (foundProducts == null) {
            try {
                int searchID = Integer.parseInt(partialName);
                Product matchingProduct = Inventory.lookupProduct(searchID);

                if (matchingProduct == null) {
                    productUpdates.setText("No matching products found");
                    productTable.setItems(Inventory.getAllProducts());
                    return;
                } else {
                    productTable.getSelectionModel().select(matchingProduct);
                }
                // If can't find matching PartID, tell user there's no match
            } catch (NumberFormatException e) {
                productUpdates.setText("No matching products found");
            }

        } else {
            productTable.setItems(foundProducts);
        }
    }

    /**
     * Opens Add Product scene to allow creation of a new inventory product
     * @param actionEvent action from button click
     * @throws IOException if fxml file is incorrect or missing
     */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        root.setStyle("-fx-font-family: 'Arial';");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene addProductScene = new Scene(root, 1200, 850);
        stage.setTitle("Add Product");
        stage.setScene(addProductScene);
        stage.show();
    }

    /**
     * Opens Modify Product scene to allow adjustment to selected product
     * @param actionEvent action from button click
     * @throws IOException if fxml file is incorrect or missing
     */
    public void onModifyProduct(ActionEvent actionEvent) throws IOException {
        if (productTable.getSelectionModel().getSelectedItem() == null) {
            productUpdates.setText("No Product Selected. Please select a product to modify it.");
        } else {
            ModifyProduct.setSelectedProduct(productTable.getSelectionModel().getSelectedItem());
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            root.setStyle("-fx-font-family: 'Arial';");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene modifyProductScene = new Scene(root, 1200, 850);
            stage.setTitle("Modify Product");
            stage.setScene(modifyProductScene);
            stage.show();
        }
    }

    /**
     * Deletes selected part after verifying with pop-up window.
     * If no part is selected, error message is displayed on screen
     * @param actionEvent action from button click
     */
    public void onDeleteProduct(ActionEvent actionEvent) {
        productUpdates.setText("");
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            productUpdates.setText("No item was selected");
            return;
        }

        // Don't delete if there are associated parts!
        if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
            productUpdates.setText("This product has parts");
        } else {
            // Verification box to double-check before deleting
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this product?");
            deleteAlert.setTitle("Products");
            deleteAlert.setHeaderText("Delete");
            deleteAlert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Arial';");

            Optional<ButtonType> result = deleteAlert.showAndWait();

            if (result.get() == ButtonType.OK) {
                boolean deleted = Inventory.deleteProduct(selectedProduct);
                productTable.getSelectionModel().clearSelection();

                if (!deleted) {
                    productUpdates.setText("There was no matching part found in inventory");
                }
}
        }
    }


    /**
     * Closes app completely
     * @param actionEvent action from button click
     */
    public void onExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
