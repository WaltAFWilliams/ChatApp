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
                }
            }
        }).start();

        String message = bufferedReader.readLine();
        while (!".exit".equalsIgnoreCase(message)){
            outPut.println(encrypt(message));
            outPut.flush();
            message = bufferedReader.readLine();
        }
        outPut.close();
        inPut.close();
        socket.close();



    }

    private static String encrypt(String message){
    String encrypted = "";
        for(int i = 0; i < message.length(); i++){
            encrypted += (char)(message.charAt(i) + 1); // A --> B   B --> C C --> D
        }
        return encrypted;
    }
}