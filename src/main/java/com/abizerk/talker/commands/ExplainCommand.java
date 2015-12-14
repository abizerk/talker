package com.abizerk.talker.commands;

import com.abizerk.talker.model.Fact;
import com.abizerk.talker.model.Link;
import com.abizerk.talker.repository.LinkRepository;
import org.apache.log4j.Logger;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abizerkhambati on 12/12/15.
 */
public class ExplainCommand extends AbstractConsoleCommand {

    public static final String COMMAND = "explain";
    private static Logger logger = Logger.getLogger(ExplainCommand.class);
    private LinkRepository linkRepository;

    public ExplainCommand(LinkRepository linkRepository) {
        super(COMMAND);
        this.linkRepository = linkRepository;
    }

    @Override
    public void execute(String command, Console console) {
        if (isValidCommand(command)) {
            Pattern pattern = Pattern.compile("\\((.*?)\\)");
            Matcher matcher = pattern.matcher(command);
            ArrayList<String> matches = new ArrayList<String>();
            while (matcher.find())
                matches.add(matcher.group());

            console.printf("Ok so you want me to tell you more about the fact " + matches.get(0) + "\n");
            Fact factToExplain = new Fact(matches.get(0).replaceAll("[\\(\\)]", ""));
            List<Link> childFacts = linkRepository.findByParentFact_factValue(factToExplain.getFactValue());
            List<Link> parentFacts = linkRepository.findByChildFact_factValue(factToExplain.getFactValue());
            printLinks(console, childFacts, "Child Facts");
            printLinks(console, parentFacts, "Parent Facts");
        } else {
            console.printf("ERROR: Please enter the command in the correct format.\n");
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public boolean isValidCommand(String command) {
        logger.info("Command is: " + command);
        if (super.isValidCommand(command)) {
            logger.info("Command starts with: " + command);
            Pattern pattern = Pattern.compile("\\((.*?)\\)");
            Matcher matcher = pattern.matcher(command);
            ArrayList<String> matches = new ArrayList<String>();
            while (matcher.find())
                matches.add(matcher.group());

            if (matches.size() == 1) {
                logger.info("Command format matches.");
                return true;
            }
        }
        logger.info("Command does not match, is invalid.");
        return false;
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

}
