package com.chlog.auto.controllers;

import com.chlog.auto.controllers.AbstractReportLoader;
import com.chlog.auto.models.Options;
import com.chlog.auto.models.Table;

public class ReportLoader extends AbstractReportLoader{
	@Override
	protected void initOptions(){
		Options.setUrl("http://hst-api.wialon.com");
		Options.setToken("8b848d6d67907ef2cc51af36ce945db2624586AC5115AB72BE03CD5D4DD267AE185C9316");
		Options.setResourceName("????? ????????");
		try{
			Options.setFromTime("2016-02-08 08:00:00");
			Options.setToTime("2016-02-08 17:00:00");	
			System.out.println(Options.getUrl());
			System.out.println(Options.getToken());	
			System.out.println(Options.getFromTimeD());			
			System.out.println(Options.getToTimeD());			
			System.out.println(Options.getReportId());
			System.out.println(Options.getResourceName());			
			System.in.read();			
		}catch(Exception e){
			processingDataError("getOptions",e.getMessage());			
		}		
	}
	@Override
	protected void exportTable(Table table){
		if(table!=null)
			System.out.println(table);
	}
	@Override
	protected void dispose(){}
	
	public static void main(String[] args){
		new Thread(new ReportLoader()).start();		
	}	
}