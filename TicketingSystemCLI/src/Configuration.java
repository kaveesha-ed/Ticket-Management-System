import java.io.*;
import java.util.Scanner;

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    // Getters and Setters
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    // Save configuration to a text file
    public boolean saveToFile(String filename) {
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            writer.println("TotalTickets=" + totalTickets);
            writer.println("TicketReleaseRate=" + ticketReleaseRate);
            writer.println("CustomerRetrievalRate=" + customerRetrievalRate);
            writer.println("MaxTicketCapacity=" + maxTicketCapacity);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found - " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error saving configuration: " + e.getMessage());
            return false;
        }
    }

    public boolean loadFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split("=");
                switch (parts[0]) {
                    case "TotalTickets":
                        totalTickets = Integer.parseInt(parts[1]);
                        break;
                    case "TicketReleaseRate":
                        ticketReleaseRate = Integer.parseInt(parts[1]);
                        break;
                    case "CustomerRetrievalRate":
                        customerRetrievalRate = Integer.parseInt(parts[1]);
                        break;
                    case "MaxTicketCapacity":
                        maxTicketCapacity = Integer.parseInt(parts[1]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid configuration key: " + parts[0]);
                }
            }
            return isValid(); // Validate the configuration after loading
        } catch (FileNotFoundException e) {
            return false; // Let Main handle the error message
        } catch (Exception e) {
            System.out.println("Error loading configuration: " + e.getMessage());
            return false;
        }
    }

    // Validate configuration values
    public boolean isValid() {
        return totalTickets > 0 &&
                ticketReleaseRate > 0 &&
                customerRetrievalRate > 0 &&
                maxTicketCapacity > 0;
    }
}