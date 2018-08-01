package com.neffets.grafischerregeleditor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Pattern;

/* Klasse DialogSettings stellt einen Custom Dialog für die Gateway-Einstellungen zur Verfügung
 * erbt von AppCombatDialogFragment um auf bestehende Methoden & Style zuzugreifen
 * validiert Eingaben, speichert und ließt diese über SharedPrefences
 */
public class DialogSettings extends AppCompatDialogFragment {

    //String-Variablen für jeweilige Settings in SharedPreference (valuekey)
    private static final String KEY_OPENHAB_IP = "openhab_ip", KEY_OPENHAB_USER = "openhab_user", KEY_OPENHAB_PASSWORD = "openhab_password";

    //EditText-Variablen für jeweiligen Setting Input
    private EditText input_openhab_ip, input_openhab_user, input_openhab_password;

    //Label-Variablen fur jeweiligen Input
    private TextInputLayout input_layout_openhab_ip, inputLayout__layout_openhab_user, input_layout_openhab_password;

    //Konstruktor für Settings Dialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Instanziert einen DialogBuilder
        AlertDialog.Builder settingsBuilder = new AlertDialog.Builder(getActivity());

        //Instanziert einen LayoutInflater
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        //Rendert die Custom View dialog_settings.xml in den Dialog
        View SettingsView = layoutInflater.inflate(R.layout.dialog_settings,null);

        //Input-Felder aus der View den EditText Variablen zuweisen
        input_openhab_ip = SettingsView.findViewById(R.id.input_openhab_ip);
        input_openhab_user = SettingsView.findViewById(R.id.input_openhab_user);
        input_openhab_password = SettingsView.findViewById(R.id.input_openhab_password);

        //Labels für jeweiligen Input aus der View zuweisen
        input_layout_openhab_ip = SettingsView.findViewById(R.id.input_layout_openhab_ip);
        inputLayout__layout_openhab_user = SettingsView.findViewById(R.id.input_layout_openhab_user);
        input_layout_openhab_password = SettingsView.findViewById(R.id.input_layout_openhab_password);

        // GatewayIP aus SharedPreferences lesen und als Text in Inputfeld der View schreiben
        SharedPreferences sp_openhab_ip = Objects.requireNonNull(getContext()).getSharedPreferences(KEY_OPENHAB_IP, 0);
        input_openhab_ip.setText(sp_openhab_ip.getString(KEY_OPENHAB_IP,""));
        //Text Listener hinzufügen, um Errormessages direkt zu entfernen
        input_openhab_ip.addTextChangedListener(new InputChangesListener(input_openhab_ip));

        // GatewayUser aus SharedPreferences lesen und als Text in Inputfeld der View schreiben
        SharedPreferences sp_openhab_user = getContext().getSharedPreferences(KEY_OPENHAB_USER, 0);
        input_openhab_user.setText(sp_openhab_user.getString(KEY_OPENHAB_USER, ""));
        //Text Listener hinzufügen, um Errormessages direkt zu entfernen
        input_openhab_user.addTextChangedListener(new InputChangesListener(input_openhab_user));

        // GatewayPassword aus SharedPreferences lesen und als Text in Inputfeld der View schreiben
        SharedPreferences sp_gatewayPassword= getContext().getSharedPreferences(KEY_OPENHAB_PASSWORD, 0);
        input_openhab_password.setText(sp_gatewayPassword.getString(KEY_OPENHAB_PASSWORD, ""));
        //Text Listener hinzufügen, um Errormessages direkt zu entfernen
        input_openhab_password.addTextChangedListener(new InputChangesListener(input_openhab_password));

