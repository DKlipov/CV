package ru.main.control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class TelemetryContainer {

	public TelemetryContainer(String type){
		last=LocalDateTime.now();
		this.type=type;
		values=new CopyOnWriteArrayList<Integer>();
		xAxis=new CopyOnWriteArrayList<String>();	
	}
	
	public LocalDateTime last;
	public CopyOnWriteArrayList<Integer> values;
	public CopyOnWriteArrayList<String> xAxis;
	public String type;
	
	
}
