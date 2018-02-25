package tk.ckknight.productqueue.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tk.ckknight.productqueue.R;
import tk.ckknight.productqueue.network.model.StockProductData;
import tk.ckknight.productqueue.network.model.StockProductInfo;
import tk.ckknight.productqueue.network.model.UserLoginResp;
import tk.ckknight.productqueue.network.rest.ckKnight.CkRestClient;
import tk.ckknight.productqueue.network.rest.ckKnight.CkRestInterface;
import tk.ckknight.productqueue.network.rest.ckKnight.ProgressSubscriber;
import tk.ckknight.productqueue.network.rest.ckKnight.SubscriberOnNextListener;
import tk.ckknight.productqueue.ui.adapter.ProductAdapter;
import tk.ckknight.productqueue.util.SharedPrefHelper;
import tk.ckknight.productqueue.util.SysUtil;

/**
 * Created by sauyee on 19/1/18.
 */

public class ProductListFragment extends Fragment {
    @BindView(R.id.product_list)
    RecyclerView mRecyclerListView;

    private Context mContext;
    private Activity mActivity;
    private String userId, userName;
    private ProductAdapter mListAdapter;
    private ArrayList<StockProductData> mDataList;

    public static ProductListFragment getInstance() {
        ProductListFragment fragment = new ProductListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        mActivity = getActivity();

        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        ButterKnife.bind(this, view);
        setupListConfig();

        UserLoginResp loginResp = SysUtil.getLoginUserId(mContext);
        if(loginResp != null) {
            userId = loginResp.getUserId();
            userName = loginResp.getName();
            if (!TextUtils.isEmpty(userId)) {
                getBranchStockProductList("chinatown", userId);
            }
        }
        return view;
    }

    private void setupListConfig() {
        mRecyclerListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerListView.setLayoutManager(layoutManager);
    }

    private void getBranchStockProductList(final String branch, final String userid) {
        final CkRestInterface apiInterface = CkRestClient.Companion.getInstance().getInterface();
        apiInterface.getBranchStockProductList(branch, userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(new SubscriberOnNextListener<StockProductInfo>() {
                    @Override
                    public void onNext(StockProductInfo response) {
                        _Debug("onNext -------" + response.getMessage());
                        if (response.getStatus().equals("NOK")) {
                            String msg = response.getMessage();
                            if(!TextUtils.isEmpty(msg)) {
                                String displayMsg = msg;
                                switch (displayMsg){
                                    case "Please login":
                                        SharedPrefHelper.writeStringToSharePref(mContext, SharedPrefHelper.PREF_KEY_LOGIN_INFO, "");
                                        SharedPrefHelper.writeStringToSharePref(mContext, SharedPrefHelper.PREF_KEY_USERNAME, "");
                                        displayMsg = "Invalid token. Please re-login.";
                                        break;
                                }
                                Toast.makeText(mContext, displayMsg, Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else if (response.getStatus().equals("OK")) {
                            mDataList = new ArrayList<>();
                            mDataList.addAll(response.getProduct());
                            mListAdapter = new ProductAdapter(mContext, mActivity, mDataList, null);
                            mRecyclerListView.setAdapter(mListAdapter);
                        }
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
