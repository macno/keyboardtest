package com.example.macno.keyboardtest;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private InputMethodManager inputMethodManager;

    private ConstraintLayout mView;

    private EditText mEditTextHidden;

    //private boolean mEditing = false;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("TEST", "onTextChanged: " + s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = (ConstraintLayout)findViewById(R.id.cl_main);


        attachHiddenEditText();

        findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKeyboard("");
            }
        });

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        closeKeyboard();
    }

    @Override
    protected void onPause() {
        closeKeyboard();
        super.onPause();
    }

    private void attachHiddenEditText() {
        mEditTextHidden = new EditText(this);
        mEditTextHidden.setLines(1);
        mEditTextHidden.setSingleLine(true);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(0,0);
        mView.addView(mEditTextHidden, mView.getChildCount(), params);

        inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    private void openKeyboard(String prevText) {
        mEditTextHidden.setFocusable(true);
        mEditTextHidden.setFocusableInTouchMode(true);
        mEditTextHidden.requestFocus();
        mEditTextHidden.setText(prevText);
        mEditTextHidden.addTextChangedListener(mTextWatcher);
        mEditTextHidden.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("Test", "onKey -> Event Action: " + event.getAction() + " keyCode: " + keyCode );
                if(event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER) {
                        closeKeyboard();
                        return true;
                    }
                }
                return false;
            }
        });
        //mEditing = true;
        inputMethodManager.showSoftInput(mView, InputMethodManager.SHOW_FORCED);
        IBinder token = mView.getApplicationWindowToken();
        inputMethodManager.showSoftInputFromInputMethod(token,
                InputMethodManager.SHOW_FORCED);
        inputMethodManager.toggleSoftInputFromWindow(token,
                InputMethodManager.SHOW_FORCED, 0);
    }

    private void closeKeyboard() {
        //mEditing = false;
        mEditTextHidden.setFocusable(false);
        mEditTextHidden.setFocusableInTouchMode(false);
        mEditTextHidden.clearFocus();
        mEditTextHidden.removeTextChangedListener(mTextWatcher);
        mEditTextHidden.setOnKeyListener(null);
        mEditTextHidden.setText("");
        inputMethodManager.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

}
