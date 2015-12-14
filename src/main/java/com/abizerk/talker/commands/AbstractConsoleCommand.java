package com.abizerk.talker.commands;

import com.abizerk.talker.repository.FactRepository;
import com.abizerk.talker.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Console;

public abstract class AbstractConsoleCommand {

    private String command;

    public AbstractConsoleCommand(String command) {
        this.command = command;
    }

    /**
     * This method executes the command and prints any output to the command line.
     *
     * @param command
     */
    public abstract void execute(String command, Console console);

    /**
     * This method returns a help message explaining the
     * purpose and usage of this command.
     *
     * @return a help message as a string
     */
    public abstract String help();

    /**
     * This method takes in a command and ensures that it is valid,
     * this includes checking that the correct command is used with
     * the correct syntax and parameters.
     *
     * @param command as a string
     * @return true when a command is valid and can be processed, false otherwise.
     */
    public boolean isValidCommand(String command) {
        if (command != null && command.startsWith(this.getCommand())) {
            return true;
        }
        return false;
    }

    public String getCommand() {
        return this.command;
    }


}
