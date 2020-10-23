package com.example.emediconn.Extras;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.emediconn.R;

public class GenericTextWatcher3 implements TextWatcher {
    private final EditText[] editText;
    private View view;
    public GenericTextWatcher3(View view, EditText editText[])
    {
        this.editText = editText;
        this.view = view;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        switch (view.getId()) {

            case R.id.editTextone_one1:
                if (text.length() == 1)
                    editText[1].requestFocus();
                break;
            case R.id.editTexttwo_two2:

                if (text.length() == 1)
                    editText[2].requestFocus();
                else if (text.length() == 0)
                    editText[0].requestFocus();
                break;
            case R.id.editTextthree_three3:
                if (text.length() == 1)
                    editText[3].requestFocus();
                else if (text.length() == 0)
                    editText[1].requestFocus();
                break;
            case R.id.editTextfour_four4:
                if (text.length() == 1)
                    editText[4].requestFocus();
                else if (text.length() == 0)
                    editText[2].requestFocus();
                break;
            case R.id.editTextfive_five5:
                if (text.length() == 1)
                    editText[5].requestFocus();
                else if (text.length() == 0)
                    editText[3].requestFocus();
                break;
            case R.id.editTextsix_six6:
                if (text.length() == 0)
                    editText[4].requestFocus();
                break;
        }
    }
}
