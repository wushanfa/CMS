package com.gentlehealthcare.mobilecare.constant;

/**
 * Created by ouyang on 15/6/24.
 */
public enum TemplateConstant {
    INJECTION {
        @Override
        public String GetTemplateId() {
            return "AA-2";
        }
    },
    MEDICINE {
        @Override
        public String GetTemplateId() {
            return "AA-1";
        }
    },
    BLOOD_TEST {
        @Override
        public String GetTemplateId() {
            return "AA-4";
        }
    },
    INSULIN {
        @Override
        public String GetTemplateId() {
            return "AA-3";
        }
    },
    TRANSFUSION {
        @Override
        public String GetTemplateId() {
            return "AA-5";
        }
    };


    public abstract String GetTemplateId();
}
