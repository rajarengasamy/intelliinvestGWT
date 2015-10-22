package com.intelliinvest.client;

import java.util.Date;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.intelliinvest.client.config.Constants;
import com.intelliinvest.client.datasource.CommentDS;
import com.intelliinvest.client.datasource.PostDS;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.GroupValueFunction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.TreeGrid;

public class BlogsPanel implements IntelliInvestPanel {
	Boolean resize = false;
	boolean isAdmin = false;

	Layout blogsPanelLayout;
	
	Layout blogsSummaryLayout;
	ListGrid blogsGrid;
	HTMLPane inlineContentPane = new HTMLPane();
	final EnhancedGrid enhancedBlogsGrid;
	final PostDS postDS;
	
	Layout blogDetailsLayout = new VLayout();
	Layout blogContentLayout = new VLayout();
	Layout commentsLayout = new VLayout();
	Label titleLabel= new Label();
	HStack blogHeaderStack = new HStack();
	HTMLPane blogContentPane = new HTMLPane();
	
	final EnhancedGrid enhancedCommentsGrid;
	final CommentDS commentDS;
	TreeGrid commentsGrid;
	public static String DEFAULT_TEXT = "Enter your comments here...";

	public BlogsPanel() {
		blogsPanelLayout = new VLayout();
		this.enhancedBlogsGrid = new EnhancedGrid();
		this.enhancedCommentsGrid = new EnhancedGrid();
		postDS = new PostDS();
		commentDS = new CommentDS(-1);
		initialize();

	}

	@Override
	public boolean isVisible() {
		return blogsPanelLayout.isVisible();
	}

	public Layout getBlogPanelLayout() {
		return blogsPanelLayout;
	}

	@Override
	public void markForResize() {
		this.resize = true;
	}

