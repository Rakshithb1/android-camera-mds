package client;

import static client.FileChooserFragment.LAST_UPLOAD_DATE;
import static nprime.reg.mocksbi.constants.ClientConstants.FTM_KEY_ALIAS;
import static nprime.reg.mocksbi.constants.ClientConstants.FTM_KEY_STORE_PASSWORD;
import static nprime.reg.mocksbi.constants.ClientConstants.FTM_LAST_UPLOAD_DATE;
import static nprime.reg.mocksbi.constants.ClientConstants.DEVICE_USAGE;
import static nprime.reg.mocksbi.constants.ClientConstants.FACE_DEVICE_STATUS;
import static nprime.reg.mocksbi.constants.ClientConstants.FACE_RESPONSE_DELAY;
import static nprime.reg.mocksbi.constants.ClientConstants.FACE_SCORE;
import static nprime.reg.mocksbi.constants.ClientConstants.FINGER_DEVICE_STATUS;
import static nprime.reg.mocksbi.constants.ClientConstants.FINGER_RESPONSE_DELAY;
import static nprime.reg.mocksbi.constants.ClientConstants.FINGER_SCORE;
import static nprime.reg.mocksbi.constants.ClientConstants.IRIS_DEVICE_STATUS;
import static nprime.reg.mocksbi.constants.ClientConstants.IRIS_RESPONSE_DELAY;
import static nprime.reg.mocksbi.constants.ClientConstants.IRIS_SCORE;
import static nprime.reg.mocksbi.constants.ClientConstants.DEVICE_KEY_ALIAS;
import static nprime.reg.mocksbi.constants.ClientConstants.DEVICE_KEY_STORE_PASSWORD;
import static nprime.reg.mocksbi.constants.ClientConstants.DEVICE_LAST_UPLOAD_DATE;
import static nprime.reg.mocksbi.utility.DeviceConstants.DEFAULT_TIME_DELAY;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.slider.Slider;

import java.util.ArrayList;

import nprime.reg.mocksbi.R;
import nprime.reg.mocksbi.constants.ClientConstants;
import nprime.reg.mocksbi.utility.DateUtil;
import nprime.reg.mocksbi.utility.DeviceConstants;
import nprime.reg.mocksbi.utility.FileUtils;

/**
 * @author Anshul.Vanawat
 */

public class ConfigurationActivity extends AppCompatActivity {

    private static final String TAG = ConfigurationActivity.class.getName();
    private static final String LAST_UPDATE = "Last updated : ";

    private EditText device_keyAliasEditText;
    private EditText device_keyStorePasswordEditText;
    private EditText ftm_keyAliasEditText;
    private EditText ftm_keyStorePasswordEditText;
    private Slider faceSlider;
    private Slider fingerSlider;
    private Slider irisSlider;
    private TextView faceScoreTextView;
    private TextView fingerScoreTextView;
    private TextView irisScoreTextView;
    private Spinner faceDeviceStatus;
    private Spinner fingerDeviceStatus;
    private Spinner irisDeviceStatus;
    private EditText faceResponseDelayEditText;
    private EditText fingerResponseDelayEditText;
    private EditText irisResponseDelayEditText;
    private Spinner deviceUsageSpinner;

    private String device_currentKeyAlias;
    private String device_currentKeyPassword;
    private String ftm_currentKeyAlias;
    private String ftm_currentKeyPassword;
    private int currentFaceScore;
    private int currentFingerScore;
    private int currentIrisScore;
    private String device_lastUploadDate;
    private String ftm_lastUploadDate;
    private String currentFaceDeviceStatus;
    private String currentFingerDeviceStatus;
    private String currentIrisDeviceStatus;
    private String currentDeviceUsage;
    private int currentFaceResponseDelay;
    private int currentFingerResponseDelay;
    private int currentIrisResponseDelay;


    SharedPreferences sharedPreferences;
    private FileChooserFragment device_fileChooserFragment;
    private FileChooserFragment ftm_fileChooserFragment;
    DateUtil dateUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dateUtil = new DateUtil(this);

        device_keyAliasEditText = findViewById(R.id.device_key_alias);
        device_keyStorePasswordEditText = findViewById(R.id.device_key_store_password);
        ftm_keyAliasEditText = findViewById(R.id.ftm_key_alias);
        ftm_keyStorePasswordEditText = findViewById(R.id.ftm_key_store_password);

        faceSlider = findViewById(R.id.slider_face_score);
        fingerSlider = findViewById(R.id.slider_finger_score);
        irisSlider = findViewById(R.id.slider_iris_score);
        faceScoreTextView = findViewById(R.id.tx_face_score);
        fingerScoreTextView = findViewById(R.id.tx_finger_score);
        irisScoreTextView = findViewById(R.id.tx_iris_score);
        faceDeviceStatus = findViewById(R.id.face_device_status);
        fingerDeviceStatus = findViewById(R.id.finger_device_status);
        irisDeviceStatus = findViewById(R.id.iris_device_status);
        faceResponseDelayEditText = findViewById(R.id.face_response_delay_millis);
        fingerResponseDelayEditText = findViewById(R.id.finger_response_delay_millis);
        irisResponseDelayEditText = findViewById(R.id.iris_response_delay_millis);
        deviceUsageSpinner = findViewById(R.id.deviceUsage);

