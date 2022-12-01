package model;

import javafx.collections.*;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contains all data related to Inventory Products. Methods allow access to private data members
 *
 * @author Brian Schwemmer
 */
public class Product implements Initializable{

    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Creates new Product in inventory along with a list of assocaited parts
     * @param id int containing unique product identifier
     * @param name string with descriptive product name
     * @param price double indicating cost of product
     * @param stock int indicating inventory level
     * @param min int indicating minimum inventory level
     * @param max int indicating maximum inventory level
     */
    public Product (int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this. name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        associatedParts = FXCollections.observableArrayList();
    }

    // Setters
    /**
     * Sets ProductID
     * @param id int containing new ProductID to use
     */
    public void setId(int id) {this.id = id;}

    /**
     * Sets Product Name
     * @param name string containing new Product Name to use
     */
    public void setName(String name) {this.name = name;}

    /**
     * Sets Product ID
     * @param price double containing new Product price to use
     */
    public void setPrice(double price) {this.price = price;}

    /**
     * Sets Product Stock Level
     * @param stock int containing new Product stock level to use
     */
    public void setStock(int stock) {this.stock = stock;}

    /**
     * Sets Product Min level
     * @param min int containing new Product min level to use
     */
    public void setMin(int min) {this.min = min;}

    /**
     * Sets Product Max level
     * @param max int containing new Product Max level to use
     */
    public void setMax(int max) {this.max = max;}

    // Getters

    /**
     * Fetches ProductID
     * @return int indicating this ProductID
     */
    public int getId() {return id;}

    /**
     * Fetches Product Name
     * @return string indicating this Product name
     */
    public String getName() {return name;}

    /**
     * Fetches Product price
     * @return double indicating this Product price
     */
    public double getPrice() {return price;}

    /**
     * Fetches Product stock
     * @return int indicating this Product stock levels
     */
    public int getStock() {return stock;}

    /**
     * Fetches Product min level
     * @return int indicating this Product min stock level
     */
    public int getMin() {return min;}

    /**
     * Fetches Product max level
     * @return int indicating this Product max stock level
     */
    public int getMax() {return max;}

    /**
     * Creates empty associated parts list for new products
     * @param url unused parameter
     * @param resourceBundle unused parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        associatedParts = FXCollections.observableArrayList();
    }

    /**
     * Adds given part to this product's associated parts list
     * @param part part to add to associated parts list
     */
    public void addAssociatedPart (Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes given part from this product's associated parts list, if present
     * @param selectedAssociatedPart part to delete from associated parts list
     * @return true if part is found and deleted. False if part can't be found or deleted
     */
    public boolean deleteAssociatedPart (Part selectedAssociatedPart) {
        boolean partFound = false;
        boolean partDeleted = false;

        partFound = getAllAssociatedParts().contains(selectedAssociatedPart);
        if (partFound) {
            this.getAllAssociatedParts().remove(selectedAssociatedPart);
            partDeleted = true;
        } else {
            partDeleted = false;
        }
        return partDeleted;
    }

    /**
     * Fetches all parts associated with this product
     * @return Observable List containing all associated parts for this product
     */
    public ObservableList<Part> getAllAssociatedParts () {
        return associatedParts;
    }
}
