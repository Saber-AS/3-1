import java.io.*;
import java.net.*;
import java.util.*;

public class AddressBookServer {
    private static Map<String, String> addressBook = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    String command = (String) input.readObject();
                    switch (command) {
                        case "ADD":
                            String name = (String) input.readObject();
                            String phone = (String) input.readObject();
                            addressBook.put(name, phone);
                            output.writeObject("Contact added.");
                            break;
                        case "GET":
                            name = (String) input.readObject();
                            output.writeObject(addressBook.get(name));
                            break;
                        case "LIST":
                            output.writeObject(addressBook);
                            break;
                        default:
                            output.writeObject("Invalid command.");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
