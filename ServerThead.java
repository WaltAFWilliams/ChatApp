import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThead extends Server implements Runnable{

	Socket socket;
	String userName;

	public ServerThead(Socket socket){
		this.socket = socket;
	}

	private String decrypt(String message){
	  	String deencrypted = "";
	  	for(int i = 0; i < message.length(); i++){
	      	deencrypted += (char)(message.charAt(i) - 1); // B --> A   C --> B D --> C
	    }
	  	return deencrypted;
	}

	@Override
	public void run(){
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			userName = socket.getRemoteSocketAddress().toString();
			System.out.println("user@ " + userName + " has joined the chat room.");
			print("user@ " + userName + " has joined the chat room.");
			print("List of all users in the chatroom:");
			for (Socket socket : sockets) {
				print(socket.getRemoteSocketAddress().toString());
			}

			boolean done = false;
			while (!done){
				String message = bufferedReader.readLine();
				if (message == null){
					done = true;
					continue;
				}
				String encryptedMsg = "user@ " + userName + " : (encrypted) " + message;
				String decryptedMsg = "user@ " + userName + " : (decrypted) " + decrypt(message);
				print(encryptedMsg);
				print(decryptedMsg);
			}


			closeConnect();
		} catch (IOException e) {
			try {
				closeConnect();
			} catch (IOException e1){
				e1.printStackTrace();
			}
		}
	}



	private void print(String msg) throws IOException{
		PrintWriter outPut = null;
		synchronized (sockets){
		for (Socket sc : sockets){
				outPut = new PrintWriter(sc.getOutputStream());
				outPut.println(msg);
				outPut.flush();
		}
		}
	}

		//@throws

	public void closeConnect() throws IOException{
		System.out.println("user@ " + userName + " has left the chat room.");
		print("user@ " + userName + " has left the chat room.");
		synchronized (sockets){
			sockets.remove(socket);
		}
		socket.close();
	}


}