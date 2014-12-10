package com.yslivka.licencechecker;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

public class LicenceCheckerAsyncLoader extends AsyncTaskLoader<LicenceEnum> {
    final int PAUSE = 2;
    public final static String LICENCE_KEY = "licence_key";

    private String licence;

    public LicenceCheckerAsyncLoader(Context context, Bundle args) {
        super(context);
        if (args != null) {
            licence = args.getString(LICENCE_KEY);
        }
    }

    @Override
    public LicenceEnum loadInBackground() {
        try {
            TimeUnit.SECONDS.sleep(PAUSE);
            if (TextUtils.equals(licence, getContext().getResources().getString(R.string.trial))) {
                return LicenceEnum.TRIAL;
            } else if (TextUtils.equals(licence, getContext().getResources().getString(R.string.pro))) {
                return LicenceEnum.PRO;
            }
            return LicenceEnum.ERROR;
        } catch (InterruptedException e) {
            return null;
        }
    }

}