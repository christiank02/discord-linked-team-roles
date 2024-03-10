package de.aimless.aimless_java_bot.command;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum CommandName {

    LINKEDROLE("linkedrole"),
    ADD("add"),
    REMOVE("remove"),
    SET("set"),
    LIST("list"),
    REGISTERMETADATACOMMAND("registermetadata"),
    BOOSTERROLE("boosterrole");

    private final String name;

    CommandName(String name) {
        this.name = name;
    }

    private static final Map<String, CommandName> COMMAND_NAME_MAP = Stream.of(values())
            .collect(Collectors.toMap(CommandName::getName, e -> e));

    public static CommandName fromCommandName(String commandName) {
        return COMMAND_NAME_MAP.get(commandName.toLowerCase());
    }
}
