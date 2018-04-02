package com.hulk.delivery.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hulk.delivery.R;

/**
 * Created by hulk-out on 2017/11/28.
 */

public class AlertDialogUtils {

    private MaterialDialog.Builder builder;
    private MaterialDialog dialog;
    private static final String TAG = "AlertDialogUtils";
    private Toast toast;

    public static AlertDialogUtils getInstance() {
        return new AlertDialogUtils();
    }

    /***
     * 普通无标题提示框
     * @param mContext
     * @param contentRes 内容
     */
    public void showBasicDialogNoTitle(Context mContext, @StringRes int contentRes) {
        builder = new MaterialDialog.Builder(mContext)
                .content(contentRes)
                .positiveText(R.string.agree);

        dialog = builder.build();
        dialog.show();
    }

    /***
     * 普通无标题提示框
     * @param mContext
     * @param content 内容
     */
    public void showBasicDialogNoTitle(Context mContext, String content) {
        builder = new MaterialDialog.Builder(mContext)
                .content(content)
                .positiveText(R.string.agree);

        dialog = builder.build();
        dialog.show();
    }

    /***
     * 不确定进度的进程提示框
     * @param mContext
     * @param progressDialogTitle
     * @param progressDialogContent
     */
    public void showIndeterminateProgressDialog(Context mContext, @StringRes int progressDialogTitle, @StringRes int progressDialogContent) {
        builder = new MaterialDialog.Builder(mContext)
                .title(progressDialogTitle)
                .content(progressDialogContent)
                .canceledOnTouchOutside(false)
                .progress(true, 0);

        dialog = builder.build();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    private void showToast(Context mContext, String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
