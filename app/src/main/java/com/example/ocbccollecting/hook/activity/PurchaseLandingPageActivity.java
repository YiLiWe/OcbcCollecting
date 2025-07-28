package com.example.ocbccollecting.hook.activity;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.utils.ViewUtil;

import java.util.List;

//钱包转账输入卡号，和编码
public class PurchaseLandingPageActivity extends BaseActivity {

    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.purchase.ui.PurchaseLandingPageActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        //判断两个代付信息
        if (getTakeLatestOrderBean() == null && getOcbcImputationBean() == null) {
            activity.finish();
            return;
        }
        positioningView("field_category");
    }

    @Override
    public void onResumed(Activity activity) {
        if (getOcbcImputationBean() == null) {
            activity.finish();
        }
    }

    @Override
    public void positioningView(View view) {
        switch (ViewUtil.getResourceEntryName(view)) {
            case "field_category":
                selectBank(view);
                break;
            case "field_rev_no":
                InputAmount(view);
                break;
            case "btn_continue":
                ViewUtil.performClick(view);
                positioningView("keyboard_view");
                break;
            case "keyboard_view":
                view.setVisibility(View.GONE);
                positioningView("amount_input_edit_text");
                break;
            case "amount_input_edit_text":
                amount_input_edit_text(view);
                break;
            case "payment_select_amount":
                View btn_continue = ViewUtil.findViewById(view, "btn_continue");
                if (!ViewUtil.performClick(btn_continue)) {

                }
                break;
        }
    }

    //输入金额
    private void amount_input_edit_text(View view) {
        if (view instanceof EditText editText) {
            editText.post(() -> {
                if (getOcbcImputationBean()!=null) {
                    editText.setText(String.valueOf(getOcbcImputationBean().getAmount()));
                }else {
                    editText.setText(String.valueOf(getTakeLatestOrderBean().getAmount()));
                }
                getHandler().postDelayed(() -> positioningView("payment_select_amount"), 3000);
            });
        }
    }

    //输入银行
    private void InputAmount(View view) {
        EditText editText = ViewUtil.findViewById(view, "edit_txt");
        if (getOcbcImputationBean() != null) {
            ViewUtil.editText(editText, getOcbcImputationBean().getCard());
        } else {
            ViewUtil.editText(editText, getTakeLatestOrderBean().getCardNumber());
        }
        positioningView("btn_continue");
    }

    //选择银行编码
    private void selectBank(View view) {
        if (view instanceof ViewGroup viewGroup) {
            List<EditText> editTexts = ViewUtil.findAllEditTexts(viewGroup);
            editTexts.forEach(ViewUtil::performClick);
        }
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        if (event.getCode() == 2) {
            getActivity().finish();
        } else if (event.getCode() == 89) {
            positioningView("field_rev_no");
        }
    }
}
