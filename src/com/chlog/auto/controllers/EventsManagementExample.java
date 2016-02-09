package com.chlog.auto.controllers;

import com.chlog.auto.models.EventsHandlerExample;

import com.wialon.core.Errors;
import com.wialon.core.Session;
import com.wialon.core.EventProvider;
import com.wialon.extra.UpdateSpec;
import com.wialon.extra.SearchSpec;
import com.wialon.item.Item;
import com.wialon.item.Resource;
import com.wialon.item.Unit;
import com.wialon.item.prop.Report;
import com.wialon.item.prop.Report.ReportInterval;
import com.wialon.remote.handlers.ResponseHandler;
import com.wialon.remote.handlers.SearchResponseHandler;

import com.chlog.auto.models.ReportResultObj;
import com.chlog.auto.models.AutoObject;
import com.chlog.auto.models.Table;
import com.chlog.auto.models.RowObject;
import com.chlog.auto.controllers.datahelper.DataHelper; 

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collection;



public class EventsManagementExample implements Runnable{
 private Session session;
 private final String url="http://hst-api.wialon.com";//"http://kit-api.wialon.com";
 private final String token="8b848d6d67907ef2cc51af36ce945db2624586AC5115AB72BE03CD5D4DD267AE185C9316";
 private final String resourceName="Челны Логистик";
 private final Item.ItemType resourceType=Item.ItemType.avl_resource;
 private final Item.ItemType autoObjectType=Item.ItemType.avl_unit;
 
 private DataHelper datahelper=null;
 private AutoObject[] autoObjects;
 private Item resourceObject;
 private Report reportObject;
 private static ReportInterval reportInterval;
 
 private byte processFlag=0;
 
 private static int reportId=2;

 public static void initReportInterval(int from,int to){
  reportInterval=new ReportInterval(from,to,16777216);
 }

 private void login(){
  try{
   datahelper=new DataHelper();
   session.initSession(this.url);
   System.out.println("INIT: "+token);
   session.loginToken(this.token,new ResponseHandler() {
    @Override
    public void onSuccess(String response){
     super.onSuccess(response);
     System.out.println(String.format("Logged succesfully. User name is %s", session.getCurrUser().getName()));
     processFlag=0;
     searchObjectBySysName(resourceType, resourceName);             
    }
    @Override
    public void onFailure(int errorCode,Throwable throwableError){
     super.onFailure(errorCode,throwableError);
     System.out.println("ERROR: "+Errors.getErrorText(errorCode));
     logout();
    }
   });
  }catch(Exception e){
   if(datahelper!=null){
    datahelper.close();
    datahelper=null; 
    logout();
   }
  }
 }

