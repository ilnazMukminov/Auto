package com.chlog.auto.models;

import com.wialon.item.Item;
import com.wialon.item.prop.Report;

import java.util.Date;
import java.util.TimeZone;


import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Options{
	private static String url;
	private static String token;
	private static String resourceName;
	private static Item.ItemType resourceType=Item.ItemType.avl_resource;
	private static Item.ItemType objectType=Item.ItemType.avl_unit;
	private static Date fromTime;
	private static Date toTime;
	private static int reportId=2;
	private static String[] arrayTableNameReport;
	private static String tableNameFromSS;
	
	public static void setArrayTableNameReport(String[] value){
		arrayTableNameReport=value;
	}
	public static void setTableNameFromSS(String value){
		tableNameFromSS=value;
	}	
	public static void setUrl(String value){
		url=value;
	}
	public static void setToken(String value){
		token=value;
	}
	public static void setResourceName(String value){
		resourceName=value;
	}
	public static void setFromTime(long value){
		fromTime=new Date(value);
	}
	public static void setFromTime(Date value){
		fromTime=value;
	}
	public static void setFromTime(String value) throws ParseException{
		fromTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);		
	}
	public static void setToTime(long value){
		toTime=new Date(value);
	}
	public static void setToTime(Date value){
		toTime=value;
	}
	public static void setToTime(String value) throws ParseException{
		toTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);		
	}
	public static void setReportId(int value){
		reportId=value;
	}
	
	public static String[] getArrayTableNameReport(){
		return arrayTableNameReport;
	}
	public static String getTableNameFromSS(){
		return tableNameFromSS;
	}
	public static String getToken(){
		return token;
	}
	public static String getUrl(){
		return url;
	}
	public static String getResourceName(){
		return resourceName;
	}
	public static Item.ItemType getResourceType(){
		return resourceType;
	}
	public static Item.ItemType getObjectType(){
		return objectType;
	}
	public static long getFromTime(){
		return fromTime.getTime();
	}
	public static long getToTime(){
		return toTime.getTime();
	}
	public static Date getFromTimeD(){
		return fromTime;
	}
	public static Date getToTimeD(){
		return toTime;
	}
	public static Report.ReportInterval getReportInterval(){
		Report.ReportInterval interval=new Report.ReportInterval(getFromTime()/1000,getToTime()/1000,0);//16777216
		return interval;
	}	
	public static int getReportId(){
		return reportId;
	}
}