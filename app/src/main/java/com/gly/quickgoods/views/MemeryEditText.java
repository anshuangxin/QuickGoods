package com.gly.quickgoods.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by gly on 2017/9/13.
 */

@SuppressLint("AppCompatCustomView")
public class MemeryEditText extends EditText {
    private static final String TEXT_KEY = "textkey";
    private SharedPreferences sp;

    public MemeryEditText(Context context) {
        this(context, null);
    }

    public MemeryEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MemeryEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        int id = getId();
        sp = context.getSharedPreferences(id + "", Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(getText().toString())) {
            setText(sp.getString(TEXT_KEY, ""));
        }

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (null != sp) {
            sp.edit().putString(TEXT_KEY, text.toString()).commit();
        }
    }
}
