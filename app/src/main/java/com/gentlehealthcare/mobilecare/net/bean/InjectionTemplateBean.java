package com.gentlehealthcare.mobilecare.net.bean;

import com.gentlehealthcare.mobilecare.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 注射部位主要算法类
 * Created by ouyang on 15/6/24.
 */
public class InjectionTemplateBean {
    private String patId;
    private boolean result;
    private String msg;

    public InjectionTemplateBean(int isReload) {
        this.isReload = isReload;
    }


    public InjectionTemplateBean() {

    }

    private int isReload;//0 获取 1 重新配置

    private PlaceStatusInfo A;//左上臂
    private PlaceStatusInfo B;//右上臂
    private PlaceStatusInfo C;//左腹
    private PlaceStatusInfo D;//右腹
    private PlaceStatusInfo E;//左大腿
    private PlaceStatusInfo F;//右大腿
    private PlaceStatusInfo G;//左外臀
    private PlaceStatusInfo H;//右外臀
    private PlaceStausItem previous;//上次打的部位
    private PlaceStausItem current;//本次打的部位


    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    @Override
    public String toString() {
        return "?username="+ UserInfo.getUserName()+"&patId="+getPatId();
    }


    public PlaceStatusInfo getH() {
        return H;
    }

    public void setH(PlaceStatusInfo h) {
        H = h;
    }

    public PlaceStatusInfo getG() {
        return G;
    }

    public void setG(PlaceStatusInfo g) {
        G = g;
    }

    public PlaceStatusInfo getF() {
        return F;
    }

    public void setF(PlaceStatusInfo f) {
        F = f;
    }

    public PlaceStatusInfo getE() {
        return E;
    }

    public void setE(PlaceStatusInfo e) {
        E = e;
    }

    public PlaceStatusInfo getD() {
        return D;
    }

    public void setD(PlaceStatusInfo d) {
        D = d;
    }

    public PlaceStatusInfo getC() {
        return C;
    }

    public void setC(PlaceStatusInfo c) {
        C = c;
    }

    public PlaceStatusInfo getB() {
        return B;
    }

    public void setB(PlaceStatusInfo b) {
        B = b;
    }

    public PlaceStatusInfo getA() {
        return A;
    }

    public void setA(PlaceStatusInfo a) {
        A = a;
    }

    /**
     * 获取该病人最大的注射过位置
     * @return
     */
    public Map<String,String> GetMaxInjectionedSiteNo(){
        Map<String,String> map=new HashMap<String, String>();
        int maxInjectionedSiteNo=0;
        int  maxInjectionedSiteNoA=getA().GetMaxInjectionedSiteNo();
        int maxInjectionedSiteNoB=getB().GetMaxInjectionedSiteNo();
        int  maxInjectionedSiteNoC=getC().GetMaxInjectionedSiteNo();
        int maxInjectionedSiteNoD=getD().GetMaxInjectionedSiteNo();
        int  maxInjectionedSiteNoE=getE().GetMaxInjectionedSiteNo();
        int maxInjectionedSiteNoF=getF().GetMaxInjectionedSiteNo();
        int  maxInjectionedSiteNoG=getG().GetMaxInjectionedSiteNo();
        int maxInjectionedSiteNoH=getH().GetMaxInjectionedSiteNo();
        String site="A";
        if (maxInjectionedSiteNo<maxInjectionedSiteNoA){
            site="A";
            maxInjectionedSiteNo=maxInjectionedSiteNoA;
        }
        if (maxInjectionedSiteNo<maxInjectionedSiteNoB){
            site="B";
            maxInjectionedSiteNo=maxInjectionedSiteNoB;
        }
        if (maxInjectionedSiteNo<maxInjectionedSiteNoC){
            site="C";
            maxInjectionedSiteNo=maxInjectionedSiteNoC;
        }
        if (maxInjectionedSiteNo<maxInjectionedSiteNoD){
            site="D";
            maxInjectionedSiteNo=maxInjectionedSiteNoD;
        }
        if (maxInjectionedSiteNo<maxInjectionedSiteNoE){
            site="E";
            maxInjectionedSiteNo=maxInjectionedSiteNoE;
        }
        if (maxInjectionedSiteNo<maxInjectionedSiteNoF){
            site="F";
            maxInjectionedSiteNo=maxInjectionedSiteNoF;
        }
        if (maxInjectionedSiteNo<maxInjectionedSiteNoG){
            site="G";
            maxInjectionedSiteNo=maxInjectionedSiteNoG;
        }
        if (maxInjectionedSiteNo<maxInjectionedSiteNoH){
            site="H";
            maxInjectionedSiteNo=maxInjectionedSiteNoH;
        }
        map.put("site",site);
        map.put("siteNo",maxInjectionedSiteNo+"");
        return map;
    }