	@Override
	public void resize() {
		if (!resize) {
			return;
		}
		resize = false;
		blogsPanelLayout.setWidth(IntelliInvest.WIDTH - 225	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0));
		blogsPanelLayout.setHeight(IntelliInvest.HEIGHT - 165);
		blogsSummaryLayout.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0) ); 
		blogsSummaryLayout.setHeight(IntelliInvest.HEIGHT-165); 
		inlineContentPane.setWidth(IntelliInvest.WIDTH - 280 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		blogsGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
		blogsGrid.setHeight(IntelliInvest.HEIGHT-242);
		enhancedBlogsGrid.resize();
		
		if(null!=commentsGrid){
			blogDetailsLayout.setWidth(IntelliInvest.WIDTH - 235	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0));
			blogDetailsLayout.setHeight((IntelliInvest.HEIGHT - 180));
			titleLabel.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
			blogHeaderStack.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
			blogContentPane.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
			blogContentLayout.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
			commentsLayout.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0) ); 
			commentsLayout.setHeight(IntelliInvest.HEIGHT-210); 
			commentsGrid.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
			commentsGrid.setHeight(IntelliInvest.HEIGHT-210); 
			enhancedCommentsGrid.resize();
		}
	}

	public void initialize() {
		blogsSummaryLayout = getBlogSummaryLayout();
		blogDetailsLayout = getBlogDetailsLayout();
		blogsPanelLayout.addMember(blogsSummaryLayout, 0);
		blogsPanelLayout.addMember(blogDetailsLayout, 1);
		blogDetailsLayout.hide();
	}
	
	private Layout getBlogSummaryLayout(){
		Layout blogsSummaryLayoutT = new VLayout();
		blogsSummaryLayoutT.setShowEdges(false);   
		blogsSummaryLayoutT.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0) ); 
		blogsSummaryLayoutT.setHeight(IntelliInvest.HEIGHT-165); 
		blogsSummaryLayoutT.setMembersMargin(5);
		blogsSummaryLayoutT.setPadding(5);
		
		final IButton createNewBlogBtn = new IButton("New Blog");
		createNewBlogBtn.setWidth(100);
		createNewBlogBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getNewBlogsWindow().show();
			}
		});
		
		if(null!=IntelliInvest.userDetailData && IntelliInvest.userDetailData.getUserType().equals("Admin")){
			blogsSummaryLayoutT.addMember(createNewBlogBtn);
		}
		
		
		blogsGrid = new ListGrid(){
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				if (null!=this.getFieldName(colNum) && this.getFieldName(colNum).equals("action")) {
					Label viewLink = new Label("View");
					viewLink.setHeight(25);
					viewLink.setWidth(35);
					viewLink.setStyleName("clickable");
					viewLink.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if(null==IntelliInvest.userDetailData || null==IntelliInvest.userDetailData.getUserId()){
			             		SC.say("Only logged in user can participate in blogs");
			             		return;
			             	}else{
								blogsPanelLayout.removeMember(blogDetailsLayout);
								blogDetailsLayout.destroy();
								blogDetailsLayout = getBlogDetailsLayout(record);
								blogsPanelLayout.addMember(blogDetailsLayout, 1);
								blogsSummaryLayout.hide();
								blogDetailsLayout.show();
			             	}
						}
					});
					HStack actionStack = new HStack();
					actionStack.setHeight(25);
					actionStack.addMember(viewLink);
					if(null!=IntelliInvest.userDetailData && IntelliInvest.userDetailData.getUserType().equals("Admin")){
						Label editLink = new Label("Edit");
						editLink.setHeight(25);
						editLink.setWidth(35);
						editLink.setStyleName("clickable");
						editLink.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								getEditBlogsWindow(record).show();
							}
						});
						actionStack.setMembersMargin(0);
						actionStack.setPadding(0);
						actionStack.addMember(editLink);
					}
					return actionStack;
				} else {
					return null;
				}
			}
			@Override  
            protected Canvas getExpansionComponent(final ListGridRecord record) {  
				Layout layout = new Layout();
				inlineContentPane = new HTMLPane();
				inlineContentPane.setWidth(IntelliInvest.WIDTH - 280 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
				inlineContentPane.setHeight(100);
				inlineContentPane.setOverflow(Overflow.VISIBLE);
				layout.addMember(inlineContentPane);
				IntelliInvestServiceFactory.blogsService.getBlogContents(new Integer(record.getAttribute("postId")), new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						inlineContentPane.setContents(result);
						inlineContentPane.redraw();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						inlineContentPane.setContents("Error");
					}
				});
				return layout;
			}
		};
		
		blogsGrid.setWidth(IntelliInvest.WIDTH - 235 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
		blogsGrid.setHeight(IntelliInvest.HEIGHT-210); 
		if(null!=IntelliInvest.userDetailData && IntelliInvest.userDetailData.getUserType().equals("Admin")){
			blogsGrid.setHeight(IntelliInvest.HEIGHT-235); 
		}
		ListGridField postIdField = new ListGridField("postId", "PostId", 120);
		postIdField.setHidden(true);
		ListGridField authorField = new ListGridField("author", "Author", 120);
		ListGridField titleField = new ListGridField("title", "Title");
        ListGridField createdTimeField = new ListGridField("createdTime", "Created", 100);
        createdTimeField.setType(ListGridFieldType.DATETIME);
        createdTimeField.setGroupValueFunction(new GroupValueFunction() {
			@SuppressWarnings("deprecation")
			@Override
			public Object getGroupValue(Object value, ListGridRecord record,
					ListGridField field, String fieldName, ListGrid grid) {
				Date date = (Date) value;
				return Constants.months[date.getMonth()] + " " + (date.getYear()+1900) ;
			}
		});
        ListGridField updatedTimeField = new ListGridField("updatedTime", "Updated", 100);
        updatedTimeField.setType(ListGridFieldType.DATETIME);
        
        ListGridField actionField = new ListGridField("action", "Action", 45);
        if(null!=IntelliInvest.userDetailData && IntelliInvest.userDetailData.getUserType().equals("Admin")){
        	actionField.setWidth(70);
        }
        actionField.setAlign(Alignment.CENTER);
        
        authorField.setWidth("10%");
        authorField.setWrap(false);
        authorField.setAutoFitWidth(true);
        titleField.setWidth("54%");
        titleField.setWrap(false);
        createdTimeField.setWidth("13%");
        createdTimeField.setWrap(false);
        createdTimeField.setAutoFitWidth(true);
        updatedTimeField.setWidth("13%");
        updatedTimeField.setWrap(false);
        updatedTimeField.setAutoFitWidth(true);
        actionField.setWidth("10%");
        actionField.setWrap(false);
        actionField.setAutoFitWidth(true);
		
        blogsGrid.setFields(postIdField, authorField, titleField, createdTimeField, updatedTimeField, actionField);
        blogsGrid.setWrapCells(true);
        blogsGrid.setShowRecordComponents(true);
        blogsGrid.setShowRecordComponentsByCell(true);
		blogsGrid.setCanExpandRecords(true);
        blogsGrid.setCanEdit(false);
        blogsGrid.setDataSource(postDS);
        blogsGrid.setShowHeaderMenuButton(false);
        blogsGrid.setShowFilterEditor(true);  
        blogsGrid.setCanReorderFields(false);   
        blogsGrid.setAutoFetchData(true);
        blogsGrid.setAutoSaveEdits(false);
        blogsGrid.setShowHeaderMenuButton(false);
        blogsGrid.setShowHeaderContextMenu(false);
        blogsGrid.setCanResizeFields(false);
        blogsGrid.setFixedRecordHeights(false); 
        blogsGrid.setGroupByField("createdTime");
        blogsGrid.setSortField("createdTime");
        blogsGrid.setSortDirection(SortDirection.DESCENDING);
        blogsGrid.setGroupStartOpen(GroupStartOpen.FIRST);
        blogsSummaryLayoutT.addMember(enhancedBlogsGrid.enhanceGrid("Blogs", blogsGrid, true));
        enhancedBlogsGrid.getSaveButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				blogsGrid.saveAllEdits();
				postDS.executeAdd();
				postDS.executeUpdate();
				postDS.executeRemove();
			}
		});
        
        enhancedBlogsGrid.getSaveButton().setVisible(false);
        enhancedBlogsGrid.getRefreshButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(null==IntelliInvest.userDetailData || blogsGrid.getAllEditRows().length>0){
					SC.ask("Unsaved data will be lost. Do you want to continue?" , new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							if(value){
								postDS.setFetch(true);
								blogsGrid.invalidateCache();
								blogsGrid.fetchData();
							}else{
								return;
							}
						}
					});
				}else{
					postDS.setFetch(true);
					blogsGrid.invalidateCache();
					blogsGrid.fetchData();
				}
			}
		});
        
        return blogsSummaryLayoutT;
	}

	private Window getNewBlogsWindow(){
		final Window blogsWindow = new Window(); 
		blogsWindow.setWidth(IntelliInvest.WIDTH - 235	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0));
		blogsWindow.setHeight((IntelliInvest.HEIGHT - 180));
		blogsWindow.setShowMinimizeButton(false);  
		blogsWindow.setIsModal(true);  
		blogsWindow.setPadding(5);
		blogsWindow.setShowCloseButton(true);
		blogsWindow.setTitle("Add New Blog");
		blogsWindow.centerInPage();
		
		final TextBox titleTextBox = new TextBox();
		titleTextBox.setTitle("Title");
		titleTextBox.setWidth(IntelliInvest.WIDTH - 250	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0)+""); 
		titleTextBox.setHeight("20");
		blogsWindow.addItem(titleTextBox);
		
