package com.intelliinvest.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.data.PaymentData;
import com.intelliinvest.client.service.IntelliInvestServiceFactory;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
//import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class PaymentPanel implements IntelliInvestPanel{
	final Layout paymentPanelLayout;
	Boolean resize = false;
	
	static Layout paymentGatewayLayout;
	static Layout directPaymentDetailsLayout;
	
	static Map<String,PaymentData> paymentDetailsMap = new HashMap<String,PaymentData>();
	
	public PaymentPanel() {
		this.paymentPanelLayout = new VLayout();
		if(null!=IntelliInvest.userDetailData && null!=IntelliInvest.userDetailData.getUserId()){
			
			IntelliInvestServiceFactory.utilService.getPaymentData(new AsyncCallback<Map<String,PaymentData>>() {
				@Override
				public void onSuccess(Map<String, PaymentData> paymentDetails) {
					paymentDetailsMap = paymentDetails;
					initialize();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					SC.say("Error retrieving Risk Return data. Please contact admin.");
				}
			});
		}else{
			initialize();
		}
	}
	
	@Override
	public boolean isVisible() {
		return paymentPanelLayout.isVisible();
	}
	
	public Layout getPaymentPanelLayout() {
		return paymentPanelLayout;
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
		
		paymentGatewayLayout.setWidth(IntelliInvest.WIDTH - 240 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		paymentGatewayLayout.setHeight((IntelliInvest.HEIGHT-180) /2);
		
		directPaymentDetailsLayout.setWidth(IntelliInvest.WIDTH - 240 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		directPaymentDetailsLayout.setHeight((IntelliInvest.HEIGHT-180) /2);
		
		paymentPanelLayout.setWidth(IntelliInvest.WIDTH - 225 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		paymentPanelLayout.setHeight(IntelliInvest.HEIGHT-165);
		
	}
	
	public void initialize(){
		paymentPanelLayout.setWidth(IntelliInvest.WIDTH-225-(IntelliInvest.LEFT_PANEL.isVisible()?200:0)); 
		paymentPanelLayout.setHeight(IntelliInvest.HEIGHT-165);    
		paymentGatewayLayout = getPaymentOptions();
//		directPaymentDetailsLayout = getDirectPaymentDetails();
		VStack vStack = new VStack();
		vStack.addMember(paymentGatewayLayout);
//		vStack.addMember(directPaymentDetailsLayout);
		paymentPanelLayout.addMember(vStack);
	}
	
	private Layout getPaymentOptions(){
		VLayout paymentGatewayLayout = new VLayout();
		paymentGatewayLayout.setMembersMargin(10);
		paymentGatewayLayout.setWidth(IntelliInvest.WIDTH - 240 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
		paymentGatewayLayout.setHeight((IntelliInvest.HEIGHT-180));
		
		paymentGatewayLayout.setPadding(20);
		ListGrid planGrid = new ListGrid();
		planGrid.setAutoFitData(Autofit.BOTH);
		
		planGrid.setEmptyMessage("Please login to see plan details." );
		
		ListGridRecord[] listGridRecords = new ListGridRecord[paymentDetailsMap.keySet().size()];
		int i=0;
		for(String key : paymentDetailsMap.keySet()){
			PaymentData paymentData = paymentDetailsMap.get(key);
			listGridRecords[i] = new PlanRecord(key, "Plan_" + key.replace(":", "_"), paymentData.getNoOfStocks().toString(),
					paymentData.getNoOfMonths().toString(), paymentData.getAmount().toString(), new Double(paymentData.getAmount()/2).toString());
			i++;
		}
		
		ListGridField planNameField = new ListGridField("planName", "Plan Name");
		planNameField.setAlign(Alignment.CENTER);
		ListGridField noOfStocksField = new ListGridField("noOfStocks", "No of stocks"); 
		noOfStocksField.setAlign(Alignment.CENTER);
		ListGridField noOfMonthsField = new ListGridField("noOfMonths", "No of months");  
		noOfMonthsField.setAlign(Alignment.CENTER);
		ListGridField amountField = new ListGridField("amount", "Amount  (Rs.)");
		amountField.setAlign(Alignment.CENTER);
		ListGridField discountedAmountField = new ListGridField("discountedAmount", "Discounted Amount (Rs.)");
		discountedAmountField.setAlign(Alignment.CENTER);
		
		planGrid.setFields(planNameField, noOfStocksField, noOfMonthsField, amountField, discountedAmountField);
		planGrid.setCanEdit(false);
		planGrid.setData(listGridRecords);
		
		paymentGatewayLayout.addMember(planGrid);
		
		final IButton payButton = new IButton("Go to Payment Gateway");
		payButton.setWidth(175);
		payButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SC.confirm("You will be be redirected to external site for payment."
						+ "\nOnce payment is confirmed your plan will be activated within 24 to 48 hours."
						+ "\nDo you wish to continue to payment gateway?", 
						new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if(null==value){
									SC.say("If you have any problem continuing with payment, please drop us a mail to intellii@intelliinvest.com");
								}else if(value){
									com.google.gwt.user.client.Window.open("https://www.payumoney.com/webfronts/#/index/intelliinvest","_blank",null);
								}
							}
						});
			}
		});
		
		paymentGatewayLayout.addMember(payButton);
		return paymentGatewayLayout;
	}
	
	
