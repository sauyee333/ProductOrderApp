package tk.ckknight.productqueue.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import tk.ckknight.productqueue.network.model.CustomerData;
import tk.ckknight.productqueue.network.model.UserLoginResp;

/**
 * Created by sauyee on 24/1/18.
 */

public class SysUtil {

    public static UserLoginResp getLoginUserId(Context context) {
        String userId = "";
        Gson gson = new Gson();
        String json = SharedPrefHelper.readStringFromSharedPref(context, SharedPrefHelper.PREF_KEY_LOGIN_INFO, "");
        UserLoginResp loginResp = gson.fromJson(json, UserLoginResp.class);
        if (loginResp != null) {
            String status = loginResp.getStatus();
            if (!TextUtils.isEmpty(status) && status.equals("OK")) {
                userId = loginResp.getUserId();
                if (!TextUtils.isEmpty(userId)) {
                    _Debug("userId is: (" + userId + ")");
                }
            }
        }
        return loginResp;
    }

    public static CustomerData getCustomerLoginInfo(Context context) {
        Gson gson = new Gson();
        String json = SharedPrefHelper.readStringFromSharedPref(context, SharedPrefHelper.PREF_KEY_CUSTOMER_LOGIN_INFO, "");
        CustomerData loginResp = gson.fromJson(json, CustomerData.class);
        return loginResp;
    }

    public static void clearClientInfo(Context context) {
        SharedPrefHelper.writeStringToSharePref(context, SharedPrefHelper.PREF_KEY_CUSTOMER_LOGIN_INFO, "");
    }

    private static void _Debug(String str) {
        Log.d("widget", str);
    }
}
