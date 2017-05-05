package com.gentlehealthcare.mobilecare.tool;

import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadOrdersBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadOrdersInfo;
import com.gentlehealthcare.mobilecare.net.bean.OrderItemBean;

import java.util.List;

/**
 * Created by zhiwei on 2016/4/25.
 */
public class BeanTrans {

    public static OrderItemBean loadOrdersToOrderItem(LoadOrdersBean args) {
        OrderItemBean resultBean = new OrderItemBean();
        List<LoadOrdersInfo> infos = args.getSubOrders();
        String orderText = "";
        String wrongDosage = "";
        String performResult = "";
        String administration = "";
        String speed = "";
        String frequency = "";
        String injectionTool = "";
        if (!infos.isEmpty() && infos.size() > 0) {
            wrongDosage = infos.get(0).getDosage();
            performResult = infos.get(0).getPerformStatus();
            administration = infos.get(0).getAdministration();
            speed = infos.get(0).getSpeed();
            frequency = infos.get(0).getFrequency();
            injectionTool = infos.get(0).getInjectionTool();
            for (int i = 0; i < infos.size(); i++) {
                LoadOrdersInfo info = infos.get(i);
                String dosage = "";
                if (StringTool.isNotEmpty(info.getDosage())) {
                    dosage = info.getDosage();
                }
                orderText += info.getOrderText() + "," + dosage;
                if (infos.size() > i + 1) {
                    orderText += orderText + "||";
                }
            }
        }
        resultBean.setDosage(wrongDosage);
        resultBean.setPerformResult("");
        resultBean.setPerformStatus(performResult);
        resultBean.setDosageUnits("");
        resultBean.setOrderText(orderText);
        resultBean.setPlanStartTime(args.getPlanTime());
        resultBean.setAdministration(administration);
        resultBean.setSpeedUnits("");
        resultBean.setSpeed(speed);
        resultBean.setPlanOrderNo(args.getPlanOrderNo());
        resultBean.setFrequency(frequency);
        resultBean.setInjectionTool(injectionTool);
        resultBean.setTemplateId(args.getTemplateId());
        resultBean.setDosage2("");
        resultBean.setEventEndTime(args.getEventEndTime());
        resultBean.setNurseInOperate(args.getNurseInOperate());
        resultBean.setPlanTimeAttr("");
        resultBean.setEventStartTime(args.getEventStartTime());
        resultBean.setRelatedBarCode(args.getRelatedBarcode());
        resultBean.setEventEntTime("");
        return resultBean;
    }

    public static OrderItemBean orderListBeanToOrderItemBean(OrderListBean orderListBean){
        OrderItemBean resultBean = new OrderItemBean();
        resultBean.setDosage(orderListBean.getDosage());
        resultBean.setPerformResult("");
        resultBean.setPerformStatus(orderListBean.getPerformStatus());
        resultBean.setDosageUnits("");
        resultBean.setOrderText(orderListBean.getOrderText());
        resultBean.setPlanStartTime(orderListBean.getStartTime());
        resultBean.setAdministration(orderListBean.getAdministration());
        resultBean.setSpeedUnits("");
        resultBean.setSpeed(orderListBean.getSpeed());
        resultBean.setPlanOrderNo(orderListBean.getPlanOrderNo());
        resultBean.setFrequency(orderListBean.getFrequency());
        resultBean.setInjectionTool(orderListBean.getInjectionTool());
        if(StringTool.isEmpty(orderListBean.getTemplateId())){
            resultBean.setTemplateId("");

        }else{
            resultBean.setTemplateId(orderListBean.getTemplateId().toString());
        }
        resultBean.setDosage2("");
        if(StringTool.isEmpty(orderListBean.getEventEndTime())){
            resultBean.setEventEndTime("");
        }else{
            resultBean.setEventEndTime(orderListBean.getEventEndTime().toString());
        }
        resultBean.setNurseInOperate(orderListBean.getNurseInOperate());
        resultBean.setPlanTimeAttr("");
        if(StringTool.isEmpty(orderListBean.getEventStartTime())){
            resultBean.setEventStartTime("");
        }else{
            resultBean.setEventStartTime(orderListBean.getEventStartTime().toString());
        }
        resultBean.setRelatedBarCode(orderListBean.getRelatedBarcode());
        resultBean.setEventEntTime("");
        return resultBean;
    }
}
