public class Customer implements Runnable {
    private final TicketPool pool;

    public Customer(TicketPool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        try {
            while (!pool.isLimitReached()) {
                pool.removeTicket();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted. Exiting.");
            Thread.currentThread().interrupt(); // Restore the interruption status
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " encountered an error: " + e.getMessage());
        }
        System.out.println(Thread.currentThread().getName() + " stopped. Ticket limit reached.");
    }
}
