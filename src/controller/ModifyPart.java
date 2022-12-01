package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Modifies an existing inventory part if saved
 *
 * @author Brian Schwemmer
 */
public class ModifyPart implements Initializable {
    public TextField partID;
    public TextField partName;
    public TextField partStock;
    public TextField partPrice;
    public TextField partMax;
    public TextField partMachineOrCompany;
    public TextField partMin;
    public RadioButton inHouse;
    public RadioButton outsourced;
    public Label machineOrCompany;
    public Label exceptions;
    private int partIndex;
    private static Part selectedPart;

    /**
     * Sets given part to be the selectedPart, available for retrieval privately
     * @param part part to set as selected
     */
    public static void setSelectedPart(Part part) {
        selectedPart = part;
    }

    /**
     * Sets text field data and proper labels for scene presentation
     * @param url unused parameter
     * @param resourceBundle unused parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIndex = Inventory.getAllParts().indexOf(selectedPart);
        partID.setText(Integer.toString(selectedPart.getId()));
        partName.setText(selectedPart.getName());
        partName.isEditable();
        partStock.setText(Integer.toString(selectedPart.getStock()));
        partPrice.setText(Double.toString(selectedPart.getPrice()));
        partMax.setText(Integer.toString(selectedPart.getMax()));
        partMin.setText(Integer.toString(selectedPart.getMin()));

        if (selectedPart instanceof InHouse) {
            inHouse.setSelected(true);
            onInHouse(new ActionEvent());
            partMachineOrCompany.setText(Integer.toString(((InHouse) selectedPart).getMachineId()));
        } else {
            outsourced.setSelected(true);
            onOutsourced(new ActionEvent());
            if (selectedPart instanceof Outsourced) {
                partMachineOrCompany.setText(((Outsourced) selectedPart).getCompanyName());
            }
        }
    }

    /**
     * Changes text field label to indicate it is for the Machine ID, not Company Name
     * @param actionEvent action from clicking In-House radio button
     */
    public void onInHouse(ActionEvent actionEvent) {
        machineOrCompany.setText("Machine ID");
    }

    /**
     * Changes text field label to indicate it is for the Company Name, not Machine ID
     * @param actionEvent action from clicking Outsourced radio button
     */
    public void onOutsourced(ActionEvent actionEvent) {
        machineOrCompany.setText("Company Name");
    }

    /**
     * Saves all data entered on current screen if valid and replaces the current part in inventory
     *
     * @param actionEvent action from clicking on save button
     * @throws IOException if fxml file isn't found or can't open
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        exceptions.setText("");
        String exceptionsList = "";
        int id = -1;
        int stock = -1;
        int max = -1;
        int min = -1;
        int machineID = -1;
        double price = -1;
        String name, companyName = "";
        boolean validPart = true;

        // Convert ID to int
        try {
            id = Integer.parseInt(partID.getText());
        } catch (NumberFormatException e) {
            exceptionsList += "Part ID is not an integer\n";
            validPart = false;
        }

        name = partName.getText();
        if (name.isBlank()) {
            exceptionsList += "No data in Name field\n";
            validPart = false;
        }

        // Check Inventory Stock data
        try {
            stock = Integer.parseInt(partStock.getText());
        } catch (NumberFormatException e) {
            exceptionsList += "Inventory is not an integer\n";
            validPart = false;
        }

        // Check Price data
        try {
            price = Double.parseDouble(partPrice.getText());
        } catch (NumberFormatException e) {
            exceptionsList += "Price is not a double\n";
            validPart = false;
        }

        // Check Max data
        try {
            max = Integer.parseInt(partMax.getText());
        } catch (NumberFormatException e) {
            exceptionsList += "Max is not an integer\n";
            validPart = false;
        }

        // Check Max data
        try {
            min = Integer.parseInt(partMin.getText());
        } catch (NumberFormatException e) {
            exceptionsList += "Min is not an integer\n";
            validPart = false;
        }

        // Check that Min and Max are correct and Stock is between them
        if (max < min) {
            exceptionsList += "Min must be less than Max\n";
            validPart = false;
        } else {
            if (stock > max || stock < min) {
                exceptionsList += "Inv must be between Min and Max\n";
                validPart = false;
            }
        }

        if (inHouse.isSelected()) {

            if (partMachineOrCompany.getText() == null) {
                exceptionsList += "No data in MachineID field\n";
                validPart = false;
            }
            // Check Machine ID data
            try {
                machineID = Integer.parseInt(partMachineOrCompany.getText());
            } catch (NumberFormatException e) {
                exceptionsList += "Machine ID is not an integer\n";
                validPart = false;
            }

            // If any item is uninitialized this code won't run. Ignore warning.
            if (validPart) {
                Inventory.updatePart(partIndex, (new InHouse(id, name, price, stock, min, max, machineID)));
                onCancel(actionEvent);
            }

        } else {
            companyName = partMachineOrCompany.getText();
            if (companyName == null) {
                exceptionsList += "No data in MachineID field\n";
                validPart = false;
            }

            // If any item is uninitialized this code won't run. Ignore warning.
            if (validPart) {
                Inventory.updatePart(partIndex, (new Outsourced(id, name, price, stock, min, max, companyName)));
                onCancel(actionEvent);
            }
        }

        if (!exceptionsList.isEmpty()) {
            exceptionsList = "Exception:\n" + exceptionsList;
        }
        exceptions.setText(exceptionsList);
    }

    /**
     * Redirects stage back to MainMenu scene
     *
     * @param actionEvent action from clicking on cancel button
     * @throws IOException if fxml file is missing or cannot be opened
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
