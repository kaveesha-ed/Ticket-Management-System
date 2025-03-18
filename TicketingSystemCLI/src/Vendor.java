public class Vendor implements Runnable {
    private final TicketPool pool;
    private final int releaseRate;

    public Vendor(TicketPool pool, int releaseRate) {
        this.pool = pool;
        this.releaseRate = releaseRate;
    }

    @Override
    public void run() {
        try {
            while (!pool.isLimitReached()) {
                pool.addTickets(releaseRate);
                Thread.sleep(2000);
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
