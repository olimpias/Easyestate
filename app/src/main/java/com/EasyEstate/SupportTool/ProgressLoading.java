package com.EasyEstate.SupportTool;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.EasyEstate.R;

/**
 * Created by canturker on 09/04/15.
 */
public class ProgressLoading extends Dialog {
    private TextView messageTextView;
    public ProgressLoading(Context context,String message) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loadingdialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        messageTextView = (TextView)findViewById(R.id.loadingDialogTextView);
        if(message != null){
            messageTextView.setText(message);
        }
        setCancelable(false);
    }
}
