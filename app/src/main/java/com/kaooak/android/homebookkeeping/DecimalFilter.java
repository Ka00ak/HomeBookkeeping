package com.kaooak.android.homebookkeeping;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalFilter implements InputFilter {

    private int numBefore;
    private int numAfter;

    public DecimalFilter(int numBefore, int numAfter) {
        this.numBefore = numBefore;
        this.numAfter = numAfter;
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {

        String spannedStr = spanned.toString();

        String spanned1 = spannedStr.substring(0, i2);
        String charStr = charSequence.toString();
        String spanned2 = spannedStr.substring(i3, spannedStr.length());

        String str = spanned1 + charStr + spanned2;

        if (str.equals(".")) {
            return "0.";
        } else {
            String strs[] = str.split("[.,]");
            if (strs.length == 2) {
                if (strs[0].length() > numBefore)
                    return "";
                if (strs[1].length() > numAfter)
                    return "";
            }
            return null;
        }
    }
}
