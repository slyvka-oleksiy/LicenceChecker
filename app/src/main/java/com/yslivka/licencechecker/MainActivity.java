package com.yslivka.licencechecker;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<LicenceEnum> {

    private static final int LICENCE_LOADER_ID = 0;
    private static final String LICENCE_KEY = "LICENCE_KEY";

    private TextView mLicenceTextView;
    private EditText mLicenceEditText;
    private Button mSubmitButton;
    private Button mResetButton;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLicenceTextView = (TextView) findViewById(R.id.licence_text_view);
        mLicenceEditText = (EditText) findViewById(R.id.licence_edit_text);
        mSubmitButton = (Button) findViewById(R.id.submit_buttom);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loader<LicenceEnum> loader;
                Bundle bundle = new Bundle();
                bundle.putString(LicenceCheckerAsyncLoader.LICENCE_KEY, mLicenceEditText.getText().toString());
                loader = getLoaderManager().restartLoader(LICENCE_LOADER_ID, bundle, MainActivity.this);
                loader.forceLoad();
            }
        });
        mResetButton = (Button) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLicenceTextView.setText("");
                mLicenceEditText.setText("");
                mSharedPreferencesEditor.putString(LICENCE_KEY, "").commit();
                mLicenceEditText.setVisibility(View.VISIBLE);
                mSubmitButton.setVisibility(View.VISIBLE);
            }
        });

        mSharedPreferences = getSharedPreferences(MainActivity.class.getSimpleName(), MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();

        String licenceKey = mSharedPreferences.getString(LICENCE_KEY, null);
        if (!TextUtils.isEmpty(licenceKey)) {
            mLicenceTextView.setText(licenceKey);
            mLicenceEditText.setVisibility(View.GONE);
            mSubmitButton.setVisibility(View.GONE);
        }

        Bundle bundle = new Bundle();
        bundle.putString(LicenceCheckerAsyncLoader.LICENCE_KEY, mLicenceEditText.getText().toString());
        getLoaderManager().initLoader(LICENCE_LOADER_ID, bundle, this);
    }

    @Override
    public Loader<LicenceEnum> onCreateLoader(int id, Bundle args) {
        Loader<LicenceEnum> loader = null;
        if (id == LICENCE_LOADER_ID) {
            loader = new LicenceCheckerAsyncLoader(this, args);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<LicenceEnum> loader, LicenceEnum result) {
        switch (result) {
            case TRIAL:
            case PRO:
                mLicenceTextView.setText(result.getLicence());
                mSharedPreferencesEditor.putString(LICENCE_KEY, result.getLicence()).commit();
                mLicenceEditText.setVisibility(View.GONE);
                mSubmitButton.setVisibility(View.GONE);
                break;
            case ERROR:
                mLicenceTextView.setText(result.getLicence());
                mSharedPreferencesEditor.putString(LICENCE_KEY, "").commit();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<LicenceEnum> loader) {
    }
}