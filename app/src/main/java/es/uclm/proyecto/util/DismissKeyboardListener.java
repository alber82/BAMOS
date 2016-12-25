package es.uclm.proyecto.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by alber on 03/05/2016.
 */
public class DismissKeyboardListener implements View.OnClickListener {

    Activity mAct;

    public DismissKeyboardListener(Activity act) {
        this.mAct = act;
    }

    @Override
    public void onClick(View v) {
        if ( v instanceof ViewGroup) {
            hideSoftKeyboard( this.mAct );
        }
    }
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager)
                mAct.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}

