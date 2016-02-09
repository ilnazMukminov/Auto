package com.chlog.auto.models;

import com.wialon.item.Unit;
import com.chlog.auto.models.ReportResultObj;
import com.chlog.auto.models.Table;

public class AutoObject{
	public static int number=0;
	private ReportResultObj reportResultObj;
	private Unit unit;
	
	public AutoObject(){}

	public AutoObject(Unit unit){
		this.unit=unit;
	}
	public void setReportResult(ReportResultObj value){
		this.reportResultObj=value;
		int countT=getTablesCount();
		if(countT>0){
			for(int i=0;i<countT;++i){
				getTable(i).setIndex(i);
			}
		}
	}
	public ReportResultObj getReportResultObj(){
		return this.reportResultObj;
	}
	public Unit getUnit(){
		return this.unit;
	} 
	public long getId(){
		if(this.unit!=null)
			return this.unit.getId();
		return -1;
	}
	public Table getTable(int i){
		if(this.reportResultObj!=null)
			return this.reportResultObj.getTable(i);
		return null;		
	}
	public int getTableIndex(int i){
		Table tb=this.getTable(i);
		if(tb!=null)
			return tb.getIndex(i);
		return -1;		
	}
	public int getTablesCount(){
		if(this.reportResultObj!=null)
			return this.reportResultObj.getTablesCount();
		return 0;
	}
	@Override
	public String toString(){
		if(this.unit!=null)
			return this.unit.getName();
		return "";
	}
}