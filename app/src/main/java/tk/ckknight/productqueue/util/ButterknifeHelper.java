package tk.ckknight.productqueue.util;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by sauyee on 20/1/18.
 */

public class ButterknifeHelper {
    public static final ButterKnife.Action<View> SELECT_NAV_BUTTONS = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setSelected(true);
        }
    };

    public static final ButterKnife.Action<View> UNSELECT_NAV_BUTTONS = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setSelected(false);
        }
    };
}