        ArrayList<String> deviceStatus = new ArrayList<>();
        deviceStatus.add(DeviceConstants.ServiceStatus.READY.getStatus());
        deviceStatus.add(DeviceConstants.ServiceStatus.BUSY.getStatus());
        deviceStatus.add(DeviceConstants.ServiceStatus.NOT_READY.getStatus());
        deviceStatus.add(DeviceConstants.ServiceStatus.NOT_REGISTERED.getStatus());

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deviceStatus);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        faceDeviceStatus.setAdapter(statusAdapter);
        fingerDeviceStatus.setAdapter(statusAdapter);
        irisDeviceStatus.setAdapter(statusAdapter);

        ArrayList<String> deviceUsage = new ArrayList<>();
        deviceUsage.add(DeviceConstants.DeviceUsage.Authentication.getDeviceUsage());
        deviceUsage.add(DeviceConstants.DeviceUsage.Registration.getDeviceUsage());

        ArrayAdapter<String> deviceUsageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deviceUsage);
        deviceUsageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        deviceUsageSpinner.setAdapter(deviceUsageAdapter);

        //default values
        device_currentKeyAlias = sharedPreferences.getString(DEVICE_KEY_ALIAS, "");
        device_currentKeyPassword = sharedPreferences.getString(DEVICE_KEY_STORE_PASSWORD, "");
        ftm_currentKeyAlias = sharedPreferences.getString(FTM_KEY_ALIAS, "");
        ftm_currentKeyPassword = sharedPreferences.getString(FTM_KEY_STORE_PASSWORD, "");
        currentFaceScore = sharedPreferences.getInt(FACE_SCORE, 30);
        currentFingerScore = sharedPreferences.getInt(FINGER_SCORE, 30);
        currentIrisScore = sharedPreferences.getInt(IRIS_SCORE, 30);
        device_lastUploadDate = sharedPreferences.getString(DEVICE_LAST_UPLOAD_DATE, "");
        ftm_lastUploadDate = sharedPreferences.getString(FTM_LAST_UPLOAD_DATE, "");
        currentFaceDeviceStatus = sharedPreferences.getString(FACE_DEVICE_STATUS, DeviceConstants.ServiceStatus.READY.getStatus());
        currentFingerDeviceStatus = sharedPreferences.getString(FINGER_DEVICE_STATUS, DeviceConstants.ServiceStatus.READY.getStatus());
        currentIrisDeviceStatus = sharedPreferences.getString(IRIS_DEVICE_STATUS, DeviceConstants.ServiceStatus.READY.getStatus());
        currentFaceResponseDelay = sharedPreferences.getInt(FACE_RESPONSE_DELAY, DEFAULT_TIME_DELAY);
        currentFingerResponseDelay = sharedPreferences.getInt(FINGER_RESPONSE_DELAY, DEFAULT_TIME_DELAY);
        currentIrisResponseDelay = sharedPreferences.getInt(IRIS_RESPONSE_DELAY, DEFAULT_TIME_DELAY);
        currentDeviceUsage = sharedPreferences.getString(DEVICE_USAGE, DeviceConstants.DeviceUsage.Registration.getDeviceUsage());

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        device_fileChooserFragment = (FileChooserFragment) fragmentManager.findFragmentById(R.id.device_fragmentContainerView);
        if (device_fileChooserFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(LAST_UPLOAD_DATE, device_lastUploadDate);
            device_fileChooserFragment.setArguments(bundle);
        }
        ftm_fileChooserFragment = (FileChooserFragment) fragmentManager.findFragmentById(R.id.ftm_fragmentContainerView);
        if (ftm_fileChooserFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(LAST_UPLOAD_DATE, ftm_lastUploadDate);
            ftm_fileChooserFragment.setArguments(bundle);
        }

        faceSlider.addOnChangeListener((slider, value, fromUser) -> {
            int intVal = (int) value;
            faceScoreTextView.setText(String.valueOf(intVal));
        });

        fingerSlider.addOnChangeListener((slider, value, fromUser) -> {
            int intVal = (int) value;
            fingerScoreTextView.setText(String.valueOf(intVal));
        });

        irisSlider.addOnChangeListener((slider, value, fromUser) -> {
            int intVal = (int) value;
            irisScoreTextView.setText(String.valueOf(intVal));
        });

        resetScreen();
    }

    public void onSave(View view) {
        Uri device_fileUri = device_fileChooserFragment.getSelectedUri();
        Uri ftm_fileUri = ftm_fileChooserFragment.getSelectedUri();

        if (device_fileUri != null && !saveFile(device_fileUri, ClientConstants.DEVICE_P12_FILE_NAME)) {
            Toast.makeText(this, "Failed to save reg p.12 file! Please try again.", Toast.LENGTH_LONG).show();
            return;
        } else {
            device_lastUploadDate = dateUtil.getDateTime(System.currentTimeMillis());
        }

        if (ftm_fileUri != null && !saveFile(ftm_fileUri, ClientConstants.FTM_P12_FILE_NAME)) {
            Toast.makeText(this, "Failed to save auth p.12 file! Please try again.", Toast.LENGTH_LONG).show();
            return;
        } else {
            ftm_lastUploadDate = dateUtil.getDateTime(System.currentTimeMillis());
        }

        device_currentKeyAlias = device_keyAliasEditText.getText().toString();
        device_currentKeyPassword = device_keyStorePasswordEditText.getText().toString();
        ftm_currentKeyAlias = ftm_keyAliasEditText.getText().toString();
        ftm_currentKeyPassword = ftm_keyStorePasswordEditText.getText().toString();
        currentFaceScore = (int) faceSlider.getValue();
        currentFingerScore = (int) fingerSlider.getValue();
        currentIrisScore = (int) irisSlider.getValue();
        currentFaceDeviceStatus = faceDeviceStatus.getSelectedItem().toString();
        currentFingerDeviceStatus = fingerDeviceStatus.getSelectedItem().toString();
        currentIrisDeviceStatus = irisDeviceStatus.getSelectedItem().toString();
        currentFaceResponseDelay = Integer.parseInt(faceResponseDelayEditText.getText().toString());
        currentFingerResponseDelay = Integer.parseInt(fingerResponseDelayEditText.getText().toString());
        currentIrisResponseDelay = Integer.parseInt(irisResponseDelayEditText.getText().toString());
        currentDeviceUsage = deviceUsageSpinner.getSelectedItem().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEVICE_KEY_ALIAS, device_currentKeyAlias);
        editor.putString(DEVICE_KEY_STORE_PASSWORD, device_currentKeyPassword);
        editor.putString(FTM_KEY_ALIAS, ftm_currentKeyAlias);
        editor.putString(FTM_KEY_STORE_PASSWORD, ftm_currentKeyPassword);
        editor.putInt(FACE_SCORE, currentFaceScore);
        editor.putInt(FINGER_SCORE, currentFingerScore);
        editor.putInt(IRIS_SCORE, currentIrisScore);
        editor.putString(DEVICE_LAST_UPLOAD_DATE, LAST_UPDATE + device_lastUploadDate);
        editor.putString(FTM_LAST_UPLOAD_DATE, LAST_UPDATE + ftm_lastUploadDate);
        editor.putString(FACE_DEVICE_STATUS, currentFaceDeviceStatus);
        editor.putString(FINGER_DEVICE_STATUS, currentFingerDeviceStatus);
        editor.putString(IRIS_DEVICE_STATUS, currentIrisDeviceStatus);
        editor.putInt(FACE_RESPONSE_DELAY, currentFaceResponseDelay);
        editor.putInt(FINGER_RESPONSE_DELAY, currentFingerResponseDelay);
        editor.putInt(IRIS_RESPONSE_DELAY, currentIrisResponseDelay);
        editor.putString(DEVICE_USAGE, currentDeviceUsage);

        editor.apply();

        //navigate back to previous activity
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onReset(View view) {
        resetScreen();
    }

    private void resetScreen() {
        device_keyAliasEditText.setText(device_currentKeyAlias);
        device_keyStorePasswordEditText.setText(device_currentKeyPassword);
        ftm_keyAliasEditText.setText(ftm_currentKeyAlias);
        ftm_keyStorePasswordEditText.setText(ftm_currentKeyPassword);
        faceSlider.setValue(currentFaceScore);
        fingerSlider.setValue(currentFingerScore);
        irisSlider.setValue(currentIrisScore);
        device_fileChooserFragment.resetSelection(device_lastUploadDate);
        ftm_fileChooserFragment.resetSelection(ftm_lastUploadDate);
        setSpinner(faceDeviceStatus, currentFaceDeviceStatus);
        setSpinner(fingerDeviceStatus, currentFingerDeviceStatus);
        setSpinner(irisDeviceStatus, currentIrisDeviceStatus);
        setSpinner(deviceUsageSpinner, currentDeviceUsage);
        faceResponseDelayEditText.setText(String.format("%d", currentFaceResponseDelay));
        fingerResponseDelayEditText.setText(String.format("%d", currentFingerResponseDelay));
        irisResponseDelayEditText.setText(String.format("%d", currentIrisResponseDelay));
    }

    private boolean saveFile(Uri fileUri, String fileName) {
        try {
            FileUtils.SaveFileInAppStorage(getApplicationContext(), fileUri, fileName);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e);
            return false;
        }
    }

    private void setSpinner(Spinner spinner, String value) {
        int position = ((ArrayAdapter<String>) spinner.getAdapter()).getPosition(value);
        spinner.setSelection(position);
    }
}