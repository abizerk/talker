package com.abizerk.talker.commands;

import java.io.Console;

/**
 * Created by abizerkhambati on 12/12/15.
 */
public class HelpCommand extends AbstractConsoleCommand {

    public static final String COMMAND = "help";

    public HelpCommand() {
        super(COMMAND);
    }

    @Override
    public void execute(String command, Console console) {
        if (isValidCommand(command)) {
            printHelpInformation(console);
        }
    }

    @Override
    public String help() {
        return null;
    }

    private void printHelpInformation(Console console) {
        console.printf("\nhelp ----------------------------- display command options.\n");
        console.printf("exit ----------------------------- exit out of Talker.\n");
        console.printf("(fact)---------------------------- create a fact.\n");
        console.printf("(parent fact) > (child fact)------ create a linked fact.\n");
        console.printf("explain (fact) ------------------- retrieves a fact with its links.\n");
        console.printf("\n");
    }
}
