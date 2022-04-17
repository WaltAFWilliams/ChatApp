import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client{
    public static void main(String[] args) throws IOException{
        Socket socket = new Socket("127.0.0.1", 1111);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter outPut = new PrintWriter(socket.getOutputStream());
        BufferedReader inPut = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Runnable(){
            @Override
            public void run(){

                try{
                    while(true){
                        System.out.println(inPut.readLine());
                    }
                }
                catch(IOException e){
                    // e.printStackTrace();
                    // System.out.println("Exiting chatroom...");
                }
            }
        }).start();

        String message = bufferedReader.readLine();
        while (!".exit".equalsIgnoreCase(message)){
            outPut.println(message);
            outPut.flush();
            message = bufferedReader.readLine();
        }
        outPut.close();
        inPut.close();
        socket.close();



    }
}