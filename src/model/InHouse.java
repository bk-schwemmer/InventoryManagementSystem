package model;

/**
 * Class is supplemental to Part Class by adding additional private data members
 * and methods for setting and getting that data
 *
 * @author Brian Schwemmer
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * Constructor to create in house part
     * @param id int to identify part
     * @param name string to identify part
     * @param price double to indicate cost
     * @param stock int to indicate inventory level
     * @param min int to indicate min inventory level
     * @param max int to indicate max inventory level
     * @param machineId int to identify crafting fixture
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Changes MachineID field to given ID
     * @param machineId int to indicate new MachineID
     */
    public void setMachineId(int machineId) {this.machineId = machineId;}

    /**
     * Fetches current MachineID of this part
     * @return int with current MachineID of this part
     */
    public int getMachineId() {return machineId;}

}
