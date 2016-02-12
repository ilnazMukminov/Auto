package com.chlog.auto.controllers;

import com.chlog.auto.controllers.AbstractReportLoader;
import com.chlog.auto.controllers.datahelper.DataHelper;
import com.chlog.auto.models.Options;
import com.chlog.auto.models.Table;

public class ReportLoaderDB extends AbstractReportLoader{
	private DataHelper dataHelper;
	
	@Override
	protected void initOptions(){
		System.out.println("Start initialization");
		try{
			dataHelper=new DataHelper();
			dataHelper.initOptions();	
			System.out.println("Successful initialization");								
		}catch(Exception e){
			processingDataError("getOptions",e.getMessage());			
		}		
	}
	@Override
	protected void exportTable(String nameObject,Table table){
		if(table!=null){
			System.out.println("Object: "+nameObject+". Successful unloading table ("+table.getLabel()+") from the site");
			try{				
				if(dataHelper!=null){
					if(table.getCountColumns()==6){
						dataHelper.dataBindStops(nameObject,table);
						System.out.println("Successful loading of the table ("+table.getLabel()+")  in the database");
						
					}else
						if(table.getCountColumns()==8){
							dataHelper.dataBindTrips(nameObject,table);
							System.out.println("Successful loading of the table ("+table.getLabel()+")  in the database");
						}							
				}
			}catch(Exception e){
				processingDataError("exportTable",e.getMessage());
			}
			
		}
			
	}	
	public static void main(String[] args){
		new Thread(new ReportLoaderDB()).start();		
	}

	@Override
	protected void dispose(){
		if(dataHelper!=null){
			dataHelper.close();
		}		
		System.out.println("Exit the program");
	}
}