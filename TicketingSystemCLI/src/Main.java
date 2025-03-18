import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Configuration config = new Configuration();

        while (true) {
            // Prompt user to load or create a new configuration
            String choice;
            while (true) {
                System.out.print("Do you want to load a saved configuration? (yes/no): ");
                choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("yes") || choice.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }

            if ("yes".equalsIgnoreCase(choice)) {
                while (true) {
                    System.out.print("Enter the filename to load from: ");
                    String filename = scanner.nextLine();
                    if (config.loadFromFile(filename)) {
                        System.out.println("Configuration loaded successfully.");
                        break;
                    } else {
                        System.out.println("Error: File not found - " + filename);
                        System.out.print("Do you want to try again? (yes/no): ");
                        String retryChoice = scanner.nextLine().trim().toLowerCase();
                        if (!retryChoice.equals("yes")) {
                            break;
                        }
                    }
                }

                if (!config.isValid()) {
                    // If the user chose not to retry loading the file, loop back to the initial prompt
                    continue;
                }
            } else {
                // Create a new configuration
                int totalTickets = 0;
                while (true) {
                    try {
                        System.out.print("Enter total tickets: ");
                        totalTickets = Integer.parseInt(scanner.nextLine().trim());
                        if (totalTickets > 0) break;
                        System.out.println("Total tickets must be greater than zero.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a positive integer.");
                    }
                }
                config.setTotalTickets(totalTickets);

                int releaseRate = 0;
                while (true) {
                    try {
                        System.out.print("Enter ticket release rate: ");
                        releaseRate = Integer.parseInt(scanner.nextLine().trim());
                        if (releaseRate > 0) break;
                        System.out.println("Release rate must be greater than zero.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a positive integer.");
                    }
                }
                config.setTicketReleaseRate(releaseRate);

                int retrievalRate = 0;
                while (true) {
                    try {
                        System.out.print("Enter customer retrieval rate: ");
                        retrievalRate = Integer.parseInt(scanner.nextLine().trim());
                        if (retrievalRate > 0) break;
                        System.out.println("Retrieval rate must be greater than zero.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a positive integer.");
                    }
                }
                config.setCustomerRetrievalRate(retrievalRate);

                int maxCapacity = 0;
                while (true) {
                    try {
                        System.out.print("Enter maximum ticket capacity: ");
                        maxCapacity = Integer.parseInt(scanner.nextLine().trim());
                        if (maxCapacity > 0) break;
                        System.out.println("Max capacity must be greater than zero.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a positive integer.");
                    }
                }
                config.setMaxTicketCapacity(maxCapacity);

                System.out.print("Enter filename to save configuration: ");
                String filename = scanner.nextLine();
                if (!config.saveToFile(filename)) {
                    System.out.println("Error: Failed to save configuration to file. Exiting.");
                    return;
                }
            }

            // Initialize TicketPool and threads
            try {
                TicketPool pool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());
                int numVendors = 3; // Example: 3 vendors
                int numCustomers = 5; // Example: 5 customers

                for (int i = 1; i <= numVendors; i++) {
                    Thread vendorThread = new Thread(new Vendor(pool, config.getTicketReleaseRate()));
                    vendorThread.setName("Vendor-" + i);
                    vendorThread.start();
                }

                for (int i = 1; i <= numCustomers; i++) {
                    Thread customerThread = new Thread(new Customer(pool));
                    customerThread.setName("Customer-" + i);
                    customerThread.start();
                }
                break; // Exit main loop after successful initialization
            } catch (Exception e) {
                System.out.println("Error initializing threads: " + e.getMessage());
            }
        }
    }
}
