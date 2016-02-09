package com.chlog.auto.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="settings")
public class OptionsSettingsTable{
	@Element
	private String tableName;
	@Element
	private String group;
	@Element
	private int code;
	@Element
	private String urlField;
	@Element
	private String tokenField;
	@Element
	private String resourceNameField;
	@Element
	private String fromTimeField;
	@Element
	private String toTimeField;
	@Element
	private String reportIdField;
	
	public String getTableName(){
		return this.tableName;
	}	
	public String getGroup(){
		return this.group;
	}	
	public int getCode(){
		return this.code;
	}
	public String getUrlField(){
		return this.urlField;
	}	
	public String getTokenField(){
		return this.tokenField;
	}
	public String getResourceNameField(){
		return this.resourceNameField;
	}	
	public String getFromTimeField(){
		return this.fromTimeField;
	}	
	public String getToTimeField(){
		return this.toTimeField;
	}	
	public String getReportIdField(){
		return this.reportIdField;
	}
}