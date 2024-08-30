import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class JBot {
    private static boolean isRunning = true;
    private static Map<String, JBotCommand> commandMap;
    private static ArrayList<Task> taskList;

    private static void hLine() {
        System.out.println("________________________________________");
    }

    public static void initCommandMap() {
        commandMap = new HashMap<>();
        commandMap.put("list", ListCommand.getInstance());
        commandMap.put("bye", ByeCommand.getInstance());
        commandMap.put("mark", MarkCommand.getInstance());
        commandMap.put("unmark", UnmarkCommand.getInstance());
        commandMap.put("todo", ToDoCommand.getInstance());
        commandMap.put("deadline", DeadlineCommand.getInstance());
        commandMap.put("event", EventCommand.getInstance());
        commandMap.put("delete", DeleteCommand.getInstance());
    }

    private static void greetUser() {
        hLine();
        System.out.println("Hello! I'm JBot");
        System.out.println("What can I do for you?");
        hLine();
    }

    public static void main(String[] args) {
        Storage storage = new Storage();
        JBot.taskList = Storage.parseData();
        initCommandMap();
        greetUser();
        Scanner sc = new Scanner(System.in);

        while (JBot.isRunning) {
            String userInput = sc.nextLine();
            String inputCommand = userInput.split(" ")[0];
            JBotCommand command = commandMap.get(inputCommand);

            try {
                if (command == null) {
                    throw new InvalidCommandException("Invalid command: " + inputCommand);
                }

                hLine();
                command.run(userInput, taskList);
                hLine();

                // Save the task list after each command
                Storage.updateData(taskList);

                if (command.equals(ByeCommand.getInstance())) {
                    sc.close();
                    JBot.isRunning = false;
                }
            } catch (InvalidCommandException e) {
                hLine();
                System.out.println(e.getMessage());
                hLine();
            }
        }
    }
}