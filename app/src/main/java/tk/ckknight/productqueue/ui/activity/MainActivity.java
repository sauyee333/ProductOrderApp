package tk.ckknight.productqueue.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import tk.ckknight.productqueue.R;
import tk.ckknight.productqueue.ui.fragment.MainFragment;
import tk.ckknight.productqueue.ui.listener.IFragmentHostListener;

public class MainActivity extends AppCompatActivity implements IFragmentHostListener {

    private Context mContext;
    private ProgressDialog mLoadingDialog;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = MainActivity.this;
        _Debug("main activity mmmmmmmm");
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.content,
                MainFragment.getInstance()).commit();
        mLoadingDialog = setupLoadingDialog();
    }

    private void showFragment(Fragment frag, boolean popCurrent) {
        String fragmentTag = frag.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();

        if (popCurrent) {
            manager.popBackStack();
        }
//        if ((manager.findFragmentByTag(fragmentTag) == null)) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, frag, fragmentTag);
        transaction.addToBackStack(fragmentTag);
        transaction.commit();
//        }
    }

    private ProgressDialog setupLoadingDialog() {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage(getResources().getString(R.string.pleaseWait));
        return dialog;
    }

    private void showLoadingDialog() {
        if (!isFinishing() && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    private void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    private void showToast(String displayString) {
        hideToast();
        if (!isFinishing() && !TextUtils.isEmpty(displayString)) {
            mToast = new Toast(mContext);
            mToast.makeText(mContext, displayString, Toast.LENGTH_LONG).show();
        }
    }

    private void hideToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    @Override
    public void onFragmentBackPress() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onShowToast(String displayString) {
        showToast(displayString);
    }

    @Override
    public void onShowFragment(Fragment fragment, boolean popCurrent) {
        showFragment(fragment, popCurrent);
    }

    @Override
    public void onShowLoadingDialog(boolean show) {
        if (show) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void onLoadMainActivity(Bundle bundle) {

    }


    private static void _Debug(String str) {
        Log.d("widget", str);
    }
}
