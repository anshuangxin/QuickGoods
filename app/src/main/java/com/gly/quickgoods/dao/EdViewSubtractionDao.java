package com.gly.quickgoods.dao;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by gly on 2017/9/23.
 * 根据两个Edittext的数字算出差值赋值给textview
 */

public class EdViewSubtractionDao {

    public void subtraction(final EditText bigeditText, final EditText small, final TextView result) {
        bigeditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    Integer kefu = Integer.valueOf(charSequence.toString());
                    Integer yingshou = Integer.valueOf(small.getText().toString());
                    result.setText(kefu - yingshou + "");
                } catch (Exception e) {
                    result.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        small.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    Integer kefu = Integer.valueOf(bigeditText.getText().toString());
                    Integer yingshou = Integer.valueOf(charSequence.toString());
                    result.setText(kefu - yingshou + "");
                } catch (Exception e) {
                    result.setText("0");
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}