//	private Layout getDirectPaymentDetails(){
//		VLayout directPaymentDetailsLayout = new VLayout();
//		directPaymentDetailsLayout.setPadding(20);
//		directPaymentDetailsLayout.setWidth(IntelliInvest.WIDTH - 240 - (IntelliInvest.LEFT_PANEL.isVisible()?200:0));
//		directPaymentDetailsLayout.setHeight((IntelliInvest.HEIGHT-180) /2);
//		Label htmlPane = new Label();
//		htmlPane.setContents("<br/><b style=\"font-size:13px;text-align:center;\"> Direct Payment Details<b><br/><br/>"
//				+ "<table style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\">"
//					+ "<tr>"
//						+ "<td style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\"><b>Name<b></td>"
//						+ "<td style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\">Malvika Ahuja</td>"
//					+ "</tr>"
//					+ "<tr>"
//						+ "<td style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\"><b>Account Number<b></td>"
//						+ "<td style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\">112501500603</td>"
//					+ "</tr>" 
//					+ "<tr>"
//						+ "<td style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\"><b>IFSC Code<b></td>"
//						+ "<td style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\">ICIC0001125</td>"
//					+ "</tr>"
//					+ "<tr>"
//						+ "<td style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\"><b>Bank<b></td>"
//						+ "<td style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\">ICICI Bank</td>"
//					+ "</tr>"
//					+ "<tr>"
//						+ "<td style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\"><b>Branch<b></td>"
//						+ "<td style=\"font-size:11px;border:1px solid black;border-collapse:collapse;text-align:center;\">New Delhi</td>"
//					+ "</tr>"
//				+ "</table>");
//		directPaymentDetailsLayout.addMember(htmlPane);
//		return directPaymentDetailsLayout;
//	}
	

}

 class PlanRecord extends ListGridRecord {  
  
    public PlanRecord() {  
    }  
  
    public PlanRecord(String planId, String planName, String noOfStocks, String noOfMonths, String amount, String discountedAmount) {  
        setPlanId(planId);
        setPlanName(planName);
        setNoOfStocks(noOfStocks);
        setNoOfMonths(noOfMonths);
        setAmount(amount);
        setDiscountedAmount(discountedAmount);
    }  
  
    
    public String getPlanId() {  
        return getAttribute("planId");  
    }  
  
    public void setPlanId(String planId) {  
        setAttribute("planId", planId);  
    }  
  
    public String getPlanName() {  
        return getAttribute("planName");  
    }  
  
    public void setPlanName(String planName) {  
        setAttribute("planName", "<b style=\"font-size:12px;text-align:center;\">" + planName + "</b>");  
    }  
    
    public String getNoOfStocks() {  
        return getAttribute("noOfStocks");  
    }  
  
    public void setNoOfStocks(String noOfStocks) {  
        setAttribute("noOfStocks", "<b style=\"font-size:12px;text-align:center;\">" + noOfStocks + "</b>");  
    }  
    
    public String getNoOfMonths() {  
        return getAttribute("noOfMonths");  
    }  
  
    public void setNoOfMonths(String noOfMonths) {  
        setAttribute("noOfMonths", "<b style=\"font-size:12px;text-align:center;\">" + noOfMonths + "</b>");  
    }  
    public String getAmount() {  
        return getAttribute("amount");  
    }  
  
    public void setAmount(String amount) {  
        setAttribute("amount", "<b style=\"font-size:12px;text-align:center;\">" + amount + "</b>");  
    }  
    
    public String getDiscountedAmount() {  
        return getAttribute("discountedAmount");  
    }  
  
    public void setDiscountedAmount(String discountedAmount) {  
        setAttribute("discountedAmount", "<b style=\"font-size:12px;text-align:center;\">" + discountedAmount + "</b>");  
    }  
}  
