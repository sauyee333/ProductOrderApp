package tk.ckknight.productqueue.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tk.ckknight.productqueue.R;
import tk.ckknight.productqueue.network.model.UserLoginInput;
import tk.ckknight.productqueue.network.model.UserLoginResp;
import tk.ckknight.productqueue.network.model.UserLogoutInput;
import tk.ckknight.productqueue.network.model.UserLogoutResp;
import tk.ckknight.productqueue.network.rest.ckKnight.CkRestClient;
import tk.ckknight.productqueue.network.rest.ckKnight.CkRestInterface;
import tk.ckknight.productqueue.network.rest.ckKnight.ProgressSubscriber;
import tk.ckknight.productqueue.network.rest.ckKnight.SubscriberOnNextListener;
import tk.ckknight.productqueue.util.ButterknifeHelper;
import tk.ckknight.productqueue.util.SharedPrefHelper;
import tk.ckknight.productqueue.util.SysUtil;

/**
 * Created by sauyee on 19/1/18.
 */

public class UserAccountFragment extends Fragment {

    //logout
    @BindView(R.id.logoutLayout)
    LinearLayout logoutLayout;
    @BindView(R.id.username)
    TextView username;

    //signup
    @BindView(R.id.userSignupLayout)
    LinearLayout userSignupLayout;
    @BindViews({R.id.signupText, R.id.signupUnderline})
    List<View> navSignup;
    @BindView(R.id.mobileSignup)
    EditText mobileSignup;
    @BindView(R.id.nameSignup)
    EditText nameSignup;
    @BindView(R.id.emailSignup)
    EditText emailSignup;
    @BindView(R.id.birthSignup)
    EditText birthSignup;

    //login
    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;
    @BindViews({R.id.loginText, R.id.loginUnderline})
    List<View> navLogin;
    @BindView(R.id.adminLoginLayout)
    LinearLayout adminLoginLayout;
    @BindView(R.id.userNameInput)
    EditText userNameInput;
    @BindView(R.id.passwordInput)
    EditText passwordInput;
    @BindView(R.id.useAdmin)
    CheckBox useAdmin;
//    @BindView(R.id.userNameLabel)
//    TextView userNameLabel;
//    @BindView(R.id.passwordLabel)
//    TextView passwordLabel;

    private Activity mActivity;
    private Context mContext;
    private String userId, userName;

    public static UserAccountFragment getInstance() {
        UserAccountFragment fragment = new UserAccountFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_account, container, false);
        _Debug("UserAccountFragment onCreateView");
        mActivity = getActivity();
        mContext = getContext();
        ButterKnife.bind(this, view);
        loadLoginPage();

        return view;
    }

    private void highlightLoginButton() {
        ButterKnife.apply(navLogin, ButterknifeHelper.SELECT_NAV_BUTTONS);
        ButterKnife.apply(navSignup, ButterknifeHelper.UNSELECT_NAV_BUTTONS);
    }

    private void highlightSignupButton() {
        ButterKnife.apply(navLogin, ButterknifeHelper.UNSELECT_NAV_BUTTONS);
        ButterKnife.apply(navSignup, ButterknifeHelper.SELECT_NAV_BUTTONS);
    }

    private void showLoginScreen() {
        logoutLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
    }

    private void showLogoutScreen() {
        String usrname = SharedPrefHelper.readStringFromSharedPref(mContext, SharedPrefHelper.PREF_KEY_USERNAME, "");
        if (!TextUtils.isEmpty(usrname)) {
            _Debug("userName: (" + userName + ")");
            username.setText("Login as " + userName);
            username.setVisibility(View.VISIBLE);
        } else {
            _Debug("userName invi");
            username.setVisibility(View.GONE);
        }
        logoutLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnLogin)
    public void loadLoginPage() {
        highlightLoginButton();
        if (!checkSavedLoginInfo()) {
            showLoginScreen();
        } else {
            showLogoutScreen();
        }

        adminLoginLayout.setVisibility(View.VISIBLE);
        userSignupLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnSignup)
    public void loadSignupPage() {
        highlightSignupButton();
        userSignupLayout.setVisibility(View.VISIBLE);
        adminLoginLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.signupDone)
    public void proceedSignUp() {
        String mobile = mobileSignup.getText().toString();
        String name = nameSignup.getText().toString();
        String email = emailSignup.getText().toString();
        String dateOfBirth = birthSignup.getText().toString();

        if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(dateOfBirth)) {

        } else {
            Toast.makeText(mContext, "Incomplete details. Please fill in the form.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @OnClick(R.id.loginDone)
    public void proceedLogin() {
        String usrName = userNameInput.getText().toString();
        String name = passwordInput.getText().toString();
        if (!TextUtils.isEmpty(usrName) && !TextUtils.isEmpty(name)) {
            if (useAdmin.isChecked()) {
                postUserLogin(usrName, name);
            } else {

            }
        } else {
            Toast.makeText(mContext, "Incomplete details. Please fill in the form.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @OnClick(R.id.logoutButton)
    public void logoutUser() {
//        String username = SharedPrefHelper.readStringFromSharedPref(mContext, SharedPrefHelper.PREF_KEY_USERNAME, "");
        if (!TextUtils.isEmpty(userId)) {
            postUserLogout(userId);
        }
        SharedPrefHelper.removeStringFromSharedPref(mContext, SharedPrefHelper.PREF_KEY_USERNAME);
        showLoginScreen();
    }

    private boolean checkSavedLoginInfo() {
        UserLoginResp userLoginResp = SysUtil.getLoginUserId(mContext);
        if (userLoginResp != null) {
            userId = userLoginResp.getUserId();
            userName = userLoginResp.getName();
            if (!TextUtils.isEmpty(userId)) {
                return true;
            }
        }
        return false;
    }

    private void updateAdminMenu() {
        useAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked) {
//                    userNameLabel.setText("Username");
//                    passwordLabel.setText("Password");
//                } else {
//                    userNameLabel.setText("Mobile");
//                    passwordLabel.setText("Name");
//                }
            }
        });
    }

    

    private static void _Debug(String str) {
        Log.d("widget", str);
    }
}