//		final RichTextEditor richTextEditor=new RichTextEditor();
//		richTextEditor.setWidth(IntelliInvest.WIDTH - 250	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0)); 
//		richTextEditor.setHeight((IntelliInvest.HEIGHT - 280));
//		richTextEditor.setShowEdges(true);
//        richTextEditor.setOverflow(Overflow.HIDDEN);  
//        richTextEditor.setCanDragResize(true); 
//        blogsWindow.addItem(richTextEditor);
        
		DynamicForm form = new DynamicForm();
        final TextAreaItem textBox =new TextAreaItem();
        textBox.setShowTitle(false);
        textBox.setWidth("" + (IntelliInvest.WIDTH - 250	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0)) ); 
        textBox.setHeight("" + (IntelliInvest.HEIGHT - 280));
        form.setFields(textBox);
        blogsWindow.addItem(form);
		
		final IButton saveBlogBtn = new IButton("Save");
		saveBlogBtn.setWidth(100);
		saveBlogBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Record record = new Record();
				record.setAttribute("postId", -1);
				record.setAttribute("author", IntelliInvest.userDetailData.getUsername());
				record.setAttribute("title", titleTextBox.getValue());
//				String content = richTextEditor.getValue();
				String content = textBox.getValue().toString();
				record.setAttribute("content", content);
				postDS.addData(record);
				postDS.executeAdd();
			}
		});
		
		final IButton cancelBlogBtn = new IButton("Close");
		cancelBlogBtn.setWidth(100);
		cancelBlogBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				blogsWindow.destroy();
			}
		});
		
		HStack buttonStack = new HStack();
		buttonStack.setPadding(5);
		buttonStack.setMembersMargin(20);
		buttonStack.setHeight(25);
		buttonStack.addMember(saveBlogBtn);
		buttonStack.addMember(cancelBlogBtn);
		blogsWindow.addItem(buttonStack);
		
		return blogsWindow;
	}
	
	private Window getEditBlogsWindow(final ListGridRecord record){
		final Window blogsWindow = new Window(); 
		blogsWindow.setWidth(IntelliInvest.WIDTH - 235	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0));
		blogsWindow.setHeight((IntelliInvest.HEIGHT - 180));
		blogsWindow.setShowMinimizeButton(false);  
		blogsWindow.setIsModal(true);  
		blogsWindow.setPadding(5);
		blogsWindow.setShowCloseButton(true);
		blogsWindow.setTitle("Edit Blog");
		blogsWindow.centerInPage();
		
		final TextBox titleTextBox = new TextBox();
		titleTextBox.setTitle("Title");
		titleTextBox.setWidth(IntelliInvest.WIDTH - 250	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0)+""); 
		titleTextBox.setHeight("20");
		titleTextBox.setValue(record.getAttribute("title"));
		blogsWindow.addItem(titleTextBox);
		
		DynamicForm form = new DynamicForm();
		final TextAreaItem textBox =new TextAreaItem();
        textBox.setShowTitle(false);
        textBox.setWidth("" + (IntelliInvest.WIDTH - 250	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0)) ); 
        textBox.setHeight("" + (IntelliInvest.HEIGHT - 280));
        form.setFields(textBox);
        blogsWindow.addItem(form);
