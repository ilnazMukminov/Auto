package com.chlog.auto.models;

import com.chlog.auto.models.Table;
import java.util.ArrayList;

public class ReportResult{
	private ArrayList<Table> tables;
 
	public Table getTable(int i){
		if(i<getTablesCount())
			return this.tables.get(i);
		return null;
	}
	
	public int getTablesCount(){
		if(this.tables!=null && this.tables.size()>0)
			return tables.size();
		return 0;
	}
	
    public void trimTables(){
		if(getTablesCount()>0){
			int i=0;
			while(i<this.tables.size()){
				if(this.tables.get(i).getIsDelete()){
					this.tables.remove(i);
					this.tables.trimToSize();					
				}else{
					++i;
				}					
			}
		}		
	}	
} 