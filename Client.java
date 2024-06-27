import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    // Class fields
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMsg() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc = new Scanner(System.in);
            while (socket.isConnected()) {
                String msgToSend = sc.nextLine();
                bufferedWriter.write(username + ": " + msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromChat;
                while (socket.isConnected()) {
                    try {
                        msgFromChat = bufferedReader.readLine();
                        if (msgFromChat != null) {
                            System.out.println(msgFromChat);
                        } else {
                            // If msgFromChat is null, it means the connection is closed
                            closeEverything(socket, bufferedReader, bufferedWriter);
                            break;
                        }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //** MAIN METHOD **
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String userName = sc.nextLine();

        try {
            Socket socket = new Socket("localhost", 1234);
            Client client = new Client(socket, userName);
            client.listenMsg();
            client.sendMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