//		final Layout richTextEditorLayout = new Layout();
//		richTextEditorLayout.setWidth(IntelliInvest.WIDTH - 250	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0)); 
//		richTextEditorLayout.setHeight((IntelliInvest.HEIGHT - 280));
//        blogsWindow.addItem(richTextEditorLayout);
		
        IntelliInvestServiceFactory.blogsService.getBlogContents(new Integer(record.getAttribute("postId")), new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
//				DynamicForm form = new DynamicForm();
//		        final TextAreaItem textBox =new TextAreaItem();
//		        textBox.setShowTitle(false);
//		        textBox.setWidth("" + (IntelliInvest.WIDTH - 250	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0)) ); 
//		        textBox.setHeight("" + (IntelliInvest.HEIGHT - 280));
//		        form.setFields(textBox);
//		        blogsWindow.addItem(form);
		        textBox.setValue(result);
//				richTextEditorLayout.addMember(richTextEditor);
			}
			
			@Override
			public void onFailure(Throwable caught) {
//				final RichTextEditor richTextEditor=new RichTextEditor();
//				richTextEditor.setID("richTextEditor");
//				richTextEditor.setWidth(IntelliInvest.WIDTH - 250	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0)); 
//				richTextEditor.setHeight((IntelliInvest.HEIGHT - 280));
//				richTextEditor.setShowEdges(true);
//		        richTextEditor.setOverflow(Overflow.HIDDEN);  
//		        richTextEditor.setCanDragResize(true); 
				textBox.setValue("Error retrieving value");
//				richTextEditorLayout.addMember(richTextEditor);
				
			}
		});
        
		final IButton saveBlogBtn = new IButton("Save");
		saveBlogBtn.setWidth(100);
		saveBlogBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				record.setAttribute("title", titleTextBox.getValue());
