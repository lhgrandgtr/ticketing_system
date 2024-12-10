package main.backend;

import main.backend.ticket.TicketPool;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ApplicationArguments appArgs, TicketPool ticketPool) {
		return (arguments) -> {
			System.out.println("================================");
			System.out.println("Welcome to the Ticketing System!");
			System.out.println("================================");

			int releaseRate = Integer.parseInt(appArgs.getOptionValues("releaseRate").get(0));
			int retrievalRate = Integer.parseInt(appArgs.getOptionValues("retrievalRate").get(0));
			int maxTicketCapacity = Integer.parseInt(appArgs.getOptionValues("maxTicketCapacity").get(0));

			System.out.println("Total Tickets: | Ticket Release Rate: " + releaseRate +
					" | Customer Retrieval Rate: " + retrievalRate +
					" | Max Ticket Capacity: " + maxTicketCapacity);
		};
	}

	@Bean
	TicketPool ticketPool(ApplicationArguments appArgs) throws IOException {
		Path logDir = Paths.get("logs");
		if (!Files.exists(logDir)) {
			Files.createDirectory(logDir);
		}

		Path logFilePath = logDir.resolve("ticket_pool.log");
		Logger logger = new Logger(new PrintWriter(new FileWriter(logFilePath.toFile(), true), true));

		int releaseRate = Integer.parseInt(appArgs.getOptionValues("releaseRate").get(0));
		int retrievalRate = Integer.parseInt(appArgs.getOptionValues("retrievalRate").get(0));
		int maxTicketCapacity = Integer.parseInt(appArgs.getOptionValues("maxTicketCapacity").get(0));

		return new TicketPool(releaseRate, retrievalRate, maxTicketCapacity, logger);
	}
}
