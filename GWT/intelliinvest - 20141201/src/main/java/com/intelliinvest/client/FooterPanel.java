package com.intelliinvest.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;

public class FooterPanel implements IntelliInvestPanel{
	final Layout footerPanel;
	Boolean resize = false;
	
	private static int FOOTER_HEIGHT = 30;
	
	public static String aboutUs = ""
			+ "<br>We are an enthusiastic bunch of guys who believe that everything in this world can be represented as an equation (no we are not rocket scientists) We  are professional  who have spent several years in the investment banking industry bridging the gap between IT and Business."
			+ "<br><br>During this time we interacted with different types of traders, be it FX, Equity, Rates or structured products. What you see on this website is driven from what we have learnt by those interactions. On the education side we hold decent enough degrees to boost of MNC jobs."
			+ "<br><br>For the last few years we have tried to use technical trading techniques to make money for ourselves. What we realized is that equity in emerging market should be treated differently and technical trading tools developed so far and available in the market don't suit emerging market equity."
			+ "<br><br>We also realized that these days local market is greatly coupled with foreign market interlinked in such a way that we need to look at broader picture. We have to look at elections happening somewhere in Europe (Greece would be a better example) to understand how market would behave in Singapore. We have to look at a war happening in Afghanistan to understand how London market behaves. Well the conclusion is, spend rest of your life to understand all this, find correlation do regression and finally when you arrive at a equation to explain the relationship (Which we can bet would still be wrong) you are done with everything, or a better way would be to start getting your head into the charts and understand technical trading to generate money as we have done. "
			+ "<br><br>So in order to provide you a solution we have teamed up and present to you a solution which would give you a different angle to look at your investments. We would like you to get a grasp of the market dynamics, understand our signals, find a reason of why the \"signal\" and overtime you would start making money. Of the 10 trading signals even if a couple of trades brings some losses, other would be some jackpots which would more than cover up for your losses. In the end you would be sitting on the right side."
			+ "<br><br>Wish you happy trading with us."
			+ "<br><br><b>Malvika Gulati<br>VP, Intelliinvest.com</b>";
	
	public static String contactUs= ""
									+ "If you have queries related to usage of site or information on how to use this site for trading. <br>"
									+ "Plase contact us either via contact us in Left side of site or you can directly mail to <a href=\"mailto:admin@intelliinvest.com\"><b>admin@intelliinvest.com</b></a>"
									+ "<br><br>"
									+ "If you have queries related to getting plans or issues related to user creation <br>"
									+ "Plase send a mail to <a href=\"mailto:intellii@intelliinvest.com\"><b>intellii@intelliinvest.com</b></a>";
	
	public static String disclaimer = ""
										+ "<br><b>intelliinvest.com</b> has taken due care and caution in compilation of data for its web site. "
										+ "The views and investment tips expressed by investment experts on intelliinvest.com are their own, "
										+ "and not that of the website or its management. "
										+ "<br>intelliinvest.com advises users to check with certified experts before taking any investment decision. "
										+ "However, intelliinvest.com does not guarantee the accuracy, adequacy or completeness of any information "
										+ "and is not responsible for any errors or omissions or for the results obtained from the use of such information. "
										+ "intelliinvest.com especially states that it has no financial liability whatsoever to any user on account of the use of information provided on its website."
										+ "<br>intelliinvest.com is not responsible for any errors, omissions or representations on any of our pages or on any links on any of our pages. "
										+ "<br>intelliinvest.com does not endorse in anyway any advertisers on our web pages. "
										+ "Please verify the veracity of all information on your own before undertaking any alliance. The information on this website is updated from time to time. "
										+ "<br>intelliinvest.com however excludes any warranties (whether expressed or implied), as to the quality, accuracy, efficacy, completeness, performance, "
										+ "fitness or any of the contents of the website, including (but not limited) to any comments, feedback and advertisements contained within the Site. "
										+ "<br>This website contains material in the form of inputs submitted by users and "
										+ "intelliinvest.com accepts no responsibility for the content or "
										+ "accuracy of such content nor does intelliinvest.com make any representations by virtue of the contents of this website in respect of the existence or "
										+ "availability of any goods and services advertised in the contributory sections. "
										+ "<br>intelliinvest.com makes no warranty that the contents of the website are free from infection by viruses or anything else which has contaminating or destructive properties "
										+ "and shall have no liability in respect thereof. "
										+ "<br>Stock trading is inherently risky and you agree to assume complete and full responsibility for the outcomes of all trading decisions that you make, "
										+ "including but not limited to loss of capital. "
										+ "<br>None of the stock trading calls made by us should be construed as an offer to buy or sell securities, nor advice to do so."
										+ " All comments and posts made by mcd, group companies associated with it and employees/owners are for information purposes only "
										+ "and under no circumstances should be used for actual trading. "
										+ "<br>Under no circumstances should any person at this site make trading decisions based solely on the information discussed herein. "
										+ "We are not a qualified financial advisor and you should not construe any information discussed herein to constitute investment advice. "
										+ "<br>It is informational in nature. You should consult a qualified broker or other financial advisor prior to making any actual investment or trading decisions. "
										+ "<br>You agree to not make actual stock trades based on comments on the site, nor on any techniques presented nor discussed in this site or "
										+ "any other form of information presentation. "
										+ "<br>All information is for educational and informational use only. "
										+ "You agree to consult with a registered investment advisor, which we are not, prior to making any trading decision of any kind. "
										+ "Hypothetical or simulated performance results have certain inherent limitations. "
										+ "<br>Unlike an actual performance record, simulated results do not represent actual trading. "
										+ "No representation is being made that any account will or is likely to achieve profits or losses similar to those shown."
										+ "<br>We encourage all investors to use the information on the site as a resource only to further their own research on all featured companies, "
										+ "stocks, mutual funds, sectors, markets and information presented on the site.";
									
	public FooterPanel() {

		this.footerPanel = new HStack();
		initialize();
	}
	
	public Layout getFooterLayout() {
		return footerPanel;
	}
	
	@Override
	public boolean isVisible() {
		return footerPanel.isVisible();
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
		footerPanel.setWidth(IntelliInvest.WIDTH - 5);
	}
	
	public void initialize(){
		footerPanel.setShowEdges(false);   
        footerPanel.setWidth(IntelliInvest.WIDTH -5);      
        footerPanel.setHeight(FOOTER_HEIGHT);   
        footerPanel.setMembersMargin(100);   
        footerPanel.setLayoutTopMargin(5);
        footerPanel.setPadding(0);
        footerPanel.setStyleName("intelliinvest");
        footerPanel.setAlign(Alignment.CENTER);
      
        IButton aboutUs = new IButton("About us", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				StaticWindow.show(600, 480, "About Us", FooterPanel.aboutUs);
			}
		});
        
        IButton contactUs = new IButton("Contact us", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				StaticWindow.show(500, 170, "Contact Us", FooterPanel.contactUs);
			}
		});
        
        IButton disclaimer = new IButton("Disclaimer", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				StaticWindow.show(600, 530, "Disclaimer", FooterPanel.disclaimer);
			}
		});
        
//        IButton termsAndCondition = new IButton("Terms & Condition", new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				
//			}
//		});
        
        footerPanel.addMember(aboutUs);
        footerPanel.addMember(contactUs);
        footerPanel.addMember(disclaimer);
//        footerPanel.addMember(termsAndCondition);
        
//        String contents = "Disclaimer - This site does not hold any responsibilty for any loss occurring due to above provided information."
//        		+ " Details provided are based on Special algorithm designed and for information purpose only."; 
	}
}
