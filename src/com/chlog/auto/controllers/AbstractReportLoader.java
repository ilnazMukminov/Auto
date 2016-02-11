package com.chlog.auto.controllers;

import com.chlog.auto.controllers.AbstractReportController;
import com.chlog.auto.models.Options;
import com.chlog.auto.models.AutoObject;
import com.chlog.auto.models.Table;
import com.chlog.auto.models.RowObject;
import com.chlog.auto.models.ReportResultObj;
import com.wialon.item.Item;
import com.wialon.item.Unit;

import com.google.gson.Gson;

public abstract class AbstractReportLoader extends AbstractReportController{
	private AutoObject[] auto;
	
	protected abstract void exportTable(String nameObj,Table table);
			
	@Override
	protected void processingError(String functionName,String error){
		System.out.println("Error ("+functionName+"): "+error);
	}
	@Override
	protected long getAutoObjectId(){
		return auto[AutoObject.number].getId();
	}
	@Override
	protected void initArrayAutoObjects(Item... items){
		auto=new AutoObject[items.length];
		for(int i=0;i<items.length;++i){
			auto[i]=new AutoObject((Unit)items[i]);
			System.out.println(auto[i]);
		}
	}
	@Override
	protected boolean checkNumberAutoObject(){
		return AutoObject.number<auto.length;
	}
	@Override
	protected void incNumberAutoObject(){
		++AutoObject.number;
		Table.number=0;	
	}
	@Override 
	protected int getTableNumber(){
		if(auto!=null && auto.length>AutoObject.number && auto[AutoObject.number].getTable(Table.number)!=null)			
			return auto[AutoObject.number].getTableIndex(Table.number);
		return 0;
	}
	@Override
	protected int getTableRowsCount(){
		if(auto!=null && auto.length>AutoObject.number && auto[AutoObject.number].getTable(Table.number)!=null)			
			return auto[AutoObject.number].getTable(Table.number).getCountRows();
		return 0;
	}
	@Override
	protected void incNumberTable(){
		++Table.number;
	}
	@Override
	protected boolean checkNumberTable(){
		if(auto!=null && auto.length>AutoObject.number)
			return auto[AutoObject.number].getTablesCount()>Table.number;
		return false;
	}	
	@Override
	protected void initRowsForTable(String json,Gson gson){
		RowObject[] rowObj=new RowObject[auto[AutoObject.number].getTable(Table.number).getCountRows()];   
		rowObj=gson.fromJson(json, rowObj.getClass());  
		auto[AutoObject.number].getTable(Table.number).setRows(rowObj);
		exportTable(auto[AutoObject.number].getName(),auto[AutoObject.number].getTable(Table.number));
	}
	@Override
	protected void initTablesForAutoObject(String json,Gson gson){
		ReportResultObj resObj=new ReportResultObj();   
		resObj=gson.fromJson(json, resObj.getClass());
		auto[AutoObject.number].setReportResult(resObj,Options.getTablesNameReport());
		Table.number=0;		
	}		
}