    /**
     * 获取该病人将要打的部位
     * @return Map<String,String>  siteNo 将要打的部位
     *                             site  部位
     */
    public Map<String,String> GetWillInjectionSiteNo(){
        Map<String,String> map=GetMaxInjectionedSiteNo();
        int maxInjectionedSiteNo=Integer.valueOf(map.get("siteNo"));
        int willInjectionSiteNo=0;
        Map<String,Integer> retMapA=getA().getStatus().equals("1")?null:getA().GetWillInjectionSiteNo(maxInjectionedSiteNo);
        int  willInjectionSiteNoA=retMapA==null?0:retMapA.get("minWillInjectionSite");
        Map<String,Integer> retMapB=getB().getStatus().equals("1")?null:getB().GetWillInjectionSiteNo(maxInjectionedSiteNo);
        int  willInjectionSiteNoB=retMapB==null?0:retMapB.get("minWillInjectionSite");
        Map<String,Integer> retMapC=getC().getStatus().equals("1")?null:getC().GetWillInjectionSiteNo(maxInjectionedSiteNo);
        int  willInjectionSiteNoC=retMapC==null?0:retMapC.get("minWillInjectionSite");
        Map<String,Integer> retMapD=getD().getStatus().equals("1")?null:getD().GetWillInjectionSiteNo(maxInjectionedSiteNo);
        int  willInjectionSiteNoD=retMapD==null?0:retMapD.get("minWillInjectionSite");
        Map<String,Integer> retMapE=getE().getStatus().equals("1")?null:getE().GetWillInjectionSiteNo(maxInjectionedSiteNo);
        int  willInjectionSiteNoE=retMapE==null?0:retMapE.get("minWillInjectionSite");
        Map<String,Integer> retMapF=getF().getStatus().equals("1")?null:getF().GetWillInjectionSiteNo(maxInjectionedSiteNo);
        int  willInjectionSiteNoF=retMapF==null?0:retMapF.get("minWillInjectionSite");
        Map<String,Integer> retMapG=getG().getStatus().equals("1")?null:getG().GetWillInjectionSiteNo(maxInjectionedSiteNo);
        int  willInjectionSiteNoG=retMapG==null?0:retMapG.get("minWillInjectionSite");
        Map<String,Integer> retMapH=getH().getStatus().equals("1")?null:getH().GetWillInjectionSiteNo(maxInjectionedSiteNo);
        int  willInjectionSiteNoH=retMapH==null?0:retMapH.get("minWillInjectionSite");

        int itemNo=0;
        String site="A";
        if (willInjectionSiteNo==0||willInjectionSiteNo>willInjectionSiteNoA){
            site="A";
            willInjectionSiteNo=willInjectionSiteNoA;
            itemNo=retMapA==null?0:retMapA.get("itemNo");
        }
        if (willInjectionSiteNo==0||(willInjectionSiteNo>willInjectionSiteNoB&&willInjectionSiteNoB>0)){
            site="B";
            willInjectionSiteNo=willInjectionSiteNoB;
            itemNo=retMapB==null?0:retMapB.get("itemNo");
        }
        if (willInjectionSiteNo==0||(willInjectionSiteNo>willInjectionSiteNoC&&willInjectionSiteNoC>0)){
            site="C";
            willInjectionSiteNo=willInjectionSiteNoC;
            itemNo=retMapC==null?0:retMapC.get("itemNo");
        }
        if (willInjectionSiteNo==0||(willInjectionSiteNo>willInjectionSiteNoD&&willInjectionSiteNoD>0)){
            site="D";
            willInjectionSiteNo=willInjectionSiteNoD;
            itemNo=retMapD==null?0:retMapD.get("itemNo");
        }
        if (willInjectionSiteNo==0||(willInjectionSiteNo>willInjectionSiteNoE&&willInjectionSiteNoE>0)){
            site="E";
            willInjectionSiteNo=willInjectionSiteNoE;
            itemNo=retMapE==null?0:retMapE.get("itemNo");
        }
        if (willInjectionSiteNo==0||(willInjectionSiteNo>willInjectionSiteNoF&&willInjectionSiteNoF>0)){
            site="F";
            willInjectionSiteNo=willInjectionSiteNoF;
            itemNo=retMapF==null?0:retMapF.get("itemNo");
        }
        if (willInjectionSiteNo==0||(willInjectionSiteNo>willInjectionSiteNoG&&willInjectionSiteNoG>0)){
            site="G";
            willInjectionSiteNo=willInjectionSiteNoG;
            itemNo=retMapG==null?0:retMapG.get("itemNo");
        }
        if (willInjectionSiteNo==0||(willInjectionSiteNo>willInjectionSiteNoH&&willInjectionSiteNoH>0)){
            site="H";
            willInjectionSiteNo=willInjectionSiteNoH;
            itemNo=retMapH==null?0:retMapH.get("itemNo");
        }
        map.clear();
        map.put("site",site);
        map.put("siteNo",willInjectionSiteNo+"");
        map.put("itemNo",itemNo+"");
        return map;
    }


