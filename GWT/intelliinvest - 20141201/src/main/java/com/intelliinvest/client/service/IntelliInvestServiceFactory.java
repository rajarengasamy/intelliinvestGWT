package com.intelliinvest.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class IntelliInvestServiceFactory {
	public static IntelliInvestServiceAsync stockDetailsService = GWT.create (IntelliInvestService.class);
	public static IntelliInvestServiceAsync comboBoxService = GWT.create (IntelliInvestService.class);
	public static IntelliInvestServiceAsync intelliInvestDSService = GWT.create (IntelliInvestService.class);
	public static final LoginServiceAsync loginService = GWT.create(LoginService.class);
	public static final UtilServiceAsync utilService = GWT.create(UtilService.class);
	public static final BlogsServiceAsync blogsService = GWT.create(BlogsService.class);
	
	public static void initialize(){
		((ServiceDefTarget) stockDetailsService).setServiceEntryPoint( GWT.getModuleBaseURL() + "StockDetails");
		((ServiceDefTarget) comboBoxService).setServiceEntryPoint( GWT.getModuleBaseURL() + "ComboBox");
		((ServiceDefTarget) intelliInvestDSService).setServiceEntryPoint( GWT.getModuleBaseURL() + "IntelliInvestDS");
	}
}
