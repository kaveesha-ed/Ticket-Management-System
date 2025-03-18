package com.example.demo.service;

import com.example.demo.model.Configuration;
import com.example.demo.model.Ticket;
import com.example.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    private List<String> logs = new ArrayList<>();
    private Lock lock = new ReentrantLock();
    private Condition vendorCondition = lock.newCondition();
    private Condition customerCondition = lock.newCondition();

    private int totalTickets = 0;
    private int maxCapacity = 0;
    private int releaseRate = 0;
    private int retrievalRate = 0;
    private boolean isRunning = false;

    public void saveConfiguration(Configuration configuration) {
        lock.lock();
        try {
            this.totalTickets = configuration.getTotalTickets();
            this.maxCapacity = configuration.getMaxCapacity();
            this.releaseRate = configuration.getReleaseRate();
            this.retrievalRate = configuration.getRetrievalRate();
            logs.add("Configuration saved: Total Tickets=" + totalTickets +
                    ", Max Capacity=" + maxCapacity +
                    ", Ticket Release Rate=" + releaseRate +
                    ", Customer Retrieval Rate=" + retrievalRate);
            ticketRepository.deleteAll();
            for (int i = 0; i < totalTickets; i++) {
                Ticket ticket = new Ticket();
                ticket.setStatus("available");
                ticketRepository.save(ticket);
            }
        } finally {
            lock.unlock();
        }
    }

    public int getTicketCount() {
        lock.lock();
        try {
            return ticketRepository.findAll().size(); // Fetch accurate count from the database
        } finally {
            lock.unlock();
        }
    }

    public void addTickets() {
        lock.lock();
        try {
            if (!isRunning) return; // Respect stop condition
            while (totalTickets >= maxCapacity) {
                logs.add("Vendor: Waiting to add tickets (capacity full).");
                vendorCondition.await();
            }
            int ticketsToAdd = Math.min(releaseRate, maxCapacity - totalTickets);
            for (int i = 0; i < ticketsToAdd; i++) {
                Ticket ticket = new Ticket();
                ticket.setStatus("available");
                ticketRepository.save(ticket);
                totalTickets++;
            }
            logs.add("Vendor: Added " + ticketsToAdd + " tickets. Total: " + totalTickets);
            customerCondition.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void retrieveTickets() {
        lock.lock();
        try {
            if (!isRunning) return; // Respect stop condition
            while (totalTickets < retrievalRate) {
                logs.add("Customer: Not enough tickets available. Waiting...");
                customerCondition.await();
            }
            int ticketsToRetrieve = Math.min(retrievalRate, totalTickets);
            for (int i = 0; i < ticketsToRetrieve; i++) {
                Ticket ticket = ticketRepository.findFirstByStatus("available");
                if (ticket != null) {
                    ticket.setStatus("sold");
                    ticketRepository.save(ticket);
                    totalTickets--;
                }
            }
            logs.add("Customer: Purchased " + ticketsToRetrieve + " tickets. Remaining: " + totalTickets);
            vendorCondition.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void startOperation() {
        isRunning = true;
        logs.add("System started.");

        Thread vendorThread = new Thread(() -> {
            while (isRunning) {
                try {
                    lock.lock();
                    addTickets();
                    Thread.sleep(1000); // Add a pause to prevent flooding logs
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread customerThread = new Thread(() -> {
            while (isRunning) {
                try {
                    lock.lock();
                    retrieveTickets();
                    Thread.sleep(1000); // Add a pause to prevent flooding logs
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } finally {
                    lock.unlock();
                }
            }
        });

        vendorThread.start();
        customerThread.start();
    }

    public void stopOperation() {
        isRunning = false;
        logs.add("System stopped.");
        lock.lock();
        try {
            vendorCondition.signalAll();
            customerCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public List<String> getLogs() {
        return logs;
    }
}

