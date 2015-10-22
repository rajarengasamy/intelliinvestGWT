package com.intelliinvest.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.datasource.NewsDS;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VStack;

public class LeftPanel implements IntelliInvestPanel{
	
	final SectionStack sectionStack;
	final VStack newsCanvas;
	Boolean resize = false;
	
	public LeftPanel() {
		sectionStack = new SectionStack();
		this.newsCanvas = new VStack();
		initialize();
	}
	
	public Layout getLeftLayout() {
		return sectionStack;
	}
	
	@Override
	public void markForResize() {
		this.resize = true;
	}
	
	@Override
	public void resize() {
		if(!resize){
			return;
		}
		resize = false;
		newsCanvas.setHeight(IntelliInvest.HEIGHT-490);
		sectionStack.setHeight(IntelliInvest.HEIGHT-120);
	}
	
	@Override
	public boolean isVisible() {
		return sectionStack.isVisible();
	}

	public static final DynamicForm contactForm = new DynamicForm(); 	
	
	public DynamicForm getContactForm() {
		return contactForm;
	}
	
	static Boolean mouseAboveGrid = false;
	static Integer newsGridCenter = 0;
	
	public void initialize(){

        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);   
        sectionStack.setWidth(198);   
        sectionStack.setHeight(IntelliInvest.HEIGHT-120);  
        sectionStack.setShowResizeBar(true);
        
        newsCanvas.setWidth(198);
	    newsCanvas.setHeight(IntelliInvest.HEIGHT-490);
	    newsCanvas.setMargin(1);
	    newsCanvas.addMember(getTopStoriesGrid());
	    newsCanvas.setCursor(Cursor.HAND);
	    
        SectionStackSection newsSection = new SectionStackSection("Top Stories");
        newsSection.setExpanded(true);  
        newsSection.setCanCollapse(true);
        newsSection.addItem(newsCanvas);  
        sectionStack.addSection(newsSection);   
  
        SectionStackSection contactSection = new SectionStackSection("Contact Us");   
        
        contactForm.setNumCols(2);
        contactForm.setHeight(200);
        contactForm.setColWidths("100", "125");
        contactForm.setPadding(3);  
        
        TextItem subjectItem = new TextItem("subject");  
        subjectItem.setTitle("Subject");
        subjectItem.setWidth("125");
        
        TextItem contactNoItem = new TextItem("contactNo"); 
        contactNoItem.setTitle("Contact No");
        contactNoItem.setWidth("125");
        contactNoItem.setWrapTitle(false);
        contactNoItem.setKeyPressFilter("[0-9.]"); 
        
        TextItem mailIdItem = new TextItem("mailId"); 
        mailIdItem.setTitle("Mail Id");
        mailIdItem.setWidth("125");
        mailIdItem.setWrapTitle(false);
       
        final TextAreaItem messageItem = new TextAreaItem("message");  
        messageItem.setShowTitle(false);  
        messageItem.setColSpan(2);  
        messageItem.setWidth("188");  
        messageItem.setHeight("100");  
  
        ButtonItem sendButton = new ButtonItem("Send", "Send");
        sendButton.setColSpan(2); 
        sendButton.setWidth("80");
        sendButton.setShowTitle(false);
        sendButton.setAlign(Alignment.RIGHT);
        
        sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				IntelliInvestServiceFactory.utilService.contactUs(
						contactForm.getField("mailId").getValue().toString(), 
						contactForm.getField("contactNo").getValue().toString(), 
						contactForm.getField("subject").getValue().toString(), 
						contactForm.getField("message").getValue().toString(), 
						new AsyncCallback<String>() {
							@Override
							public void onSuccess(String result) {
								messageItem.setValue("");
								SC.say(result);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								SC.say("Error contacting. Please drop mail to IntelliInvest admin at intelli@intelliinvest.com");
								
							}
						}
							);
			}
		});
        
        contactForm.setFields(subjectItem, contactNoItem, mailIdItem, messageItem, sendButton);  
          
        contactSection.setExpanded(true);   
        contactSection.setCanCollapse(true);   
        contactSection.addItem(contactForm);   
        sectionStack.addSection(contactSection);   
  
        HTMLFlow htmlFlow2 = new HTMLFlow();   
        htmlFlow2.setOverflow(Overflow.AUTO);   
        htmlFlow2.setPadding(2);
        htmlFlow2.setContents(
        		"Double click to edit."
        		+ "<br>Click Save button after editing."
        		+ "<br>Use chart to make better decisions."
        		+ "<br>Use onscreen refresh button instead of browser refresh"
        		);
        htmlFlow2.setHeight(80);
        SectionStackSection aboutUsSection = new SectionStackSection("Tips to use site?"); 
        aboutUsSection.setExpanded(true);  
        aboutUsSection.setCanCollapse(true);   
        aboutUsSection.addItem(htmlFlow2);   
        sectionStack.addSection(aboutUsSection);   
	}
	
	private ListGrid getTopStoriesGrid(){
		final ListGrid newsGrid = new ListGrid();
        newsGrid.setWidth(198);  
        newsGrid.setHeight("100%");  
        ListGridField newsHeading = new ListGridField("title", "News");  
        newsGrid.setFixedRecordHeights(false);  
        final NewsDS newsDS = new NewsDS("topStories" , "/intelliinvest/stocknews.ns");
        newsGrid.setDataSource(newsDS);  
        newsGrid.setWrapCells(true);
        newsGrid.setCanHover(true);
        newsGrid.setHoverWidth(300);
        newsGrid.setHoverHeight(250);
        newsGrid.setHoverMoveWithMouse(true);
        newsGrid.setAutoFetchData(true);
        newsGrid.setOverflow(Overflow.HIDDEN);
        newsGrid.setShowHeader(false);
        newsGrid.setHoverCustomizer(new HoverCustomizer() {
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
            	 String description = record.getAttribute("description");
            	 description = (null==description || "".equals(description))?"Not available" : description;
            	 String pubDate = record.getAttribute("pubDate");
            	 pubDate = (null==pubDate || "".equals(pubDate))?"Not available" : pubDate;
            	 String hover = "<b>Description:</b><br>" + description + "<br><b>Published Date:</b><br>" + pubDate;
                 return hover;
            }
        });
        newsGrid.addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				ListGridRecord record = event.getRecord();
				String link = record.getAttribute("link");
				if(null==link || "".equals(link)){
					SC.say("No link to get News.");
				}
				com.google.gwt.user.client.Window.open(record.getAttribute("link"),"_blank",null);
			}
		});
        newsGrid.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				mouseAboveGrid = true;
			}
		});
        
        newsGrid.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				mouseAboveGrid = false;
			}
		});
        newsGrid.setFields(newsHeading);  
        final Timer liveGridTimer = new Timer() {
			@Override
			public void run() {
				if(!mouseAboveGrid  && null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
					newsGrid.invalidateCache();
					newsDS.fetchData();
				}
			}
		};
			
		newsGrid.addDataArrivedHandler(new DataArrivedHandler() {
			@Override
			public void onDataArrived(DataArrivedEvent event) {
				liveGridTimer.scheduleRepeating(Constants.TOP_STORIES_REFRSH_INTERVAL);
			}
		});
		
		newsGrid.setCursor(Cursor.HAND);
		
        newsGrid.draw();
        return newsGrid;
	}
}
