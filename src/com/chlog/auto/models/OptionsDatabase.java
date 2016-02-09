package com.chlog.auto.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import java.io.File;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root(name="database")
public class OptionsDatabase{
	@Element
	private String serverName;
	@Element
	private int portName;
	@Element
	private String databaseName;
	@Element
	private String user;
	@Element
	private String password;
	@Element(name="settings")
	private OptionsSettingsTable optionsSettingsTable;
	@Element(name="report")
	private OptionsReport optionsReport;
	
	public OptionsReport getOptionsReport(){
		return this.optionsReport;
	}	
	public OptionsSettingsTable getOptionsSettingsTable(){
		return this.optionsSettingsTable;
	}	
	public String getServerName(){
		return this.serverName;
	}
	public int getPortName(){
		return this.portName;
	}
	public String getDatabaseName(){
		return this.databaseName;
	}
	public String getUser(){
		return this.user;
	}
	public String getPassword(){
		return this.password;
	}
	
	private OptionsDatabase(){}
	
	public static OptionsDatabase getInstance() throws Exception{
		File xml = new File("resources/database.xml");
		Serializer ser=new Persister();
		OptionsDatabase option=ser.read(OptionsDatabase.class,xml);
		System.out.println(option.getServerName());
		System.out.println(option.getPortName());
		System.out.println(option.getDatabaseName());
		System.out.println(option.getUser());
		
		System.out.println(option.getOptionsSettingsTable().getUrlField());
		System.out.println(option.getOptionsSettingsTable().getTokenField());
		System.out.println(option.getOptionsSettingsTable().getResourceNameField());
		System.out.println(option.getOptionsSettingsTable().getFromTimeField());
		System.out.println(option.getOptionsSettingsTable().getToTimeField());
		System.out.println(option.getOptionsSettingsTable().getReportIdField());
		System.out.println(option.getOptionsSettingsTable().getCode());
		System.out.println(option.getOptionsSettingsTable().getGroup());
		return option;
	}  
}