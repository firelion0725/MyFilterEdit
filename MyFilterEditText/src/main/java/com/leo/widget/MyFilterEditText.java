package com.leo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * @author leo, ZhangWei
 * @date 2018/4/25
 * @function 定制可自动过滤的EditText
 */
public class MyFilterEditText extends AppCompatEditText {

    private static final int NUMBER_DECIMAL = 8194;
    private static final String DECIMAL = ".";

    static class LoginFilterValue {
        static final char ZERO = '0';
        static final char NINE = '9';
        static final char CAPITAL_A = 'A';
        static final char CAPITAL_Z = 'Z';
        static final char LOWER_CAPITAL_A = 'a';
        static final char LOWER_CAPITAL_Z = 'z';
        static final char CHINESE_START = '\u4e00';
        static final char CHINESE_END = '\u9fa5';
        static final char INTERVAL = '·';
    }

    /**
     * 不过滤
     */
    private static final int NONE_DEFAULT = 0;
    /**
     * 过滤空格
     */
    private static final int SPACE_FILTER = 1;
    /**
     * 过滤特殊字符包括空格（只有中文 英文大小写及"·" 可以通过）
     */
    private static final int SPECIAL_CHARACTERS_FILTER = 2;
    /**
     * 小数位数过滤器（保留两位）
     */
    private static final int NUMBER_DECIMAL_FILTER = 4;
    //private static final int BORDER_LEFT = 8;

    private int[] filterNums = {SPACE_FILTER, SPECIAL_CHARACTERS_FILTER, NUMBER_DECIMAL_FILTER};

    /**
     * 当前值
     */
    private int myFilterNum = NONE_DEFAULT;

    List<InputFilter> filterList = new ArrayList<>();

    public MyFilterEditText(Context context) {
        super(context);
        initEdit(context, null);
    }

    public MyFilterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEdit(context, attrs);
    }

    public MyFilterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEdit(context, attrs);
    }


    InputFilter decimalDigitsFilter = (source, start, end, dest, start2, end2) -> {
        String destString = String.valueOf(dest);
        if (getInputType() == NUMBER_DECIMAL) {
            if (destString.contains(DECIMAL)) {
                int bitPos = destString.indexOf(DECIMAL);
                int numOfBits = destString.length() - bitPos;
                if (numOfBits > 2) {
                    return "";
                }
            }
        }
        return source;
    };


    InputFilter inputSpaceFilter = (source, start, end, dest, start2, end2) -> TextUtils.equals(source, " ") ? "" : null;

    LoginFilter inputSpecialCharacters = new LoginFilter.UsernameFilterGeneric() {
        @Override
        public boolean isAllowed(char c) {
            if (LoginFilterValue.ZERO <= c && c <= LoginFilterValue.NINE) {
                return true;
            }
            if (LoginFilterValue.LOWER_CAPITAL_A <= c && c <= LoginFilterValue.LOWER_CAPITAL_Z) {
                return true;
            }
            if (LoginFilterValue.CAPITAL_A <= c && c <= LoginFilterValue.CAPITAL_Z) {
                return true;
            }
            if (LoginFilterValue.CHINESE_START <= c && c <= LoginFilterValue.CHINESE_END) {
                return true;
            }
            if (c == LoginFilterValue.INTERVAL) {
                return true;
            }
            return false;
        }
    };

    /**
     * 判断输入的字符中是否有特殊字符
     *
     * @return true:包含特殊字符 false ：不包含
     */
    public boolean isHasSpecialCharacters() {
        return getText().toString().replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0;
    }

    private void initEdit(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyFilterEditText);
            myFilterNum = typedArray.getInteger(R.styleable.MyFilterEditText_filter, NONE_DEFAULT);
            typedArray.recycle();
        }
        initFilters();
        setFilter();
    }

    private void initFilters() {
        //通过循环遍历 过滤器数组 查看哪些过滤器被添加 并设置过滤器
        for (int filterNumber : filterNums) {
            if (containsFlag(myFilterNum, filterNumber)) {
                addDefaultFilter(filterNumber);
            }
        }
        setFilter();
    }

    /**
     * 设置过滤器
     */
    private void setFilter() {
        InputFilter[] inputFilters = filterList.toArray(new InputFilter[filterList.size()]);
        setFilters(inputFilters);
    }

    /**
     * 给外部添加自定义过滤器的方法
     */
    public void addFilter(InputFilter inputFilter) {
        filterList.add(inputFilter);
        setFilter();
    }

    /**
     * 根据XML 传入的参数加入默认模式下的过滤器
     *
     * @param filterNum 过滤器Num
     */
    private void addDefaultFilter(int filterNum) {
        InputFilter inputFilter = null;
        switch (filterNum) {
            case SPACE_FILTER:
                inputFilter = inputSpaceFilter;
                break;
            case SPECIAL_CHARACTERS_FILTER:
                inputFilter = inputSpecialCharacters;
                break;
            case NUMBER_DECIMAL_FILTER:
                inputFilter = decimalDigitsFilter;
                break;
            default:
                break;
        }

        if (inputFilter != null) {
            filterList.add(inputFilter);
        }
    }

    /**
     * 将指定的字符串转换成制定小数点位数的double
     */
    public static String formatChangeToDouble(String value) {
        BigDecimal bd1 = new BigDecimal(value);
        bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd1.toString();
    }

    //-------------flag 方法---------------------

    /**
     * flagSet是否包含flag
     */
    private boolean containsFlag(int flagSet, int flag) {
        return (flagSet | flag) == flagSet;
    }

    /**
     * flagSet添加flag
     */
    private int addFlag(int flagSet, int flag) {
        return flagSet | flag;
    }

    /**
     * flagSet反转成flag
     */
    private int toggleFlag(int flagSet, int flag) {
        return flagSet ^ flag;
    }

    /**
     * flagSet移除flag
     */
    private int removeFlag(int flagSet, int flag) {
        return flagSet & (~flag);
    }


}
