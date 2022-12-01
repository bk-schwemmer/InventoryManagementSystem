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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Modifies existing product in inventory
 *
 * @author Brian Schwemmer
 */
public class ModifyProduct implements Initializable {
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
    public TextField productID;
    public TextField productName;
    public TextField productStock;
    public TextField productPrice;
    public TextField productMax;
    public TextField productMin;
    public Label exceptions;

    private ObservableList tempAssociatedParts;
    private int productIndex;
    private int id;
    private String name;
    private int stock;
    private double price;
    private int max;
    private int min;
    private static Product selectedProduct;

    /**
     * Sets local variables to populate text fields in new scene and populates data in parts tables
     * @param url unused parameter
     * @param resourceBundle unused parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allPartsTable.setItems(Inventory.getAllParts());
        if (selectedProduct != null) {
            productIndex = Inventory.getAllProducts().indexOf(selectedProduct);
            tempAssociatedParts = FXCollections.observableArrayList();
            tempAssociatedParts.addAll(selectedProduct.getAllAssociatedParts());

            // Display selected product info
            id = selectedProduct.getId();
            name = selectedProduct.getName();
            stock = selectedProduct.getStock();
            price = (selectedProduct.getPrice());
            max = selectedProduct.getMax();
            min = selectedProduct.getMin();

            // Display selected product info
            productID.setText(Integer.toString(id));
            productName.setText(name);
            productName.isEditable();
            productStock.setText(Integer.toString(stock));
            productPrice.setText(Double.toString(price));
            productMax.setText(Integer.toString(max));
            productMin.setText(Integer.toString(min));
        }
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
     * Sets given product as selected product for further reference locally
     * @param product product to mark as selected
     */
    public static void setSelectedProduct(Product product) {
        selectedProduct = product;
    }

    /**
     * Filters and highlights parts based on partial name string or integer part ID text input
     *
     * @param actionEvent action generated from hitting 'enter' in search box
     */
    public void onPartSearch(ActionEvent actionEvent) {
        partUpdates.setText("");
        String partialName = partSearch.getText();
        partSearch.clear();
        allPartsTable.getSelectionModel().clearSelection();

        if (partialName == "") {
            allPartsTable.setItems(Inventory.getAllParts());
            return;
        }

        ObservableList<Part> foundParts = Inventory.lookupPart(partialName);

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
            }
            catch (NumberFormatException e) {
                partUpdates.setText("No matching parts found");
            }

        } else {
            allPartsTable.setItems(foundParts);
        }
    }

    /**
     * Adds selected part in all parts table list to the associated parts table list
     *
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
     * Removes associated part from associatedParts table
     *
     * @param actionEvent action from clicking 'remove associated part' button
     */
    public void onRemovePart(ActionEvent actionEvent) {
        Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {

            /**
             * Generate alert box to verify before deleting
             */
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
     * Saves modified product and all associated part info, if valid, and replaces current product in inventory
     *
     * @param actionEvent action created from clicking 'Save' button
     * @throws IOException if fxml file for MainMenu scene is invalid or missing
     *
     * <p>
     *     <b>RUNTIME ERROR</b>
     *     I encountered a RuntimeException error during creation of this method caused by an IndexOutOfBoundsException.
     *     After searching through the method I found that I had accidentally assigned the 'getNextProductID()' index
     *     as the index argument to the updateProduct() method when saving and updating the product that was modified.
     *     Changing this to the correct productIndex which is assigned from the selectedProduct variable resolved
     *     the error.
     * </p>
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        exceptions.setText("");
        String exceptionsList = "";
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
            Product newProduct = (new Product(id, name, price, stock, min, max));
            newProduct.getAllAssociatedParts().setAll(tempAssociatedParts);
            Inventory.updateProduct(productIndex, newProduct);
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
