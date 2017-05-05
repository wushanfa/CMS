package com.gentlehealthcare.mobilecare.activity.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.BaseFragment;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.AllergyBean;
import com.gentlehealthcare.mobilecare.net.bean.LoadThreeTestBean;
import com.gentlehealthcare.mobilecare.net.bean.ModifyThreeTestBean;
import com.gentlehealthcare.mobilecare.net.bean.OtherProjectBean;
import com.gentlehealthcare.mobilecare.net.impl.LoadAllergysDataRequest;
import com.gentlehealthcare.mobilecare.net.impl.LoadOtherProjectDataRequest;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.ICUCommonMethod;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeTestPastTimeFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "ThreeTestActivityPastTimeFragment";

    private View view;
/**
 * 自定义键盘
 */
//    private MyKeyBoradDialog mykey;
    /**
     * 三个提示选项
     */
    private List<AllergyBean> allergyBeans = new ArrayList<AllergyBean>();
    private List<OtherProjectBean> otherProjectBeans = new ArrayList<OtherProjectBean>();

    private Button guomintishi, add1, add2;
    private EditText et_weight, et_dabian1, et_dabian2, et_xiaobian1, et_xiaobian2, et_chushuiliang,
            et_rushuiliang, et_xuantian, et_xuantian2;

    private List<EditText> editList = new ArrayList<EditText>();
    private int which = 0;
    private int myflag1 = 0;
    private int myflag2 = 0;
    private String orgtext2 = "";
    private String orgtext1 = "";

    private boolean isChanged1 = false;
    private boolean isChanged2 = false;

    private StringBuffer sWeight = new StringBuffer();
    private StringBuffer sdabian = new StringBuffer();
    private StringBuffer sdadian2 = new StringBuffer();
    private StringBuffer sxiaobian = new StringBuffer();
    private StringBuffer sxiaobian2 = new StringBuffer();
    private StringBuffer schushuiliang = new StringBuffer();
    private StringBuffer srushuiliang = new StringBuffer();
    private StringBuffer sadd = new StringBuffer();
    private StringBuffer sadd2 = new StringBuffer();

    private String nWeight = "";
    private String nWoundDrainage7 = "";
    private String nWoundDrainage6 = "";
    private String nWoundDrainage5 = "";
    private String nWoundDrainage4 = "";
    private String nWoundDrainage3 = "";
    private String nWoundDrainage2 = "";
    private String nB = "";
    private String nWoundDrainage1 = "";
    private String nDrugAllery = "";
    private String nWaterOut = "";
    private String nWaterIn = "";
    private String nU = "";
    private String nU2 = "";
    private String nF = "";
    private String nF2 = "";

    private Map<String, ModifyThreeTestBean> modMap = new HashMap<String, ModifyThreeTestBean>();
    private Map<String, LoadThreeTestBean> map = new HashMap<String, LoadThreeTestBean>();

    private ModifyThreeTestBean addMod1 = new ModifyThreeTestBean();
    private ModifyThreeTestBean addMod2 = new ModifyThreeTestBean();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.template_yest_time, container, false);

        loadAllergyData();
        loadOtherItem();

        initView();
/**
 * 自定义键盘
 */
