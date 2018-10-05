package ru.connect;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ru.beans.Mailer;
import ru.main.control.TelemetryContainer;

public class Device {
	private String name="robot1";
	private Connector connector;
	private int[] values=new int[4];
	private int x=130,y=370,a=0,t1=0,t2=0,sound=0,gas=0,motion=0;
	private ArrayList<String> arrayLog;
	private ArrayList<String> messageLog;
	private String patrolPath;
	private volatile int alarmState=0;
	
	public boolean getAlarmState(){
		return alarmState==1;
	}
	
	public void setAlarmState(int i){
		alarmState=i;
	}
	
	public String getPatrolPath(){
		return patrolPath;
	}
	
	public void setPatrolPath(String path){
		patrolPath=path;
		sendMessage("loadPath "+path);
	}
	
	
	public void startPatrol(){
		sendMessage("startPatrol");
	}
	
	public void stopPatrol(){
		sendMessage("stopPatrol");
	}
	
	public boolean getStatus(){
		if(connector==null){return false;}
		return !connector.isClosed();
	}
	void setConnector(Connector CC){
		connector=CC;
		initNewDevice();
	}
	private void doAlarm(String text){
		if(messageLog.size()>500){
			messageLog=new ArrayList<>();
		}
		messageLog.add(new Date()+ text);
		if(alarmState==0){return;}
		Mailer.dispath("PAKOOVP Alarm", name+" сообщает: возможная критическая ситуация, в "+new Date()+ text+" в охраняемом помещении");
	}
	
	void updateProperty(){
		if(connector==null){return;}
		if(!connector.hasNewMessage()){return;}
		synchronized(connector){
		List<String> al=connector.getInputMessage();
		for(String s : al){
			if(s.contains("name")){
				name=s.substring(s.indexOf(' ')+1);
		
				messageLog.add(new Date()+ " связь с "+name+" установлена");
			}else if(s.contains("tlmt")&&alarmState!=0){
				try{
			        String f=s.substring(5);
			        this.motion=Integer.parseInt(f.substring(0,f.indexOf(';')));
			        if(motion==1){doAlarm(" обнаружено движение");}
			        f=f.substring(f.indexOf(';')+2);
			        this.gas=Integer.parseInt(f.substring(0,f.indexOf(';')));
			        if(gas==1){doAlarm(" обнаружена утечка газа");}
			        f=f.substring(f.indexOf(';')+2);
			        this.sound=Integer.parseInt(f.substring(0,f.indexOf(';')));
			        if(sound==1){doAlarm(" обнаружен шум");}
				}
					catch(Exception e){
						motion=0;sound=0;gas=0;
						System.out.println(s);
						e.printStackTrace();
					}
			}else if(s.contains("tmtr")){
				System.out.println("have new tmtr");
				try{
					String f=";"+s.substring(5).replaceAll("[:-]", "");
					for(int i=0;i<(s.length()/5)-1;i++){
						f=f.substring(f.indexOf(';')+1);
						String type=f.substring(0,2);
						f=f.substring(2);
						int val=Integer.parseInt(f.substring(0,f.indexOf(';')));
						if(type.equals("TA")){
							addVal(val,"Температура1");
						}else if(type.equals("TB")){
							values[0]=val;
							addVal(val,"Температура2");
						}else if(type.equals("TC")){
							addVal(val,"Температура3");
						}else if(type.equals("SN")){
							addVal(val,"Шум");
						}else if(type.equals("GO")){
							values[1]=val;
							addVal(val,"Уровень газа");
						}else if(type.equals("PA")){
							values[3]=(int) (val/1.333);
							addVal((int) (val/1.333),"Давление");
						}else if(type.equals("VV")){
							values[2]=val;
							addVal(val,"Относительная влажность");
						}
					}		
				}	catch(Exception e){
						t1=0;t2=0;
						System.out.println(s);
						e.printStackTrace();
					}
			}else if(s.contains("coor")){
				try{
			        String f=s.substring(5);
			        this.x=Integer.parseInt(f.substring(0,f.indexOf(';')));
			        f=f.substring(f.indexOf(';')+2);
			        this.y=Integer.parseInt(f.substring(0,f.indexOf(';')));
			        f=f.substring(f.indexOf(';')+2);
			        this.a=Integer.parseInt(f.substring(0,f.indexOf(';')));}
					catch(Exception e){
						x=0;y=0;a=0;
						System.out.println(s);
						e.printStackTrace();
					}
				}else {
					if(arrayLog.size()>500){
						arrayLog=new ArrayList<String>();
					}
					arrayLog.add(s);
			}
		}
		connector.clearMessageList();
		}
	}
	
