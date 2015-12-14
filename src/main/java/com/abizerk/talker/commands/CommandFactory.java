package com.abizerk.talker.commands;

import com.abizerk.talker.repository.FactRepository;
import com.abizerk.talker.repository.LinkRepository;

/**
 * Created by abizerkhambati on 11/12/15.
 */
public class CommandFactory {

    private LinkRepository linkRepository;
    private FactRepository factRepository;

    public CommandFactory(LinkRepository linkRepository, FactRepository factRepository) {
        this.linkRepository = linkRepository;
        this.factRepository = factRepository;
    }

    public AbstractConsoleCommand getCommand(String command) {
        if (command.startsWith(LinkFactsCommand.COMMAND)) {
            return new LinkFactsCommand(linkRepository, factRepository);
        } else if(command.startsWith(HelpCommand.COMMAND)){
            return new HelpCommand();
        } else if (command.startsWith(ExitCommand.COMMAND)) {
            return new ExitCommand();
        } else if (command.startsWith(ExplainCommand.COMMAND)) {
            return new ExplainCommand(linkRepository);
        }

        return null;
    }
}
