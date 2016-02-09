package com.chlog.auto.models;

import com.chlog.auto.models.Table;
import java.util.ArrayList;

public class ReportResult{
	private ArrayList<Table> tables;
 
	//public Table[] getTables(){
	//	return this.tables;
	//}
	
	public Table getTable(int i){
		if(i<getTablesCount())
			return this.tables.get(i);
		return null;
	}
	
	public int getTablesCount(){
		if(this.tables!=null && tables.size()>0)
			return tables.size();
		return 0;
	}
	
	public 
} 