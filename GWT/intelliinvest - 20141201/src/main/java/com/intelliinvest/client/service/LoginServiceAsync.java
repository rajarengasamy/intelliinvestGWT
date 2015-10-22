package com.intelliinvest.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intelliinvest.client.data.UserDetailData;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface LoginServiceAsync {
  void login(String mail, String password, AsyncCallback<UserDetailData> callback);
  void register(String userName, String mail, String phone, String password, Boolean sendNotification, AsyncCallback<UserDetailData> callback);
  void save(String userName, String mail, String phone, String oldPassword, String newPassword, Boolean sendNotification, AsyncCallback<UserDetailData> callback);
  void forgotPassword(String mail, AsyncCallback<String> callback);
  void loginExists(String mailId, AsyncCallback<Boolean> callback);
}
