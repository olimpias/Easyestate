package com.EasyEstate.SupportTool;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.EasyEstate.R;

/**
 * Created by canturker on 09/04/15.
 */
public class ProgressLoading extends Dialog {
    private TextView messageTextView;
    public ProgressLoading(Context context,String message) {
        super(context);
        setContentView(R.layout.loadingdialog);
        messageTextView = (TextView)findViewById(R.id.loadingDialogTextView);
        if(message != null){
            messageTextView.setText(message);
        }
        setCancelable(false);
    }
}
