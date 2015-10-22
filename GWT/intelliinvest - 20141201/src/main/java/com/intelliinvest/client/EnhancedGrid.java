package com.intelliinvest.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VStack;

public class EnhancedGrid {
	final VStack enhancedGrid;
	IButton saveButton;
	IButton expandButton;
	IButton refreshButton;
	ListGrid grid;
	final Label emptyLabel;
	String title;
	boolean isEditable = false;
	boolean isExpandable = false;
	
	public EnhancedGrid() {
		enhancedGrid = new VStack();
		emptyLabel = new Label();
	}
	
	public IButton getSaveButton() {
		return saveButton;
	}
	
	public IButton getRefreshButton() {
		return refreshButton;
	}
	
	public IButton getExpandButton() {
		return expandButton;
	}
	
	public void setExpandable(boolean isExpandable) {
		this.isExpandable = isExpandable;
	}
	
	public void resize(){
		if(null!=grid){
			System.out.println("Resizing enhance grid to " + grid.getWidth());
			enhancedGrid.setWidth(grid.getWidth());
		}
	}
	
	Layout enhanceGrid(String title, ListGrid grid){
		return enhanceGrid(title, grid, false);
	}
	
	Layout enhanceGrid(String title, final ListGrid gridTmp, boolean isEditable){
		return enhanceGrid(title, gridTmp, isEditable, "80%");
	}
	
	Layout enhanceGrid(String title, final ListGrid gridTmp, boolean isEditable, String width){
		this.isEditable = isEditable;
		this.grid = gridTmp;
		this.title = title;
		enhancedGrid.setWidth(grid.getWidth());
		enhancedGrid.setHeight("25");
		enhancedGrid.setAlign(Alignment.CENTER);
		
		HStack gridHeader = new HStack();
		gridHeader.setWidth("100%");
		gridHeader.setPadding(0);
		gridHeader.setMembersMargin(0);
		gridHeader.setMargin(0);
		gridHeader.setHeight("25");
		gridHeader.setStyleName("intelliinvest");
		gridHeader.setAlign(Alignment.CENTER);
		
		Label titleLabel = new Label();
		titleLabel.setWidth(width);
		titleLabel.setWrap(false);
		titleLabel.setHeight("25");
		titleLabel.setContents("<B>" + title + "</B>");
		titleLabel.setAlign(Alignment.CENTER);
	
		gridHeader.addMember(titleLabel);
		
		if(isEditable)
		{
			saveButton = new IButton("Save");
			saveButton.setIcon("[SKIN]/actions/save.png");
			saveButton.setHeight("22");
			saveButton.setWidth("9%");
			saveButton.setAutoFit(true);
			gridHeader.addMember(saveButton);

			Label label = new Label();
			label.setHeight("22");
			label.setWidth("2%");
			gridHeader.addMember(label);
			
			refreshButton = new IButton("Refresh");
			refreshButton.setIcon("[SKIN]/actions/refresh.png");
			refreshButton.setAutoFit(true);
			refreshButton.setHeight("22");
			refreshButton.setWidth("9%");
			gridHeader.addMember(refreshButton);
		}
		
		if(isExpandable)
		{
			expandButton = new IButton("Expand");
			expandButton.setIcon("[SKIN]/actions/plus.png");
			expandButton.setHeight("22");
			expandButton.setWidth("9%");
			expandButton.setAutoFit(true);
			gridHeader.addMember(expandButton);

			Label label = new Label();
			label.setHeight("22");
			label.setWidth("2%");
			gridHeader.addMember(label);
			
			refreshButton = new IButton("Refresh");
			refreshButton.setIcon("[SKIN]/actions/refresh.png");
			refreshButton.setAutoFit(true);
			refreshButton.setHeight("22");
			refreshButton.setWidth("9%");
			gridHeader.addMember(refreshButton);
		}
		
        enhancedGrid.addMember(gridHeader);
 
        grid.setBaseStyle("tallCell");
        grid.setAlternateRecordStyles(true);
        grid.setShowClippedValuesOnHover(true);
        grid.setAutoFitFieldWidths(true);
        grid.setAutoFitWidthApproach(AutoFitWidthApproach.TITLE);
        grid.setAutoFitHeaderHeights(true);        
        enhancedGrid.addMember(grid);
        return enhancedGrid;
	}
}
