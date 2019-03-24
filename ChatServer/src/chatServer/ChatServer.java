package chatServer;

import java.awt.TextArea;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jeremy Sam
 */
public class ChatServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);
        ChatFrame frame = new ChatFrame();
        frame.setVisible(true);
        TextArea area = frame.textArea1;
        ThreadGroup tg = new ThreadGroup("Clients");
       
        while(true){
            Socket s = serverSocket.accept();
            new MyThread(area, s, tg);
        }
    }

    
}
