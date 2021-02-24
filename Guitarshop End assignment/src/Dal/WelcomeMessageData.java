package Dal;



import nl.inholland.exam.WelcomeMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WelcomeMessageData {

    public List<WelcomeMessage> readMessages() {
        ArrayList<WelcomeMessage> messages = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("src/Resources/Files/WelcomeMessages.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    WelcomeMessage message = (WelcomeMessage) ois.readObject();
                    messages.add(message);
                } catch (EOFException eofe) { // Inspired from the lecture
                    break;
                }

            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return messages;
    }
}

