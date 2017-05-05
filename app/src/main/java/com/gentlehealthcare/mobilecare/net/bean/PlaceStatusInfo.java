package com.gentlehealthcare.mobilecare.net.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ouyang on 15/6/25.
 */
public class PlaceStatusInfo {
    private List<PlaceStatusBean> statusList;
    private String status;//0 可打 1不可打
    private String siteDesc;

    public List<PlaceStatusBean> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<PlaceStatusBean> statusList) {
        this.statusList = statusList;
    }

    /**
     * 获取最大的注射过位置
     * @return
     */
    public int GetMaxInjectionedSiteNo(){
        int maxInjectionedSiteNo=0;
//        int minNo=0;
        if (statusList!=null&&statusList.size()>0){
            for (PlaceStatusBean placeStatusBean:statusList){
                int siteNo=Integer.valueOf(placeStatusBean.getSiteNo());
            if (placeStatusBean.getStatus().equals("1")){
                maxInjectionedSiteNo=maxInjectionedSiteNo<siteNo?siteNo:maxInjectionedSiteNo;
            }
//                if ((minNo==0||minNo>siteNo)&&(placeStatusBean.getStatus().equals("0")||placeStatusBean.getStatus().equals("1")))
//                    minNo=siteNo;
            }
        }
        return maxInjectionedSiteNo;
//        return maxInjectionedSiteNo==0&&minNo!=0?minNo:maxInjectionedSiteNo;
    }

    /**
     * 获取奔驰将要打的部位
     * @param maxInjectionedSiteNo  最大注射过的位置
     * @return
     */
//    public int GetWillInjectionSiteNo(int maxInjectionedSiteNo){
//        int minWillInjectionSite=0;
//        if (statusList!=null&&statusList.size()>0){
//            for (PlaceStatusBean placeStatusBean:statusList) {
//                int siteNo = Integer.valueOf(placeStatusBean.getSiteNo());
//
//                if (siteNo>maxInjectionedSiteNo&&placeStatusBean.getStatus().equals("0")){
//                    minWillInjectionSite=minWillInjectionSite<siteNo?minWillInjectionSite:siteNo;
//                    if (minWillInjectionSite==0||minWillInjectionSite>siteNo)
//                        minWillInjectionSite=siteNo;
//
//                }
//            }
//        }
//        return minWillInjectionSite;
//    }

    public Map<String,Integer> GetWillInjectionSiteNo(int maxInjectionedSiteNo){
        int minWillInjectionSite=0;
        Map<String,Integer> map=new HashMap<String, Integer>();
        int itemNo=0;
        if (statusList!=null&&statusList.size()>0){
            for (PlaceStatusBean placeStatusBean:statusList) {
                int siteNo = Integer.valueOf(placeStatusBean.getSiteNo());

                if (siteNo>maxInjectionedSiteNo&&placeStatusBean.getStatus().equals("0")){
                    minWillInjectionSite=minWillInjectionSite<siteNo?minWillInjectionSite:siteNo;
                    if (minWillInjectionSite==0||minWillInjectionSite>siteNo) {
                        minWillInjectionSite = siteNo;
                        itemNo=Integer.valueOf(placeStatusBean.getItemNo());
                    }

                }
            }
        }
        map.put("itemNo",itemNo);
        map.put("minWillInjectionSite",minWillInjectionSite);
        return map;
    }

    /**
     * 获取本次将要打的部位
     * @return
     */
    public int GetWillInjectionSiteNo(){
        Map<String,Integer> retMap=GetWillInjectionSiteNo(GetMaxInjectionedSiteNo());
       return retMap==null?0:retMap.get("minWillInjectionSite");
    }


    /**
     * 获取下次将要打的部位
     * @param willInjectionSiteNo  本次将要打的部位
     * @return
     */
    public int GetNextWillInjectionSiteNo(int willInjectionSiteNo){
        int nextWillInjectionSiteNo=0;
        if (statusList!=null&&statusList.size()>0){
            for (PlaceStatusBean placeStatusBean:statusList) {
                int siteNo = Integer.valueOf(placeStatusBean.getSiteNo());
                if (siteNo>willInjectionSiteNo&&placeStatusBean.getStatus().equals("0")){
                    nextWillInjectionSiteNo=nextWillInjectionSiteNo<siteNo?nextWillInjectionSiteNo:siteNo;
                }
            }
        }
        return nextWillInjectionSiteNo;
    }

    /**
     * 获取下次将要打的部位
     * @return
     */
    public int GetNextWillInjectionSiteNo(){
       return GetNextWillInjectionSiteNo(GetWillInjectionSiteNo());
    }

    /**
     * 获取上次将要打的部位
     * @param willInjectionSiteNo 本次将要打的部位
     * @return
     */
    public int GetLastInjectionedSiteNo(int willInjectionSiteNo){
        int lastInjectionedSiteNo=0;
        if (statusList!=null&&statusList.size()>0){
            for (PlaceStatusBean placeStatusBean:statusList) {
                int siteNo = Integer.valueOf(placeStatusBean.getSiteNo());
                if (siteNo<willInjectionSiteNo&&placeStatusBean.getStatus().equals("1")){
                    lastInjectionedSiteNo=lastInjectionedSiteNo<siteNo?siteNo:lastInjectionedSiteNo;
                }
            }
        }
        return lastInjectionedSiteNo;
    }

    /**
     * 获取上次将要打的部位
     * @return
     */
    public int GetLastInjectionedSiteNo(){
        return GetLastInjectionedSiteNo(GetWillInjectionSiteNo());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc;
    }
}
