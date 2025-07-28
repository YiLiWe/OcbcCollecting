package com.example.ocbccollecting.hook;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson2.JSON;
import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.hook.bean.APPConfig;
import com.example.ocbccollecting.hook.bean.BankAccountBean;
import com.example.ocbccollecting.hook.bean.ReceiptBean;
import com.example.ocbccollecting.hook.entity.OnlineTransactionEntity;
import com.example.ocbccollecting.rest.OkhttpUtils;
import com.example.ocbccollecting.sqlite.MyProvider;
import com.example.ocbccollecting.task.DeviceTask;
import com.example.ocbccollecting.task.bean.ImputationBeanOrder;
import com.example.ocbccollecting.task.bean.TakeLatestOrderBean;
import com.example.ocbccollecting.utils.Logs;
import com.example.ocbccollecting.utils.Md5Utils;
import com.google.gson.Gson;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * HookSystem
 */
public class MainHook implements IXposedHookLoadPackage {
    private static ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private String receiveMd5 = null;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.samsung.android.cidmanager")) HookSystem(lpparam);
        if (lpparam.packageName.equals("com.ocbcnisp.onemobileapp")) {
            //收款数据,和付款结果
            collectingData();
            hookInstrumentation(lpparam);
        }
    }

    private void hookInstrumentation(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.app.Instrumentation", lpparam.classLoader, "callApplicationOnCreate", Application.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args[0] instanceof Application application) {
                    APPConfig appConfig = new APPConfig();
                    appConfig.setContext(application.getApplicationContext());
                    appConfig.setCardNumber(query(application, "cardNumber"));
                    appConfig.setUrl(query(application, "url"));
                    appConfig.setMode(query(application, "mode"));
                    appConfig.setPayURL(query(application, "payURL"));
                    if (appConfig.isEmpty()) {
                        Toast.makeText(application, "信息配置不完整，或未打开Ocbc Payment保持后台", Toast.LENGTH_SHORT).show();
                        Logs.log("信息不完整，或未打开Ocbc Payment保持后台");
                        return;
                    }
                    activityLifecycleCallbacks = new ActivityLifecycleCallbacks(appConfig);
                    application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
                    new DeviceTask(appConfig, activityLifecycleCallbacks, new Handler(Looper.getMainLooper())).start();
                }
            }
        });
    }


    private void HookSystem(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.app.Instrumentation", lpparam.classLoader, "callApplicationOnCreate", Application.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (param.args[0] instanceof Application application) {
                    if (application.getPackageName().equals("com.samsung.android.cidmanager")) {
                        System.exit(0);
                    }
                }
            }
        });
    }

    public String query(Context context, String name) {
        Uri uri = Uri.parse("content://" + MyProvider.AUTHORITES + "/query");
        try (Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                "name=?",
                new String[]{name},
                null)) {
            return cursor != null && cursor.moveToFirst() ? cursor.getString(2) : "";
        }
    }


    //代收
    private void collectingData() {
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Context mContext = (Context) param.args[0];
                ClassLoader classLoader = mContext.getClassLoader();
                //代收
                Class<?> OnlineTransactionEntity = classLoader.loadClass("com.ocbcnisp.byon.data.entity.OnlineTransactionEntity");
                XposedBridge.hookAllConstructors(OnlineTransactionEntity, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Object o = param.thisObject;
                        String json = new Gson().toJson(o);
                        String md5 = Md5Utils.md5(json);
                        if (md5.equals(receiveMd5)) {
                            return;
                        }
                        receiveMd5 = md5;
                        OnlineTransactionEntity onlineTransactionEntity = JSON.to(OnlineTransactionEntity.class, json);
                        handlerReceiveData(onlineTransactionEntity);
                    }
                });
                //有两次。第一次输入密码成功以后，跳转支付界面
                Class<?> TransactionHistoryEntity = classLoader.loadClass("com.ocbcnisp.byon.domain.model.Receipt");
                XposedBridge.hookAllConstructors(TransactionHistoryEntity, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Object o = param.thisObject;
                        String json = new Gson().toJson(o);
                        handlerReceipt(json);
                    }
                });
                //账号信息
                Class<?> BankAccount = classLoader.loadClass("com.ocbcnisp.byon.domain.model.BankAccount");
                XposedBridge.hookAllConstructors(BankAccount, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Object o = param.thisObject;
                        String json = new Gson().toJson(o);
                        handlerBankAccount(json);
                    }
                });
            }
        });
    }

    //处理账号信息
    private void handlerBankAccount(String json) {
        BankAccountBean bankCardDTO = JSON.to(BankAccountBean.class, json);
        List<BankAccountBean.AssetSummariesDTO> assetSummariesDTO = bankCardDTO.getAssetSummaries();
        assetSummariesDTO.forEach(assetSummariesDTO1 -> {
            List<BankAccountBean.AssetSummariesDTO.AccountBalancesDTO> accountBalances = assetSummariesDTO1.getAccountBalances();
            accountBalances.forEach(accountBalancesDTO -> {
                long balance = accountBalancesDTO.getBalance();
                if (balance == 0 || activityLifecycleCallbacks == null) return;
                activityLifecycleCallbacks.getRunTask().setMoney(balance);
                activityLifecycleCallbacks.getTakeLatestOrderRun().setBalance(balance);
            });
        });
    }

    //转账结果信息。两次，1密码，2转账界面
    private void handlerReceipt(String json) {
        ReceiptBean receiptBean = JSON.to(ReceiptBean.class, json);
        List<ReceiptBean.ListTransactionDTO> listTransaction = receiptBean.getListTransaction();
        listTransaction.forEach(listTransactionDTO -> {
            ImputationBeanOrder ocbcImputationBean = activityLifecycleCallbacks.getRunTask().getOcbcImputationBean();
            TakeLatestOrderBean takeLatestOrderBean = activityLifecycleCallbacks.getTakeLatestOrderRun().getTakeLatestOrderBean();
            if (ocbcImputationBean == null && takeLatestOrderBean == null) return;
            if (ocbcImputationBean != null) {
                OkhttpUtils.postOcbcImputation(activityLifecycleCallbacks.getAppConfig(), ocbcImputationBean, 1, null);
                activityLifecycleCallbacks.getRunTask().setMoney(0);
                activityLifecycleCallbacks.getRunTask().setImputationBeanOrder(null);
            } else {
                if (listTransactionDTO.getTransactionStatus().equals("S")) {
                    OkhttpUtils.PullPost(1, "Transaction Successful", activityLifecycleCallbacks.getAppConfig(), takeLatestOrderBean);
                } else {
                    OkhttpUtils.PullPost(0, "支付界面，支付失败,可能成功", activityLifecycleCallbacks.getAppConfig(), takeLatestOrderBean);
                }
                activityLifecycleCallbacks.getTakeLatestOrderRun().setBalance(0);
                activityLifecycleCallbacks.getTakeLatestOrderRun().setTakeLatestOrderBean(null);
            }
            //订单号
            listTransactionDTO.getDescriptions().forEach(descriptionsDTO -> {
                if (descriptionsDTO.getTitle().equals("Reference Number")) {
                    listTransactionDTO.setReferenceNumber(descriptionsDTO.getValue());
                }
            });

            Logs.d("转账信息：" + JSON.toJSONString(listTransactionDTO));
        });
    }


    //处理代收数据
    private void handlerReceiveData(OnlineTransactionEntity onlineTransactionEntity) {
        if (onlineTransactionEntity.getHistories().isEmpty()) return;
        activityLifecycleCallbacks.onMessageEvent(new MessageEvent(0));
        APPConfig appConfig = activityLifecycleCallbacks.getAppConfig();
        onlineTransactionEntity.setCardNumber(appConfig.getCardNumber());
        OkhttpUtils.postOcbcReceive(appConfig, onlineTransactionEntity);
    }
}