    /**
     * 获取该病人下次将要打的部位
     * @return
     */
    public Map<String,String> GetNextWillInjectionSiteNo(){
        Map<String,String> map=GetWillInjectionSiteNo();
        int willInjectionSiteNo=Integer.valueOf(map.get("siteNo"));
        int nextWillInjectionSiteNo=0;
        int  nextWillInjectionSiteNoA=getA().GetNextWillInjectionSiteNo(willInjectionSiteNo);
        int nextWillInjectionSiteNoB=getB().GetNextWillInjectionSiteNo(willInjectionSiteNo);
        int  nextWillInjectionSiteNoC=getC().GetNextWillInjectionSiteNo(willInjectionSiteNo);
        int nextWillInjectionSiteNoD=getD().GetNextWillInjectionSiteNo(willInjectionSiteNo);
        int  nextWillInjectionSiteNoE=getE().GetNextWillInjectionSiteNo(willInjectionSiteNo);
        int nextWillInjectionSiteNoF=getF().GetNextWillInjectionSiteNo(willInjectionSiteNo);
        int  nextWillInjectionSiteNoG=getG().GetNextWillInjectionSiteNo(willInjectionSiteNo);
        int nextWillInjectionSiteNoH=getH().GetNextWillInjectionSiteNo(willInjectionSiteNo);
        String site="A";
        if (nextWillInjectionSiteNo<nextWillInjectionSiteNoA){
            site="A";
            nextWillInjectionSiteNo=nextWillInjectionSiteNoA;
        }
        if (nextWillInjectionSiteNo<nextWillInjectionSiteNoB){
            site="B";
            nextWillInjectionSiteNo=nextWillInjectionSiteNoB;
        }
        if (nextWillInjectionSiteNo<nextWillInjectionSiteNoC){
            site="C";
            nextWillInjectionSiteNo=nextWillInjectionSiteNoC;
        }
        if (nextWillInjectionSiteNo<nextWillInjectionSiteNoD){
            site="D";
            nextWillInjectionSiteNo=nextWillInjectionSiteNoD;
        }
        if (nextWillInjectionSiteNo<nextWillInjectionSiteNoE){
            site="E";
            nextWillInjectionSiteNo=nextWillInjectionSiteNoE;
        }
        if (nextWillInjectionSiteNo<nextWillInjectionSiteNoF){
            site="F";
            nextWillInjectionSiteNo=nextWillInjectionSiteNoF;
        }
        if (nextWillInjectionSiteNo<nextWillInjectionSiteNoG){
            site="G";
            nextWillInjectionSiteNo=nextWillInjectionSiteNoG;
        }
        if (nextWillInjectionSiteNo<nextWillInjectionSiteNoH){
            site="H";
            nextWillInjectionSiteNo=nextWillInjectionSiteNoH;
        }
        map.clear();
        map.put("site",site);
        map.put("siteNo",nextWillInjectionSiteNo+"");
        return map;
    }


