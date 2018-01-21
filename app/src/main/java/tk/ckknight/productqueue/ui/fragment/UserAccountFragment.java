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

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tk.ckknight.productqueue.R;
import tk.ckknight.productqueue.util.ButterknifeHelper;

/**
 * Created by sauyee on 19/1/18.
 */

public class UserAccountFragment extends Fragment {

    //signup
    @BindViews({R.id.signupText, R.id.signupUnderline})
    List<View> navSignup;
    @BindView(R.id.signupLayout)
    LinearLayout signupLayout;
    @BindView(R.id.mobileSignup)
    EditText mobileSignup;
    @BindView(R.id.nameSignup)
    EditText nameSignup;
    @BindView(R.id.emailSignup)
    EditText emailSignup;
    @BindView(R.id.birthSignup)
    EditText birthSignup;

    //login
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
        updateAdminMenu();
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

    @OnClick(R.id.btnLogin)
    public void loadLoginPage() {
        highlightLoginButton();
        loginLayout.setVisibility(View.VISIBLE);
        signupLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnSignup)
    public void loadSignupPage() {
        highlightSignupButton();
        signupLayout.setVisibility(View.VISIBLE);
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
        } else {
            Toast.makeText(mContext, "Incomplete details. Please fill in the form.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void updateAdminMenu() {
        useAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    loginFirstText.setText("Username");
                    loginSecondText.setText("Password");
                } else {
                    loginFirstText.setText("Mobile");
                    loginSecondText.setText("Name");
                }
            }
        });
    }

    private static void _Debug(String str) {
        Log.d("widget", str);
    }
}