//				String content = ((RichTextEditor)richTextEditorLayout.getMember("richTextEditor")).getValue();
				String content = textBox.getValue().toString();
				record.setAttribute("content", content);
				postDS.updateData(record);
				postDS.executeUpdate();
			}
		});
		
		final IButton cancelBlogBtn = new IButton("Close");
		cancelBlogBtn.setWidth(100);
		cancelBlogBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				blogsWindow.destroy();
			}
		});
		
		HStack buttonStack = new HStack();
		buttonStack.setPadding(5);
		buttonStack.setMembersMargin(20);
		buttonStack.setHeight(25);
		buttonStack.addMember(saveBlogBtn);
		buttonStack.addMember(cancelBlogBtn);
		blogsWindow.addItem(buttonStack);
		
		return blogsWindow;
	}
	
	private Layout getBlogDetailsLayout(){
		VLayout blogDetailsLayout = new VLayout();
		return blogDetailsLayout;
	}
	private Layout getBlogDetailsLayout(final ListGridRecord record){
		VLayout blogDetailsLayout = new VLayout();
		blogDetailsLayout.setWidth(IntelliInvest.WIDTH - 235	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0));
		blogDetailsLayout.setHeight((IntelliInvest.HEIGHT - 180));
		
		blogContentLayout = getBlogContentsLayout(record);
		blogDetailsLayout.addMember(blogContentLayout);
		
		commentsLayout = getCommentsLayout(record);
		blogDetailsLayout.addMember(commentsLayout);
		
		return blogDetailsLayout;
	}
	
	private Layout getBlogContentsLayout(final ListGridRecord record) {
		
		VLayout blogContentLayout = new VLayout();
		blogContentLayout.setStyleName("intelliinvest");	
		blogContentLayout.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		
		titleLabel = new Label();
		titleLabel.setStyleName("intelliinvest");	
		titleLabel.setContents("<font Style=\"color:black;align:center;valign:center;font-size:13px;\"> <b>" + record.getAttribute("title") + "</b></font>");
		titleLabel.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		titleLabel.setHeight(25);
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setWrap(true);
		blogContentLayout.addMember(titleLabel);
		
		Label authorLabel = new Label();
		authorLabel.setContents("<font Style=\"color:black;align:center;valign:center;font-size:12px;white-space:nowrap\"> <b>Contributor: </b>" + record.getAttribute("author") + "</font>");
		authorLabel.setWidth("50%");
		authorLabel.setHeight(25);
		authorLabel.setWrap(false);
		authorLabel.setAlign(Alignment.LEFT);
		
//		Label createdTimeLabel = new Label();
//		createdTimeLabel.setContents("<font Style=\"color:blue;align:center;valign:center;font-size:12px;white-space:nowrap\"> <b>Created:</b>" + record.getAttribute("createdTime") + "</font>");
//		createdTimeLabel.setHeight(25);
//		createdTimeLabel.setWrap(false);
//		createdTimeLabel.setAlign(Alignment.CENTER);
//		
//		Label lastUpdatedLabel = new Label();
//		lastUpdatedLabel.setContents("<font Style=\"color:blue;align:center;valign:center;font-size:12px;white-space:nowrap\"> <b>Updated:</b>" + record.getAttribute("updatedTime") + "</font>");
//		lastUpdatedLabel.setHeight(25);
//		lastUpdatedLabel.setWrap(false);
//		lastUpdatedLabel.setAlign(Alignment.CENTER);
		
		Label backLabel = new Label();
		backLabel.setContents("Back to Blogs List");
		backLabel.setStyleName("clickableBig");
		backLabel.setHeight(25);
		backLabel.setWidth("50%");
		backLabel.setWrap(false);
		backLabel.setCursor(Cursor.HAND);
		backLabel.setAlign(Alignment.RIGHT);
		backLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				blogsSummaryLayout.show();
				blogDetailsLayout.hide();
			}
		});
		backLabel.setAlign(Alignment.RIGHT);
		blogHeaderStack = new HStack();
		blogHeaderStack.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		blogHeaderStack.addMember(authorLabel);
