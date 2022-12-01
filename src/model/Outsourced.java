package model;

/**
 * Class is supplemental to Part Class by adding additional private data members
 * and methods for setting and getting that data
 *
 * @author Brian Schwemmer
 */
public class Outsourced extends Part {
    private String companyName;

    /**
     * Constructor to create Outsourced part
     * @param id int to identify part
     * @param name string to identify part
     * @param price double to indicate cost
     * @param stock int to indicate inventory level
     * @param min int to indicate min inventory level
     * @param max int to indicate max inventory level
     * @param companyName string to represent manufacturer
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Changes companyName field to given name
     * @param companyName string to indicate new company name
     */
    public void setCompanyName(String companyName) {this.companyName = companyName;}

    /**
     * Fetches current company name of this part
     * @return string with current company name of this part
     */
    public String getCompanyName() {return companyName;}
}
