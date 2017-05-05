package com.gentlehealthcare.mobilecare.net;

/**
 * 密钥过时异常
 * Created by ouyang on 2015/7/1.
 */
public class KeyObsoleteException extends Exception{
    public KeyObsoleteException() {
    }
    public KeyObsoleteException(String detailMessage) {
        super(detailMessage);
    }
}
