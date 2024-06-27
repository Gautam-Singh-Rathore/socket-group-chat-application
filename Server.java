import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // Server class variables or fields
    ServerSocket serverSocket ;
    // Constructor
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    // Start Server Method
    private void startServer()  {
        try {
            // we will use the while loop to continuously run the server
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept(); // This is a blocking method
                System.out.println("A new client has connected !");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (Exception e){
            closeServersocket();
        }

    }
    // Close everything method
    private void closeServersocket(){
        try {
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // ** MAIN METHOD **
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
