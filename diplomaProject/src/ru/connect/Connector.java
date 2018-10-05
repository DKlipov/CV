package ru.connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

class ConnectReader extends Thread {
	Connector connector;
	InputStream in;

	ConnectReader(Connector TH, InputStream br) {
		in = br;
		connector = TH;
		this.setDaemon(true);
		this.start();
	}

	@Override
	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String inStr;
		int nullNum = 0;
		while (true) {
			try {
				inStr = br.readLine();
				if (inStr == null) {
					nullNum++;
					sleep(100);
					if (nullNum > 10) {
						connector.close();
						connector.interrupt();
						break;
					}
				} else {
					nullNum = 0;
				}
				connector.addMessage(inStr);
				if(connector.dev!=null){connector.dev.updateProperty();}
			} catch (Exception e) {
				connector.close();
				connector.interrupt();
				break;
			}
			;

		}
	}
}

public class Connector extends Thread {
	private PrintWriter out = null;
	private ConnectReader CR = null;
	private Socket sock;
	Device dev;
	String ipstring;
	volatile boolean  hasnew=false;
	private volatile boolean isClose = false;
	private volatile CopyOnWriteArrayList<String> inputList;
	private volatile CopyOnWriteArrayList<String> outputList;

	//////////////////////////////////////////////////////////////
	Connector(Socket sock) {
		ipstring=sock.getInetAddress().toString().replace("/","");
		System.out.println("Connector create "+sock.toString());
		this.sock = sock;
		outputList = new CopyOnWriteArrayList<>();
		inputList = new CopyOnWriteArrayList<>();
		try {
			if (sock == null) {
				System.out.println("null");
			}
			InputStream inStream = sock.getInputStream();
			out = new PrintWriter(sock.getOutputStream(), true);
			CR = new ConnectReader(this, inStream);
			setDaemon(true);
			this.start();
		} catch (Exception e) {
			System.out.println("Can t init new connection");
			e.printStackTrace();
		}
	}
	
	void setDevice(Device dev){
		this.dev=dev;
	}
	boolean isClosed() {
		return isClose;
	}
	boolean hasNewMessage(){
		return hasnew;
	}	
	void close() {
		isClose = true;
		this.interrupt();
	}

	void addMessage(String s) {
		synchronized (inputList) {
			hasnew=true;
			if (inputList.size() > 1000) {
				inputList.clear();
			}
			inputList.add(s);
		}
	}

	public List getInputMessage() {
		return inputList;
	}

	public void clearMessageList() {
		inputList.clear();
		hasnew=false;
	}

	public void sendMessage(String str) {
		outputList.add(str);
		this.interrupt();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);
				send();
			} catch (InterruptedException e) {
				send();
			} catch (Exception e) {
				System.out.println("client close");
				e.printStackTrace();
			} finally {
				if (isClose) {
					break;
				}
			}
		}
		System.out.println("Device "+dev.getIp()+" unconnected");
		if(dev!=null){dev.setClose();}
	}

	private void send() {
			if (outputList.size() <= 0) {
				return;
			}
			while (outputList.size() > 0) {
				String s= outputList.remove(0);
				try {
					out.println(s + "\0");
				} catch (Exception e) {
					System.out.println("cant write in socket");
					e.printStackTrace();
				}
			}
	}
}
