package com.abizerk.talker;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.abizerk.talker.commands.AbstractConsoleCommand;
import com.abizerk.talker.commands.CommandFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.abizerk.talker.model.Fact;
import com.abizerk.talker.model.Link;
import com.abizerk.talker.repository.FactRepository;
import com.abizerk.talker.repository.LinkRepository;

@SpringBootApplication
public class TalkerApp implements CommandLineRunner{
	
	private static final String EXIT_COMMAND = "exit";

	private static final String HELP_COMMAND = "help";

	@Autowired
	private FactRepository factRepository;

	@Autowired
	private LinkRepository linkRepository;

	public static void main(String[] args) {
		SpringApplication.run(TalkerApp.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		printAWelcomeMessageForTalker();
		
		Console console = System.console();

		CommandFactory commandFactory = new CommandFactory(linkRepository, factRepository);
		
		while(true) {
			String command = console.readLine(">");
			AbstractConsoleCommand commandToRun = commandFactory.getCommand(command);
			if (commandToRun != null) {
				commandToRun.execute(command, console);
			} else {
				console.printf("ERROR: Unrecognised Command, Please Try Again!\n");
				continue;
			}
		}
	}

	/**
	 * Prints a welcome message when talker is started.
	 */
	private void printAWelcomeMessageForTalker() {
		System.out.println("====================================");
		System.out.println("==========Welcome to Talker=========");
		System.out.println("====================================");
		System.out.println("\n Please enter a command or type help.");
	}
	

	
}
