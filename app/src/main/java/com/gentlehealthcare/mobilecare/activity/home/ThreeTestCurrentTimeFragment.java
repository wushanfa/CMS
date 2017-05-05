package com.gentlehealthcare.mobilecare.activity.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.net.bean.LoadThreeTestBean;
import com.gentlehealthcare.mobilecare.net.bean.ModifyThreeTestBean;
import com.gentlehealthcare.mobilecare.tool.ICUCommonMethod;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class  ThreeTestCurrentTimeFragment extends BaseFragment {
    private static final String TAG = "ThreeTestActivityCurrentTimeFragment";

    private View view;
    /**
     * 自定义键盘
     */
//    private MyKeyBoradDialog mykey;

    private EditText et_tiwen, et_xintiao, et_huxi, et_maibo, et_pingfen,
            et_xueyashousuo7, et_xueyashuzhang7, et_xueyashousuo11,
            et_xueyashuzhang11, et_xueyashousuo15, et_xueyashuzhang15,
            et_xueyashousuo19, et_xueyashuzhang19;
    private List<EditText> editList = new ArrayList<EditText>();

    private StringBuffer stiwen = new StringBuffer();
    private StringBuffer sxintiao = new StringBuffer();
    private StringBuffer shuixi = new StringBuffer();
    private StringBuffer smaibo = new StringBuffer();
    private StringBuffer spingfen = new StringBuffer();
    private StringBuffer sxueyashousuo7 = new StringBuffer();
    private StringBuffer sxueyashuzhang7 = new StringBuffer();
    private StringBuffer sxueyashousuo11 = new StringBuffer();
    private StringBuffer sxueyashuzhang11 = new StringBuffer();
    private StringBuffer sxueyashousuo15 = new StringBuffer();
    private StringBuffer sxueyashuzhang15 = new StringBuffer();
    private StringBuffer sxueyashousuo19 = new StringBuffer();
    private StringBuffer sxueyashuzhang19 = new StringBuffer();

    private String nT = "";
    private String nHearRate = "";
    private String nR = "";
    private String nP = "";
    private String nPainScore = "";
    private String nDp19 = "";
    private String nSp19 = "";
    private String nDp15 = "";
    private String nSp15 = "";
    private String nDp11 = "";
    private String nSp11 = "";
    private String nDp7 = "";
    private String nSp7 = "";

    private Map<String, ModifyThreeTestBean> modMap = new HashMap<String, ModifyThreeTestBean>();

    private Map<String, LoadThreeTestBean> map = new HashMap<String, LoadThreeTestBean>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.template_current_time, container, false);

        initView();
/**
 * 自定义键盘
 */