	public int[] getValues(){
		return values;
	}
	
	private void addVal(int val,String nameVal){
		LocalDateTime nowTime = LocalDateTime.now();
		int hour = nowTime.getHour();
		int minute = nowTime.getMinute();
		String now=hour+":"+minute;
		int counter=0;
		if(containers.containsKey(nameVal)){
			TelemetryContainer container = containers.get(nameVal);
			String last=container.last.getHour()+":"+container.last.getMinute();
			while(!now.equals(last)){
				System.out.println("tmtr update "+nameVal);
				container.xAxis.add(now);
				container.values.add(val);
				container.last=container.last.plusMinutes(1);
				hour = container.last.getHour();
				minute = container.last.getMinute();
				last=hour+":"+minute;
				counter++;
				if(counter>10){
					System.err.println("Error in Device.java, can not update chart");
					System.err.println(!now.equals(last));
					System.err.println(now);
					System.err.println(last);
					break;
				}
			}
			container.last=nowTime;
		} else{
			TelemetryContainer container = new TelemetryContainer(nameVal);
			container.xAxis.add(now);
			container.values.add(val);
			containers.put(nameVal, container);
		}
	}
	
	void setClose(){
		this.connector=null;
		doAlarm(" связь с устройством "+name+" потеряна");
	}
	
	private Device(){
		arrayLog=new ArrayList<>();
		messageLog=new ArrayList<>();
	}
	
	private void initNewDevice(){
		connector.setDevice(this);
		connector.sendMessage("getname");
		try {Thread.sleep(1000);} catch (InterruptedException e) {}
		connector.sendMessage("getcor");
		try {Thread.sleep(1000);} catch (InterruptedException e) {} 
		connector.sendMessage("setPosition XX130;YY370;KI0");
		try {Thread.sleep(1000);} catch (InterruptedException e) {} 
		connector.sendMessage("setEcho 1");
		updateProperty();
	}
	
	public Device(String n){
		this();	
		System.out.println("Empty device create "+n);
		name=n;
	}

	public Device (Connector CC){
		this();
		connector=CC;
		initNewDevice();
	}
	public void sendMessage(String str){
		if(connector==null){return;}
		connector.sendMessage(str);
	}
	public String getName(){
		return "robot1";
	}
	public String getIp(){
		if(connector==null){return "127.0.0.1";}
		return connector.ipstring;
	}
	public String[] getLog(int i){
		String[] ret=new String[i];
		String[] base=messageLog.toArray(new String[messageLog.size()]);
		for(int f=0;f<i;f++){
			if(base.length<=f){break;}
			ret[f]=base[base.length-f-1];
		}
		return ret;
	}
	public int[] getTemperature(){
		int[] ret=new int[2];
		ret[0]=this.t1;
		ret[1]=this.t2;
		return ret;
	}
	public int[] getTelemetry(){
		int[] ret=new int[3];
		ret[0]=this.sound;
		ret[1]=this.gas;
		ret[2]=this.motion;
		return ret;
	}
	
	
	public int[] getCoordinate(){
		int[] ret=new int[3];
		ret[0]=this.x;
		ret[1]=this.y;
		ret[2]=this.a;
		return ret;
	}

	public int getCameraPort() {
		// TODO Auto-generated method stub
		return 8085;
	}
	
	public ConcurrentHashMap<String,TelemetryContainer> containers=new ConcurrentHashMap<>();
	
	public TelemetryContainer getContainer(String type){
		return containers.get(type);
	}
	
	
	public List<String> getListCharts(){
		Enumeration<String> s=containers.keys();
		ArrayList<String> ret=Collections.list(s);
        return ret;
	}
}
