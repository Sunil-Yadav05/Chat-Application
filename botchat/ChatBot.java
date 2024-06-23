package botchat;
import java.util.*;

public class ChatBot {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hi there, I am your new chatbot");
        boolean running = true;
        String input;

        while (running) {
            System.out.println("");
            input = sc.nextLine();
            
            if (input.equals("h1")) {
                System.out.println("H1 There");
            } else if (input.equals("bye")) {
                System.out.println("Bye");
                running = false;
            } else if (input.equals("how are you")) {
                System.out.println("I am fine, what about you?");
            } else {
                System.out.println("I don't understand that command.");
            }
        }

        sc.close();
    }
}

