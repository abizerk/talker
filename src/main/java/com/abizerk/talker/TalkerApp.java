package com.abizerk.talker;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		
		while(true) {
			String command = console.readLine(">");
			if (command.equalsIgnoreCase(HELP_COMMAND)) {
				printHelpInformation(console);
			} else if (command.equalsIgnoreCase(EXIT_COMMAND)) {
				exitFromTalker(console);
			} else if (command.startsWith("(")) {
				Pattern pattern = Pattern.compile("\\((.*?)\\)");
				Matcher matcher = pattern.matcher(command);
				ArrayList<String> matches = new ArrayList<String>();
				while (matcher.find())
					matches.add(matcher.group());
				
				console.printf("Attempting to understand your command .... found " + matches.size() + " facts.\n");
				if (matches.size() > 2 || matches.size() < 2) {
					console.printf("ERROR: Please enter the command in the correct format.\n");
					continue;
				} else if(matches.size() == 2) {
					StringTokenizer st = new StringTokenizer(command, ">");
					if (st.countTokens() < 2) {
						console.printf("ERROR: Please enter the command in the correct format.\n");
						continue;
					}
					String parentFact = st.nextToken().replaceAll("[\\(\\)]", "").trim();
					String childFact = st.nextToken().replaceAll("[\\(\\)]", "").trim();
					createParentChildLinkIfNoneAlreadyExist(parentFact, childFact, console);
				}
				
			} else if (command.startsWith("explain")) {
				Pattern pattern = Pattern.compile("\\((.*?)\\)");
				Matcher matcher = pattern.matcher(command);
				ArrayList<String> matches = new ArrayList<String>();
				while (matcher.find())
					matches.add(matcher.group());
				
				if (matches.size() != 1) {
					console.printf("ERROR: Please enter the command in the correct format.\n");
					continue;
				}
				console.printf("Ok so you want me to tell you more about the fact " + matches.get(0) + "\n");
				Fact factToExplain = new Fact(matches.get(0).replaceAll("[\\(\\)]", ""));
				List<Link> childFacts = linkRepository.findByParentFact_factValue(factToExplain.getFactValue());
				List<Link> parentFacts = linkRepository.findByChildFact_factValue(factToExplain.getFactValue());
				printLinks(console, childFacts, "Child Facts");
				printLinks(console, parentFacts, "Parent Facts");
			} else {
				continue;
			}
		}
	}

	/**
	 * Given a list of links, it prints the links with a label.
	 * 
	 * @param console
	 * @param facts
	 * @param linkLabel
	 */
	private void printLinks(Console console, List<Link> facts, String linkLabel) {
		if (facts == null || facts.isEmpty()) {
			console.printf(linkLabel + ": No Facts Found. \n");
			return;
		}
		console.printf(linkLabel + ": \n");
		for (Link link : facts) {
			console.printf("    - " + link + "\n");
		}
		
		console.printf("\n");
	}

	private void exitFromTalker(Console console) {
		console.printf("See you next time ....\n");
		System.exit(0);
	}

	private void printHelpInformation(Console console) {
		console.printf("\nhelp ----------------------------- display command options.\n");
		console.printf("exit ----------------------------- exit out of Talker.\n");
		console.printf("(fact)---------------------------- create a fact.\n");
		console.printf("(parent fact) > (child fact)------ create a linked fact.\n");
		console.printf("explain (fact) ------------------- retrieves a fact with its links.\n");
		console.printf("\n");
	}

	private void printAWelcomeMessageForTalker() {
		System.out.println("====================================");
		System.out.println("==========Welcome to Talker=========");
		System.out.println("====================================");
		System.out.println("\n Please enter a command or type help.");
	}
	
	/**
	 * This method takes in a parent and child fact, and attempts to create a
	 * link between them.  It checks if both parent, and child facts already
	 * exists and if they do, then it checks whether there is already a link
	 * between parent and child facts, if there is then no new link or facts
	 * will be created.  Otherwise if either of parent and/or child fact has
	 * not yet been created, then they will be created and a link will be created
	 * between the two.
	 * 
	 * @param: String parentFact, String childFact, Console console
	 * @return: Link between parent and childFact, either new or existing.
	 */
	private Link createParentChildLinkIfNoneAlreadyExist(String parentFact, String childFact, Console console) {
		console.printf("Got it .... " + parentFact + " links to " + childFact + "\n");
		Fact parentFactObj = factRepository.findByFactValue(parentFact);
		Fact childFactObj = factRepository.findByFactValue(childFact);
		Link parentChildLink = null;
		/*
		 * Scenarios to consider:
		 *  - Parent fact already exists
		 *  - Child fact already exists
		 *  - Link between parent and child fact already exists
		 */
		
		/*
		 * If both facts already exists then check whether the link exists as well,
		 * if it does then just return no need to process further.
		 */
		if (parentFactObj != null && childFactObj != null) {
			//  Attempt to find the link between the two
			List<Link> existingParentChildLinks = linkRepository.findByParentFactFactValueAndChildFactFactValue(parentFact, childFact);
			if (existingParentChildLinks.size() > 0) {
				/*
				 *   If there are more than one parent child links for the same values 
				 *   for parent and child then it makes sense to return the first one.
				 */
				return existingParentChildLinks.get(0);
			}
		}
		
		/*
		 * If either of parent or child fact have not been created yet, then create them.
		 */
		
		if(parentFactObj == null) {
			parentFactObj = new Fact(parentFact);
			factRepository.save(parentFactObj);
		}
		
		if (childFactObj == null) {
			childFactObj = new Fact(childFact);
			factRepository.save(childFactObj);
		}
		
		/*
		 * Finally save the link.
		 */
		
		parentChildLink = linkRepository.save(new Link(parentFactObj, childFactObj));
		
		return parentChildLink;

	}
	
}
