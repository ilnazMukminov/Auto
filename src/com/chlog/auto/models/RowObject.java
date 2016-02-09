package com.chlog.auto.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RowObject{
	private Object[] c;
	
	public void jsonParse(){
		for(int i=0;i<this.c.length;++i){
			int pos1=this.c[i].toString().indexOf("{t=");
			if(pos1!=-1){
				pos1=+3;
				int pos2=this.c[i].toString().indexOf("=",pos1);
				if(pos2!=-1){
					String s=this.c[i].toString().substring(pos1,pos2-3);
					this.c[i]=null;
					this.c[i]=new String(s);
				}
			}
		}
	}
 
	private String getItem(int i){
		if(i<this.c.length){
			return this.c[i].toString();
		}else{
			return null;
		}
	}
	
	public int getIntItem(int i) throws Exception{
		return Integer.parseInt(this.getItem(i));
	}
	
	public String getStrItem(int i){
		return this.getItem(i);
	}
	
	public Date getDateItem(int i) throws Exception{
		return new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(this.getItem(i));		
	}
	
	@Override
	public String toString(){
		String result="";
		for(int i=0;i<this.c.length;++i){
			result=result+c[i].toString()+" | ";
		}
		return result;
	}
}