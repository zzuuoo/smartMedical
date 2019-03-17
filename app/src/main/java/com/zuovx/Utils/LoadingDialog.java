package com.zuovx.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zuovx.R;
import com.zuovx.View.LVCircularRing;

public class LoadingDialog {
    LVCircularRing mLoadingView;
    Dialog mLoadingDialog;

    public LoadingDialog(Context context, String msg) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.loading_dialog_view, null);

        LinearLayout layout = view.findViewById(R.id.dialog_view);

        mLoadingView = view.findViewById(R.id.lv_circularring);

        TextView loadingText = view.findViewById(R.id.loading_text);

        loadingText.setText(msg);

        mLoadingDialog = new Dialog(context, R.style.loading_dialog);

        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void show(){
        mLoadingDialog.show();
        mLoadingView.startAnim();
    }

    public void close(){
        if (mLoadingDialog!=null) {
            mLoadingView.stopAnim();
            mLoadingDialog.dismiss();
            mLoadingDialog=null;
        }
    }
}