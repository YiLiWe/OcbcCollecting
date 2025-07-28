package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.rest.OkhttpUtils;
import com.example.ocbccollecting.utils.ViewUtil;

import java.util.List;

public class TransferActivity extends BaseActivity {
    private boolean isOk = false;

    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.ui.dashboard.home.transfer.TransferActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        if (getOcbcImputationBean() == null && getTakeLatestOrderBean() == null) {
            activity.finish();
            return;
        }
        //定时任务,避免卡界面
        getHandler().postDelayed(() -> {
            if (isOk) return;
            if (getOcbcImputationBean() != null) {
                OkhttpUtils.postOcbcImputation(getActivityLifecycleCallbacks().getAppConfig(), getOcbcImputationBean(), 2, "Error Occurred");
                setOcbcImputationBean(null);
                getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(4));
            } else {
                OkhttpUtils.PullPost(0, "Error Occurred", getActivityLifecycleCallbacks().getAppConfig(), getTakeLatestOrderBean());
                setTakeLatestOrderBean(null);
                getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(4));
            }
        }, 120_000);
        positioningView("new_recipient_menu_view");
    }

    @Override
    public void onResumed(Activity activity) {
        super.onResumed(activity);
    }

    @Override
    public void positioningView(View view) {
        super.positioningView(view);
        switch (ViewUtil.getResourceEntryName(getActivity(), view.getId())) {
            case "new_recipient_menu_view":
                click(view);
                positioningView("bank_recipient_field");
                break;
            case "bank_recipient_field":
                handlerNewRecipient(view);
                break;
            case "recipient_account_number_field":
                inputCardNumber(view);
                break;
            case "next_button":
                click(view);
                positioningView("keyboard_view");
                break;
            case "keyboard_view":
                keyboard_viewGone();
                positioningView("input_amount_edit_text");
                break;
            case "input_amount_edit_text":
                keyboard_viewGone();
                inputMoney(view);
                break;
            case "transfer_purpose_field":
                ChooseTransferPurpose(view);
                break;
            case "input_amount_root_constraint_layout":
                input_amount_root_constraint_layout(view);
                break;
            case "base_frame_container":
                isOk = true;//完成步骤，避免成功了，还提交
                base_frame_container(view);
                break;
        }
    }


    //隐藏输入按键
    private void keyboard_viewGone() {
        View base_frame_container = ViewUtil.findViewById(getActivity(), "base_frame_container");
        if (base_frame_container == null) return;
        View keyboard_view = ViewUtil.findViewById(base_frame_container, "keyboard_view");
        if (keyboard_view == null) return;
        if (keyboard_view.getVisibility() != View.GONE) {
            keyboard_view.post(() -> {
                keyboard_view.setVisibility(View.GONE);
                keyboard_view.requestLayout();
            });
        } else {
            keyboard_view.requestLayout();
        }
    }

    private void base_frame_container(View base_frame_container) {
        View view = ViewUtil.findViewById(base_frame_container, "confirm_btn");
        if (!ViewUtil.performClick(view)) {
            getHandler().postDelayed(() -> base_frame_container(base_frame_container), 1000);
        }
    }

    private void input_amount_root_constraint_layout(View input_amount_root_constraint_layout) {
        if (input_amount_root_constraint_layout instanceof ViewGroup view) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = 20000;
            view.setLayoutParams(layoutParams);
        }
        View view = ViewUtil.findViewById(input_amount_root_constraint_layout, "next_button");
        if (!ViewUtil.performClick(view)) {
            getHandler().postDelayed(() -> input_amount_root_constraint_layout(input_amount_root_constraint_layout), 1000);
        } else {
            positioningView("base_frame_container");
        }

    }

    private void ChooseTransferPurpose(View view) {
        EditText editText = ViewUtil.findViewById(view, "edit_txt");
        click(editText);
    }

    //输入金额
    private void inputMoney(View view) {
        if (view instanceof EditText editText) {
            if (getOcbcImputationBean() != null) {
                ViewUtil.editText(editText, String.valueOf(getOcbcImputationBean().getAmount()));
            } else {
                ViewUtil.editText(editText, String.valueOf(getTakeLatestOrderBean().getAmount()));
            }
            positioningView("transfer_purpose_field");
        }
    }

    //输入卡号
    private void inputCardNumber(View view) {
        EditText editText = ViewUtil.findViewById(view, "edit_txt");
        if (getOcbcImputationBean() != null) {
            ViewUtil.editText(editText, String.valueOf(getOcbcImputationBean().getCard()));
        } else {
            ViewUtil.editText(editText, String.valueOf(getTakeLatestOrderBean().getCardNumber()));
        }
        positioningView("next_button");
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        if (event.getCode() == 1) {
            positioningView("recipient_account_number_field");
        } else if (event.getCode() == 3) {
            positioningView("input_amount_root_constraint_layout");
        } else if (event.getCode() == 89) {
            positioningView("input_amount_root_constraint_layout");
        }
    }


    //选择银行转账
    private void handlerNewRecipient(View view) {
        if (view instanceof ViewGroup viewGroup) {
            List<EditText> editTexts = ViewUtil.findAllEditTexts(viewGroup);
            editTexts.forEach(ViewUtil::performClick);
        }
    }
}
