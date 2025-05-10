package az.arvilo.climanager;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {

    private final List<Command> commands;
    private final String appName;

    public CommandLineInterface(String appName) {
        this.commands = new ArrayList<>();
        this.appName = appName;
    }

    private void setCommand(String name, CommandRunner runner) {
        commands
                .stream()
                .filter(command ->
                        command.getName().equals(name)
                )
                .findAny()
                .ifPresentOrElse(
                        command -> command.setRunner(runner),
                        () -> addCommand(new Command(name, runner))
                );
    }

    private void addCommand(Command command) {
        commands.add(command);
    }

    private String parseInputAndGetName(String input) {
        input = input.trim();
        input = input.replaceAll("\\s+", " ");

        return input.contains(" ") ?
                input.split(" ")[0] :
                input;
    }

    private String[] parseInputAndGetArgs(String input) {
        input = input.trim();
        input = input.replaceAll("\\s+", " ");
        int firstSpaceIndex = input.indexOf(' ');
        if (firstSpaceIndex == -1) {

            return new String[0];
        }
        input = input.substring(firstSpaceIndex + 1);

        return input.split(" ");
    }

    private String runCommand(String input) {
        if (input == null) {
            throw new RuntimeException("Input can't be null.");
        }
        input = input.trim();
        input = input.replaceAll("\\s+", " ");
        if (input.isBlank()) {

            return null;
        }
        String commandName = parseInputAndGetName(input);
        String[] args = parseInputAndGetArgs(input);

        return commands
                .stream()
                .filter(command -> command.getName().equals(commandName))
                .findFirst()
                .map(command -> command.run(args))
                .orElseGet(() -> String.format(
                                "%s command doesn't exist.",
                                commandName
                        )
                );
    }

    /**
     * Registers all valid static methods from the given class that are annotated with {@link CliCommand}.
     * <p>
     * A method is considered valid if:
     * <ul>
     *   <li>It is static</li>
     *   <li>Returns a {@code String}</li>
     *   <li>Takes a single {@code String[]} parameter</li>
     *   <li>The {@code CliCommand} name is non-null, non-empty, not {@code "exit"}, and contains only lowercase letters</li>
     * </ul>
     * These methods will be automatically registered as CLI commands and invoked when their name is typed in the interface.
     *
     * @param clazz the class containing static methods annotated with {@code CliCommand}
     */
    public void addClass(Class<?> clazz) {
        Arrays.stream(clazz
                        .getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(CliCommand.class))
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getReturnType().equals(String.class))
                .filter(method -> method.getParameterTypes().length == 1)
                .filter(method -> method.getParameterTypes()[0].equals(String[].class))
                .filter(method -> {
                            String name = method
                                    .getAnnotation(CliCommand.class)
                                    .name();

                            return name != null &&
                                    !name.equals("exit") &&
                                    name.matches("[a-z]+");
                        }
                )
                .forEach(method ->
                        setCommand(
                                method.getAnnotation(CliCommand.class).name(),
                                args -> {
                                    try {

                                        return (String) method.invoke(null, (Object) args);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        )
                );
    }

    /**
     * Starts the interactive command-line interface.
     * <p>
     * Continuously prompts the user for input, executes the corresponding command if found,
     * and prints the result. The loop stops when the user types {@code exit}.
     */
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.printf("%s>", appName.toUpperCase());
            String input = scanner.nextLine();
            if (input.trim().equals("exit")) {
                break;
            }
            String result = runCommand(input);
            if (result != null) {
                System.out.println(result);
            }
        }
    }
}
