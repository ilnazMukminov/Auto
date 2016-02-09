package com.chlog.auto.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="report")
public class OptionsReport{
	@Element
	private String tableNameField;
	@Element
	private String tablesForReportField;
	
	public String getTableNameField(){
		return this.tableNameField;
	}	
	public String getTablesForReportField(){
		return this.tablesForReportField;
	}	
}