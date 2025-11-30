public class Launcher {
    public static void main(String[] args) {
        System.out.println("Select a game to run:");
        System.out.println("1. Texas Hold'em");
        System.out.println("2. Blackjack");

        try {
            System.out.print("Enter choice: ");
            int choice = System.in.read();

            if (choice == '1') {
                System.out.println("Launching Texas Hold'em...");
                poker.Main.main(new String[]{});
            } else if (choice == '2') {
                System.out.println("Launching Blackjack...");
                blackjack.Main.main(new String[]{});
            } else {
                System.out.println("Invalid choice. Exiting.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
