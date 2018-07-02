package com.tonkia.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.sql.SQLOutput;

public class EditTextUtils {

    private static final int DECIMAL_DIGITS = 2;//小数的位数

    public static void setPoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + DECIMAL_DIGITS + 1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }
}
