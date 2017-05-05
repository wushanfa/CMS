package com.gentlehealthcare.mobilecare.presenter;

import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.model.IInsulinPatrolModel;
import com.gentlehealthcare.mobilecare.model.impl.InsulinPatrolModel;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.view.IPatrolView;

/**
 * Created by zhiwei on 2016/5/17.
 */
public class InsulinPatrolPresenter {

    private IPatrolView patrolView;
    private IInsulinPatrolModel patrolModel;

    public InsulinPatrolPresenter(IPatrolView patrolView) {
        this.patrolView = patrolView;
        patrolModel = new InsulinPatrolModel();
    }

    public void showWhichText(int which) {
        switch (which) {
            case GlobalConstant.GUO_MIN:
                patrolView.showDialog("过敏反应", patrolView.getGuoMin(), patrolView.getGuominBoolean(),
                        GlobalConstant.GUO_MIN);
                break;
            case GlobalConstant.DI_XUE_TANG:
                patrolView.showDialog("低血糖反应", patrolView.getDixueTang(), patrolView.getDixuetangBoolean(),
                        GlobalConstant.DI_XUE_TANG);
                break;
            case GlobalConstant.ZHU_SHE:
                patrolView.showDialog("注射部位皮试", patrolView.getZhushe(), patrolView.getZhusheBoolean(),
                        GlobalConstant.ZHU_SHE);
                break;
            default:
                break;
        }
    }

    public void putWhichTextResult(int type, String str) {
        switch (type) {
            case GlobalConstant.GUO_MIN:
                patrolView.setGuoMin(str);
                break;
            case GlobalConstant.DI_XUE_TANG:
                patrolView.setDiXueTang(str);
                break;
            case GlobalConstant.ZHU_SHE:
                patrolView.setZhuShe(str);
                break;
            default:
                break;
        }
    }

    public void initText() {
        patrolView.initTextView();
    }

    public void uploadResult(String patId, String planOrderNo, String guoMin, String diXueTang, String zhuShe) {
        patrolView.showProgressDialog();
        String result = "";
        if (!guoMin.equals("")) {
            result = guoMin;
        }
        if (!diXueTang.equals("")) {
            result += diXueTang;
        }
        if (!zhuShe.equals("")) {
            result += zhuShe;
        }
        patrolModel.uploadResult(patId, planOrderNo, result, new InsulinPatrolModel.onUpLoadResult() {
            @Override
            public void onLoadSuccess() {
                patrolView.dismissProgressDialog();
                patrolView.activityFinish();
            }

            @Override
            public void onLoadFail(String msg, Exception e) {
                patrolView.dismissProgressDialog();
                patrolView.showToast("提交失败");
            }
        });
    }

    public void setTime(String str) {
//        patrolView.setTime(DateTool.spliteDate(GlobalConstant.DATE, str), DateTool.spliteDate(GlobalConstant
// .TIME,
//                str));
        patrolView.setTime(DateTool.getCurrDate(), DateTool.getCurrTime());
    }
}
