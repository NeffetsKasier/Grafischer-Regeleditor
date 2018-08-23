package com.neffets.grafischerregeleditor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.neffets.grafischerregeleditor.db_modell.Brick_Function;
import com.neffets.grafischerregeleditor.db_modell.DatabaseHelper;
import com.neffets.grafischerregeleditor.db_modell.MyDbContentFiller;
import com.neffets.grafischerregeleditor.db_modell.Operator;
import com.neffets.grafischerregeleditor.db_modell.Rule;
import com.neffets.grafischerregeleditor.db_modell.Service;

import java.util.ArrayList;


public class Main extends AppCompatActivity {
    private Button bt_edit_rule, bt_create_rule;
    Rule selected_rule;
    Service selected_service;
    //DatenbankVariablen initialisieren
    DatabaseHelper db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(getApplicationContext());
        /*MyDbContentFiller fillMyDb = new MyDbContentFiller(db);
        fillMyDb.FillServicesAndRules();
        fillMyDb.FillBackend();*/
        //CreateButton zuweisen und OnClickListener um Dialog anzuzeigen
        bt_create_rule = findViewById(R.id.bt_create_rule);
        bt_create_rule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog_CreateNewRule();
            }
        });
        //EditButton zuweisen und OnClickListener um Dialog anzuzeigen
        bt_edit_rule = findViewById(R.id.bt_edit_rule);
        bt_edit_rule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                show_dialog_select_service();
            }
        });
    }

    //Settings Icon der Actionbar hinzufügen
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_settings,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_action_settings:
                showDialog_Settings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDialog_Settings(){
        DialogSettings dialogSettings = new DialogSettings();
        dialogSettings.show(getSupportFragmentManager(),"settings");
    }

    public void showDialog_CreateNewRule(){
        DialogCreateNewRule dialogCreateNewRule = new DialogCreateNewRule();
        dialogCreateNewRule.show(getSupportFragmentManager(),"createNewRule");
    }

    public void show_dialog_select_service(){
        final ArrayList<Service> service_array = db.getAllServices();
        ArrayAdapter operator_adapter = new ArrayAdapter<>(this, R.layout.textview_select_brick_function, service_array);
        android.app.AlertDialog select_service_dialog = new android.app.AlertDialog.Builder(this)
                .setTitle(R.string.input_select_service)
                .setIcon(R.drawable.ic_edit_black)
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Dialog schließen
                        dialog.dismiss();
                        //Abgebrochen als Toast Benachrichtigung in Main
                        Toast.makeText(getApplicationContext(),R.string.toast_hint_selection_aborted,Toast.LENGTH_SHORT).show();
                    }
                })
                .setAdapter(operator_adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        selected_service = service_array.get(whichButton);
                        show_dialog_select_rule();
                    }
                }).create();
        select_service_dialog.setCanceledOnTouchOutside(false);
        select_service_dialog.show();
    }

    public void show_dialog_select_rule() {
        final ArrayList<Rule> rule_array = db.getAllRulesInService(selected_service.getId());
        ArrayAdapter rule_adapter = new ArrayAdapter<>(this, R.layout.textview_select_brick_function, rule_array);
        android.app.AlertDialog select_rule_dialog = new android.app.AlertDialog.Builder(this)
                .setTitle(R.string.input_select_rule)
                .setIcon(R.drawable.ic_edit_black)
                .setNegativeButton(R.string.button_back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        show_dialog_select_service();
                    }
                })
                .setAdapter(rule_adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        selected_rule = rule_array.get(whichButton);
                        String text = getString(R.string.general_term_rule)+" '"+ selected_rule.getName()+"' "+getString(R.string.toast_phrase_now_editable);
                        Intent intent = new Intent(getApplicationContext(), EditRule.class);
                        intent.putExtra("Rule", selected_rule);
                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        select_rule_dialog.setCanceledOnTouchOutside(false);
        select_rule_dialog.show();
    }


}
