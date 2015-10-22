package com.intelliinvest.client;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.FocusEvent;
import com.smartgwt.client.widgets.form.fields.events.FocusHandler;

public class Util {
	static final byte[] SECRET_KEY = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
			13, 14, 15, 16 };

	public static native int getScreenWidth() /*-{
		return $wnd.screen.width;
	}-*/;

	public static native int getScreenHeight() /*-{
		return $wnd.screen.height;
	}-*/;

	public static FocusHandler getEmptyTextHandler(final String defaultValue) {
		return new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if (((FormItem) event.getSource()).getValue().equals(
						defaultValue))
					((FormItem) event.getSource()).setValue("");
			}
		};
	}

	public static BlurHandler getDefaultTextHandler(final String defaultValue) {
		return new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (((FormItem) event.getSource()).getValue().equals(""))
					((FormItem) event.getSource()).setValue(defaultValue);

			}
		};
	}
}