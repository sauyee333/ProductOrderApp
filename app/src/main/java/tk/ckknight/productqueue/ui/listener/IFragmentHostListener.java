package tk.ckknight.productqueue.ui.listener;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by sauyee on 13/1/18.
 */

public interface IFragmentHostListener {
    void onFragmentBackPress();

    void onShowFragment(Fragment fragment, boolean popCurrent);

    void onShowLoadingDialog(boolean show);

    void onShowToast(String displayString);

    void onLoadMainActivity(Bundle bundle);

}

