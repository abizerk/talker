package com.abizerk.talker.commands;

import com.abizerk.talker.model.Fact;
import com.abizerk.talker.model.Link;
import com.abizerk.talker.repository.FactRepository;
import com.abizerk.talker.repository.LinkRepository;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abizerkhambati on 8/12/15.
 */
public class LinkFactsCommand extends AbstractConsoleCommand {

    public static final String COMMAND = "(";
    private LinkRepository linkRepository;
    private FactRepository factRepository;

    public LinkFactsCommand(LinkRepository linkRepository, FactRepository factRepository) {
        super(COMMAND);
        this.linkRepository = linkRepository;
        this.factRepository = factRepository;
    }

    @Override
    public void execute(String command, Console console) {
        if (isValidCommand(command)) {
            StringTokenizer st = new StringTokenizer(command, ">");
            String parentFact = st.nextToken().replaceAll("[\\(\\)]", "").trim();
            String childFact = st.nextToken().replaceAll("[\\(\\)]", "").trim();
            createParentChildLinkIfNoneAlreadyExist(parentFact, childFact, console);

        } else {
            throw new RuntimeException("Command: "  + command + " is invalid for creating links between facts.");
        }
    }

    @Override
    public String help() {
        return "(fact 1) > (fact 2) creates a link between fact 1 and fact 2, " +
                "with fact 1 being the child and fact 2 a parent.";

    }

    @Override
    public boolean isValidCommand(String command) {
        if (!command.startsWith(COMMAND)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(command);
        ArrayList<String> matches = new ArrayList<String>();
        while (matcher.find())
            matches.add(matcher.group());

        if (matches.size() > 2 || matches.size() < 2) {
            return false;
        } else if(matches.size() == 2) {
            StringTokenizer st = new StringTokenizer(command, ">");
            if (st.countTokens() < 2) {
               return false;
            }
        }
        return true;
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

    public String toString() {
        return "Linking Facts Together";
    }
}
