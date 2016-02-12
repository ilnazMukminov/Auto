package com.chlog.auto.controllers;

import com.wialon.core.Errors;
import com.wialon.core.Session;
import com.wialon.extra.SearchSpec;
import com.wialon.item.Item;
import com.wialon.item.Unit;
import com.wialon.item.prop.Report;
import com.wialon.remote.handlers.ResponseHandler;
import com.wialon.remote.handlers.SearchResponseHandler;

import com.chlog.auto.models.Options;
import com.google.gson.Gson;

public abstract class AbstractReportController implements Runnable{
	private Session session;
	private Item resourceObject;
	private Report reportObject;
	
	private static SearchFlag flagForSearch=SearchFlag.RESOURCE;	
	
	private enum SearchFlag{RESOURCE,AUTO}
		
	protected abstract void initOptions();	
	protected abstract void dispose();	
	protected abstract void processingError(String functionName,String error);
	protected abstract long getAutoObjectId();
	protected abstract void initArrayAutoObjects(Item... items);
	protected abstract boolean checkNumberAutoObject();
	protected abstract void incNumberAutoObject();
	protected abstract void incNumberTable();
	protected abstract boolean checkNumberTable();
	protected abstract int getTableNumber();
	protected abstract int getTableRowsCount();
	protected abstract void initRowsForTable(String json,Gson gson);
	protected abstract void initTablesForAutoObject(String json,Gson gson);
	
	private void init(){
		this.session=Session.getInstance();  
		initOptions();		
		session.initSession(Options.getUrl());
		login();
	}
	
	protected void processingDataError(String functionName,String error){
		processingError(functionName,error);
		logoff();
	}
	
	private void processingDataErrorEx(String functionName,int errorCode){
		System.out.println(errorCode);
		processingError(functionName,Errors.getErrorText(errorCode));
		processingDataLogoff();
		
	}
	
	private void processingDataError(String functionName,int errorCode){
		System.out.println(errorCode);
		processingError(functionName,Errors.getErrorText(errorCode));
		logoff();
	}
	
	private void processingDataLogin(){
		System.out.println(Options.getResourceName());
		searchObjectBySysName(Options.getResourceType(), Options.getResourceName());
	}
	
	private void processingDataSearch(Item... items){
		if(items!=null && items.length>0){
			if(flagForSearch==SearchFlag.RESOURCE){
				flagForSearch=SearchFlag.AUTO;
				resourceObject=items[0];
				reportObject=new Report(new java.util.Hashtable<String,String>(),"REPORT",resourceObject,"","");  				
				searchObjectBySysName(Options.getObjectType(), "*");   
			}else{
				flagForSearch=SearchFlag.RESOURCE;	
				initArrayAutoObjects(items);			
				clearReport();
			}
		}else{
			processingDataError("processingDataSearch","No data");
		}		
	}
	
	private void processingDataClear(){
		if(checkNumberAutoObject()){
			createReport(getAutoObjectId());
		}else{
			logoff();
		}
	}
	
	private void processingDataCreateReport(String response){
		if(checkNumberAutoObject()){
			initTablesForAutoObject(response,this.session.getGson());
			if(getTableRowsCount()>0)
				getReportRows();
			else
				processingDataReportRows("");
		}else{
			logoff();
		}     
	}
	
	private void processingDataReportRows(String response){
		if(checkNumberTable()){
			initRowsForTable(response,this.session.getGson());
			incNumberTable();
			if(getTableRowsCount()>0)
				getReportRows();
			else{
				incNumberAutoObject();
				clearReport();
			}				
		}else{
			incNumberAutoObject();
			clearReport();
		}		
	}
	
	protected void processingDataLogoff(){
		this.dispose();
		try{
			System.in.read();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.exit(0);
	}	
		
	protected void login(){
		session.loginToken(Options.getToken(),new ResponseHandler() {
			@Override
			public void onSuccess(String response){
				super.onSuccess(response);
				processingDataLogin();             
			}
			@Override
			public void onFailure(int errorCode,Throwable throwableError){
				super.onFailure(errorCode,throwableError);
				processingDataError("login",errorCode);				
			}
		});
	}
	
	protected void clearReport(){
		reportObject.cleanupResult(new ResponseHandler(){
			@Override
			public void onSuccess(String response){
				super.onSuccess(response);
				processingDataClear();
			}
			@Override
			public void onFailure(int errorCode,Throwable throwableError){
				super.onFailure(errorCode,throwableError);
				processingDataError("clearReport",errorCode);
			}
		});
	}
	
	protected void createReport(long objectId){  
		System.out.println(Options.getReportId());
		System.out.println(objectId);
		System.out.println(Options.getReportInterval());
		
		reportObject.execReport(Options.getReportId(),objectId,0,Options.getReportInterval(),new ResponseHandler(){
			@Override
			public void onSuccess(String response){
				super.onSuccess(response);				
				processingDataCreateReport(response);    
			}
			@Override
			public void onFailure(int errorCode,Throwable throwableError){
				super.onFailure(errorCode,throwableError);
				processingDataError("createReport",errorCode);	
			}
		});
	}
	
	protected void searchObjectBySysName(Item.ItemType type, String valueMask){
		SearchSpec searchSpec=new SearchSpec();
		searchSpec.setItemsType(type);
		searchSpec.setPropName("sys_name");
		searchSpec.setPropValueMask(valueMask);
		searchSpec.setSortType("sys_name");
		session.searchItems(searchSpec,1,Item.dataFlag.base.getValue(),0,Integer.MAX_VALUE,new SearchResponseHandler(){
			@Override
			public void onSuccessSearch(Item... items){
				super.onSuccessSearch(items);
				processingDataSearch(items);
			}
			@Override
			public void onFailure(int errorCode, Throwable throwableError){
				super.onFailure(errorCode,throwableError);
				processingDataError("searchObjectBySysName",errorCode);
			}
		});
	}
	
	protected void getReportRows(){
		System.out.println(getTableNumber());
		System.out.println(getTableRowsCount());
		reportObject.selectResultRows(getTableNumber(), 0, getTableRowsCount(), 0, new ResponseHandler(){
			@Override
			public void onSuccess(String response){
				super.onSuccess(response);
				processingDataReportRows(response);
			}
			@Override
			public void onFailure(int errorCode,Throwable throwableError){
				super.onFailure(errorCode,throwableError);
				processingDataError("getReportRows",errorCode);
			}
		});
	} 
	
	protected void logoff(){
		session.logout(new ResponseHandler(){
			@Override
			public void onSuccess(String response){
				super.onSuccess(response);
				processingDataLogoff();
			}
			@Override
			public void onFailure(int errorCode, Throwable throwableError){
				super.onFailure(errorCode,throwableError);
				processingDataErrorEx("logoff",errorCode);				
			}
		});
	}
 
  	@Override
	public void run(){
		this.init();
	}	
}