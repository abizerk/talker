package com.abizerk.talker.commands;

import java.io.Console;

/**
 * Created by abizerkhambati on 12/12/15.
 */
public class ExitCommand extends AbstractConsoleCommand {

    public static final String COMMAND = "exit";

    public ExitCommand() {
        super(COMMAND);
    }

    @Override
    public void execute(String command, Console console) {
        if (isValidCommand(command)) {
            exitFromTalker(console);
        }
    }

    @Override
    public String help() {
        return null;
    }


    private void exitFromTalker(Console console) {
        console.printf("See you next time ....\n");
        System.exit(0);
    }
}
