import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class Server{
    protected static List<Socket> sockets = new Vector<>();

    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(1111);
        boolean done = false;

        while(!done){
            try{
                System.out.println("Binding to port, please wait  ...");
                System.out.println("Waiting for a client ...");
                Socket accept = serverSocket.accept();
                synchronized(sockets){
                    sockets.add(accept);
                }

                Thread thread = new Thread(new ServerThead(accept));
                thread.start();
            }
            catch (Exception e){
                done = true;
                e.printStackTrace();
            }
        }
        serverSocket.close();
    }

}