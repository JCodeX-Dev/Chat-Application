package chatServer;


import java.awt.TextArea;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jeremy Sam
 */
public class MyThread extends Thread{
    Socket s;
    ThreadGroup tg;
    JFrame frame;
    TextArea area;
    OutputStream os;
    InputStream is;
    static int count = 1;
    
    public MyThread(TextArea area, Socket s, ThreadGroup tg) throws IOException {
        super(tg, "client: "+count);
 
        this.area = area;
        this.s = s;
        this.tg = tg;
        os = s.getOutputStream();
        is = s.getInputStream();
        System.out.println("Client "+count+" connected");
        count++;
        start();
    }

    public void SendData(String data){
        try {
            data = data + "&";
            os.write(data.getBytes());
        } catch (IOException ex) {
            System.out.println("error in sending");
        }
    }
    
    @Override
    public void run() {
       // System.out.println("running");
       String st = "start";
        while(true){
            try {
               // System.out.println("reading");
            if(is.available()>0){
                
                //System.out.println("message reciiiiiiiiiiiiiiieeeeeeeeeeeeeeeeeved");
                int d ;
                String str = "";
                
                while((d=is.read())!=38){
                   // if(d!=0){                      
                        //System.out.println("gooooooooooiiiiiiiiiing");
                        str = str + (char)d;
                       // System.out.println(str);
                    //}
                }
                if(str.startsWith("#terminate#%")){ 
                    String[] ter = str.split("%");
                    st = ter[0];
                    str = ter[1]; 
                }
                area.append(str+"\n");
                MyThread[] list = new MyThread[tg.activeCount()];
                int l = tg.enumerate(list);
                System.out.println("No. of clients"+tg.activeCount());
                for(int i=0;i<l;i++){
                    if(list[i].getId()!=Thread.currentThread().getId()){
                        list[i].SendData(str);
                    }
                }
                if(st.equalsIgnoreCase("#terminate#")){
                    throw new IOException();
                }
            }} catch (IOException ex) {
                try {
                    System.out.println(Thread.currentThread().getName()+" disconnected");
                    s.close();
                    Thread.currentThread().stop();
                } catch (IOException ex1) {
                    System.out.println("error in terminating");
                }
            }
        }
    }
}
