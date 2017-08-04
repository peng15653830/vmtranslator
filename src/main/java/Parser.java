import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Parser {
    private LinkedList<String> commands = new LinkedList<>();

    public Parser() {
        try {
            List<String> originCommands = Files.readAllLines(Paths.get("D:\\Program Files\\nand2tetris\\projects\\08\\ProgramFlow\\FibonacciSeries\\FibonacciSeries.vm"), StandardCharsets.UTF_8);
            originCommands.forEach(command -> {
                if (command.indexOf("//") != -1) {
                    command = command.substring(0, command.indexOf("//")).trim();
                } else {
                    command = command.trim();
                }
                if (command.length() > 0) {
                    commands.add(command);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasMoreCommands() {
        return commands.size() > 0;
    }

    public void advance() {
        if (hasMoreCommands()) {
            commands.removeFirst();
        }
    }

    public CommandType commandType() {
        String command = commands.getFirst();
        if (command.startsWith("push")) {
            return CommandType.C_PUSH;
        }
        if (command.startsWith("pop")) {
            return CommandType.C_POP;
        }
        if (command.startsWith("function")) {
            return CommandType.C_FUNCTION;
        }
        if (command.startsWith("label")) {
            return CommandType.C_LABEL;
        }
        if (command.startsWith("call")) {
            return CommandType.C_CAll;
        }
        if (command.startsWith("return")) {
            return CommandType.C_RETURN;
        }
        if (command.startsWith("goto")) {
            return CommandType.C_GOTO;
        }
        if (command.startsWith("if-goto")) {
            return CommandType.C_IF;
        }
        return CommandType.C_ARITHMETIC;
    }

    public String arg1() {
        String command = commands.getFirst();
        String[] array = command.split(" ");
        if (array.length > 1) {
            return array[1];
        }else {
            return array[0];
        }
    }

    public Integer arg2() {
        String command = commands.getFirst();
        String[] array = command.split(" ");
        if (array.length > 2) {
            return Integer.parseInt(array[2]);
        }
        return null;
    }

}