//        hideSystemKeyBoard();

        addEditTextInList();

        et_weight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("WEIGHT") == null) {
                    mod = setModifyBean("WEIGHT", "weight", et_weight, "Kg");
                } else {
                    mod = modMap.get("WEIGHT");
                    mod.setVitalSignsValues(et_weight.getText().toString());
                }
                modMap.put("WEIGHT", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_dabian1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("F") == null) {
                    mod = setModifyBean("F", "f", et_dabian1, "次");
                } else {
                    mod = modMap.get("F");
                    mod.setVitalSignsValues(et_dabian1.getText().toString());
                }
                modMap.put("F", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_dabian2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("F") == null) {
                    mod = setModifyBean("F", "f", et_dabian2, "次");
                } else {
                    mod = modMap.get("F");
                    mod.setVitalSignsValues2(et_dabian2.getText().toString());
                    mod.setUnits2("ml");
                }
                modMap.put("F", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_xiaobian1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("U") == null) {
                    mod = setModifyBean("U", "u", et_xiaobian1, "次");
                } else {
                    mod = modMap.get("U");
                    mod.setVitalSignsValues(et_xiaobian1.getText().toString());
                }
                modMap.put("U", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xiaobian2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("U") == null) {
                    mod = setModifyBean("U", "u", et_xiaobian2, "次");
                } else {
                    mod = modMap.get("U");
                    mod.setVitalSignsValues2(et_xiaobian2.getText().toString());
                    mod.setUnits2("ml");
                }
                modMap.put("U", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_chushuiliang.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("WATER_OUT") == null) {
                    mod = setModifyBean("WATER_OUT", "water_out",
                            et_chushuiliang, "ml");
                } else {
                    mod = modMap.get("WATER_OUT");
                    mod.setVitalSignsValues(et_chushuiliang.getText()
                            .toString());
                }
                modMap.put("WATER_OUT", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_rushuiliang.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ModifyThreeTestBean mod = new ModifyThreeTestBean();
                if (modMap.get("WATER_IN") == null) {
                    mod = setModifyBean("WATER_IN", "water_in", et_rushuiliang,
                            "ml");
                } else {
                    mod = modMap.get("WATER_IN");
                    mod.setVitalSignsValues(et_rushuiliang.getText().toString());
                }
                modMap.put("WATER_IN", mod);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xuantian.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isChanged1) {
                    addMod1.setVitalSigns(getOrgText(add1.getText().toString()));
                    addMod1.setOrgVsId(!getOrgText(add1.getText().toString()).equals("") ? map.get(getOrgText
                            (add1.getText().toString())).getVsId() : "");
                }
                addMod1.setVitalSignsValues(et_xuantian.getText().toString());

                modMap.put("add", addMod1);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_xuantian2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isChanged2) {
                    addMod2.setVitalSigns(getOrgText(add2.getText().toString()));
                    addMod2.setOrgVsId(!getOrgText(add2.getText().toString()).equals("") ? map.get(getOrgText
                            (add2.getText().toString())).getVsId() : "");
                }
                addMod2.setVitalSignsValues(et_xuantian2.getText().toString());
                modMap.put("add2", addMod2);
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
        guomintishi = (Button) view.findViewById(R.id.yaowuguomintishi);
        add1 = (Button) view.findViewById(R.id.add1);
        add2 = (Button) view.findViewById(R.id.add2);
        add1.setOnClickListener(this);
        add2.setOnClickListener(this);
        guomintishi.setOnClickListener(this);
        et_weight = (EditText) view.findViewById(R.id.et_weight);
        et_dabian1 = (EditText) view.findViewById(R.id.et_dabian1);
        et_dabian2 = (EditText) view.findViewById(R.id.et_dabian2);
        et_xiaobian1 = (EditText) view.findViewById(R.id.et_xiaobian1);
        et_xiaobian2 = (EditText) view.findViewById(R.id.et_xiaobian2);
        et_chushuiliang = (EditText) view.findViewById(R.id.et_chushuiliang);
        et_rushuiliang = (EditText) view.findViewById(R.id.et_rushuiliang);
        et_xuantian = (EditText) view.findViewById(R.id.et_xuantian);
        et_xuantian2 = (EditText) view.findViewById(R.id.et_xuantian2);
        /**
         * 自定义键盘
         */
//        et_dabian2.setOnFocusChangeListener(this);
//        et_weight.setOnFocusChangeListener(this);
//        et_weight.setOnClickListener(this);
//        et_dabian1.setOnFocusChangeListener(this);
//        et_dabian1.setOnClickListener(this);
//        et_dabian2.setOnClickListener(this);
//        et_xiaobian1.setOnClickListener(this);
//        et_xiaobian1.setOnFocusChangeListener(this);
//        et_xiaobian2.setOnClickListener(this);
//        et_xiaobian2.setOnFocusChangeListener(this);
//        et_chushuiliang.setOnClickListener(this);
//        et_chushuiliang.setOnFocusChangeListener(this);
//        et_rushuiliang.setOnClickListener(this);
//        et_rushuiliang.setOnFocusChangeListener(this);
//        et_xuantian.setOnClickListener(this);
//        et_xuantian.setOnFocusChangeListener(this);
//        et_xuantian2.setOnClickListener(this);
//        et_xuantian2.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yaowuguomintishi:
                ShowAllergyAlert(allergyBeans);
                break;
            case R.id.add1:
                which = 1;
                ShowAddItemAlert(otherProjectBeans, which);
                break;
            case R.id.add2:
                which = 2;
                ShowAddItemAlert(otherProjectBeans, which);
                break;
