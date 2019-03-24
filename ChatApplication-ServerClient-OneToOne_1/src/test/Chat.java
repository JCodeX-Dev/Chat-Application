
package test;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * chat class
 * manages incoming & outgoing messages
 * @author Jeremy Sam
 */
public class Chat extends javax.swing.JFrame {

    Socket socket;
    InputStream  is;
    OutputStream os;
    String name;
    Thread dataReceiver;
    /**
     * gets inputstream & output stream details
     * creates thread object which runs to listen incoming messages in the input stream
     * @param socket    socket details to which the client is to be connected
     * @param name      name of the chat user
     * @throws IOException 
     */
    public Chat(Socket socket , String name) throws IOException {
        initComponents();
        setTitle(name);
        this.name = name;
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        String send = name+" connected...&";
        os.write(send.getBytes());
        os.flush();
        textField1.addTextListener(new TextListener() {
            @Override
            public void textValueChanged(TextEvent e) {
                textField1textValueChanged();
            }
        });
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                textField1.setVisible(false);
                textField1.setText("#terminate#");
                textField1textValueChanged();
                dispose();
                System.exit(0);
            }
            
        });
        dataReceiver = new  Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                      
                        if(is.available()>0){
                            int data;
                            textArea1.setForeground(Color.red);
                            while((data=is.read())!=38){               //reads data until unicode value 38 for '&' is read
                                textArea1.append(""+(char)data);    //writes data into textarea by typecasting unicode into character then converting to string
                            }
                            textArea1.append("\n");
                        }
                    
                    } catch (IOException ex) {
                           System.out.println("incoming error");
                    }
                }
            }
        });
        dataReceiver.start();
    } 

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button1 = new java.awt.Button();
        textField1 = new java.awt.TextField();
        textArea1 = new java.awt.TextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        button1.setActionCommand("button");
        button1.setLabel("Send");
        button1.setName("Send"); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        textField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textArea1, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addContainerGap())
        );

        button1.getAccessibleContext().setAccessibleName("Connect");

        pack();
    }// </editor-fold>//GEN-END:initComponents
/**
 * 
 * reads text from textfield & writes into outputstream by converting string into bytes
 */
    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        String text = textField1.getText();  
        
        try {
            
            textArea1.append("You: "+text+"\n");          
            text = name+": "+text +"&";
            os.write(text.getBytes());
            textField1.setText("");
        } catch (IOException ex) {
            textArea1.append("Message sending failed!!!!\n");
        }      
    }//GEN-LAST:event_button1ActionPerformed

    private void textField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textField1ActionPerformed
              button1ActionPerformed(evt);
               
    }//GEN-LAST:event_textField1ActionPerformed

    private void textField1textValueChanged() {
        
        System.out.println(textField1.getText());
        if(textField1.getText().equalsIgnoreCase("#terminate#")){
            try {
                os.write(("#terminate#%"+name+" disconnected&").getBytes());
                button1.setVisible(false);
                textField1.setEnabled(false);
                textArea1.append("Chat disconnected...\nReconnet to chat again\n");
                textArea1.setEditable(false);
                
                dataReceiver.stop();
                socket.close();
                
            } catch (IOException ex) {
                textArea1.append("Error in disconnecting");
            }
        }
    }

   
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button1;
    private java.awt.TextArea textArea1;
    private java.awt.TextField textField1;
    // End of variables declaration//GEN-END:variables
}