//		blogHeaderStack.addMember(createdTimeLabel);
//		blogHeaderStack.addMember(lastUpdatedLabel);
		blogHeaderStack.addMember(backLabel);
		blogHeaderStack.setHeight(25);
		
		blogContentLayout.addMember(blogHeaderStack);
		blogContentPane = new HTMLPane();
		blogContentPane.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		blogContentPane.setHeight(100);
		blogContentPane.setOverflow(Overflow.VISIBLE);
		blogContentLayout.addMember(blogContentPane);
		IntelliInvestServiceFactory.blogsService.getBlogContents(new Integer(record.getAttribute("postId")), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				blogContentPane.setContents(result);
				blogContentPane.redraw();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				blogContentPane.setContents("Error");
			}
		});
		
		final IButton leaveCommentBtn = new IButton();
		leaveCommentBtn.setTitle("Leave comment");
		leaveCommentBtn.setPrompt("Click here to leave the comment");
		
		leaveCommentBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getNewCommentWindow(new Integer(record.getAttribute("postId")), new Integer(record.getAttribute("postId"))).show();
			}
		});
		
		blogContentLayout.addMember(leaveCommentBtn);
		return blogContentLayout;
	}

	private Window getNewCommentWindow(final Integer postId, final Integer parentId) {
		
		final Window commentsWindow = new Window(); 
		commentsWindow.setWidth(IntelliInvest.WIDTH - 245	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0));
		commentsWindow.setHeight(220);
		commentsWindow.setShowMinimizeButton(false);  
		commentsWindow.setIsModal(true);  
		commentsWindow.setShowCloseButton(true);
		commentsWindow.setTitle("Add Comment");
		commentsWindow.centerInPage();
		
		final RichTextEditor richTextEditor=new RichTextEditor();
		richTextEditor.setWidth(IntelliInvest.WIDTH - 245	- (IntelliInvest.LEFT_PANEL.isVisible() ? 200 : 0)); 
		richTextEditor.setHeight(150);
		richTextEditor.setShowEdges(true);
        richTextEditor.setOverflow(Overflow.HIDDEN);  
        richTextEditor.setCanDragResize(true); 
        commentsWindow.addItem(richTextEditor);
        
        Label label = new Label();
        label.setHeight(5);
        commentsWindow.addItem(label);
        
		final IButton leaveCommentBtn = new IButton();
		leaveCommentBtn.setTitle("Save");
		leaveCommentBtn.setPrompt("Click here to save comment");
		
		leaveCommentBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(
					ClickEvent event) {
				Record record = new Record();
				record.setAttribute("commentId", -1);
				record.setAttribute("postId", postId);
				record.setAttribute("parentId", parentId);
				record.setAttribute("commenter", IntelliInvest.userDetailData.getUsername());
				record.setAttribute("content", richTextEditor.getValue());
				commentDS.addData(record);
				commentDS.executeAdd();
				
				final Timer timer = new Timer() {
					@Override
					public void run() {
							commentDS.setFetch(true);
							commentsGrid.invalidateCache();
							commentsGrid.fetchData();
						}
				};
		    
				timer.schedule(Constants.COMMENT_REFRESH_INTERVAL);
				commentsWindow.destroy();
			}
		});
		commentsWindow.addItem(leaveCommentBtn);
		
		
		return commentsWindow;
	}

	private Layout getCommentsLayout(final ListGridRecord record){
		VLayout commentsLayout = new VLayout();
		commentsLayout.setShowEdges(false);   
		commentsLayout.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0) ); 
		commentsLayout.setHeight(IntelliInvest.HEIGHT-210); 
		
		commentsGrid = new TreeGrid(){
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				if (null!=this.getFieldName(colNum) && this.getFieldName(colNum).equals("action")) {
					Label replyLink = new Label("Reply");
					replyLink.setHeight(25);
					replyLink.setWidth(35);
					replyLink.setStyleName("clickable");
					replyLink.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							getNewCommentWindow(new Integer(record.getAttribute("postId")), new Integer(record.getAttribute("commentId"))).show();
						}
					});
					HStack actionStack = new HStack();
					actionStack.setHeight(25);
					actionStack.addMember(replyLink);