//            case R.id.et_weight:
//                MyKeyBoradDialog mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new
//                        MyKeyBoradDialog.myKeyListener() {
//
//                            @Override
//                            public void onClick(View keyView) {
//                                myKeyBoardToString(keyView, et_weight, sWeight);
//                            }
//                        });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_dabian1:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_dabian1, sdabian);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_dabian2:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_dabian2, sdadian2);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xiaobian1:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xiaobian1, sxiaobian);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xiaobian2:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xiaobian2, sxiaobian2);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_chushuiliang:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_chushuiliang, schushuiliang);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//
//            case R.id.et_rushuiliang:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_rushuiliang, srushuiliang);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xuantian:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xuantian, sadd);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
//            case R.id.et_xuantian2:
//                mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM, new MyKeyBoradDialog
//                        .myKeyListener() {
//
//                    @Override
//                    public void onClick(View keyView) {
//                        myKeyBoardToString(keyView, et_xuantian2, sadd2);
//                    }
//                });
//                mykey.show();
//                mykey.getWindow().setLayout(720, 500);
//                break;
            default:
                break;
        }
    }
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
//                case R.id.et_dabian1:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM,
//                            new MyKeyBoradDialog.myKeyListener() {
//
//                                @Override
//                                public void onClick(View keyView) {
//                                    myKeyBoardToString(keyView, et_dabian1, sdabian);
//                                }
//                            });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_dabian2:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM,
//                            new MyKeyBoradDialog.myKeyListener() {
//
//                                @Override
//                                public void onClick(View keyView) {
//                                    myKeyBoardToString(keyView, et_dabian2, sdadian2);
//                                }
//                            });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xiaobian1:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM,
//                            new MyKeyBoradDialog.myKeyListener() {
//
//                                @Override
//                                public void onClick(View keyView) {
//                                    myKeyBoardToString(keyView, et_xiaobian1, sxiaobian);
//                                }
//                            });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xiaobian2:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM,
//                            new MyKeyBoradDialog.myKeyListener() {
//
//                                @Override
//                                public void onClick(View keyView) {
//                                    myKeyBoardToString(keyView, et_xiaobian2, sxiaobian2);
//                                }
//                            });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_chushuiliang:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM,
//                            new MyKeyBoradDialog.myKeyListener() {
//
//                                @Override
//                                public void onClick(View keyView) {
//                                    myKeyBoardToString(keyView, et_chushuiliang, schushuiliang);
//                                }
//                            });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_rushuiliang:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM,
//                            new MyKeyBoradDialog.myKeyListener() {
//
//                                @Override
//                                public void onClick(View keyView) {
//                                    myKeyBoardToString(keyView, et_rushuiliang, srushuiliang);
//                                }
//                            });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xuantian:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM,
//                            new MyKeyBoradDialog.myKeyListener() {
//
//                                @Override
//                                public void onClick(View keyView) {
//                                    myKeyBoardToString(keyView, et_xuantian, sadd);
//                                }
//                            });
//                    mykey.show();
//                    mykey.getWindow().setLayout(720, 500);
//                    break;
//                case R.id.et_xuantian2:
//                    if (mykey!=null){
//                        mykey.dismiss();
//                    }
//                    mykey = new MyKeyBoradDialog(getActivity(), Gravity.BOTTOM,
//                            new MyKeyBoradDialog.myKeyListener() {
//
//                                @Override
//                                public void onClick(View keyView) {
//                                    myKeyBoardToString(keyView, et_xuantian2, sadd2);
//                                }
//                            });
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
//        HideSoftKeyBroad.hideSoftInputMethod(et_weight, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_dabian1, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_dabian2, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xiaobian1, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xiaobian2, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_chushuiliang, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_rushuiliang, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xuantian, myWindow);
//        HideSoftKeyBroad.hideSoftInputMethod(et_xuantian2, myWindow);
//
//    }

    /**
     * 显示allergy单选列表对话框
     *
     * @param list 列表数据
     */
    private void ShowAllergyAlert(final List<AllergyBean> list) {
        final String[] strs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strs[i] = list.get(i).getItemName();
        }
        AlertDialog alert = new AlertDialog.Builder(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT).setTitle("请选择")
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        guomintishi.setText(strs[i]);
                        ModifyThreeTestBean mod = new ModifyThreeTestBean();
                        if (modMap.get("DRUG_ALLERGY") == null) {
                            mod.setVitalSigns("DRUG_ALLERGY");
                            if (map.get("drug_allery") != null) {
                                mod.setOrgVsId(map.get("drug_allery").getVsId() == null ? "null"
                                        : map.get("drug_allery").getVsId());
                                mod.setVitalSignsValues2(map.get("drug_allery")
                                        .getVitalSignsValues2() == null ? "null"
                                        : map.get("drug_allery")
                                        .getVitalSignsValues2());
                            }
                        } else {
                            mod = modMap.get("DRUG_ALLERGY");
                        }
                        mod.setVitalSignsValues(strs[i]);
                        modMap.put("DRUG_ALLERGY", mod);
                    }
                }).create();
        alert.show();
        alert.getWindow().setLayout(550, 460);
    }

    /**
     * 显示增加项单选列表对话框
     *
     * @param list 列表数据
     */
    private void ShowAddItemAlert(final List<OtherProjectBean> list, int which) {
        final int myWhich = which;
        int finalNum = 0;
        final String[] strs = new String[list.size() + 1];
        for (int i = 0; i < list.size(); i++) {
            strs[i] = list.get(i).getItemName();
            finalNum = i;
        }
        strs[finalNum + 1] = "增    加    项:";
        AlertDialog alert = new AlertDialog.Builder(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT).setTitle("请选择")
                .setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (myWhich == 1) {
                            if (myflag1 == 0) {
                                orgtext1 = add1.getText().toString();
                                myflag1 = 1;
                            }
                            add1.setText(strs[i]);
                            et_xuantian.requestFocus();
                            if (i <= list.size() - 1) {
                                addMod1.setVitalSigns(list.get(i).getItemCode());
                                addMod1.setOrgVsId(!getOrgText(orgtext1)
                                        .equals("") ? map.get(
                                        getOrgText(orgtext1)).getVsId() : "");
                                isChanged1 = true;
                                et_xuantian.setText("");
                                sadd = new StringBuffer();
                                CCLog.e("这是真" + orgtext1 + getOrgText(orgtext1));
                            }
                        } else {

                            if (myflag2 == 0) {
                                orgtext2 = add2.getText().toString();
                                myflag2 = 1;
                            }

                            add2.setText(strs[i]);
                            et_xuantian2.requestFocus();
                            if (i <= list.size() - 1) {
                                addMod2.setVitalSigns(list.get(i).getItemCode());
                                addMod2.setOrgVsId(!getOrgText(orgtext2)
                                        .equals("") ? map.get(
                                        getOrgText(orgtext2)).getVsId() : "");
                                isChanged2 = true;
                                et_xuantian2.setText("");
                                sadd2 = new StringBuffer();
                                CCLog.e("这是真2" + orgtext2
                                        + getOrgText(orgtext2));
                            }
                        }

                    }
                }).create();
        alert.show();
        alert.getWindow().setLayout(600, 800);
    }

    /**
     * 加载allergy
     */
    public void loadAllergyData() {
        AllergyBean allergyBean = new AllergyBean();
        RequestManager.connection(new LoadAllergysDataRequest(getActivity(),
                new IRespose<AllergyBean>() {

                    @Override
                    public void doResult(String result)
                            throws KeyObsoleteException {
                        CCLog.i(TAG, "药物过敏的数据<<" + result);
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<AllergyBean>>() {
                        }.getType();
                        List<AllergyBean> list = gson.fromJson(result, type);

                        if (!allergyBeans.isEmpty()) {
                            allergyBeans.clear();
                        }
                        allergyBeans.addAll(list);
                    }

                    @Override
                    public void doResult(AllergyBean t, int id) {
                    }

                    @Override
                    public void doException(Exception e, boolean networkState) {
                    }
                }, 0, true, allergyBean));
    }

    /**
     * 加载增加的选项
     */
    public void loadOtherItem() {
        OtherProjectBean otherProject = new OtherProjectBean();
        RequestManager.connection(new LoadOtherProjectDataRequest(
                getActivity(), new IRespose<OtherProjectBean>() {

            @Override
            public void doResult(String result)
                    throws KeyObsoleteException {
                CCLog.i(TAG, "体温单其他项目的数据<<" + result);
                Gson gson = new Gson();
                Type type = new TypeToken<List<OtherProjectBean>>() {
                }.getType();
                List<OtherProjectBean> list = gson.fromJson(result,
                        type);

                if (!list.isEmpty()) {
                    otherProjectBeans.clear();
                }
                otherProjectBeans.addAll(list);
            }

            @Override
            public void doResult(OtherProjectBean t, int id) {

            }

            @Override
            public void doException(Exception e, boolean networkState) {

            }
        }, 0, true, otherProject));
    }

    /**
     * 我的键盘点击事件
     */
    private void myKeyBoardToString(View view, EditText myEt, StringBuffer sb) {
        switch (view.getId()) {
            case R.id.one:
                sb.append(1);
                myEt.setText(sb.toString());
                break;
            case R.id.two:
                sb.append(2);
                myEt.setText(sb.toString());
                break;
            case R.id.three:
                sb.append(3);
                myEt.setText(sb.toString());
                break;
            case R.id.four:
                sb.append(4);
                myEt.setText(sb.toString());
                break;
            case R.id.five:
                sb.append(5);
                myEt.setText(sb.toString());
                break;
            case R.id.six:
                sb.append(6);
                myEt.setText(sb.toString());
                break;
            case R.id.seven:
                sb.append(7);
                myEt.setText(sb.toString());
                break;
            case R.id.eight:
                sb.append(8);
                myEt.setText(sb.toString());
                break;
            case R.id.nine:
                sb.append(9);
                myEt.setText(sb.toString());
                break;
            case R.id.zero:
                sb.append(0);
                myEt.setText(sb.toString());
                break;
            case R.id.point:
                sb.append(".");
                myEt.setText(sb.toString());
                break;
            case R.id.btn_e:
                sb.append("E");
                myEt.setText(sb.toString());
                break;
            case R.id.btn_slash:
                int num = ICUCommonMethod.getEdtiText(editList);
                ICUCommonMethod.nextEditText(editList, num);
                break;
            case R.id.clear:
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                myEt.setText(sb.toString());
                break;
            case R.id.thirtyeight:
                sb.append("38");
                myEt.setText(sb.toString());
                break;
            case R.id.thirtynine:
                sb.append("39");
                myEt.setText(sb.toString());
                break;
            case R.id.thirtyseven:
                sb.append("37");
                myEt.setText(sb.toString());
                break;
            case R.id.thirtysix:
                sb.append("36");
                myEt.setText(sb.toString());
                break;
            case R.id.maohao:
                sb.append(":");
                myEt.setText(sb.toString());
                break;
            case R.id.underline:
                sb.append("-");
                myEt.setText(sb.toString());
                break;
            case R.id.forth:
                sb.append("40");
                myEt.setText(sb.toString());
                break;
            case R.id.g:
                sb.append("g");
                myEt.setText(sb.toString());
                break;
            case R.id.ml:
                sb.append("ml");
                myEt.setText(sb.toString());
                break;
            case R.id.btn_star:
                sb.append("*");
                myEt.setText(sb.toString());
                break;
            case R.id.xiangshang:
                int oldNum = ICUCommonMethod.getEdtiText(editList);
                ICUCommonMethod.perEditText(editList, oldNum);
                break;
            default:
                break;
        }
    }

    public ModifyThreeTestBean setModifyBean(String vitalsigns, String flag,
                                             EditText myEt, String units) {
        ModifyThreeTestBean mod = new ModifyThreeTestBean();
        mod.setVitalSigns(vitalsigns);
        if (map.get(flag) != null) {
            mod.setOrgVsId(map.get(flag).getVsId());
            // .isEmpty() ? "thisIsNull": map.get(flag).getVsId()
            mod.setVitalSignsValues2(map.get(flag).getVitalSignsValues2() == null ? "null"
                    : map.get(flag).getVitalSignsValues2());
        }
        mod.setVitalSignsValues(myEt.getText().toString() == null ? "null"
                : myEt.getText().toString());
        return mod;
    }

    public void myClear() {
        sWeight = new StringBuffer();
        sWeight = new StringBuffer();
        sdabian = new StringBuffer();
        sdadian2 = new StringBuffer();
        sxiaobian = new StringBuffer();
        sxiaobian2 = new StringBuffer();
        schushuiliang = new StringBuffer();
        srushuiliang = new StringBuffer();
        sadd = new StringBuffer();
        sadd2 = new StringBuffer();
        nWeight = "";
        et_weight.setText("");
        nF = "";
        nF2 = "";
        et_dabian1.setText("");
        et_dabian2.setText("");
        nU = "";
        nU2 = "";
        et_xiaobian1.setText("");
        et_xiaobian2.setText("");
        nWaterIn = "";
        et_rushuiliang.setText("");
        nWaterOut = "";
        et_chushuiliang.setText("");
        nDrugAllery = "";
        guomintishi.setText("");
        nWoundDrainage1 = "";
        et_xuantian.setText("");
        et_xuantian2.setText("");
        nB = "";
        nWoundDrainage2 = "";
        nWoundDrainage3 = "";
        nWoundDrainage4 = "";
        nWoundDrainage5 = "";
        nWoundDrainage6 = "";
        nWoundDrainage7 = "";
        add1.setText("增    加    项:");
        add2.setText("增    加    项:");
        modMap = new HashMap<String, ModifyThreeTestBean>();
    }

    public void addButton() {

    }

    private String getOrgText(String orgText) {
        String id = "";
        if (orgText.equals("切口负压引流（ml）")) {
            id = "WOUND_DRAINAGE1";
        } else if (orgText.equals("胸腔闭式引流（ml）")) {
            id = "WOUND_DRAINAGE2";
        } else if (orgText.equals("脑室引流（ml）")) {
            id = "WOUND_DRAINAGE3";
        } else if (orgText.equals("头皮下引流（ml）")) {
            id = "WOUND_DRAINAGE4";
        } else if (orgText.equals("硬膜下引流（ml）")) {
            id = "WOUND_DRAINAGE5";
        } else if (orgText.equals("硬膜外引流（ml）")) {
            id = "WOUND_DRAINAGE6";
        } else if (orgText.equals("鼻腔出血（ml）")) {
            id = "WOUND_DRAINAGE7";
        } else if (orgText.equals("手术时间")) {
            id = "B";
        }
        return id;
    }

    public void setList(Map<String, LoadThreeTestBean> map, int myflag1,
                        int myflag2, boolean isChanged1, boolean isChanged2) {
        this.map = map;
        this.myflag1 = myflag1;
        this.myflag2 = myflag2;
        this.isChanged1 = isChanged1;
        this.isChanged2 = isChanged2;
        if (map.get("weight") != null) {
            nWeight = map.get("weight").getVitalSignsValues();
            CCLog.e("weight>>>>>>>>" + nWeight + map.get("weight").getVsId());
            et_weight.setText(nWeight);
            sWeight = new StringBuffer(nWeight != null ? nWeight : "");
        }
        if (map.get("f") != null) {
            nF = map.get("f").getVitalSignsValues();
            nF2 = map.get("f").getVitalSignsValues2();
            et_dabian1.setText(nF);
            sdabian = new StringBuffer(nF != null ? nF : "");
            if (nF2 != null) {
                et_dabian2.setText(nF2);
                sdadian2 = new StringBuffer(nF2 != null ? nF2 : "");
            }
        }
        if (map.get("u") != null) {
            nU = map.get("u").getVitalSignsValues();
            nU2 = map.get("u").getVitalSignsValues2();
            et_xiaobian1.setText(nU);
            sxiaobian = new StringBuffer(nU != null ? nU : "");
            if (nU2 != null) {
                et_xiaobian2.setText(nU2);
                sxiaobian2 = new StringBuffer(nU2 != null ? nU2 : "");
            }
        }
        if (map.get("water_in") != null) {
            nWaterIn = map.get("water_in").getVitalSignsValues();
            et_rushuiliang.setText(nWaterIn);
            srushuiliang = new StringBuffer(nWaterIn != null ? nWaterIn : "");
        }
        if (map.get("water_out") != null) {
            nWaterOut = map.get("water_out").getVitalSignsValues();
            et_chushuiliang.setText(nWaterOut);
            schushuiliang = new StringBuffer(nWaterOut != null ? nWaterOut : "");
        }
        if (map.get("drug_allery") != null) {
            nDrugAllery = map.get("drug_allery").getVitalSignsValues();
            guomintishi.setText(nDrugAllery != null ? nDrugAllery : "");
        }
        if (map.get("WOUND_DRAINAGE1") != null) {

            nWoundDrainage1 = map.get("WOUND_DRAINAGE1").getVitalSignsValues();
            if (add1.getText().equals("增    加    项:")) {
                add1.setText("切口负压引流（ml）");
                et_xuantian.setText(nWoundDrainage1);
                sadd = new StringBuffer(
                        nWoundDrainage1 != null ? nWoundDrainage1 : "");
            } else {
                add2.setText("切口负压引流（ml）");
                et_xuantian2.setText(nWoundDrainage1);
                sadd2 = new StringBuffer(
                        nWoundDrainage1 != null ? nWoundDrainage1 : "");
            }
        }
        if (map.get("B") != null) {

            nB = map.get("B").getVitalSignsValues();
            if (add1.getText().equals("增    加    项:")) {
                add1.setText("手术时间");
                et_xuantian.setText(nB);
                sadd = new StringBuffer(nB != null ? nB : "");
            } else {
                add2.setText("手术时间");
                et_xuantian2.setText(nB);
                sadd2 = new StringBuffer(nB != null ? nB : "");
            }
        }
        if (map.get("WOUND_DRAINAGE2") != null) {

            nWoundDrainage2 = map.get("WOUND_DRAINAGE2").getVitalSignsValues();
            if (add1.getText().equals("增    加    项:")) {
                add1.setText("胸腔闭式引流（ml）");
                et_xuantian.setText(nWoundDrainage2);
                sadd = new StringBuffer(
                        nWoundDrainage2 != null ? nWoundDrainage2 : "");
            } else {
                add2.setText("胸腔闭式引流（ml）");
                et_xuantian2.setText(nWoundDrainage2);
                sadd2 = new StringBuffer(
                        nWoundDrainage2 != null ? nWoundDrainage2 : "");
            }
        }
        if (map.get("WOUND_DRAINAGE3") != null) {

            nWoundDrainage3 = map.get("WOUND_DRAINAGE3").getVitalSignsValues();
            if (add1.getText().equals("增    加    项:")) {
                add1.setText("脑室引流（ml）");
                et_xuantian.setText(nWoundDrainage3);
                sadd = new StringBuffer(
                        nWoundDrainage3 != null ? nWoundDrainage3 : "");
            } else {
                add2.setText("脑室引流（ml）");
                et_xuantian2.setText(nWoundDrainage3);
                sadd2 = new StringBuffer(
                        nWoundDrainage3 != null ? nWoundDrainage3 : "");
            }
        }
        if (map.get("WOUND_DRAINAGE4") != null) {

            CCLog.e("add item+>>>>>>>" + map.get("WOUND_DRAINAGE4").getVsId());
            nWoundDrainage4 = map.get("WOUND_DRAINAGE4").getVitalSignsValues();
            if (add1.getText().equals("增    加    项:")) {
                add1.setText("头皮下引流（ml）");
                et_xuantian.setText(nWoundDrainage4);
                sadd = new StringBuffer(
                        nWoundDrainage4 != null ? nWoundDrainage4 : "");
            } else {
                add2.setText("头皮下引流（ml）");
                et_xuantian2.setText(nWoundDrainage4);
                sadd2 = new StringBuffer(
                        nWoundDrainage4 != null ? nWoundDrainage4 : "");
            }
        }
        if (map.get("WOUND_DRAINAGE5") != null) {

            nWoundDrainage5 = map.get("WOUND_DRAINAGE5").getVitalSignsValues();
            if (add1.getText().equals("增    加    项:")) {
                add1.setText("硬膜下引流（ml）");
                et_xuantian.setText(nWoundDrainage5);
                sadd = new StringBuffer(
                        nWoundDrainage5 != null ? nWoundDrainage5 : "");
            } else {
                add2.setText("硬膜下引流（ml）");
                et_xuantian2.setText(nWoundDrainage5);
                sadd2 = new StringBuffer(
                        nWoundDrainage5 != null ? nWoundDrainage5 : "");
            }
        }
        if (map.get("WOUND_DRAINAGE6") != null) {

            nWoundDrainage6 = map.get("WOUND_DRAINAGE6").getVitalSignsValues();
            if (add1.getText().equals("增    加    项:")) {
                add1.setText("硬膜外引流（ml）");
                et_xuantian.setText(nWoundDrainage6);
                sadd = new StringBuffer(
                        nWoundDrainage6 != null ? nWoundDrainage6 : "");
            } else {
                add2.setText("硬膜外引流（ml）");
                et_xuantian2.setText(nWoundDrainage6);
                sadd2 = new StringBuffer(
                        nWoundDrainage6 != null ? nWoundDrainage6 : "");
            }
        }
        if (map.get("WOUND_DRAINAGE7") != null) {

            nWoundDrainage7 = map.get("WOUND_DRAINAGE7").getVitalSignsValues();
            if (add1.getText().equals("增    加    项:")) {
                add1.setText("鼻腔出血（ml）");
                et_xuantian.setText(nWoundDrainage7);
                sadd = new StringBuffer(
                        nWoundDrainage7 != null ? nWoundDrainage7 : "");
            } else {
                add2.setText("鼻腔出血（ml）");
                et_xuantian2.setText(nWoundDrainage7);
                sadd2 = new StringBuffer(
                        nWoundDrainage7 != null ? nWoundDrainage7 : "");
            }
        }
    }

    public Map<String, ModifyThreeTestBean> getModifyMap() {

        /**
         * 把增加项放入modMap
         */
        String addText1 = "";
        String addText2 = "";
        String addBtnStr1 = add1.getText().toString();
        String addBtnStr2 = add2.getText().toString();

        return modMap;
    }

    @Override
    protected void resetLayout(View view) {
        ScrollView root = (ScrollView) view.findViewById(R.id.past);
        SupportDisplay.resetAllChildViewParam(root);
    }

    private void addEditTextInList() {
        editList = ICUCommonMethod.addEditTextIntoList(et_weight, 5);
        editList = ICUCommonMethod.addEditTextIntoList(et_dabian1, 5);
        editList = ICUCommonMethod.addEditTextIntoList(et_dabian2, 5);
        editList = ICUCommonMethod.addEditTextIntoList(et_xiaobian1, 5);
        editList = ICUCommonMethod.addEditTextIntoList(et_xiaobian2, 5);
        editList = ICUCommonMethod.addEditTextIntoList(et_chushuiliang, 5);
        editList = ICUCommonMethod.addEditTextIntoList(et_rushuiliang, 5);
        editList = ICUCommonMethod.addEditTextIntoList(et_xuantian, 5);
        editList = ICUCommonMethod.addEditTextIntoList(et_xuantian2, 5);
    }
}
