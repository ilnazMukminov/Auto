package com.chlog.auto.models;

import com.chlog.auto.models.ReportResult;
import com.chlog.auto.models.Table;

public class ReportResultObj{
	private ReportResult reportResult;

	public ReportResult getReportResult(){
		return this.reportResult;
	}

	public int getTablesCount(){
		if(this.reportResult!=null)
			return this.reportResult.getTablesCount();
		return 0;
	}

	public Table getTable(int i){
		if(this.reportResult!=null)
			return this.reportResult.getTable(i);
		return null;
	}
	
	public void trimTables(){
		if(this.reportResult!=null){
			this.reportResult.trimTables();
		}
	}
}