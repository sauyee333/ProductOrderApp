package tk.ckknight.productqueue.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.ckknight.productqueue.R;

/**
 * Created by sauyee on 19/1/18.
 */

public class ProductListFragment extends Fragment {
    private Context mContext;

    public static ProductListFragment getInstance() {
        ProductListFragment fragment = new ProductListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        _Debug("ProductListFragment onCreateView");
        return view;
    }

    private static void _Debug(String str) {
        Log.d("widget", str);
    }
}
