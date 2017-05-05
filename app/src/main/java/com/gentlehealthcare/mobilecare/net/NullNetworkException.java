package com.gentlehealthcare.mobilecare.net;

/**
 * 没有网络
 * Created by ouyang on 2015-04-14.
 */
public class NullNetworkException  extends RuntimeException{
    public NullNetworkException() {
    }
    public NullNetworkException(String detailMessage) {
        super(detailMessage);
    }
}
