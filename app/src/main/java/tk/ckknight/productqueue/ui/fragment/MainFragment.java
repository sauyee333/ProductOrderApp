package tk.ckknight.productqueue.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tk.ckknight.productqueue.R;
import tk.ckknight.productqueue.network.model.AddUserInput;
import tk.ckknight.productqueue.network.model.CkOnSaleProductInfo;
import tk.ckknight.productqueue.network.model.StockProductInfo;
import tk.ckknight.productqueue.network.model.UserData;
import tk.ckknight.productqueue.network.model.UserLoginInput;
import tk.ckknight.productqueue.network.model.UserLoginResp;
import tk.ckknight.productqueue.network.model.UserLogoutInput;
import tk.ckknight.productqueue.network.model.UserLogoutResp;
import tk.ckknight.productqueue.network.rest.ckKnight.CkRestClient;
import tk.ckknight.productqueue.network.rest.ckKnight.CkRestInterface;
import tk.ckknight.productqueue.network.rest.ckKnight.ProgressSubscriber;
import tk.ckknight.productqueue.network.rest.ckKnight.SubscriberOnNextListener;

/**
 * Created by sauyee on 13/1/18.
 */

public class MainFragment extends Fragment {
    private Context mContext;

    public static MainFragment getInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
//        ButterKnife.bind(this, view);
//        mActivity = getActivity();
//        mRecyclerListView = (RecyclerView) view.findViewById(R.id.list);
//        setupListConfig();
//        showList();

        mContext = getContext();

        //michael, michael123
        //staffC, staffC123
//        postUserLogin("michael", "michael123");

//        postUserLogout("staffA");
//        getGlobalOnSaleProductList("160ac36d16d915ac065ca66d018d3e2f");

        getBranchStockProductList("chinatown", "160ac36d16d915ac065ca66d018d3e2f");
        return view;
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

    private void postAddUserByAdmin(final String userId, final String username,
                                    final String name, final String password,
                                    final String email, final String role,
                                    final String status) {
        final CkRestInterface apiInterface = CkRestClient.Companion.getInstance().getInterface();
        apiInterface.postAddUserByAdmin(new AddUserInput(userId, new UserData(username, name, password,
                email, role, status)))
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

    private void getGlobalProductList(final String userId) {
        final CkRestInterface apiInterface = CkRestClient.Companion.getInstance().getInterface();
        apiInterface.getGlobalOnSaleProductList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(new SubscriberOnNextListener<CkOnSaleProductInfo>() {
                    @Override
                    public void onNext(CkOnSaleProductInfo response) {
                        _Debug("onNext -------" + response.getMessage());
                    }

                    @Override
                    public void onError(String httpErrorCode, String httpErrorMsg, String responseMsg) {
                        _Debug("onError -------");
                    }
                }, mContext));
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
