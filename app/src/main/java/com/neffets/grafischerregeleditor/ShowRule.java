package com.neffets.grafischerregeleditor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.neffets.grafischerregeleditor.db_modell.Action;
import com.neffets.grafischerregeleditor.db_modell.DatabaseHelper;
import com.neffets.grafischerregeleditor.db_modell.Frame;
import com.neffets.grafischerregeleditor.db_modell.Precondition;
import com.neffets.grafischerregeleditor.db_modell.Rule;
import com.neffets.grafischerregeleditor.db_modell.Type;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class ShowRule extends AppCompatActivity implements Serializable {
    Rule selected_rule;
    DatabaseHelper db;
    String generated_rule_file = "",generated_when="",generated_if = "", generated_then = "";
    TextView generated_rule_view;
    Boolean firstPrecondition = true, firstAction = true, firstFrame=true, first_if = true;
    ArrayList<Frame> frames;
    ArrayList<Action> actions;
    ArrayList<Precondition> preconditions;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rule);
        selected_rule = (Rule) getIntent().getSerializableExtra("Rule");
        String title = selected_rule.getService().toString()+": "+ selected_rule.toString();
        setTitle(title);
        generated_rule_view = findViewById(R.id.generated_rule_view);
        generated_rule_file = generated_rule_file+"rule '"+selected_rule.toString()+"'\n";

        db = new DatabaseHelper(getApplicationContext());
        generated_when = generated_when+"when\n";

        frames = db.getAllFrames(selected_rule.getId());
        actions = db.getAllActions(selected_rule.getId());


        for(Frame f:frames){
            preconditions = db.getAllPreconditions(f.getId());
            if(!preconditions.isEmpty()){
                if(firstFrame){
                    firstFrame=false;
                    generated_if = generated_if+"if(";
                }else{
                    generated_if = generated_if+" || \n \t";
                    first_if = true;
                }
            }
            for(Precondition p:preconditions){
                if(firstPrecondition) {
                    firstPrecondition = false;
                    generated_when = generated_when+"\t";
                }else{
                    generated_when = generated_when+" or \n \t";

                }
                if(first_if){
                    first_if = false;
                }else{
                    generated_if = generated_if+" && ";
                }

                String type = p.getBrick_function().getFunction().getType().getName();
                String value = p.getValue();
                String openhab_name =  p.getBrick_function().getOpenhab_name();
                String operator = p.getOperator().getCharacter();
                generated_when = generated_when+" "+openhab_name+" CHANGED";
                if(type.equals("Switch")){
                    String min = p.getBrick_function().getFunction().getMin_value();
                    String max = p.getBrick_function().getFunction().getMax_value();
                    if(value.equals(max)){
                        generated_when = generated_when+" FROM "+min+" TO "+max;
                        generated_if = generated_if + openhab_name+".state"+operator+value;
                    }else if(value.equals(min)) {
                        generated_when = generated_when + " FROM " + max + " TO " + min;
                        generated_if = generated_if + openhab_name+".state"+operator+value;
                    }else if (value.equals("Switch")){
                        generated_if = generated_if +openhab_name+".state"+operator+"("+min+"||"+max+")";
                    }
                }else{
                    generated_if = generated_if +openhab_name+".state"+operator+value;
                }

            }
        }
        generated_if = generated_if+")";
        generated_then = generated_then+"\nthen\n";
        actions = db.getAllActions(selected_rule.getId());
        for(Action a:actions){
            if(firstAction){
                firstAction=false;
                generated_then = generated_then + "\t"+generated_if+"{";
            }

            String type = a.getBrick_function().getFunction().getType().getName();
            String value = a.getValue();
            String openhab_name =  a.getBrick_function().getOpenhab_name();
            boolean action_generated = false;
            if(type.equals("Switch")){
                if(value.equals("Switch")) {
                    String min = a.getBrick_function().getFunction().getMin_value();
                    String max = a.getBrick_function().getFunction().getMax_value();
                    generated_then = generated_then+"\n\t\tif("+openhab_name+".state=="+min+"){"+openhab_name+".sendCommand("+max+")}else{"+openhab_name+".sendCommand("+min+")};";
                    action_generated = true;
                }
            }
            if(!action_generated) {
                generated_then = generated_then + "\n\t\t" + openhab_name + ".sendComand(" + value + ");";
            }
        }
        generated_then=generated_then+"\n\t}";
        generated_rule_file = generated_rule_file+generated_when+generated_then;

        generated_rule_view.setText(generated_rule_file.toString());
    }
}
