package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;
import java.io.IOException;

/**
 * <p>
 *     <b>JavaDocs located in Project Folder Inventory_Management_System/javadoc</b>
 * </p>
 * <p>
 *     <b>Runtime Error Location</b>
 *     Runtime Error details may be found in the onSave javadoc header comments
 *     in the ModifyProduct.java controller class
 * </p>
 *
 *
 * Main class Initiates start of app and launches first screen.
 *
 * @author Brian Schwemmer
 *
 * <p>
 * <b>FUTURE ENHANCEMENT</b>
 * As a future enhancement to this application I believe the most worthwhile feature would be
 * exporting the inventory data to an external storage container. Whether that's writing it to
 * a file or implementing an entire database it probably doesn't matter that much. But exporting
 * and inporting the entire inventory list would increase the usability of the app exponentially.
 * </p>
 */
public class Main extends Application {
    /**
     * Starts program, loads test data and loads stage and first scene.
     * @param primaryStage main app stage
     * @throws IOException if MainMenu fxml fil is invalid or missing
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        addTestData();

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        root.setStyle("-fx-font-family: 'Arial';");
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.show();

    }

    /**
     * Adds test parts, products and associated parts to the app for testing and demonstration.
     */
    public static void addTestData() {

        // Test Parts
        Inventory.addPart(new InHouse(1,"Nut",0.99,1654, 1000, 10000, 232));
        Inventory.addPart(new Outsourced(2,"Bolt",1.99,6354, 1000, 10000, "Beats"));
        Inventory.addPart(new InHouse(3,"Blade",54.99,32, 5, 50, 234));
        Inventory.addPart(new Outsourced(4,"Engine",232.99,4, 2, 20, "Pickle Relish"));
        Inventory.addPart(new InHouse(5,"Wheel",10.99,50, 20, 500, 144));

        // Test Products
        Product test1 = new Product(1001, "Bike",399.99, 3, 0, 5);
        Inventory.addProduct(test1);
        Inventory.addProduct(new Product(1002, "Grill",199.98, 5, 2, 10));
        Inventory.addProduct(new Product(1003, "Mower",1299.00, 10, 10, 20));

        // Add some associated Parts
        Inventory.lookupProduct(1001).addAssociatedPart(Inventory.lookupPart(1));
    }

    /**
     * Launches the app
     * @param args command line args
     */
    public static void Main (String[] args){
        launch(args);
    }
}
