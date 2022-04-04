import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {


	public static ArrayList <ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String clientUsername;

	public ClientHandler(Socket socket) {

		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername = bufferedReader.readLine();
			clientHandlers.add(this);
			broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
		} catch (IOException e) {
			closeEverything (socket, bufferedReader, bufferedWriter);
		} 

	}

	@Override
	public void run(){
		String meassageFromClient;

		while (socket.isConnected()) {
			try {
				meassageFromClient= bufferedReader.readLine();
				System.out.println(meassageFromClient);
				broadcastMessage(meassageFromClient);
				if(".exit".equals(meassageFromClient)){
					System.out.println("closing socket");
					closeEverything(socket, bufferedReader, bufferedWriter);
					break;	
				}
			} catch (IOException e) {
				closeEverything(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
	}

	public void broadcastMessage(String messageToSend) {
		for (ClientHandler clientHanders : clientHandlers) {
			try {
				if (!clientHanders.clientUsername.equals(clientUsername)) {
					clientHanders.bufferedWriter.write(messageToSend);
					clientHanders.bufferedWriter.newLine();
					clientHanders.bufferedWriter.flush();
				}
			} catch (IOException e) {
				closeEverything(socket, bufferedReader, bufferedWriter);

			}
		}
	}

	public void removeClientHandler() {
		clientHandlers.remove(this);
		broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
	}

	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferReader) {
		removeClientHandler();
		try {
			if (bufferedReader != null){
				bufferedReader.close();
			}
			if (bufferedWriter != null){
				bufferedWriter.close();
			}
			if (socket != null){
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}