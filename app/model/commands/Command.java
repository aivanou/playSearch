package model.commands;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * The command that internal search engines can execute
 * Currently, for catalogs there is another way
 * @see datasources.dao.entities.EntitiesDAO
 */
public abstract class Command {

    private String name;
    protected JSONObject command;

    public static Command parseCommand(String name, JSONObject query) {
        if (name.equals("search")) {
            return new SearchCommand(name, query);
        }
        return new SearchCommand(name, query);
    }

    public abstract String fill(Map<String, String> parameter, List<String> searchFields);

    public Command(String name, JSONObject command) {
        this.name = name;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Command command1 = (Command) o;

        if (command != null ? !command.equals(command1.command) : command1.command != null) return false;
        if (name != null ? !name.equals(command1.name) : command1.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (command != null ? command.hashCode() : 0);
        return result;
    }
}
