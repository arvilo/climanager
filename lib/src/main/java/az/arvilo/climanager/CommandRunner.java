package az.arvilo.climanager;

/**
 * Indicates that the annotated static method represents a CLI command.
 * <p>
 * The method must:
 * <ul>
 *   <li>Be static</li>
 *   <li>Return a {@code String}</li>
 *   <li>Accept a single {@code String[]} argument</li>
 * </ul>
 * The command will be registered with the name provided in the {@code name()} field.
 * <p>
 * The command name must only contain lowercase letters (a–z) and must not be a reserved keyword.
 * <b>Currently, the only reserved command name is {@code "exit"}.</b>
 * <p>
 * Example usage:
 * <pre>{@code
 * @CliCommand(name = "greet")
 * public static String greet(String[] args) {
 *     return "Hello, " + String.join(" ", args);
 * }
 * }</pre>
 */
@FunctionalInterface
interface CommandRunner {

    String run(String[] args);
}
