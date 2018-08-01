package com.neffets.grafischerregeleditor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.neffets.grafischerregeleditor.db_modell.DatabaseHelper;
import com.neffets.grafischerregeleditor.db_modell.Frame;
import com.neffets.grafischerregeleditor.db_modell.Rule;
import com.neffets.grafischerregeleditor.db_modell.Service;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DialogCreateNewRule extends AppCompatDialogFragment {
    Rule selectedRule;
    Service selectedService;
    //DatenbankVariablen initialisieren
    DatabaseHelper db;
    //MyDbContentFiller fillMyDb;
    // ArrayList-Variablen für Autocomplete initialisieren
    ArrayList<Service> serviceList;
    ArrayList<Rule> ruleList;

    //Edittext-Variable für Autocomplete
    private AutoCompleteTextView autocomplete_selectOrEnterService;
    //Image-Variable für DropdownSelector
    private ImageView button_dropdown_service_selector;
    //EditText-Variablen für jeweiligen Setting Input
    private EditText input_enter_ruletitle;
    //Label-Variablen fur jeweiligen Input
    private TextInputLayout input_layout_enter_rule_title;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());
        serviceList = db.getAllServices();

        //Instanziert einen DialogBuilder
        AlertDialog.Builder createNewRuleBuilder = new AlertDialog.Builder(getActivity());
        //Instanziert einen LayoutInflater
        LayoutInflater createNewRuleInflater = getActivity().getLayoutInflater();

        //Rendert die Custom View dialog_create_new_rule.xmlDialog
        View createNewRuleView = createNewRuleInflater.inflate(R.layout.dialog_create_new_rule,null);

        //Input-Felder aus der View den EditText Variablen zuweisen
        input_enter_ruletitle = createNewRuleView.findViewById(R.id.input_enterRulename);

        //Labels für jeweiligen Input aus der View zuweisen
        input_layout_enter_rule_title = createNewRuleView.findViewById(R.id.inputLayout_enterRulename);


        //Input-Felder aus der View den EditText Variablen zuweisen
        autocomplete_selectOrEnterService = createNewRuleView.findViewById(R.id.input_selectOrEnterService);

        button_dropdown_service_selector = createNewRuleView.findViewById(R.id.bt_dropDownSelector);

        ArrayAdapter serviceAdapter = new ArrayAdapter<Service>(getContext(),android.R.layout.simple_dropdown_item_1line,serviceList);

       autocomplete_selectOrEnterService.setAdapter(serviceAdapter);


        button_dropdown_service_selector.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View v) {
                     autocomplete_selectOrEnterService.showDropDown();
            }
        });
        autocomplete_selectOrEnterService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedService = (Service) parent.getItemAtPosition(position);
            }
        });

                // Dialog mit AlertBuilder aufbauen
        createNewRuleBuilder.setView(createNewRuleView) //Layout Setzen
                .setTitle(R.string.dialog_title_new_rule) //Dialog Titel setzen
                .setIcon(R.drawable.ic_add_black_large) //Dialog Icon setzen
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
               .setPositiveButton(R.string.button_next, null);

        //Dialog erstellen
        AlertDialog createNewRuleDialog =  createNewRuleBuilder.create();
        createNewRuleDialog.setCanceledOnTouchOutside(false);

        //Verhalten des Positiv Buttons überschreiben, verhindert schließen des Dialogs bei Fehleingabe
        createNewRuleDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                Button bt_positive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                bt_positive.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        submitNewRule();
                    }
                });
            }
        });
        //Objekt Settings Dialog zurückgeben
        return createNewRuleDialog;
    }


    //Funktion um Eingaben mit Positive Button zu validieren
    private void submitNewRule() {
        if(!validateService()){
            return;
        }
        if(!validateRulename()){
            return;
        }
        //Dialog schließen, wenn alle Eingaben valide
        getDialog().dismiss();
        String text = getString(R.string.general_term_rule)+" '"+ selectedRule.getName()+"' "+getString(R.string.toast_phrase_now_editable);
        Intent intent = new Intent(getActivity(), EditRule.class);
        intent.putExtra("Service",selectedService);
        intent.putExtra("Rule",selectedRule);
        startActivity(intent);
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }

    // Funktion validiert eingegebenen User
    private boolean validateService(){
        //User aus Textinput lesen
        String newServiceName = autocomplete_selectOrEnterService.getEditableText().toString().trim();
        //RegEx Pattern für Username min 3 Zeichen: {A-Z | a-z | . | _ | -}
        Pattern regEx = Pattern.compile("[a-zA-Z0-9\\._\\-]{3,}");
        serviceList = db.getAllServices();
        for (Service existService : serviceList) {
                if (newServiceName.toLowerCase().equals(existService.getName().toLowerCase())) {
                    selectedService = existService;
                    return true;
                }
        }
        if(newServiceName.isEmpty()||!regEx.matcher(newServiceName).find()){
                autocomplete_selectOrEnterService.setError(getString(R.string.input_select_or_enter_service)); //Error Nachricht setzen
                autocomplete_selectOrEnterService.requestFocus();//Fokus auf Eingabefeld setzen
                return false;
        }else{
                Service newService = new Service(newServiceName);
                selectedService = db.addService(newService);
                String text = getString(R.string.toast_input_success_new_service)+" '"+newServiceName+"' "+getString(R.string.toast_phrase_was_created);
                Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
            return true;
        }


    }

    // Funktion validiert eingegebenen User
    private boolean validateRulename()  {
        //User aus Textinput lesen
        String rulename = input_enter_ruletitle.getText().toString().trim();
        ruleList = db.getAllRules();
        for(Rule existRule:ruleList){
            if(rulename.toLowerCase().equals(existRule.getName().toLowerCase())){
                input_layout_enter_rule_title.setError(getString(R.string.input_err_unique_enter_rulename)); //Error Nachricht setzen
                input_enter_ruletitle.requestFocus();//Fokus auf Eingabefeld setzen ;
                return false;
            }
        }
        //RegEx Pattern für Username min 3 Zeichen: {A-Z | a-z | . | _ | -}
        Pattern regEx = Pattern.compile("[a-zA-Z0-9\\._\\-]{3,}");
        // Prüfen ob User leer und mit RegEx übereinstimmt
        if(rulename.isEmpty()||!regEx.matcher(rulename).find()){
            input_layout_enter_rule_title.setError(getString(R.string.input_err_enter_rulename)); //Error Nachricht setzen
            input_enter_ruletitle.requestFocus();//Fokus auf Eingabefeld setzen
            return false;
        }else {
            Rule newRule = new Rule(rulename,selectedService);
            selectedRule  = db.addRule(newRule);
            return true;
        }
    }

}
