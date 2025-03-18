import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<Integer> tickets = new LinkedList<>();
    private final int maxCapacity; // Maximum tickets the pool can hold
    private int remainingTickets; // Remaining tickets to be released

    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity;
        this.remainingTickets = totalTickets;
    }

    public synchronized void addTickets(int count) {
        // Stop adding tickets if no remaining tickets
        if (remainingTickets <= 0) {
            return;
        }

        // Adjust count if it exceeds remaining tickets
        if (count > remainingTickets) {
            count = remainingTickets;
        }

        // Wait if the pool is full
        while (tickets.size() + count > maxCapacity) {
            System.out.println(Thread.currentThread().getName() + ": Ticket pool is full. Waiting to add tickets...");
            try {
                wait(); // Vendors wait until space becomes available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Add tickets to the pool
        for (int i = 0; i < count; i++) {
            tickets.add(1);
        }
        remainingTickets -= count; // Decrease remaining tickets
        System.out.println(Thread.currentThread().getName() + ": Added " + count + " tickets. Remaining tickets: " + remainingTickets);
        notifyAll(); // Notify waiting threads
    }

    public synchronized void removeTicket() {
        // Wait if the pool is empty
        while (tickets.isEmpty() && remainingTickets > 0) {
            System.out.println(Thread.currentThread().getName() + ": No tickets available. Waiting...");
            try {
                wait(); // Customers wait until tickets are added
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // If tickets are available, remove one
        if (!tickets.isEmpty()) {
            tickets.remove(0);
            System.out.println(Thread.currentThread().getName() + ": Purchased a ticket. Remaining in pool: " + tickets.size());
            notifyAll(); // Notify waiting threads
        }
    }

    public synchronized boolean isLimitReached() {
        return remainingTickets <= 0 && tickets.isEmpty(); // All tickets processed and pool is empty
    }
}
