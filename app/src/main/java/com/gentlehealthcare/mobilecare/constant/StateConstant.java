package com.gentlehealthcare.mobilecare.constant;

/**
 * Created by ouyang on 15/6/24.
 */
public enum StateConstant {
    //等待执行
    STATE_WAITING{
        @Override
        public String GetStateValue() {
            return "0";
        }
    },
    //执行中
    STATE_EXECUTING {
        @Override
        public String GetStateValue() {
            return "1";
        }
    },
    //已执行
    STATE_EXECUTED {
        @Override
        public String GetStateValue() {
            return "2";
        }
    },
    //取消执行
    STATE_CANCEL {
        @Override
        public String GetStateValue() {
            return "-1";
        }
    };
    public abstract String GetStateValue();
}
