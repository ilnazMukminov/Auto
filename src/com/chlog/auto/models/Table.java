package com.chlog.auto.models;

import com.chlog.auto.models.RowObject;
import com.google.gson.annotations.Expose;

public class Table{
	@Expose
	private String name;
	@Expose
	private String label;
	@Expose
	private int flags;
	@Expose
	private int rows;
	@Expose
	private int level;
	@Expose
	private int columns;
	@Expose
	private String[] header;
	private RowObject[] arrRow;
	private int index=0;
	private boolean isDelete=true;
	public static int number;
	
	public void setIndex(int value){
		this.index=value;
	}
	
	public void setIsDelete(boolean value){
		this.isDelete=value;
	}
	
	public boolean getIsDelete(){
		return this.isDelete;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public String getName(){
		return this.name;
	}
	public String getLabel(){
		return this.label;
	}
	public int getFlags(){
		return this.flags;
	}
	public int getCountRows(){
		return this.rows;
	}
	public int getLevel(){
		return this.level;
	}
	public int getCountColumns(){
		return this.columns;
	}
	public String getHeaderCell(int num){
		if(header!=null && num<this.header.length)
			return this.header[num];
		return "";
	}
	public void setRows(RowObject[] value){
		this.arrRow=value;
		if(this.arrRow!=null && this.arrRow.length>0){
			for(int i=0;i<this.arrRow.length;++i){
				this.arrRow[i].jsonParse();
			}
		}		
	}

	public RowObject getRow(int num){
		if(this.arrRow!=null && num<this.arrRow.length){
			return this.arrRow[num];
		}
		return null;
	}

	@Override
	public String toString(){
		String result="";
		for(int i=0;i<this.getCountColumns();++i){
			result=result+getHeaderCell(i)+" | ";
		}
		result=result+"\\n";
		for(int i=0;i<this.getCountRows();++i){
			result=result+this.getRow(i).toString()+"\\n";
		}
		return result;
	}
}