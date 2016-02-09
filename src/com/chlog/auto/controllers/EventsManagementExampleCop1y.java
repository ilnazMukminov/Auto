package com.chlog.auto;

import com.chlog.auto.EventsHandlerExample;

import com.wialon.core.Errors;
import com.wialon.core.Session;
import com.wialon.core.EventProvider;
import com.wialon.extra.UpdateSpec;
import com.wialon.item.Item;
import com.wialon.item.Unit;
import com.wialon.remote.handlers.ResponseHandler;

import java.io.IOException;
import java.util.Collection;


public class EventsManagementExample{
 private Session session;
 private final String url="http://hst-api.wialon.com";//"http://kit-api.wialon.com";
 private final String token="8b848d6d67907ef2cc51af36ce945db210DCB61BC6034530D521F0402F6C14A05BAB889F";
 
 private void login(){
  session.initSession(this.url);
  System.out.println("INIT: "+token);
  session.loginToken(this.token,new ResponseHandler() {
   @Override
   public void onSuccess(String response){
    super.onSuccess(response);
    System.out.println(String.format("Logged succesfully. User name is %s", session.getCurrUser().getName()));
    logout();
    //updateDataFlags();
   }

   @Override
   public void onFailure(int errorCode,Throwable throwableError){
    super.onFailure(errorCode,throwableError);
    System.out.println("ERROR: "+Errors.getErrorText(errorCode));
    logout();
   }
  });
 }

 private void updateDataFlags(){
  UpdateSpec updateSpec=new UpdateSpec();
  updateSpec.setMode(1);
  updateSpec.setType("type");
  updateSpec.setData(Item.ItemType.avl_unit);
  updateSpec.setFlags(Item.dataFlag.base.getValue() | Unit.dataFlag.lastMessage.getValue());
  session.updateDataFlags(new UpdateSpec[] {updateSpec},new ResponseHandler(){
   @Override
   public void onSuccess(String response){
    super.onSuccess(response);
    System.out.println("Update data flags is successful");
    bindEventHandler();
   }
   @Override
   public void onFailure(int errorCode,Throwable throwableError){
    super.onFailure(errorCode,throwableError);
    System.out.println(Errors.getErrorText(errorCode));
    logout();
   }
  });
 }

 private void bindEventHandler(){
  Collection<Item> sessionItems=session.getItems();
  EventsHandlerExample eventsHandlerExample=new EventsHandlerExample();
  session.addListener(eventsHandlerExample, EventProvider.events.All);
  //session.addListener(eventsHandlerExamle,EventProvider.events.All);
  if(sessionItems!=null && sessionItems.size()>0){
   System.out.println(String.format("%d items added to Session\r\nStart binding event listeners to items", sessionItems.size()));
   for(Item item:sessionItems){
    item.addListener(eventsHandlerExample,EventProvider.events.All);
   }
   System.out.println("Event listener successfully bound to items\r\nPress enter to logout and exit");
  }
  try{
   System.in.read();
   logout();
  }catch(IOException e){
   e.printStackTrace();
  }
 }

 private void logout(){
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
 

 public static void main(String[] args){
  EventsManagementExample ext=new EventsManagementExample();
  ext.session=Session.getInstance();
  ext.login();
 }
}