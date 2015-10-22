package com.intelliinvest.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.intelliinvest.client.data.UserDetailData;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
  Boolean loginExists(String mailId);
  UserDetailData login(String mail, String password);
  UserDetailData register(String userName, String mail, String phone, String password, Boolean sendNotification);
  String forgotPassword(String mail);
  UserDetailData save(String userName, String mail, String phone, String oldPassword, String newPassword, Boolean sendNotification);
}
