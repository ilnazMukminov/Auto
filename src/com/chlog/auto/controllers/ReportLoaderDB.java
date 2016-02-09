package com.chlog.auto.controllers;

import com.chlog.auto.controllers.AbstractReportLoader;
import com.chlog.auto.controllers.datahelper.DataHelper;
import com.chlog.auto.models.Options;
import com.chlog.auto.models.Table;

public class ReportLoaderDB extends AbstractReportLoader{
	private DataHelper dataHelper;
	
	@Override
	protected void initOptions(){
		try{
			DataHelper dataHelper=new DataHelper();
			dataHelper.initOptions();	
			System.out.println(Options.getUrl());
			System.out.println(Options.getToken());			
			System.out.println(Options.getToTimeD());
			System.out.println(Options.getFromTimeD());
			System.out.println(Options.getReportId());
			System.out.println(Options.getResourceName());	
			System.out.println(Options.getTableNameFromSS());
			for(int i=0;i<Options.getArrayTableNameReport().length;++i)
				System.out.println(Options.getArrayTableNameReport()[i]);			
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
	public static void main(String[] args){
		new Thread(new ReportLoaderDB()).start();		
	}

	@Override
	protected void dispose(){
		if(dataHelper!=null){
			dataHelper.close();
		}
	}
}