package model;

import javafx.collections.*;

/**
 * Holds lists of available parts and products in inventory system. Static methods allow
 * universal access to contents for sharing data.
 *
 * @author Brian Schwemmer
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    // static fields to track the next PartID and ProductID that should be used
    private static int nextPartID = 6;
    private static int nextProductID = 1004;

    /**
     * Adds a part to the allParts list.
     *
     * @param newPart the part to add to the list
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a product to the allProducts list.
     *
     * @param newProduct the product to add to the list
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Finds and returns a specific part from allParts list.
     *
     * @param partId int that uniquely identifies the part
     * @return part  if part is found, it is returned. Returns null if
     *               no part with a matching ID is found.
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts) {
            if (part.getId() == partId)
                return part;
        }
        return null;
    }

    /**
     * Finds and returns a specific product from allProducts list.
     *
     * @param productId int that uniquely identifies the product
     * @return product  if part is found, it is returned. Returns null if
     *                  no part with a matching ID is found.
     */
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId)
                return product;
        }
        return null;
    }

    /**
     * Finds any parts that contain a matching substring
     *
     * @param partName string containing a partial part name to search for
     * @return list of parts found with matching substrings. Returns null
     *          if no matches are found.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                foundParts.add(part);
            }
        }
        if (foundParts.size() == 0) {
            return null;
        }
        return foundParts;
    }

    /**
     * Finds any products that contain a matching substring
     *
     * @param productName string containing a partial product name to search for
     * @return list of parts found with matching substrings. Returns null
     *         if no matches are found.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                foundProducts.add(product);
            }
        }
        if (foundProducts.size() == 0) {
            return null;
        }
        return foundProducts;
    }

    /**
     * Replaces existing inventory part at given index with given part
     * @param index int dictating index of part to replace
     * @param selectedPart part to replace existing part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Replaces existing inventory product at given index with given product
     * @param index int dictating index of product to replace
     * @param newProduct product to replace existing product
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Removes indicated part from allParts list
     * @param selectedPart part to be removed from inventory list
     * @return true if part is in list and is removed. Returns false
     *          if part isn't found or can't be deleted.
     */
    public static boolean deletePart(Part selectedPart) {
        boolean deleteSuccessful = false;

        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            deleteSuccessful = true;
        }

        return deleteSuccessful;
    }

    /**
     * Removes indicated product from allProducts list
     * @param selectedProduct part to be removed from inventory list
     * @return true if product is in list and is removed. Returns false
     *          if product isn't found or can't be deleted.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        boolean deleteSuccessful = false;

        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            deleteSuccessful = true;
        }

        return deleteSuccessful;
    }

    /**
     * Fetches all parts in inventory
     * @return ObservableList containing all inventory Parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Fetches all products in inventory
     * @return ObservableList containing all inventory products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Fetches next PartID to be used
     * @return int to be used for ID of next part added
     */
    public static int getNextPartID() {
        return nextPartID;
    }

    /**
     * Adds 1 to int tracking nextPartID
     */
    public static void incrementNextPartID() {
        Inventory.nextPartID++;
    }

    /**
     * Fetches next ProductID to be used
     * @return int to be used for ID of next product added
     */
    public static int getNextProductID() {
        return nextProductID;
    }

    /**
     * Adds 1 to int tracking nextProductID
     */
    public static void incrementNextProductID() {
        Inventory.nextProductID++;
    }
}
