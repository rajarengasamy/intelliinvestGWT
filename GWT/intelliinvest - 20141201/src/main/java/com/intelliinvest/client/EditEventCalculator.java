package com.intelliinvest.client;

import com.intelliinvest.client.util.ConverterUtils;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;

public class EditEventCalculator{
	
	public static EditorExitHandler getPNLEditorExitCalculator() {
		return new EditorExitHandler(){
			@Override
			public void onEditorExit(EditorExitEvent event) { 
				if(event.getRowNum()<=0){
					return;
				}
				Double pnl = getPNL(event.getGrid().getEditedRecord(event.getRowNum()));
				event.getRecord().setAttribute("pnl", ConverterUtils.NumberToString(pnl,3));
				event.getGrid().refreshRow(event.getRowNum());
				event.getGrid().recalculateSummaries();
				event.getGrid().redraw();
			}
		};
	}
	
	public static EditorExitHandler getPNLROISPNLEditorExitCalculator() {
		return new EditorExitHandler(){
			@Override
			public void onEditorExit(EditorExitEvent event) {
			}
		};
	}
	
	public static EditorExitHandler getSPNLEditorExitCalculator() {
		return new EditorExitHandler(){
			@Override
			public void onEditorExit(EditorExitEvent event) {
			}
		};
	}

	public static Double getPNL(Record record) {
		try{
			return getPNL(record.getAttribute("direction"),  new Integer(record.getAttribute("quantity")), new Double(record.getAttribute("price")));
		}catch(Exception e){
			return 0D;
		}
	}
	
	public static Double getPNL(String direction, Integer quantity, Double price){
		try{
			if(direction.equalsIgnoreCase("Buy")){
				Double value = -1 * quantity * price;
				return value;
			}else if(direction.equalsIgnoreCase("Sell")){
				Double value = quantity * price;
				return value;
			}
			return 0D;
		}catch(Exception e){
			return 0D;
		}
	}
	
	public static Double getStrategyROI(Record record) {
		try{
			return getStrategyROI(record.getAttribute("signalType"), 
					new Double(record.getAttribute("currentPrice")), 
					new Double(record.getAttribute("signalPrice")));
		}catch(Exception e){
			return 0D;
		}
	}
	
	public static Double getStrategyROI(String signal, Double currentPrice, Double signalPrice){
		try{
			if(signal.toUpperCase().contains("BUY") || signal.toUpperCase().contains("HOLD") ){
				Double value = (currentPrice - signalPrice)/signalPrice;
				return value;
			}else{
				Double value = (signalPrice - currentPrice)/signalPrice;
				return value;
			}
		}catch(Exception e){
			return 0D;
		}
	}
}