    /**
     * 获取该病人上次将要打的部位
     * @return
     */
    public Map<String,String> GetLastInjectionedSiteNo(){
        Map<String,String> map=GetWillInjectionSiteNo();
        int willInjectionSiteNo=Integer.valueOf(map.get("siteNo"));
        if (willInjectionSiteNo==1){
            map.put("siteNo","0");
            return map;
        }
        int lastInjectionedSiteNo=0;
        int  lastInjectionedSiteNoA=getA().GetLastInjectionedSiteNo(willInjectionSiteNo);
        int lastInjectionedSiteNoB=getB().GetLastInjectionedSiteNo(willInjectionSiteNo);
        int  lastInjectionedSiteNoC=getC().GetLastInjectionedSiteNo(willInjectionSiteNo);
        int lastInjectionedSiteNoD=getD().GetLastInjectionedSiteNo(willInjectionSiteNo);
        int  lastInjectionedSiteNoE=getE().GetLastInjectionedSiteNo(willInjectionSiteNo);
        int lastInjectionedSiteNoF=getF().GetLastInjectionedSiteNo(willInjectionSiteNo);
        int  lastInjectionedSiteNoG=getG().GetLastInjectionedSiteNo(willInjectionSiteNo);
        int lastInjectionedSiteNoH=getH().GetLastInjectionedSiteNo(willInjectionSiteNo);
        String site="A";
        if (lastInjectionedSiteNo<lastInjectionedSiteNoA){
            site="A";
            lastInjectionedSiteNo=lastInjectionedSiteNoA;
        }
        if (lastInjectionedSiteNo<lastInjectionedSiteNoB){
            site="B";
            lastInjectionedSiteNo=lastInjectionedSiteNoB;
        }
        if (lastInjectionedSiteNo<lastInjectionedSiteNoC){
            site="C";
            lastInjectionedSiteNo=lastInjectionedSiteNoC;
        }
        if (lastInjectionedSiteNo<lastInjectionedSiteNoD){
            site="D";
            lastInjectionedSiteNo=lastInjectionedSiteNoD;
        }
        if (lastInjectionedSiteNo<lastInjectionedSiteNoE){
            site="E";
            lastInjectionedSiteNo=lastInjectionedSiteNoE;
        }
        if (lastInjectionedSiteNo<lastInjectionedSiteNoF){
            site="F";
            lastInjectionedSiteNo=lastInjectionedSiteNoF;
        }
        if (lastInjectionedSiteNo<lastInjectionedSiteNoG){
            site="G";
            lastInjectionedSiteNo=lastInjectionedSiteNoG;
        }
        if (lastInjectionedSiteNo<lastInjectionedSiteNoH){
            site="H";
            lastInjectionedSiteNo=lastInjectionedSiteNoH;
        }
        map.clear();
        map.put("site",site);
        map.put("siteNo",lastInjectionedSiteNo+"");
        return map;
    }


    public int getIsReload() {
        return isReload;
    }

    public void setIsReload(int isReload) {
        this.isReload = isReload;
    }

    public PlaceStausItem getCurrent() {
        return current;
    }

    public void setCurrent(PlaceStausItem current) {
        this.current = current;
    }

    public PlaceStausItem getPrevious() {
        return previous;
    }

    public void setPrevious(PlaceStausItem previous) {
        this.previous = previous;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