        // Dialog mit AlertBuilder aufbauen
        settingsBuilder.setView(SettingsView) //Layout Setzen
                .setTitle(R.string.dialog_title_settings) //Dialog Titel setzen
                .setIcon(R.drawable.ic_settings_black) //Dialog Icon setzen
                //Auszuführende Aktion und Text für Cancel festlegen
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Dialog schließen
                        dialog.dismiss();
                        //Abgebrochen als Toast Benachrichtigung in Main
                        Toast.makeText(getActivity(),R.string.toast_hint_input_aborted,Toast.LENGTH_SHORT).show();
                    }
                })
                //Text für Postive Button festlegen, StandartVerhalten wird später überschrieben
                .setPositiveButton(R.string.button_ok, null);

        //Dialog erstellen
        AlertDialog settingsDialog =  settingsBuilder.create();
        settingsDialog.setCanceledOnTouchOutside(false);
        //Verhalten des Positiv Buttons überschreiben, verhindert schließen des Dialogs bei Fehleingabe
        settingsDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                Button bt_positive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                bt_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitSettings();
                    }
                });
            }
        });

        //Objekt Settings Dialog zurückgeben
        return settingsDialog;
    }

    //Funktion um Eingaben mit Positive Button zu validieren
    private void submitSettings(){
        if(!validateIp()){
            Toast.makeText(getActivity(),R.string.toast_hint_check_inputs,Toast.LENGTH_SHORT).show();
            return;
        }
        if(!validateUser()){
            Toast.makeText(getActivity(),R.string.toast_hint_check_inputs,Toast.LENGTH_SHORT).show();
            return;
        }
        if(!validatePassword()){
            Toast.makeText(getActivity(),R.string.toast_hint_check_inputs,Toast.LENGTH_SHORT).show();
            return;
        }
        //Dialog schließen, wenn alle Eingaben valide
        getDialog().dismiss();
        Toast.makeText(getActivity(),R.string.toast_hint_input_saved,Toast.LENGTH_SHORT).show();
    }

    // Funktion validiert eingegebene IPv4 Adresse
    private boolean validateIp(){
        //IP aus Textinput lesen
        String ipAddr = input_openhab_ip.getText().toString().trim();

        //RegEx Pattern für IPv4
        Pattern regEx = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");

        // Prüfen ob IP-Adresse leer und mit RegEx übereinstimmt
        if(ipAddr.isEmpty()||!regEx.matcher(ipAddr).find()){
            input_layout_openhab_ip.setError(getString(R.string.input_err_openhab_ip)); //Error Nachricht setzen
            input_openhab_ip.requestFocus(); //Fokus auf Eingabefeld setzen
            return false;
        }else {
            SaveToSharedPrefs(KEY_OPENHAB_IP,ipAddr); //Wenn valide in SharedPrefs speichern
            input_layout_openhab_ip.setErrorEnabled(false); //Errornachricht entfernen
        }
        return true;
    }

    // Funktion validiert eingegebenen User
    private boolean validateUser(){
        //User aus Textinput lesen
        String user = input_openhab_user.getText().toString().trim();

        //RegEx Pattern für Username min 3 Zeichen: {A-Z | a-z | . | _ | -}
        Pattern regEx = Pattern.compile("[a-zA-Z0-9._\\-]{3,}");

        // Prüfen ob User leer und mit RegEx übereinstimmt
        if(user.isEmpty()||!regEx.matcher(user).find()){
            inputLayout__layout_openhab_user.setError(getString(R.string.input_err_openhab_user)); //Error Nachricht setzen
            input_openhab_user.requestFocus();//Fokus auf Eingabefeld setzen
            return false;
        }else {
            SaveToSharedPrefs(KEY_OPENHAB_USER,user);//Wenn valide in SharedPrefs speichern
            inputLayout__layout_openhab_user.setErrorEnabled(false);//Errornachricht entfernen
        }
        return true;
    }

    // Funktion validiert eingegebenes Passwort
    private boolean validatePassword(){
        //Passwort aus Textinput lesen
        String password = input_openhab_password.getText().toString().trim();

        //Prüfen ob Passwort leer oder weniger als 3 Zeichen hat
        if(password.isEmpty()||password.length() < 3){
            input_layout_openhab_password.setError(getString(R.string.input_err_openhab_password));//Error Nachricht setzen
            input_openhab_password.requestFocus();//Fokus auf Eingabefeld setzen
            return false;
        }else {
            SaveToSharedPrefs(KEY_OPENHAB_PASSWORD,password);//Wenn valide in SharedPrefs speichern
            input_layout_openhab_password.setErrorEnabled(false);//Errornachricht entfernen
        }
        return true;
    }

    //Funktion speichert Eingabe in SharedPreferences
    private void SaveToSharedPrefs(String valkey, String value){
        //Editor für entsprechendes ValueKey starten
        SharedPreferences.Editor edit_sharedPref = Objects.requireNonNull(getContext()).getSharedPreferences(valkey,0).edit();
        //Übergebenen Wert schreiben
        edit_sharedPref.putString(valkey,value);
        //Änderungen bestätigen
        edit_sharedPref.apply();
    }



    private class InputChangesListener implements TextWatcher{
        private View view;

        private InputChangesListener(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_openhab_ip:
                    validateIp();
                    break;
                case R.id.input_openhab_user:
                    validateUser();
                    break;
                case R.id.input_openhab_password:
                    validatePassword();
                    break;
            }
        }
    }
}


