package com.chlog.auto.controllers.datahelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.chlog.auto.models.Table;
import com.chlog.auto.models.RowObject;
import com.chlog.auto.models.OptionsDatabase;
import com.chlog.auto.models.OptionsSettingsTable;
import com.chlog.auto.models.OptionsReport;
import com.chlog.auto.models.Options;


public class DataHelper{
	private Connection connection=null;
	private OptionsSettingsTable optionsSettingTable;
	private OptionsReport optionsReportFields;
	
	public DataHelper() throws Exception{
		SQLServerDataSource datasource=new SQLServerDataSource();
		OptionsDatabase optionDb=OptionsDatabase.getInstance();
		datasource.setServerName(optionDb.getServerName());
		datasource.setPortNumber(optionDb.getPortName());
		datasource.setDatabaseName(optionDb.getDatabaseName());
		datasource.setUser(optionDb.getUser());
		datasource.setPassword(optionDb.getPassword());
		this.optionsSettingTable=optionDb.getOptionsSettingsTable();
		connection=datasource.getConnection();
	}
	
	public void close(){
		if(connection!=null) try { connection.close(); } catch(SQLException e){}
	}
	
	private void insertUnitStopsRow(String objectName,String tableName,RowObject row) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement=this.connection.prepareStatement(
			"insert into mukminovit.testRep (ObjectName, TableName, BeginTime, BeginPos, EndTime, Position, Duration, Quantity, MaxSpeed) "+
			"values (?,?,?,?,?,?,?,?,?)");
			preparedStatement.setString(1,objectName);//ObjectName
			preparedStatement.setString(2,tableName);//TableName
			preparedStatement.setTimestamp(3,new java.sql.Timestamp(row.getDateItem(1).getTime()));//BeginTime
			preparedStatement.setString(4,"_");//BeginPos
			preparedStatement.setTimestamp(5,new java.sql.Timestamp(row.getDateItem(2).getTime()));//EndTime
			preparedStatement.setString(6,row.getStrItem(4));//Position
			preparedStatement.setString(7,row.getStrItem(3));//Duration
			preparedStatement.setString(8,row.getStrItem(5));//Quantity
			preparedStatement.setString(9,"_");//MaxSpeed
			preparedStatement.executeUpdate();
		}catch (SQLException e){
			System.out.println(objectName+" _ "+tableName+" - "+e.getMessage());
			throw new SQLException(e);
		}finally{
			if(preparedStatement!=null)
				preparedStatement.close();
		}
	}
	private void insertUnitTripsRow(String objectName,RowObject row) throws Exception{
		PreparedStatement preparedStatement = null;
		try{
			
			preparedStatement=this.connection.prepareStatement(
			"insert into mukminovit.testRep (ObjectName, TableName, BeginTime, BeginPos, EndTime, Position, Duration, Quantity, MaxSpeed) "+
			"values (?,?,?,?,?,?,?,?,?)");
			preparedStatement.setString(1,objectName);//ObjectName
			preparedStatement.setString(2,"unit_trips");//TableName
			preparedStatement.setTimestamp(3,new java.sql.Timestamp(row.getDateItem(1).getTime()));
			preparedStatement.setString(4,row.getStrItem(2));//BeginPos
			preparedStatement.setTimestamp(5,new java.sql.Timestamp(row.getDateItem(3).getTime()));
			preparedStatement.setString(6,row.getStrItem(4));//Position
			preparedStatement.setString(7,row.getStrItem(5));//Duration
			preparedStatement.setString(8,row.getStrItem(6));//Quantity
			preparedStatement.setString(9,row.getStrItem(7));//MaxSpeed
			preparedStatement.executeUpdate();
		}catch (SQLException e){
			System.out.println(objectName+" _ unit_trips  "+e.getMessage());
			throw new SQLException(e);
		}finally{
			if(preparedStatement!=null)
				preparedStatement.close();
		}  
	}

	public void dataBindStops(String nameObject,Table table) throws Exception{
		System.out.println(nameObject);
		if(table!=null && table.getCountRows()>0){
			for(int i=0;i<table.getCountRows();++i){
				insertUnitStopsRow(nameObject,table.getName(),table.getRow(i));
			}
		}
	}
	
	public void dataBindTrips(String nameObject,Table table) throws Exception{
		if(table!=null && table.getCountRows()>0){
			for(int i=0;i<table.getCountRows();++i){
				insertUnitTripsRow(nameObject,table.getRow(i));
			}
		}
	}
	
	public void initOptions() throws Exception{
		PreparedStatement preparedStatement = null;
		ResultSet result=null;		
		try{
			String queryValue = "select Параметр_асс, Тип_строка from "+this.optionsSettingTable.getTableName()+" (nolock) "+"where Код_группы=? and Код_асс=?";
			byte bstr[]=queryValue.getBytes();
			queryValue=new String(bstr, "UTF-8");
			preparedStatement=this.connection.prepareStatement(queryValue);
			preparedStatement.setString(1,this.optionsSettingTable.getGroup());//CodeGroup
			preparedStatement.setInt(2,this.optionsSettingTable.getCode());//Code
			result=preparedStatement.executeQuery();
			while(result.next()){
				if(result.getString(1).trim().equals(this.optionsSettingTable.getUrlField())){
					Options.setUrl(result.getString(2));	//get URL					
				}else{
					if(result.getString(1).trim().equals(this.optionsSettingTable.getTokenField())){
						Options.setToken(result.getString(2)); //get Token						
					}else{
						if(result.getString(1).trim().equals(this.optionsSettingTable.getResourceNameField())){
							Options.setResourceName(result.getString(2)); //get Resource Name														
						}else{
							if(result.getString(1).trim().equals(this.optionsSettingTable.getFromTimeField())){
								Options.setFromTime(result.getString(2)); //get From Time								
							}else{
								if(result.getString(1).trim().equals(this.optionsSettingTable.getToTimeField())){
									Options.setToTime(result.getString(2));	//get To Time								
								}else{
									if(result.getString(1).trim().equals(this.optionsSettingTable.getReportIdField())){
										Options.setReportId(result.getInt(2)); //get Report ID
									}
								}
							}
						}
					}
				}				
			}			
		}catch (SQLException e){
			System.out.println(e.getMessage());
			throw new SQLException(e);
		}finally{
			if(preparedStatement!=null)
				preparedStatement.close();			
		}		
	}
	
}
