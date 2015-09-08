package com.abizerk.talker.commands;

import java.io.Console;

public abstract class AbstractConsoleCommand implements Command {
	
	public void execute(String command) {
		Console console = System.console();
		console.printf("This command has not been implemented.");
	}
	
	public void help() {
		Console console = System.console();
		console.printf("There is no help currently available for this command.");
	}

}
