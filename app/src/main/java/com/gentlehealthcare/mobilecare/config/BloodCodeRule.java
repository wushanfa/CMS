package com.gentlehealthcare.mobilecare.config;

/**
 * Created by Zyy on 2016/1/11.
 * 类说明：血品袋上条码的长度根据后台请求加载
 */
public class BloodCodeRule {
    /**
     * 血型码长度
     */
    private  static String bloodType;

    public static String getBloodCode() {
        return bloodCode;
    }

    public static void setBloodCode(String bloodCode) {
        BloodCodeRule.bloodCode = bloodCode;
    }

    public static String getBloodType() {
        return bloodType;
    }

    public static void setBloodType(String bloodType) {
        BloodCodeRule.bloodType = bloodType;
    }

    public static String getEffectiveDate() {
        return effectiveDate;
    }

    public static void setEffectiveDate(String effectiveDate) {
        BloodCodeRule.effectiveDate = effectiveDate;
    }

    public static String getPatCode() {
        return patCode;
    }

    public static void setPatCode(String patCode) {
        BloodCodeRule.patCode = patCode;
    }

    public static String getProductCode() {
        return productCode;
    }

    public static void setProductCode(String productCode) {
        BloodCodeRule.productCode = productCode;
    }

    /**
     *病人腕带码长度
     */
    private  static String patCode;

    /**
     *产品码长度
     */
    private  static String productCode;

    /**
     *血袋号码长度
     */
    private  static String bloodCode;
    /**
     *效期码长度
     */
    private  static String effectiveDate;
}
