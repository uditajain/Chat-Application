import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import javax.swing.*;
//import javax.swing.JLabel;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;

//127.0.0.1
public class Client extends JFrame {
	Socket socket;
	// to read
	BufferedReader br;
	// to write
	PrintWriter out;
	
	//Declaring Components 
	private JLabel heading = new JLabel("Client Area");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto" , Font.PLAIN , 20);
	

	// Constructor of Client
	public Client() {
		try {
			
			  System.out.println("sending request to server"); socket = new
			  Socket("127.0.0.1",7778); System.out.println("Connection done");
			  
			  br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			   out = new PrintWriter(socket.getOutputStream()); 
			   createGUI();
				 handleEvents();
			  startReading(); 
			 // startWriting();
			 
			
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
	
	private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("Key Released" + e.getKeyCode());
				if(e.getKeyCode()==10) {
					//System.out.println("You have pressed enter button");
					String ContentToSend=messageInput.getText();
					messageArea.append("Me: " + ContentToSend + "\n");
					out.println(ContentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
					
					
					
					
					
					
				}
			}
        	
        });
		
	}

	private void createGUI() {
		//Code to create GUI
		this.setTitle("Client Messager");
		this.setSize(600,600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Coding for Compnents
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setIcon(new ImageIcon("src/clogo.png.jpg"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder( BorderFactory.createEmptyBorder(20 , 20 , 20 , 20));
		messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);		
        //Layout of Frame
		this.setLayout(new BorderLayout());
		
		//Adding the Components to Frame
		this.add(heading , BorderLayout.NORTH);
		JScrollPane jScrollPane	= new JScrollPane(messageArea);
		this.add( jScrollPane ,  BorderLayout.CENTER);
		this.add(messageInput , BorderLayout.SOUTH);
		
		
		this.setVisible(true);
	}
	

	// Method to read the read the msg and print the msg
	public void startReading() {
		// thread to read data and give us
		Runnable r1 = () -> {
			System.out.println("reader started");
			try {
				// so that it always read
				while (true) {

					String msg = br.readLine();
					if (msg.equals("exit")) {
						System.out.println("Server has terminated the chat");
						JOptionPane.showMessageDialog(this,"Server Terminated the chat");
						messageInput.setEnabled(false);
						socket.close();
						break;

					}

					//System.out.println("Server:  " + msg);
               messageArea.append("Server:  " + msg +"\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("Connection is closed ");

			}

		};
		new Thread(r1).start();

	}

	// Method to write the msg and send it
	public void startWriting() {
		// will take data from user and send to the client

		Runnable r2 = () -> {
			System.out.println("Writer Started");
			try {
				while (!socket.isClosed()) {

					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br1.readLine();
					out.println(content);
					out.flush();

					if (content.equals("exit")) {
						socket.close();
						break;
					}

				}
				// System.out.println("Connection is closed ");
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("Connection is closed ");
			}
		};
		new Thread(r2).start();

	}

	public static void main(String[] args) {
		System.out.println("This is client....");
		new Client();
	}

}
