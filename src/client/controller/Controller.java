/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Request;
import models.Student;

/**
 *
 * @author cuongnv
 */
public class Controller {
    private String host ="localhost";
    private int port = 8888;

    private DatagramPacket dataP;
    private DatagramSocket client ;
    public Controller(){
        open();
    }
    public boolean update(Student s){
        send(new Request("update",(Object)s));
        return (boolean)receive();
        
    }
    public boolean add(Student s){
        send(new Request("add", (Object)s));
        return (boolean)receive();
    }
    public boolean delete(int ma){
        send(new Request("delete",(Object)ma));
        return (boolean)receive();
    }
    public ArrayList<Student> search(String key){
        send(new Request("search",(Object)key));
        return (ArrayList<Student>) receive();
                
    }
    public void send(Request req){
        ObjectOutputStream oos = null;
        try {
            byte[] data = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(req);
            oos.flush();
            data=baos.toByteArray();
            DatagramPacket dataP = new DatagramPacket(data, data.length,InetAddress.getByName(host),port);
            client.send(dataP);
            
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    
    }
    public Object receive(){
        Object o=null;
        try {
            byte[] data = new byte[1024];
            DatagramPacket datap =new DatagramPacket(data, data.length);
            client.receive(datap);
            
            
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            o =ois.readObject();
            
            
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o;
        
        
    }
    public void open(){
        try {
            client = new DatagramSocket();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
