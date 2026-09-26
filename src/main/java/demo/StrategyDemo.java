package demo;

import strategy.FlyingStrategy;
import strategy.Hero;
import strategy.HorseRidingStrategy;
import strategy.MovementStrategy;
import strategy.WalkingStrategy;

import java.util.Scanner;
import java.util.function.Supplier;

/**
 * Console entry point. All I/O lives here on purpose — the
 * {@code strategy} package stays free of System.out/Scanner so it can
 * be reused as-is behind a GUI in the term project.
 */
public final class StrategyDemo {

    private enum MenuOption {
        WALK("Walking", WalkingStrategy::new),
        RIDE_HORSE("Horse riding", HorseRidingStrategy::new),
        FLY("Flying", FlyingStrategy::new),
        EXIT("Exit", null);

        private final String label;
        private final Supplier<MovementStrategy> factory;

        MenuOption(String label, Supplier<MovementStrategy> factory) {
            this.label = label;
            this.factory = factory;
        }
    }

    public static void main(String[] args) {
        System.out.println("---- Strategy Pattern Demo ----");
        demonstrateNullRejection();
        runInteractiveLoop();
    }

    private static void demonstrateNullRejection() {
        System.out.println("\n-- Passing a null strategy to the Hero constructor: --");
        try {
            new Hero(null);
        } catch (NullPointerException e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }
    }

    private static void runInteractiveLoop() {
        System.out.println("\n-- Interactive strategy selection: --");

        try (Scanner scanner = new Scanner(System.in)) {
            Hero hero = new Hero(new WalkingStrategy());

            while (true) {
                printMenu();
                MenuOption choice = readOption(scanner);

                if (choice == null) {
                    System.out.println("Invalid input, try again.");
                    continue;
                }
                if (choice == MenuOption.EXIT) {
                    System.out.println("Exiting...");
                    return;
                }

                hero.setMovementStrategy(choice.factory.get());
                System.out.println(hero.move());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nChoose a movement strategy:");
        MenuOption[] options = MenuOption.values();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i].label);
        }
    }

    private static MenuOption readOption(Scanner scanner) {
        String input = scanner.nextLine().trim();
        try {
            int index = Integer.parseInt(input) - 1;
            MenuOption[] options = MenuOption.values();
            if (index >= 0 && index < options.length) {
                return options[index];
            }
        } catch (NumberFormatException ignored) {
            // falls through to "invalid input" below
        }
        return null;
    }
}