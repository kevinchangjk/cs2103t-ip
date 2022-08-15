import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Carbon {
    // chat-bot specific displays
    // ascii art generated from patorjk.com
    // logo for Carbon
    private static String logo = "                 _ _ _ ____ _    ____ ____ _  _ ____ \n" + 
            "                 | | | |___ |    |    |  | |\\/| |___ \n" + 
            "                 |_|_| |___ |___ |___ |__| |  | |___ \n" + 
            "  ▄████████    ▄████████    ▄████████ ▀█████████▄   ▄██████▄  ███▄▄▄▄   \n" + 
            " ███    ███   ███    ███   ███    ███   ███    ███ ███    ███ ███▀▀▀██▄ \n" + 
            " ███    █▀    ███    ███   ███    ███   ███    ███ ███    ███ ███   ███ \n" + 
            " ███          ███    ███  ▄███▄▄▄▄██▀  ▄███▄▄▄██▀  ███    ███ ███   ███ \n" + 
            " ███        ▀███████████ ▀▀███▀▀▀▀▀   ▀▀███▀▀▀██▄  ███    ███ ███   ███ \n" +
            " ███    █▄    ███    ███ ▀███████████   ███    ██▄ ███    ███ ███   ███ \n" + 
            " ███    ███   ███    ███   ███    ███   ███    ███ ███    ███ ███   ███ \n" + 
            " ████████▀    ███    █▀    ███    ███ ▄█████████▀   ▀██████▀   ▀█   █▀  \n" + 
            "                           ███    ███                                   \n";

    // actual introduction
    private static String intro = "Hey, Carbon here. ";
    private static String[] initPrompts = {
            "What's up?",
            "How's things going?",
            "Nice weather today, huh?",
            "How can I help you?",
            "Please don't talk to me.",
            "To get an A+ for CS2103T, you have t---[REDACTED]"
        };

    // text for exits
    private static String[] goodbyes = {
            "Bye-bye, see you again soon!",
            "Farewell. Stay safe.",
            "Nice meeting you. Let's catch up again sometime.",
            "Bye. Good riddance.",
            "...zzzzzz...",
            "What? Yeah, sorry I gotta run now."
        };

    // scanner for inputs
    private static Scanner sc = new Scanner(System.in);

    // own fields
    private Random rand;
    private boolean isRunning;
    private List<Task> tasks;

    // io display standardisation methods
    private static void printOut(String text) {
        String divider = "\n···---······---······---······---······---······---······---···\n";
        System.out.println(divider);
        System.out.println("==> " + text);
    }

    private static String printIn() {
        String receiver = "\n··-··--···--\n";
        System.out.println(receiver);
        System.out.print("<-- ");
        String input = Carbon.sc.nextLine();
        return input;
    }

    // actual constructor and init method
    private Carbon() {
        // init fields
        this.rand = new Random();
        this.tasks = new ArrayList<Task>();

        String randomPrompt = Carbon.initPrompts[
            this.rand.nextInt(Carbon.initPrompts.length)
        ];

        System.out.println(Carbon.logo);

        // extra space
        System.out.println("\n\n");
        Carbon.printOut(Carbon.intro + randomPrompt);
    }

    // main shell method
    private void runShell() {
        this.isRunning = true;
        while (this.isRunning) {
            String input = Carbon.printIn();
            this.process(input);
            if (!this.isRunning) {
                this.exit();
            }
        }
    }

    private void process(String input) {
        String lowerCaseInput = input.toLowerCase();
        switch (lowerCaseInput) {
            case "list":
                this.listItems();
                break;
            case "bye":
                this.isRunning = false;
                break;
            default:
                // unable to process as a simple command, pass to next handler
                this.processAdvanced(input);
        }
    }

    private void processAdvanced(String input) {
        String lowerCaseInput = input.toLowerCase();
        if (lowerCaseInput.startsWith("mark")) {
            int taskNumber = Integer.valueOf(input.substring("mark ".length()));
            this.setTaskDoneness(taskNumber, true);
        } else if (lowerCaseInput.startsWith("unmark")) {
            int taskNumber = Integer.valueOf(input.substring("unmark ".length()));
            this.setTaskDoneness(taskNumber, false);
        } else if (lowerCaseInput.startsWith("todo")) {
            this.addTask(input, "todo"); // TODO: change to using enums
        } else if (lowerCaseInput.startsWith("deadline")) {
            this.addTask(input, "deadline"); // TODO: change to using enums
        } else if (lowerCaseInput.startsWith("event")) {
            this.addTask(input, "event"); // TODO: change to using enums
        } else {
            // not a command, return invalid input
            String log = String.format("'%s'? I have no idea what you're saying.", input);
            Carbon.printOut(log);
        }
    }

    private void setTaskDoneness(int taskNumber, boolean doneness) {
        Task task = this.tasks.get(taskNumber - 1);
        task.changeDoneness(doneness);
        String log = String.format("Got it! \n\n    %s", task);
        Carbon.printOut(log);
    }

    private void addTask(String input, String type) {
        Task newTask;
        switch (type) {
            case "todo":
                newTask = new Todo(input);
                break;
            case "deadline":
                newTask = new Deadline(input);
                break;
            case "event":
                newTask = new Event(input);
                break;
            default:
                // should never reach here
                newTask = null;
        }
        this.tasks.add(newTask);
        String log = String.format(
                "I have added: \n    %s\n\n    We've got %s so far.", 
                newTask, 
                this.countTasks()
                );
        Carbon.printOut(log);
    }

    private void listItems() {
        int size = this.tasks.size();
        if (size == 0) {
            String log = "There are no tasks so far.";
            Carbon.printOut(log);
            return;
        }

        String log = "Here are the tasks so far. \n";
        for (int i = 0; i < size; i++) {
            String taskLog = String.format(
                    "\n    %d: %s", 
                    i + 1, 
                    this.tasks.get(i)
                    );
            log += taskLog;
        }
        Carbon.printOut(log);
    }

    private void exit() {
        String randomGoodbye = Carbon.goodbyes[
            this.rand.nextInt(Carbon.goodbyes.length)
        ];
        Carbon.printOut(randomGoodbye);
    }

    private String countTasks() {
        int count = this.tasks.size();
        if (count == 1) {
            return String.format("%d task", count);
        } else {
            return String.format("%d tasks", count);
        }
    }
    
    public static void main(String[] args) {
        Carbon shell = new Carbon();
        shell.runShell();
    }
}
