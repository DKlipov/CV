package ru.connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

class SocketListener extends Thread {
    ServerSocket servers = null;
    SocketListener(){
    }
	@Override
    public void run() {
		System.out.println("Socket server start");
        	createSocket();
        	while(true){
        	DeviceManager.addDevice(new Device(listenSocket()));
        	}
    }
	private void createSocket(){
	    try {
		      servers = new ServerSocket(50007);
		    } catch (IOException e) {
		      System.out.println("Couldn't listen to port 50007");
		      System.exit(-1);
		    }
	}
	private Connector listenSocket(){
	    Socket       fromclient = null;
	    try {
		      System.out.println("Waiting a new client");
		      fromclient= servers.accept();
		      System.out.println("Client connected");
		      Connector CR=new Connector(fromclient);
			    return CR;
		    } catch (IOException e) {
		      System.out.println("Can't accept client");
		      System.exit(-1);
		    }
	    return null;

	   }

	
}

public final class DeviceManager{
	
	public static void main(String [] args){
		DeviceManager.getDevice("null");
	}
	
    private volatile static ConcurrentHashMap<String,Device> deviceList=new ConcurrentHashMap<>();
    
    static{
    	System.out.println("Device manager init");
    	SocketListener listener = new SocketListener();
    	listener.start();
    	addDevice(new Device("robot1"));
    }
	
    static void addDevice(Device device){
    		if(deviceList.containsKey(device.getName())){
    			deviceList.remove(device.getName());
    		}
    	deviceList.put(device.getName(), device);
    }
    public static boolean containsDevice(String str){
    	return deviceList.containsKey(str);
    }
    public static Device getDevice(String str){
    	return deviceList.get(str);
    }
    public static void init(){
    	
    }
	public static String[] getDeviceNames(){
		return deviceList.keySet().toArray(new String[deviceList.keySet().size()]);
	}
	

}
