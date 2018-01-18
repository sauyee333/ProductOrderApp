package tk.ckknight.productqueue.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tk.ckknight.productqueue.R;
import tk.ckknight.productqueue.ui.fragment.CartFragment;
import tk.ckknight.productqueue.ui.fragment.InventoryFragment;
import tk.ckknight.productqueue.ui.fragment.ProductListFragment;
import tk.ckknight.productqueue.ui.fragment.QueueStatusFragment;
import tk.ckknight.productqueue.ui.fragment.UserAccountFragment;
import tk.ckknight.productqueue.ui.listener.IFragmentHostListener;

public class MainActivity extends AppCompatActivity implements IFragmentHostListener {

    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.topPanel)
    View topPanel;

    @BindView(R.id.btnInventory)
    RelativeLayout btnInventory;
    @BindView(R.id.btnQueueStatus)
    RelativeLayout btnQueueStatus;
    @BindView(R.id.btnCart)
    RelativeLayout btnCart;
    @BindView(R.id.btnLogin)
    RelativeLayout btnLogin;

    private Context mContext;
    private ProgressDialog mLoadingDialog;
    private Toast mToast;

    @OnClick(R.id.headerLogo)
    public void loadProductListPage() {
        _Debug("loadProductListPage");
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.content,
                ProductListFragment.getInstance()).commit();
    }

    @OnClick(R.id.btnInventory)
    public void loadInventoryPage() {
        _Debug("loadInventoryPage");
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.content,
                InventoryFragment.getInstance()).commit();
    }

    @OnClick(R.id.btnQueueStatus)
    public void loadQueueStatusPage() {
        _Debug("loadQueueStatusPage");
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.content,
                QueueStatusFragment.getInstance()).commit();
    }

    @OnClick(R.id.btnCart)
    public void loadCartPage() {
        _Debug("loadCartPage");
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.content,
                CartFragment.getInstance()).commit();
    }

    @OnClick(R.id.btnLogin)
    public void loadLoginPage() {
        _Debug("loadLoginPage");
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.content,
                UserAccountFragment.getInstance()).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = MainActivity.this;
        _Debug("2. main activity mmmmmmmm");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLoadingDialog = setupLoadingDialog();
        loadProductListPage();
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