 private void searchObjectBySysName(Item.ItemType type, String valueMask){
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
    System.out.println(Errors.getErrorText(errorCode));
    logout();
   }
  });  
 }

 private void processingDataSearch(Item... items){
  if(items!=null && items.length>0){ 
   if(processFlag<1){
    processFlag=1;
    resourceObject=items[0];
    reportObject=new Report(new java.util.Hashtable<String,String>(),"Отчет",resourceObject,"","");  
    System.out.println(resourceObject.getName());
    searchObjectBySysName(autoObjectType, "*");   
   }else{
    processFlag=0;
    autoObjects=new AutoObject[items.length];
    for(int i=0;i<items.length;++i){
     autoObjects[i]=new AutoObject((Unit)items[i]);
     System.out.println(autoObjects[i]);
    }
    clearReport(); 
   }
  }  
 }

 private void clearReport(){
  reportObject.cleanupResult(new ResponseHandler(){
   @Override
   public void onSuccess(String response){
    super.onSuccess(response);
    System.out.println("Clean report is successful");    
    System.out.println(response);
    processingDataClear();
   }
   @Override
   public void onFailure(int errorCode,Throwable throwableError){
    super.onFailure(errorCode,throwableError);
    System.out.println(Errors.getErrorText(errorCode));
    logout();
   }
  });
 }

 private void processingDataClear(){
  if(processFlag<autoObjects.length){
    createReport(); 
  }else{
   processingDataCreateReport("");   
  }    
 }

 private void createReport(){   
  reportObject.execReport(reportId,autoObjects[processFlag].getUnit().getId(),0,reportInterval,new ResponseHandler(){
   @Override
   public void onSuccess(String response){
    super.onSuccess(response);
    processingDataCreateReport(response);    
   }
   @Override
   public void onFailure(int errorCode,Throwable throwableError){
    super.onFailure(errorCode,throwableError);
    System.out.println(Errors.getErrorText(errorCode));
    logout();
   }
  });
 }

 private void processingDataCreateReport(String response){  
  if(processFlag<autoObjects.length){
   Gson gson = new Gson();
   ReportResultObj resObj=new ReportResultObj();   
   resObj=gson.fromJson(response, resObj.getClass());
   autoObjects[processFlag].setReportResultObj(resObj); 
   System.out.println(autoObjects[processFlag].getReportResultObj().getTablesSize());    
   getReportRows(0);
  }else{
   System.out.println("OK");  
   logout();
  }     
 }

 private void getReportRows(int tableIndex){
  reportObject.selectResultRows(tableIndex, 0, autoObjects[processFlag].getReportResultObj().getTable(tableIndex).getCountRows(), 0, new ResponseHandler(){
   @Override
   public void onSuccess(String response){
    super.onSuccess(response);
    System.out.println("get rows is successful");        
    processingDataReportRows(tableIndex,response);
   }
   @Override
   public void onFailure(int errorCode,Throwable throwableError){
    super.onFailure(errorCode,throwableError);
    System.out.println(Errors.getErrorText(errorCode));
    logout();
   } 
  });
 } 

 private void processingDataReportRows(int tableIndex,String response){    
  System.out.println(tableIndex);  
  Gson gson = new Gson();
  RowObject[] rowObj=new RowObject[autoObjects[processFlag].getReportResultObj().getTable(tableIndex).getCountRows()];   
  rowObj=gson.fromJson(response, rowObj.getClass());  
  autoObjects[processFlag].getReportResultObj().getTable(tableIndex).setRowsObjects(rowObj);
  printTable(autoObjects[processFlag].getReportResultObj().getTable(tableIndex));
  if(datahelper!=null){
   try{
    if(tableIndex==1){
     datahelper.dataBindTrips(autoObjects[processFlag].toString(),autoObjects[processFlag].getReportResultObj().getTable(tableIndex));
    }else{
     datahelper.dataBindStops(autoObjects[processFlag].toString(),autoObjects[processFlag].getReportResultObj().getTable(tableIndex));     
    }
   }catch(Exception e){
    System.out.println(e.getMessage());
    try{
     System.in.read();
    }catch(Exception ex){}     
    if(datahelper!=null){
     datahelper.close();
     datahelper=null;
    }
   }
  }
  //try{
  // System.in.read();
   ++tableIndex;
   if(tableIndex<autoObjects[processFlag].getReportResultObj().getTablesSize()){
    getReportRows(tableIndex); 
   }else{
    System.out.println("Get Rows/ OK"); 
    clearReport();
    ++processFlag;  
   }
  //}catch(IOException e){
  // e.printStackTrace();
  //}     
 }

 private void printTable(Table table){
  System.out.println(table);
 }
 
 private void logout(){
  if(datahelper!=null) datahelper.close();
  session.logout(new ResponseHandler(){
   @Override
   public void onSuccess(String response){
    super.onSuccess(response);
    System.out.println("Logout successfully");
    System.exit(0);
   }
   @Override
   public void onFailure(int errorCode, Throwable throwableError){
    super.onFailure(errorCode,throwableError);
    System.out.println(Errors.getErrorText(errorCode));
    System.exit(0);
   }
  });
 }

 @Override
 public void run(){
  session=Session.getInstance();  
  login();
 }

 public static void main(String[] args){
  EventsManagementExample.initReportInterval(1453150800,1453237199);
  new Thread(new EventsManagementExample()).start();
 }
}