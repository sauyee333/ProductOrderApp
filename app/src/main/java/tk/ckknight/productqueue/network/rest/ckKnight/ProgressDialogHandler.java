package tk.ckknight.productqueue.network.rest.ckKnight;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import tk.ckknight.productqueue.R;

/**
 * Created by sauyee on 13/1/18.
 */

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private Context mContext;
    private boolean mCancelable;
    private ProgressCancelListener mProgressCancelListener;
    private ProgressDialog mProgressDialog;

    public ProgressDialogHandler(Context context, ProgressCancelListener progressCancelListener, boolean cancelable) {
        super();
        mContext = context;
        mProgressCancelListener = progressCancelListener;
        mCancelable = cancelable;
    }

    private void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage(mContext.getResources().getString(R.string.pleaseWait));
            mProgressDialog.setCancelable(mCancelable);

            if (mCancelable) {
                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            // Dismiss only if the Activity (that the dialog attached to) is not yet destroyed
            if (!(mContext instanceof Activity) ||
                    !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) mContext).isDestroyed())) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}


