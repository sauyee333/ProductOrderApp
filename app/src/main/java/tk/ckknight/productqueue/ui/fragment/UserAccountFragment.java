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
import android.widget.RelativeLayout;
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
    @BindView(R.id.LogoutLayout)
    LinearLayout LogoutLayout;
    @BindView(R.id.username)
    TextView username;

    //signup
    @BindView(R.id.signupLayout)
    LinearLayout signupLayout;
    @BindView(R.id.inputLayout)
    RelativeLayout inputLayout;
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
    @BindView(R.id.loginOption)
    LinearLayout loginOption;
    @BindViews({R.id.loginText, R.id.loginUnderline})
    List<View> navLogin;
    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;
    @BindView(R.id.mobileLogin)
    EditText mobileLogin;
    @BindView(R.id.nameLogin)
    EditText nameLogin;
    @BindView(R.id.useAdmin)
    CheckBox useAdmin;
    @BindView(R.id.loginFirstText)
    TextView loginFirstText;
    @BindView(R.id.loginSecondText)
    TextView loginSecondText;

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

        if (!checkSavedLoginInfo()) {
            setupLoginScreen();
        } else {
            _Debug("not showing login page");
            showLogoutScreen();
        }
        return view;
    }

    private void showLogoutScreen() {
        _Debug("2. showLogoutScreen: ");
        if (!TextUtils.isEmpty(userName)) {
            _Debug("userName: (" + userName + ")");
            username.setText("Login as " + userName);
            username.setVisibility(View.VISIBLE);
        } else {
            _Debug("userName invi");
            username.setVisibility(View.GONE);
        }
        _Debug("2a. showLogoutScreen: ");
        LogoutLayout.setVisibility(View.VISIBLE);
        loginOption.setVisibility(View.GONE);
        inputLayout.setVisibility(View.GONE);
    }

    private void showLoginSignupScreen() {
        _Debug("showLoginSignupScreen");
        LogoutLayout.setVisibility(View.GONE);
        loginOption.setVisibility(View.VISIBLE);
        inputLayout.setVisibility(View.GONE);
    }

    private void setupLoginScreen() {
        showLoginSignupScreen();
        loadLoginPage();
        updateAdminMenu();
    }

    private void highlightLoginButton() {
        ButterKnife.apply(navLogin, ButterknifeHelper.SELECT_NAV_BUTTONS);
        ButterKnife.apply(navSignup, ButterknifeHelper.UNSELECT_NAV_BUTTONS);
    }

    private void highlightSignupButton() {
        ButterKnife.apply(navLogin, ButterknifeHelper.UNSELECT_NAV_BUTTONS);
        ButterKnife.apply(navSignup, ButterknifeHelper.SELECT_NAV_BUTTONS);
    }

    @OnClick(R.id.btnLogin)
    public void loadLoginPage() {
        highlightLoginButton();
        loginLayout.setVisibility(View.VISIBLE);
        signupLayout.setVisibility(View.GONE);
        inputLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnSignup)
    public void loadSignupPage() {
        highlightSignupButton();
        signupLayout.setVisibility(View.VISIBLE);
        inputLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.signupDone)
    public void proceedSignUp() {
        _Debug("read input and sign up");
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
        _Debug("read input and login");
        String mobile = mobileLogin.getText().toString();
        String name = nameLogin.getText().toString();
        if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(name)) {
            if (useAdmin.isChecked()) {
                postUserLogin(mobile, name);
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
        setupLoginScreen();
    }

    private boolean checkSavedLoginInfo() {
        UserLoginResp userLoginResp = SysUtil.getLoginUserId(mContext);
        if(userLoginResp!= null) {
            userId = userLoginResp.getUserId();
            userName = userLoginResp.getName();
            if (!TextUtils.isEmpty(userId)) {
                _Debug("userId is: (" + userId + ")");
                _Debug("going to show logout info ------");
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
//                    loginFirstText.setText("Username");
//                    loginSecondText.setText("Password");
//                } else {
//                    loginFirstText.setText("Mobile");
//                    loginSecondText.setText("Name");
//                }
            }
        });
    }

    

    private static void _Debug(String str) {
        Log.d("widget", str);
    }
}
