# CLIManager

A lightweight Java library for building interactive command-line applications using annotated static methods.

## 🏦 Package
`az.arvilo.climanager`

## ✨ Features

- Register commands using `@CliCommand` annotation
- Automatically parses and executes commands from user input
- Handles arguments via `String[]` parameters
- Built-in command loop with `exit` to terminate

## 🚀 Getting Started

### 1. Add a command class

```java
import az.arvilo.climanager.CliCommand;

public class MyCommands {

    @CliCommand(name = "hello")
    public static String hello(String[] args) {
        return "Hello, " + String.join(" ", args) + "!";
    }

    @CliCommand(name = "sum")
    public static String sum(String[] args) {
        int total = 0;
        for (String arg : args) {
            total += Integer.parseInt(arg);
        }
        return "Sum: " + total;
    }
}
```

### 2. Create and run the CLI

```java
import az.arvilo.climanager.CommandLineInterface;

public class Main {
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface("myapp");
        cli.addClass(MyCommands.class);
        cli.runApp();
    }
}
```

### 3. Example Output

```
MYAPP>hello World
Hello, World!

MYAPP>sum 10 20 30
Sum: 60

MYAPP>exit
```

## 🧠 Annotation: `@CliCommand`

Use this annotation to define a static method as a CLI command.

```java
@CliCommand(name = "commandname")
public static String yourMethod(String[] args) {
    return "result";
}
```

### Requirements:

- Method must be `static`
- Return type must be `String`
- Must accept a single `String[]` parameter
- The name must:
  - Be lowercase (`[a-z]+`)
  - Not be null or empty
  - **Not be a reserved command** (`"exit"`)

## 💡 Notes

- To exit the CLI loop, type `exit`.
- If a command is not recognized, the app will notify the user.
- You can dynamically register any number of annotated classes.

## 🔧 Advanced

If you want to manually register a command:

```java
cli.addClass(MyCommands.class); // auto-registers all valid annotated methods
```

## 🧪 License

MIT License
## ✍️ Author

Developed by Arvilo

---

Happy coding!

