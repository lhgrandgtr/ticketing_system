package com.coursework.coursework;

import com.coursework.coursework.customer.Customer;
import com.coursework.coursework.ticket.TicketPool;
import com.coursework.coursework.ticket.Ticket;
import com.coursework.coursework.vendor.Vendor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulate {
    private final TicketPool ticketPool;
    private final int numVendors;
    private final int numCustomers;
    private final AtomicInteger successfulPurchases;
    private final AtomicInteger failedPurchases;
    private final Logger logger;

    public Simulate(int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity,
                    int numVendors, int numCustomers, String logFilePath) throws IOException {

        this.numVendors = numVendors;
        this.numCustomers = numCustomers;
        this.successfulPurchases = new AtomicInteger(0);
        this.failedPurchases = new AtomicInteger(0);

        // Create logs directory if it doesn't exist
        Path logDir = Paths.get("logs");
        if (!Files.exists(logDir)) {
            Files.createDirectory(logDir);
        }

        // Initialize logger
        this.logger = new Logger(new PrintWriter(new FileWriter(logFilePath, true), true));
        this.ticketPool = new TicketPool(ticketReleaseRate, customerRetrievalRate, maxTicketCapacity, this.logger);
    }

    public void runSimulation() throws InterruptedException {
        logger.log("Starting simulation with:");
        logger.log("Vendors: " + numVendors);
        logger.log("Customers: " + numCustomers);
        logger.log("Max Ticket Capacity: " + ticketPool.getMaxTicketCapacity());
        logger.log("Ticket Release Rate: " + ticketPool.getTicketReleaseRate());
        logger.log("Customer Retrieval Rate: " + ticketPool.getCustomerRetrievalRate());

        ExecutorService vendorExecutor = Executors.newFixedThreadPool(numVendors);
        ExecutorService customerExecutor = Executors.newFixedThreadPool(numCustomers);
        CountDownLatch startLatch = new CountDownLatch(1);

        // Start vendor threads
        for (int i = 0; i < numVendors; i++) {
            Vendor vendor = new Vendor(i, ticketPool, logger);
            vendorExecutor.submit(() -> {
                try {
                    startLatch.await();
                    vendor.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Start customer threads
        for (int i = 0; i < numCustomers; i++) {
            Customer customer = new Customer(i, ticketPool, logger, successfulPurchases, failedPurchases);
            customerExecutor.submit(() -> {
                try {
                    startLatch.await();
                    customer.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        logger.log("Simulation starting...");
        startLatch.countDown();

        // Run simulation for the specified duration
        Thread.sleep(10000); // Run for 10 seconds

        // Shutdown executors
        vendorExecutor.shutdown();
        customerExecutor.shutdown();
        vendorExecutor.awaitTermination(5, TimeUnit.SECONDS);
        customerExecutor.awaitTermination(5, TimeUnit.SECONDS);

        // Print and log results
        printSimulationResults();

        // Close logger
        logger.close();
    }

    private void printSimulationResults() {
        logger.log("\nSimulation Results:");
        logger.log("-------------------");
        logger.log("Final ticket pool size: " + ticketPool.getCurrentCapacity());
        logger.log("Successful purchases: " + successfulPurchases.get());
        logger.log("Failed purchases: " + failedPurchases.get());
        logger.log("Total purchase attempts: " +
                (successfulPurchases.get() + failedPurchases.get()));

        logger.log("\nRemaining tickets in pool:");
        List<Ticket> remainingTickets = ticketPool.getAllTickets();
        remainingTickets.forEach(ticket -> logger.log(ticket.toString()));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Ticket System Simulation");
        System.out.println("----------------------------------");

        // Get simulation parameters from user
        System.out.print("Enter ticket release rate (tickets/second): ");
        int ticketReleaseRate = getValidIntInput(scanner, 1, 100);

        System.out.print("Enter customer retrieval rate (tickets/second): ");
        int customerRetrievalRate = getValidIntInput(scanner, 1, 100);

        System.out.print("Enter maximum ticket pool capacity: ");
        int maxTicketCapacity = getValidIntInput(scanner, 10, 1000);

        System.out.print("Enter number of vendors: ");
        int numVendors = getValidIntInput(scanner, 1, 50);

        System.out.print("Enter number of customers: ");
        int numCustomers = getValidIntInput(scanner, 1, 100);

        System.out.print("Enter simulation duration (seconds): ");
        int simulationDuration = getValidIntInput(scanner, 5, 300);

        // Create log file name with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String logFilePath = String.format("logs/simulation_%s.log", timestamp);

        try {
            Simulate simulation = new Simulate(
                    ticketReleaseRate,
                    customerRetrievalRate,
                    maxTicketCapacity,
                    numVendors,
                    numCustomers,
                    logFilePath
            );

            System.out.println("\nStarting simulation...");
            System.out.println("Log file: " + logFilePath);
            simulation.runSimulation();

        } catch (IOException e) {
            System.err.println("Error creating log file: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Simulation interrupted: " + e.getMessage());
        }

        scanner.close();
    }

    private static int getValidIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = scanner.nextInt();
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("Please enter a value between %d and %d: ", min, max);
                }
            } catch (Exception e) {
                System.out.print("Please enter a valid number: ");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}