//					if(null!=IntelliInvest.userDetailData && IntelliInvest.userDetailData.getUserType().equals("Admin")){
//						Label quoteLink = new Label("Quote");
//						quoteLink.setHeight(25);
//						quoteLink.setWidth(35);
//						quoteLink.setStyleName("clickable");
//						quoteLink.addClickHandler(new ClickHandler() {
//							@Override
//							public void onClick(ClickEvent event) {
//								getEditBlogsWindow(record).show();
//							}
//						});
//						actionStack.setMembersMargin(0);
//						actionStack.setPadding(0);
//						actionStack.addMember(quoteLink);
//					}
					return actionStack;
				} else {
					return null;
				}
			}
		};
		
		commentsGrid.setWidth(IntelliInvest.WIDTH - 245 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));   
		commentsGrid.setHeight(IntelliInvest.HEIGHT-210); 
		ListGridField commentIdField = new ListGridField("commentId", "CommentId", 120);
		commentIdField.setHidden(true);
		ListGridField parentIdField = new ListGridField("parentId", "ParentId", 120);
		parentIdField.setHidden(true);
		ListGridField commenterField = new ListGridField("commenter", "Commenter", 120);
		ListGridField contentField = new ListGridField("content", "Comment");
        ListGridField createdTimeField = new ListGridField("createdTime", "Created", 100);
        createdTimeField.setType(ListGridFieldType.DATETIME);
        ListGridField actionField = new ListGridField("action", "Action", 45);
        if(null!=IntelliInvest.userDetailData && IntelliInvest.userDetailData.getUserType().equals("Admin")){
        	actionField.setWidth(70);
        }
        actionField.setAlign(Alignment.CENTER);
        
        contentField.setWidth("64%");
        contentField.setWrap(false);
        commenterField.setWidth("13%");
        commenterField.setWrap(false);
        commenterField.setAutoFitWidth(true);
        createdTimeField.setWidth("13%");
        createdTimeField.setWrap(false);
        createdTimeField.setAutoFitWidth(true);
        actionField.setWidth("10%");
        actionField.setWrap(false);
        actionField.setAutoFitWidth(true);
        
        commentsGrid.setFields(commentIdField, parentIdField, contentField, commenterField, createdTimeField, actionField);
        commentsGrid.setAutoFitData(Autofit.VERTICAL);
        commentsGrid.setWrapCells(true);
        commentsGrid.setShowRecordComponents(true);
        commentsGrid.setShowRecordComponentsByCell(true);
        commentsGrid.setCanEdit(false);
        commentDS.setPostId(new Integer(record.getAttribute("postId")));
        commentsGrid.setDataSource(commentDS);
        commentsGrid.setShowHeaderMenuButton(false);
        commentsGrid.setCanReorderFields(false);   
//        commentsGrid.setAutoFetchData(true);
        commentsGrid.setAutoSaveEdits(false);
        commentsGrid.setShowHeaderMenuButton(false);
        commentsGrid.setShowHeaderContextMenu(false);
        commentsGrid.setCanResizeFields(false);
        commentsGrid.setFixedRecordHeights(false); 
        commentsGrid.setSortField("createdTime");
        commentsGrid.setSortDirection(SortDirection.DESCENDING);
        commentsGrid.setShowConnectors(true);  
        commentsGrid.setIconSize(0);
        
        enhancedCommentsGrid.setExpandable(true);
        commentsLayout.addMember(enhancedCommentsGrid.enhanceGrid("Comments", commentsGrid, false));
        enhancedCommentsGrid.getExpandButton().addClickHandler(new ClickHandler() {
        	@Override
        	public void onClick(ClickEvent event) {
        		if(enhancedCommentsGrid.getExpandButton().getTitle().equals("Expand")){
        			commentsGrid.getData().openAll();
            		enhancedCommentsGrid.getExpandButton().setTitle("Collapse");
        		}else{
        			commentsGrid.getData().closeAll();
            		enhancedCommentsGrid.getExpandButton().setTitle("Expand");
        		}
        	}
        });
        
        enhancedCommentsGrid.getRefreshButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(null==IntelliInvest.userDetailData || commentsGrid.getAllEditRows().length>0){
					SC.ask("Unsaved data will be lost. Do you want to continue?" , new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							if(value){
								commentDS.setFetch(true);
								commentsGrid.invalidateCache();
								commentsGrid.fetchData();
							}else{
								return;
							}
						}
					});
				}else{
					commentDS.setFetch(true);
					commentsGrid.invalidateCache();
					commentsGrid.fetchData();
				}
			}
		});
        
        commentDS.setFetch(true);
		commentsGrid.invalidateCache();
		commentsGrid.fetchData();
		
        return commentsLayout;
	}
}
