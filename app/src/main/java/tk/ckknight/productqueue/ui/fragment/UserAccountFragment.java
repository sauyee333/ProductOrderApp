package tk.ckknight.productqueue.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tk.ckknight.productqueue.R;
import tk.ckknight.productqueue.network.model.CustomerData;
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

    //admin logout
    @BindView(R.id.logoutLayout)
    LinearLayout logoutLayout;
    @BindView(R.id.username)
    TextView username;

    //client signup
    @BindView(R.id.userSignupLayout)
    LinearLayout userSignupLayout;
    @BindView(R.id.clientInputLayout)
    LinearLayout clientInputLayout;
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

    //client logout
    @BindView(R.id.clientLogoutLayout)
    LinearLayout clientLogoutLayout;
    @BindView(R.id.clientMobile)
    TextView clientMobile;
    @BindView(R.id.clientName)
    TextView clientName;
    @BindView(R.id.clientEmail)
    TextView clientEmail;
    @BindView(R.id.clientBirthDate)
    TextView clientBirthDate;

    //admin login
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
    private String customerMobile, customerName, customerEmail, customerBirthDate;

    public static UserAccountFragment getInstance() {
        UserAccountFragment fragment = new UserAccountFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_account, container, false);
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

    private void showCustomerLoginScreen() {
        clientLogoutLayout.setVisibility(View.GONE);
        clientInputLayout.setVisibility(View.VISIBLE);
    }

    private void showCustomerLogoutScreen() {
        _Debug("------ showCustomerLogoutScreen: " + customerMobile);
        clientMobile.setText("Mobile: " + customerMobile);
        clientName.setText("Name: " + customerName);
        clientEmail.setText("Email: " + customerEmail);
        clientBirthDate.setText("DOB: " + customerBirthDate);
        clientLogoutLayout.setVisibility(View.VISIBLE);
        clientInputLayout.setVisibility(View.GONE);
    }

    private void showAdminLoginScreen() {
        logoutLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
    }

    private void showAdminLogoutScreen() {
        String usrname = SharedPrefHelper.readStringFromSharedPref(mContext, SharedPrefHelper.PREF_KEY_USERNAME, "");
        if (!TextUtils.isEmpty(usrname)) {
            username.setText("Login as " + usrname);
            username.setVisibility(View.VISIBLE);
        } else {
            username.setVisibility(View.GONE);
        }
        logoutLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @OnClick(R.id.btnLogin)
    public void loadLoginPage() {
        highlightLoginButton();
        if (!checkSavedAdminLoginInfo()) {
            showAdminLoginScreen();
        } else {
            showAdminLogoutScreen();
        }

        adminLoginLayout.setVisibility(View.VISIBLE);
        userSignupLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnSignup)
    public void loadSignupPage() {
        highlightSignupButton();
        if (!checkSavedCustomerLoginInfo()) {
            showCustomerLoginScreen();
        } else {
            showCustomerLogoutScreen();
        }
        userSignupLayout.setVisibility(View.VISIBLE);
        adminLoginLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.birthSignup)
    public void setBirthDate() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year);
                birthSignup.setText(date);
            }
        }, yy, mm, dd);
        datePicker.show();
    }

    @OnClick(R.id.signupDone)
    public void proceedSignUp() {
        String mobile = mobileSignup.getText().toString();
        String name = nameSignup.getText().toString();
        String email = emailSignup.getText().toString();

        String dateOfBirth = birthSignup.getText().toString();

        if (isEmailValid(email)) {
            _Debug("valid email");
            if (!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(dateOfBirth)) {
                _Debug("save client info");
                CustomerData customerData = new CustomerData(mobile, name, email, dateOfBirth, "");
                Gson gson = new Gson();
                String json = gson.toJson(customerData);
                SharedPrefHelper.writeStringToSharePref(mContext, SharedPrefHelper.PREF_KEY_CUSTOMER_LOGIN_INFO, json);
                clientInputLayout.setVisibility(View.GONE);
                clientLogoutLayout.setVisibility(View.VISIBLE);
            } else {
                _Debug("NOT save client info");
                SysUtil.clearClientInfo(mContext);
                Toast.makeText(mContext, "Incomplete details. Please fill in the form.", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            _Debug("invalid email");
            Toast.makeText(mContext, "Invalid email.", Toast.LENGTH_LONG)
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
        showAdminLoginScreen();
    }

    @OnClick(R.id.clientLogoutButton)
    public void logoutCustomer() {
        SharedPrefHelper.removeStringFromSharedPref(mContext, SharedPrefHelper.PREF_KEY_CUSTOMER_LOGIN_INFO);
        showCustomerLoginScreen();
    }


    private boolean checkSavedAdminLoginInfo() {
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

    private boolean checkSavedCustomerLoginInfo() {
        CustomerData customerData = SysUtil.getCustomerLoginInfo(mContext);
        if (customerData != null) {
            customerMobile = customerData.getMobile();
            customerName = customerData.getName();
            customerEmail = customerData.getEmail();
            customerBirthDate = customerData.getDob();

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

    private void postUserLogin(final String username, final String password) {
        final CkRestInterface apiInterface = CkRestClient.Companion.getInstance().getInterface();
        apiInterface.postUserLogin(new UserLoginInput(username, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(new SubscriberOnNextListener<UserLoginResp>() {
                    @Override
                    public void onNext(UserLoginResp response) {
                        _Debug("onNext -------" + response.getStatus());
                        if (response.getStatus().equals("OK")) {
                            userId = response.getUserId();
                            Gson gson = new Gson();
                            String json = gson.toJson(response);
                            SharedPrefHelper.writeStringToSharePref(mContext, SharedPrefHelper.PREF_KEY_LOGIN_INFO, json);
                            SharedPrefHelper.writeStringToSharePref(mContext, SharedPrefHelper.PREF_KEY_USERNAME, username);
                            showAdminLogoutScreen();
                        } else {
                            Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(String httpErrorCode, String httpErrorMsg, String responseMsg) {
                        _Debug("onError -------");
                    }
                }, mContext));
    }

    private void postUserLogout(final String username) {
        final CkRestInterface apiInterface = CkRestClient.Companion.getInstance().getInterface();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = timeFormat.format(new Date());

        SharedPrefHelper.writeStringToSharePref(mContext, SharedPrefHelper.PREF_KEY_LOGIN_INFO, "");
        SharedPrefHelper.writeStringToSharePref(mContext, SharedPrefHelper.PREF_KEY_USERNAME, "");
        _Debug("timeStamp: (" + timeStamp + ")");
        apiInterface.postUserLogout(new UserLogoutInput(username, timeStamp))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(new SubscriberOnNextListener<UserLogoutResp>() {
                    @Override
                    public void onNext(UserLogoutResp response) {
                        _Debug("onNext -------" + response.getStatus());
                    }

                    @Override
                    public void onError(String httpErrorCode, String httpErrorMsg, String responseMsg) {
                        _Debug("onError -------");
                    }
                }, mContext));
    }

    private static void _Debug(String str) {
        Log.d("widget", str);
    }
}
