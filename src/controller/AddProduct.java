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
 * Allows user to create a new product in inventory
 *
 * @author Brian Schwemmer
 */
public class AddProduct implements Initializable {
    public TableView<Part> allPartsTable;
    public TableView<Part> associatedPartsTable;
    public TableColumn allPartIDCol;
    public TableColumn allPartNameCol;
    public TableColumn allPartStockCol;
    public TableColumn allPartPriceCol;
    public TableColumn associatedPartIDCol;
    public TableColumn associatedPartNameCol;
    public TableColumn associatedPartStockCol;
    public TableColumn associatedPartPriceCol;
    public TextField partSearch;
    public Label partUpdates;
    public Label exceptions;
    public TextField productID;
    public TextField productName;
    public TextField productStock;
    public TextField productPrice;
    public TextField productMax;
    public TextField productMin;
    private ObservableList<Part> tempAssociatedParts;

    /**
     * Sets local variables and assigns data to populate all parts and associated parts table views
     * @param url unused parameter
     * @param resourceBundle unused parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allPartsTable.setItems(Inventory.getAllParts());
        tempAssociatedParts = FXCollections.observableArrayList();
        associatedPartsTable.setItems(tempAssociatedParts);

        allPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Filters and highlights parts based on partial name string or integer part ID text input
     * @param actionEvent action generated from hitting 'enter' in search box
     */
    public void onPartSearch(ActionEvent actionEvent) {
        // Clear notification label
        partUpdates.setText("");

        String partialName = partSearch.getText();

        // Clear the search field and any selection in the table
        partSearch.clear();
        allPartsTable.getSelectionModel().clearSelection();

        if (Objects.equals(partialName, "")) {
            allPartsTable.setItems(Inventory.getAllParts());
            return;
        }

        ObservableList<Part> foundParts = Inventory.lookupPart(partialName);

        // Can't find match with text? Try searching PartID
        if (foundParts == null) {
            try {
                int searchID = Integer.parseInt(partialName);
                Part matchingPart = Inventory.lookupPart(searchID);

                if (matchingPart == null) {
                    partUpdates.setText("No matching parts found");
                    allPartsTable.setItems(Inventory.getAllParts());
                    return;
                } else {
                    allPartsTable.getSelectionModel().select(matchingPart);
                }
                // If can't find matching PartID, tell user there's not match
            } catch (NumberFormatException e) {
                partUpdates.setText("No matching parts found");
            }

        } else {
            allPartsTable.setItems(foundParts);
        }
    }

    /**
     * Adds selected part in all parts table list to the associated parts table list
     * @param actionEvent action from clicking 'Add' button
     */
    public void onAddPart(ActionEvent actionEvent) {
        Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            tempAssociatedParts.add(selectedPart);
            partUpdates.setText("");
        } else {
            partUpdates.setText("No part selected");
        }
    }

    /**
     * Removes selected part from associated parts table for this product
     *
     * @param actionEvent action generated from clicking 'Remove associated part' button
     */
    public void onRemovePart(ActionEvent actionEvent) {
        Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to remove this part?");
            deleteAlert.setTitle("Associated Parts");
            deleteAlert.setHeaderText("Remove");
            deleteAlert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Arial';");

            Optional<ButtonType> result = deleteAlert.showAndWait();

            if (result.get() == ButtonType.OK) {
                tempAssociatedParts.remove(selectedPart);
                associatedPartsTable.getSelectionModel().clearSelection();
                partUpdates.setText("");
            }
        } else {
            partUpdates.setText("No part selected");
        }
    }

    /**
     * Saves new product and all associated part info, if valid, and pushes it to inventory
     * @param actionEvent action created from clicking 'Save' button
     * @throws IOException if fxml file for MainMenu scene is invalid or missing
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        exceptions.setText("");
        String exceptionsList = "";
        String name = "";
        int stock = -1;
        double price = -1;
        int max = -1;
        int min = -1;
        boolean validProduct = true;

        // Check Name data
        name = productName.getText();
        if (name.isBlank()) {
            exceptionsList += "No data in Name field\n";
            validProduct = false;
        }

        // Check Inventory Stock data
        try {
            stock = Integer.parseInt(productStock.getText());
        } catch (NumberFormatException e) {
            exceptionsList += "Inventory is not an integer\n";
            validProduct = false;
        }

        // Check Price data
        try {
            price = Double.parseDouble(productPrice.getText());
        } catch (NumberFormatException e) {
            exceptionsList += "Price is not a double\n";
            validProduct = false;
        }

        // Check Max data
        try {
            max = Integer.parseInt(productMax.getText());
        } catch (NumberFormatException e) {
            exceptionsList += "Max is not an integer\n";
            validProduct = false;
        }

        // Check Min data
        try {
            min = Integer.parseInt(productMin.getText());
        } catch (NumberFormatException e) {
            exceptionsList += "Min is not an integer\n";
            validProduct = false;
        }

        // Check that Min and Max are correct and Stock is between them
        if (max < min) {
            exceptionsList += "Min must be less than Max\n";
            validProduct = false;
        } else {
            if (stock > max || stock < min) {
                exceptionsList += "Inv must be between Min and Max\n";
                validProduct = false;
            }
        }

        // If any item is uninitialized this code won't run
        if (validProduct) {
            Product newProduct = (new Product(Inventory.getNextProductID(), name, price, stock, min, max));
            newProduct.getAllAssociatedParts().setAll(tempAssociatedParts);
            Inventory.addProduct(newProduct);
            Inventory.incrementNextProductID();
            onCancel(actionEvent);
        }

        // Print any exceptions
        if (!exceptionsList.isEmpty()) {
            exceptionsList = "Exception:\n" + exceptionsList;
        }
        exceptions.setText(exceptionsList);
    }

    /**
     * Redirects stage to Main Menu scene
     *
     * @param actionEvent action from clicking 'Cancel' button or manually created
     * @throws IOException if main menu fxml file is invalid or missing
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        root.setStyle("-fx-font-family: 'Arial';");
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene mainScene = new Scene(root, 1200, 600);
        stage.setTitle("Main Menu");
        stage.setScene(mainScene);
        stage.show();
    }

}