//        hideSystemKeyBoard();

        addEditTextInList();

        et_tiwen.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("T") == null) {
                    mod = setModifyBean("T", "t", et_tiwen, "℃");
                    mod.setExamMethod("腋温");
                } else {
                    mod = modMap.get("T");
                    mod.setVitalSignsValues(et_tiwen.getText().toString());
                }
                modMap.put("T", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_xintiao.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("HEART_RATE") == null) {
                    mod = setModifyBean("HEART_RATE", "heart_rate", et_xintiao, "次/分");
                } else {
                    mod = modMap.get("HEART_RATE");
                    mod.setVitalSignsValues(et_xintiao.getText().toString());
                }
                modMap.put("HEART_RATE", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_huxi.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("R") == null) {
                    mod = setModifyBean("R", "r", et_huxi, "次/分");
                } else {
                    mod = modMap.get("R");
                    mod.setVitalSignsValues(et_huxi.getText().toString());
                }
                modMap.put("R", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_maibo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("P") == null) {
                    mod = setModifyBean("P", "p", et_maibo, "次/分");
                } else {
                    mod = modMap.get("P");
                    mod.setVitalSignsValues(et_maibo.getText().toString());
                }
                modMap.put("P", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_pingfen.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("PAIN_SCORE") == null) {
                    mod = setModifyBean("PAIN_SCORE", "pain_score", et_pingfen, "null");
                } else {
                    mod = modMap.get("PAIN_SCORE");
                    mod.setVitalSignsValues(et_pingfen.getText().toString());
                }
                modMap.put("PAIN_SCORE", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xueyashousuo7.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("SP7") == null) {
                    mod = setModifyBean("SP", "sp7", et_xueyashousuo7, "mmHg");
                } else {
                    mod = modMap.get("SP7");
                    mod.setVitalSignsValues(et_xueyashousuo7.getText().toString());
                }
                modMap.put("SP7", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xueyashousuo11.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("SP11") == null) {
                    mod = setModifyBean("SP", "sp11", et_xueyashousuo11, "mmHg");
                } else {
                    mod = modMap.get("SP11");
                    mod.setVitalSignsValues(et_xueyashousuo11.getText().toString());
                }
                modMap.put("SP11", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xueyashousuo15.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("SP15") == null) {
                    mod = setModifyBean("SP", "sp15", et_xueyashousuo15, "mmHg");
                } else {
                    mod = modMap.get("SP15");
                    mod.setVitalSignsValues(et_xueyashousuo15.getText().toString());
                }
                modMap.put("SP15", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xueyashousuo19.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("SP19") == null) {
                    mod = setModifyBean("SP", "sp19", et_xueyashousuo19, "mmHg");
                } else {
                    mod = modMap.get("SP19");
                    mod.setVitalSignsValues(et_xueyashousuo19.getText().toString());
                }
                modMap.put("SP19", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xueyashuzhang7.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("DP7") == null) {
                    mod = setModifyBean("DP", "dp7", et_xueyashuzhang7, "mmHg");
                } else {
                    mod = modMap.get("DP7");
                    mod.setVitalSignsValues(et_xueyashuzhang7.getText().toString());
                }
                modMap.put("DP7", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xueyashuzhang11.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("DP11") == null) {
                    mod = setModifyBean("DP", "dp11", et_xueyashuzhang11, "mmHg");
                } else {
                    mod = modMap.get("DP11");
                    mod.setVitalSignsValues(et_xueyashuzhang11.getText().toString());
                }
                modMap.put("DP11", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xueyashuzhang15.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("DP15") == null) {
                    mod = setModifyBean("DP", "dp15", et_xueyashuzhang15, "mmHg");
                } else {
                    mod = modMap.get("DP15");
                    mod.setVitalSignsValues(et_xueyashuzhang15.getText().toString());
                }
                modMap.put("DP15", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xueyashuzhang19.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("DP19") == null) {
                    mod = setModifyBean("DP", "dp19", et_xueyashuzhang19, "mmHg");
                } else {
                    mod = modMap.get("DP19");
                    mod.setVitalSignsValues(et_xueyashuzhang19.getText().toString());
                }
                modMap.put("DP19", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    private void initView() {
        et_tiwen = (EditText) view.findViewById(R.id.et_tiwen);
        et_xintiao = (EditText) view.findViewById(R.id.et_xintiao);
        et_huxi = (EditText) view.findViewById(R.id.et_huxi);
        et_maibo = (EditText) view.findViewById(R.id.et_maibo);
        et_pingfen = (EditText) view.findViewById(R.id.et_pingfen);
        et_xueyashousuo7 = (EditText) view.findViewById(R.id.et_xueyashousuo7);
        et_xueyashuzhang7 = (EditText) view.findViewById(R.id.et_xueyashuzhang7);
        et_xueyashousuo11 = (EditText) view.findViewById(R.id.et_xueyashousuo11);
        et_xueyashuzhang11 = (EditText) view.findViewById(R.id.et_xueyashuzhang11);
        et_xueyashousuo15 = (EditText) view.findViewById(R.id.et_xueyashousuo15);
        et_xueyashuzhang15 = (EditText) view.findViewById(R.id.et_xueyashuzhang15);
        et_xueyashousuo19 = (EditText) view.findViewById(R.id.et_xueyashousuo19);
        et_xueyashuzhang19 = (EditText) view.findViewById(R.id.et_xueyashuzhang19);
        /**
         * 自定义键盘
         */
//        et_tiwen.setOnClickListener(this);
//        et_xintiao.setOnClickListener(this);
//        et_huxi.setOnClickListener(this);
//        et_maibo.setOnClickListener(this);
//        et_pingfen.setOnClickListener(this);
//        et_xueyashousuo7.setOnClickListener(this);
//        et_xueyashuzhang7.setOnClickListener(this);
//        et_xueyashousuo11.setOnClickListener(this);
//        et_xueyashuzhang11.setOnClickListener(this);
//        et_xueyashousuo15.setOnClickListener(this);
//        et_xueyashuzhang15.setOnClickListener(this);
//        et_xueyashousuo19.setOnClickListener(this);
//        et_xueyashuzhang19.setOnClickListener(this);
//        et_xintiao.setOnFocusChangeListener(this);
//        et_huxi.setOnFocusChangeListener(this);
//        et_maibo.setOnFocusChangeListener(this);
//        et_pingfen.setOnFocusChangeListener(this);
//        et_xueyashuzhang7.setOnFocusChangeListener(this);
//        et_xueyashousuo7.setOnFocusChangeListener(this);
//        et_xueyashousuo11.setOnFocusChangeListener(this);
//        et_xueyashuzhang11.setOnFocusChangeListener(this);
//        et_xueyashousuo15.setOnFocusChangeListener(this);
//        et_xueyashousuo19.setOnFocusChangeListener(this);
//        et_xueyashuzhang19.setOnFocusChangeListener(this);
//        et_xueyashuzhang15.setOnFocusChangeListener(this);
    }
/**
 * 自定义键盘
 */
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.et_tiwen:
//                MyKeyBoradDialog mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new
//                        MyKeyBoradDialog.myKeyListener() {
//
//                            @Override
//                            public void onClick(View keyView) {
//                                myKeyBoardToString(keyView, et_tiwen, stiwen);
//                            }
//                        });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xintiao:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xintiao, sxintiao);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_huxi:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_huxi, shuixi);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_maibo:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_maibo, smaibo);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_pingfen:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_pingfen, spingfen);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xueyashousuo7:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xueyashousuo7, sxueyashousuo7);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xueyashuzhang7:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xueyashuzhang7, sxueyashuzhang7);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//
//            case R.id.et_xueyashousuo11:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xueyashousuo11, sxueyashousuo11);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xueyashuzhang11:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xueyashuzhang11, sxueyashuzhang11);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xueyashousuo15:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xueyashousuo15, sxueyashousuo15);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xueyashuzhang15:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xueyashuzhang15, sxueyashuzhang15);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xueyashousuo19:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xueyashousuo19, sxueyashousuo19);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xueyashuzhang19:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xueyashuzhang19, sxueyashuzhang19);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//
//            default:
//                break;
//        }
//    }

    /**
     * 自定义键盘
     */
//    /**
//     * 获得焦点事件
//     */
//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        if (hasFocus) {
//            switch (v.getId()) {
//                case R.id.et_tiwen:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new
//                            MyKeyBoradDialog.myKeyListener() {
//
//                                @Override
//                                public void onClick(View keyView) {
//                                    myKeyBoardToString(keyView, et_tiwen, stiwen);
//                                }
//                            });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xintiao:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_xintiao, sxintiao);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_huxi:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_huxi, shuixi);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_maibo:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_maibo, smaibo);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_pingfen:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_pingfen, spingfen);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xueyashousuo7:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_xueyashousuo7, sxueyashousuo7);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xueyashuzhang7:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_xueyashuzhang7, sxueyashuzhang7);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//
//                case R.id.et_xueyashousuo11:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_xueyashousuo11, sxueyashousuo11);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xueyashuzhang11:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_xueyashuzhang11, sxueyashuzhang11);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xueyashousuo15:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_xueyashousuo15, sxueyashousuo15);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xueyashuzhang15:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_xueyashuzhang15, sxueyashuzhang15);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xueyashousuo19:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_xueyashousuo19, sxueyashousuo19);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xueyashuzhang19:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                            .myKeyListener() {
//
//                        @Override
//                        public void onClick(View keyView) {
//                            myKeyBoardToString(keyView, et_xueyashuzhang19, sxueyashuzhang19);
//                        }
//                    });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//
//                default:
//                    break;
//            }
//
//        } else {
//
//        }
//    }
//
//    /**
//     * 隐藏自带的键盘
//     */
//    private void hideSystemKeyBoard() {
//
//        Window myWindow = getActivity().getWindow();
//        HideSoftKeyBroad.hideSoftInputMethod(et_tiwen, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xintiao, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_huxi, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_maibo, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_pingfen, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xueyashousuo7, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xueyashuzhang7, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xueyashousuo11, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xueyashuzhang11, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xueyashousuo15, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xueyashuzhang15, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xueyashuzhang19, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xueyashousuo19, myWindow);
//
//    }

//    /**
//     * 我的键盘点击事件
//     *
//     * @param view
//     * @param myEt
//     * @param sb
//     */
//    private void myKeyBoardToString(View view, EditText myEt, StringBuffer sb) {
//        switch (view.getId()) {
//            case R.id.one:
//                sb.append(1);
//                myEt.setText(sb.toString());
//                break;
//            case R.id.two:
//                sb.append(2);
//                myEt.setText(sb.toString());
//                break;
//            case R.id.three:
//                sb.append(3);
//                myEt.setText(sb.toString());
//                break;
//            case R.id.four:
//                sb.append(4);
//                myEt.setText(sb.toString());
//                break;
//            case R.id.five:
//                sb.append(5);
//                myEt.setText(sb.toString());
//                break;
//            case R.id.six:
//                sb.append(6);
//                myEt.setText(sb.toString());
//                break;
//            case R.id.seven:
//                sb.append(7);
//                myEt.setText(sb.toString());
//                break;
//            case R.id.eight:
//                sb.append(8);
//                myEt.setText(sb.toString());
//                break;
//            case R.id.nine:
//                sb.append(9);
//                myEt.setText(sb.toString());
//                break;
//            case R.id.zero:
//                sb.append(0);
//                myEt.setText(sb.toString());
//                break;
//            case R.id.point:
//                sb.append(".");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.btn_e:
//                sb.append("E");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.btn_slash:
//                int num = ICUCommonMethod.getEdtiText(editList);
//                ICUCommonMethod.nextEditText(editList, num);
//                break;
//            case R.id.clear:
//                if (sb.length() > 0) {
//                    sb.deleteCharAt(sb.length() - 1);
//                }
//                myEt.setText(sb.toString());
//                break;
//            case R.id.thirtyeight:
//                sb.append("38");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.thirtynine:
//                sb.append("39");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.thirtyseven:
//                sb.append("37");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.thirtysix:
//                sb.append("36");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.maohao:
//                sb.append(":");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.underline:
//                sb.append("-");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.forth:
//                sb.append("40");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.g:
//                sb.append("g");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.ml:
//                sb.append("ml");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.btn_star:
//                sb.append("*");
//                myEt.setText(sb.toString());
//                break;
//            case R.id.xiangshang:
//                int oldNum = ICUCommonMethod.getEdtiText(editList);
//                ICUCommonMethod.perEditText(editList, oldNum);
//                break;
//            default:
//                break;
//        }
//    }
    public ModifyThreeTestBean setModifyBean(String vitalsigns, String flag,
                                             EditText myEt, String units) {
        ModifyThreeTestBean mod = new ModifyThreeTestBean();
        mod.setVitalSigns(vitalsigns);
        LoadThreeTestBean bean = map.get(flag);
        if (bean != null) {
            mod.setOrgVsId(map.get(flag).getVsId() == null ? "thisIsNull" : map
                    .get(flag).getVsId());
            mod.setVitalSignsValues2(map.get(flag).getVitalSignsValues2() == null ? "thisIsNull"
                    : map.get(flag).getVitalSignsValues2());
        }
        mod.setVitalSignsValues(myEt.getText().toString() == null ? "thisIsNull"
                : myEt.getText().toString());
        return mod;
    }

    public void myclear() {
        stiwen = new StringBuffer();
        sxintiao = new StringBuffer();
        shuixi = new StringBuffer();
        smaibo = new StringBuffer();
        spingfen = new StringBuffer();
        sxueyashousuo7 = new StringBuffer();
        sxueyashuzhang7 = new StringBuffer();
        sxueyashousuo11 = new StringBuffer();
        sxueyashuzhang11 = new StringBuffer();
        sxueyashousuo15 = new StringBuffer();
        sxueyashuzhang15 = new StringBuffer();
        sxueyashousuo19 = new StringBuffer();
        sxueyashuzhang19 = new StringBuffer();

        nT = "";
        nHearRate = "";
        nR = "";
        nP = "";
        nPainScore = "";
        nDp19 = "";
        nSp19 = "";
        nDp15 = "";
        nSp15 = "";
        nDp11 = "";
        nSp11 = "";
        nDp7 = "";
        nSp7 = "";
        et_tiwen.setText("");
        et_xintiao.setText("");
        et_huxi.setText("");
        et_maibo.setText("");
        et_pingfen.setText("");
        et_xueyashousuo7.setText("");
        et_xueyashuzhang7.setText("");
        et_xueyashousuo11.setText("");
        et_xueyashuzhang11.setText("");
        et_xueyashousuo15.setText("");
        et_xueyashuzhang15.setText("");
        et_xueyashousuo19.setText("");
        et_xueyashuzhang19.setText("");
        modMap = new HashMap<String, ModifyThreeTestBean>();
    }

    public void setList(Map<String, LoadThreeTestBean> map) {
        this.map = map;
        if (map.get("t") != null) {
            nT = map.get("t").getVitalSignsValues();
            et_tiwen.setText(nT);
            stiwen = new StringBuffer(nT != null ? nT : "");
        }
        if (map.get("heart_rate") != null) {
            nHearRate = map.get("heart_rate").getVitalSignsValues();
            et_xintiao.setText(nHearRate);
            sxintiao = new StringBuffer(nHearRate != null ? nHearRate : "");
        }
        if (map.get("r") != null) {
            nR = map.get("r").getVitalSignsValues();
            et_huxi.setText(nR);
            shuixi = new StringBuffer(nR != null ? nR : "");
        }
        if (map.get("p") != null) {
            nP = map.get("p").getVitalSignsValues();
            et_maibo.setText(nP);
            smaibo = new StringBuffer(nP != null ? nP : "");
        }
        if (map.get("pain_score") != null) {
            nPainScore = map.get("pain_score").getVitalSignsValues();
            et_pingfen.setText(nPainScore);
            spingfen = new StringBuffer(nPainScore != null ? nPainScore : "");
        }
        if (map.get("sp7") != null) {
            nSp7 = map.get("sp7").getVitalSignsValues();
            et_xueyashousuo7.setText(nSp7);
            sxueyashousuo7 = new StringBuffer(nSp7 != null ? nSp7 : "");
        }
        if (map.get("dp7") != null) {
            nDp7 = map.get("dp7").getVitalSignsValues();
            et_xueyashuzhang7.setText(nDp7);
            sxueyashuzhang7 = new StringBuffer(nDp7 != null ? nDp7 : "");
        }
        if (map.get("sp11") != null) {
            nSp11 = map.get("sp11").getVitalSignsValues();
            et_xueyashousuo11.setText(nSp11);
            sxueyashousuo11 = new StringBuffer(nSp11 != null ? nSp11 : "");
        }
        if (map.get("dp11") != null) {
            nDp11 = map.get("dp11").getVitalSignsValues();
            et_xueyashuzhang11.setText(nDp11);
            sxueyashuzhang11 = new StringBuffer(nDp11 != null ? nDp11 : "");
        }
        if (map.get("sp15") != null) {
            nSp15 = map.get("sp15").getVitalSignsValues();
            et_xueyashousuo15.setText(nSp15);
            sxueyashousuo15 = new StringBuffer(nSp15 != null ? nSp15 : "");
        }
        if (map.get("dp15") != null) {
            nDp15 = map.get("dp15").getVitalSignsValues();
            et_xueyashuzhang15.setText(nDp15);
            sxueyashuzhang15 = new StringBuffer(nDp15 != null ? nSp15 : "");
        }
        if (map.get("sp19") != null) {
            nSp19 = map.get("sp19").getVitalSignsValues();
            et_xueyashousuo19.setText(nSp19);
            sxueyashousuo19 = new StringBuffer(map.get("sp19")
                    .getVitalSignsValues() != null ? nSp19 : "");
        }
        if (map.get("dp19") != null) {
            nDp19 = map.get("dp19").getVitalSignsValues();
            et_xueyashuzhang19.setText(nDp19);
            sxueyashuzhang19 = new StringBuffer(map.get("dp19")
                    .getVitalSignsValues() != null ? nDp19 : "");
        }
    }

    public Map<String, ModifyThreeTestBean> getModifyMap() {

        return modMap;
    }

    @Override
    protected void resetLayout(View view) {
        ScrollView root = (ScrollView) view
                .findViewById(R.id.current);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private void addEditTextInList() {
        editList = ICUCommonMethod.addEditTextIntoList(et_tiwen, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_xintiao, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_huxi, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_maibo, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_pingfen, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_xueyashousuo7, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_xueyashuzhang7, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_xueyashousuo11, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_xueyashuzhang11, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_xueyashousuo15, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_xueyashuzhang15, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_xueyashousuo19, 4);
        editList = ICUCommonMethod.addEditTextIntoList(et_xueyashuzhang19, 4);
    }
}
