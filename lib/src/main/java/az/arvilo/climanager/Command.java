package az.arvilo.climanager;

class Command {

    private final String name;
    private CommandRunner runner;

    Command(String name, CommandRunner runner) {
        this.name = name;
        this.runner = runner;
    }

    String getName() {
        return name;
    }

    void setRunner(CommandRunner runner) {
        this.runner = runner;
    }

    String run(String[] args) {

        return runner.run(args);
    }
}
