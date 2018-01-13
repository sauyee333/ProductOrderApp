package tk.ckknight.productqueue.network.rest.ckKnight;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import tk.ckknight.productqueue.R;

/**
 * Created by sauyee on 13/1/18.
 */

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context mContext;

    public ProgressSubscriber(SubscriberOnNextListener subscriberOnNextListener, Context context, boolean showDialog) {
        mSubscriberOnNextListener = subscriberOnNextListener;
        mContext = context;
        if (showDialog) {
            mProgressDialogHandler = new ProgressDialogHandler(mContext, this, true);
        }
    }

    public ProgressSubscriber(SubscriberOnNextListener subscriberOnNextListener, Context context) {
        mSubscriberOnNextListener = subscriberOnNextListener;
        mContext = context;
        mProgressDialogHandler = new ProgressDialogHandler(mContext, this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();

        String httpErrorCode = "";
        String httpErrorMsg = "";
        String responseMsg = "";

        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            ResponseBody body = ((HttpException) throwable).response().errorBody();
            httpErrorCode = httpException.code() + "";
            httpErrorMsg = httpException.message();

            String bodyData = "";
            try {
                bodyData = body.string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(bodyData)) {
                try {
                } catch (JsonParseException e) {
//                    responseMsg = httpErrorCode + " " + httpErrorMsg;
                    responseMsg = mContext.getString(R.string.problemConnectRetry);
                }
            }
        } else if (throwable instanceof UnknownHostException) {
            if (mContext != null) {
                responseMsg = mContext.getString(R.string.problemConnectRetry);
            }
        } else if (throwable instanceof SocketTimeoutException) {
            if (mContext != null) {
                responseMsg = mContext.getString(R.string.problemNetwork);
            }
        }

        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onError(httpErrorCode, httpErrorMsg, responseMsg);
        }

        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
