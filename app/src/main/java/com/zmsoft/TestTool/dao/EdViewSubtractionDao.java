package com.zmsoft.TestTool.dao;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by gly on 2017/9/23.
 * 根据两个Edittext的数字算出差值赋值给textview
 */

public class EdViewSubtractionDao {
    DecimalFormat df = new java.text.DecimalFormat("#.##");

    public void subtraction(final EditText bigeditText, final EditText small, final TextView result) {
        bigeditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    Double kefu = Double.parseDouble(charSequence.toString());
                    Double yingshou = Double.parseDouble(small.getText().toString());
                    Double cha = kefu - yingshou;
                    if (cha < 0) {
                        cha = 0d;
                    }
                    result.setText(String.valueOf(df.format(cha)));
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
                    Double kefu = Double.parseDouble(bigeditText.getText().toString());
                    Double yingshou = Double.parseDouble(charSequence.toString());
                    Double cha = kefu - yingshou;
                    if (cha < 0) {
                        cha = 0d;
                    }

                    result.setText(String.valueOf(df.format(cha) ));
                } catch (Exception e) {
                    result.setText("0");
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void subtraction(final EditText bigeditText, final EditText small, final TextView result, final Button button) {
        bigeditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    Double kefu = Double.parseDouble(charSequence.toString());
                    Double yingshou = Double.parseDouble(small.getText().toString());
                    Double cha = kefu - yingshou;
                    if (cha < 0) {
                        cha = 0d;
                        button.setEnabled(false);
                    } else {
                        button.setEnabled(true);

                    }
                    result.setText(String.valueOf(df.format(cha) ));
                } catch (Exception e) {
                    button.setEnabled(false);
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
                    Double kefu = Double.parseDouble(bigeditText.getText().toString());
                    Double yingshou = Double.parseDouble(charSequence.toString());
                    Double cha = kefu - yingshou;
                    if (cha < 0) {
                        cha = 0d;
                        button.setEnabled(false);
                    } else {
                        button.setEnabled(true);
                    }

                    result.setText(String.valueOf(df.format(cha)));
                } catch (Exception e) {
                    button.setEnabled(false);
                    result.setText(String.valueOf("0"));
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}
