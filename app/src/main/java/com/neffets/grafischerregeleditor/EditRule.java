package com.neffets.grafischerregeleditor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.neffets.grafischerregeleditor.db_modell.Action;
import com.neffets.grafischerregeleditor.db_modell.Brick;
import com.neffets.grafischerregeleditor.db_modell.Brick_Function;
import com.neffets.grafischerregeleditor.db_modell.DatabaseHelper;
import com.neffets.grafischerregeleditor.db_modell.Frame;
import com.neffets.grafischerregeleditor.db_modell.Group;
import com.neffets.grafischerregeleditor.db_modell.Operator;
import com.neffets.grafischerregeleditor.db_modell.Precondition;
import com.neffets.grafischerregeleditor.db_modell.Rule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class EditRule extends AppCompatActivity implements Serializable{
    Rule selected_rule;
    //DatenbankVariablen initialisieren
    DatabaseHelper db;
    // ArrayList-Variablen für Autocomplete initialisieren
    ArrayList<Brick> brick_array;
    ArrayList<Group> group_array;
    Adapter_Brick brick_adapter;
    AutoCompleteTextView autocomplete_group_filter;
    //Image-Variablen für DropdownSelector
    ImageView bt_dropdown_group_filter;
    Button bt_new_or_frame,bt_generate_rule;
    GridView all_bricks_view;
    LinearLayout precondition_view,action_view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rule);
        selected_rule = (Rule) getIntent().getSerializableExtra("Rule");
        String title = selected_rule.getService().toString()+": "+ selected_rule.toString();
        setTitle(title);
        //Array mit allen Bricks aus DB füllen
        db = new DatabaseHelper(getApplicationContext());
        brick_array = db.getAllBricks();
        //Bricks in GridView füllen
        all_bricks_view = findViewById(R.id.all_bricks_view);
        brick_adapter = new Adapter_Brick(this, brick_array);
        all_bricks_view.setAdapter(brick_adapter);
        final Group show_all = new Group("Alle Anzeigen");
        group_array = new ArrayList<>();
        group_array.add(show_all);
        group_array.addAll(db.getAllGroups());
        ArrayAdapter group_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,group_array);
        autocomplete_group_filter = findViewById(R.id.autocomplete_group_filter);
        autocomplete_group_filter.setAdapter(group_adapter);
        bt_dropdown_group_filter = findViewById(R.id.bt_dropdown_group_filter);

        bt_dropdown_group_filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                autocomplete_group_filter.showDropDown();
            }
        });
        autocomplete_group_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group selected_group = (Group) parent.getItemAtPosition(position);
                if(selected_group.equals(show_all)){
                    autocomplete_group_filter.setText("");
                    brick_array = db.getAllBricks();
                    brick_adapter.brick_array = brick_array;
                }else {
                    brick_array = db.getAllBricksInGroup(selected_group.getId());
                    brick_adapter.brick_array = brick_array;
                }
                all_bricks_view.setAdapter(brick_adapter);
            }
        });

        getFunctionsFromDb();

        bt_new_or_frame = findViewById(R.id.bt_or);
        bt_new_or_frame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNewFrame();
            }
        });

        getActionsFromDb();

        bt_generate_rule = findViewById(R.id.bt_generate_rule);
        bt_generate_rule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<Frame> frame_array = db.getAllFrames(selected_rule.getId());
                boolean preconditions_empty = true;
                for(Frame f: frame_array) {
                    ArrayList<Precondition> preconditions_array = db.getAllPreconditions(f.getId());
                    if(preconditions_empty) {
                        if (!preconditions_array.isEmpty()) {
                            preconditions_empty = false;
                        }
                    }
                }
                if(preconditions_empty){
                    Toast.makeText(getApplicationContext(),"Es muss mindestens eine Bedingung geben",Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<Action> actions = db.getAllActions(selected_rule.getId());
                if(actions.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Es muss mindestens eine Aktion geben",Toast.LENGTH_SHORT).show();
                    return;
                }
                upload_service_file();
               // Intent intent = new Intent(getApplicationContext(), ShowRule.class);
                // intent.putExtra("Rule",selected_rule);
                // startActivity(intent);
            }
        });

    }

    //Delete Icon der Actionbar hinzufügen
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_delete,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_delete_rule:
                showDialog_DeleteRule();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getFunctionsFromDb() {
        precondition_view = findViewById(R.id.precondition_view);
        ArrayList<Frame> frames = db.getAllFrames(selected_rule.getId());
        boolean first_frame = true;
        for (Frame f : frames) {
            LinearLayout frame_layout;
            if(first_frame) {
                frame_layout = createFrameLayout(getText(R.string.title_precondition_if), "Precondition_" + f.getId(), getText(R.string.frame_description_precondition));
                first_frame = false;
            }else
                frame_layout = createFrameLayout(getText(R.string.title_precondition_or), "Precondition_" + f.getId(), getText(R.string.frame_description_precondition));
            precondition_view.addView(frame_layout);
            LinearLayout frame_box = frame_layout.findViewById(R.id.frame_box);
            ArrayList<Precondition> preconditions = db.getAllPreconditions(f.getId());
            for (Precondition p : preconditions) {
                addBrickFunctionToFrame(p, frame_box);
            }

        }
    }
    public void getActionsFromDb(){
        action_view = findViewById(R.id.action_view);

       LinearLayout frame_layout = createFrameLayout(getText(R.string.title_action_frame),"Action",getText(R.string.frame_description_action));
       action_view.addView(frame_layout);
       LinearLayout frame_box = frame_layout.findViewById(R.id.frame_box);
       ArrayList<Action> actions = db.getAllActions(selected_rule.getId());
        for (Action a:actions){
            addBrickFunctionToFrame(a,frame_box);
        }

    }

    public LinearLayout createFrameLayout(CharSequence p_frame_title, CharSequence p_frame_tag, CharSequence p_frame_description) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        LinearLayout new_frame = (LinearLayout) layoutInflater.inflate(R.layout.linearlayout_frame, null);
        TextView frame_title = new_frame.findViewById(R.id.frame_title);
        LinearLayout frame_box = new_frame.findViewById(R.id.frame_box);
        TextView frame_description_text = new_frame.findViewById(R.id.frame_description_text);
        frame_title.setText(p_frame_title);
        frame_description_text.setText(p_frame_description);
        frame_box.setTag(p_frame_tag);
        frame_box.setOnDragListener(new BrickDragListener());
        return new_frame;
    }
    public void addNewFrame(){
        ArrayList<Frame> frame_array = db.getAllFrames(selected_rule.getId());
        for(Frame f: frame_array){
            ArrayList<Precondition> preconditions_array = db.getAllPreconditions(f.getId());
            if(preconditions_array.isEmpty()){
                Toast.makeText(getApplicationContext(),"Es darf keine Bedingung leer sein",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Frame new_frame = db.addFrame(new Frame(selected_rule));
        precondition_view.addView(createFrameLayout(getText(R.string.title_precondition_or),"Precondition_"+new_frame.getId(),getText(R.string.frame_description_precondition)));
    }

    class BrickDragListener implements View.OnDragListener {
        Drawable enterShape = getDrawable(R.drawable.shape_dropfield_rectangle_highlight);
        Drawable normalShape = getDrawable(R.drawable.shape_dropfield_rectangle_normal);

        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View dragged_Brick = (View) event.getLocalState();
                    LinearLayout frameLayout = (LinearLayout) v;
                    Brick brick = db.getBrick(Long.parseLong(dragged_Brick.getTag().toString().substring(6)));
                    show_dialog_choose_function(brick,frameLayout);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(normalShape);
                default:
                    break;
            }
            return true;
        }
    }
    class DeleteOnLongClickListener implements View.OnLongClickListener{
        public boolean onLongClick(View clicked_brick) {
            ViewGroup frame_layout = (ViewGroup) clicked_brick.getParent();
            String frame_tag = frame_layout.getTag().toString();
            if(frame_tag.equals("Action")){
                Action function = db.getAction(Long.parseLong(clicked_brick.getTag().toString().substring(7)));
                db.deleteAction(function.getId());
                frame_layout.removeView(clicked_brick);
                ArrayList<Action> actions = db.getAllActions(selected_rule.getId());
                if(actions.isEmpty()){
                    ViewGroup frame_layout_parent = (ViewGroup) frame_layout.getParent();
                    LinearLayout frame_description = frame_layout_parent.findViewById(R.id.frame_description);
                    frame_description.findViewById(R.id.frame_description_icon).setVisibility(View.VISIBLE);
                    TextView frame_description_text = frame_description.findViewById(R.id.frame_description_text);
                    frame_description_text.setText(getText(R.string.frame_description_action));
                }
            }else{
                Precondition function = db.getPrecondition(Long.parseLong(clicked_brick.getTag().toString().substring(13)));
                db.deletePrecondition(function.getId());
                frame_layout.removeView(clicked_brick);
                ArrayList<Precondition> preconditions = db.getAllPreconditions(function.getFrame().getId());
                if (preconditions.isEmpty()){
                    ArrayList<Frame> frames = db.getAllFrames(selected_rule.getId());
                    if (frames.size()>1){
                        db.deleteFrame(function.getFrame().getId());
                        ViewGroup frame_layout_parent = (ViewGroup) frame_layout.getParent();
                        ViewGroup frame_layout_grany = (ViewGroup) frame_layout_parent.getParent();
                        frame_layout_grany.removeView(frame_layout_parent);
                    }else if(frames.size()==1){
                        ViewGroup frame_layout_parent = (ViewGroup) frame_layout.getParent();
                        LinearLayout frame_description = frame_layout_parent.findViewById(R.id.frame_description);
                        frame_description.findViewById(R.id.frame_description_icon).setVisibility(View.VISIBLE);
                        TextView frame_description_text = frame_description.findViewById(R.id.frame_description_text);
                        frame_description_text.setText(getText(R.string.frame_description_precondition));
                    }
                }else {
                    ArrayList<Precondition> preconditions_in_frame = db.getAllPreconditions(function.getFrame().getId());
                    if(preconditions_in_frame.isEmpty()){
                        ViewGroup frame_layout_parent = (ViewGroup) frame_layout.getParent();
                        LinearLayout frame_description = frame_layout_parent.findViewById(R.id.frame_description);
                        frame_description.findViewById(R.id.frame_description_icon).setVisibility(View.VISIBLE);
                        TextView frame_description_text = frame_description.findViewById(R.id.frame_description_text);
                        frame_description_text.setText(getText(R.string.frame_description_precondition));
                    }
                }
            }
            return true;
        }
    }
    public void addBrickFunctionToFrame(Action function, LinearLayout frameLayout){
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View newBrick = layoutInflater.inflate(R.layout.linearlayout_brick, null);

        LinearLayout brick_layout = newBrick.findViewById(R.id.brick_layout);
        ImageView brick_icon = newBrick.findViewById(R.id.brick_icon);
        ImageView brick_and_icon = newBrick.findViewById(R.id.and_icon);
        TextView brick_title = newBrick.findViewById(R.id.brick_title);
        TextView brick_condition = newBrick.findViewById(R.id.brick_condition);

        int drawable_id = getApplicationContext().getResources().getIdentifier(function.getBrick_function().getBrick().getIcon().getFilename(),"drawable",getApplicationContext().getPackageName());
        brick_icon.setImageResource(drawable_id);
        brick_layout.setTag("Action_"+function.getId());
        brick_layout.setOnLongClickListener(new DeleteOnLongClickListener());
        brick_title.setText(function.getBrick_function().getBrick().getName());
        String text = function.getBrick_function().getFunction().getName()+" = "+function.getValue();
        brick_condition.setText(text);
        brick_and_icon.setVisibility(View.VISIBLE);

        frameLayout.addView(newBrick);
        newBrick.setVisibility(View.VISIBLE);

        LinearLayout frame_description = frameLayout.findViewById(R.id.frame_description);
        frame_description.findViewById(R.id.frame_description_icon).setVisibility(View.GONE);
        frameLayout.removeView(frame_description);

        TextView frame_description_text = frame_description.findViewById(R.id.frame_description_text);
        frame_description_text.setText(getText(R.string.frame_description_more_actions));

        frameLayout.addView(frame_description);
        Toast.makeText(getApplicationContext(),"Gerät durch langen Klick entfernen",Toast.LENGTH_SHORT).show();
    }
    public void addBrickFunctionToFrame(Precondition function, LinearLayout frameLayout){
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View newBrick = layoutInflater.inflate(R.layout.linearlayout_brick, null);

        LinearLayout brick_layout = newBrick.findViewById(R.id.brick_layout);
        ImageView brick_icon = newBrick.findViewById(R.id.brick_icon);
        ImageView brick_and_icon = newBrick.findViewById(R.id.and_icon);
        TextView brick_title = newBrick.findViewById(R.id.brick_title);
        TextView brick_condition = newBrick.findViewById(R.id.brick_condition);

        int drawable_id = getApplicationContext().getResources().getIdentifier(function.getBrick_function().getBrick().getIcon().getFilename(),"drawable",getApplicationContext().getPackageName());
        brick_icon.setImageResource(drawable_id);
        brick_layout.setTag("Precondition_"+function.getId());
        brick_layout.setOnLongClickListener(new DeleteOnLongClickListener());
        brick_title.setText(function.getBrick_function().getBrick().getName());
        String text = function.getBrick_function().getFunction().getName()+" "+function.getOperator().getCharacter()+" "+function.getValue();
        brick_condition.setText(text);
        brick_and_icon.setVisibility(View.VISIBLE);

        frameLayout.addView(newBrick);
        newBrick.setVisibility(View.VISIBLE);

        LinearLayout frame_description = frameLayout.findViewById(R.id.frame_description);
        frame_description.findViewById(R.id.frame_description_icon).setVisibility(View.GONE);
        TextView frame_description_text = frame_description.findViewById(R.id.frame_description_text);
        frame_description_text.setText(getText(R.string.frame_description_more_preconditions));

        frameLayout.removeView(frame_description);
        frameLayout.addView(frame_description);
        Toast.makeText(getApplicationContext(),"Gerät durch langen Klick entfernen",Toast.LENGTH_SHORT).show();
    }

    public void showDialog_DeleteRule(){
        new AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_delete_rule)+": "+ selected_rule.getName())
            .setMessage(getString(R.string.dialog_text_delete_rule))
            .setIcon(R.drawable.ic_delete_black)
            .setNegativeButton(R.string.button_cancel, null)
            .setPositiveButton(R.string.button_next, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String text = getString(R.string.general_term_rule)+" '"+ selected_rule.getName()+"' "+getString(R.string.toast_hint_was_deleted);
                    db.deleteRule(selected_rule);
                    Intent intent = new Intent(getApplicationContext(), Main.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                }})
            .show();
    }

    public void addNewBrickFunctionToDb(Brick_Function brick_function,String brick_function_value, Operator operator, LinearLayout frameLayout){
        String frame_tag = frameLayout.getTag().toString();
        if (frame_tag.equals("Action")){
            Action action = db.addAction(new Action(selected_rule,brick_function,brick_function_value));
            addBrickFunctionToFrame(action,frameLayout);
        }else {
            Frame pre_frame = db.getFrame(Long.parseLong(frameLayout.getTag().toString().substring(13)));
            Precondition precondition = db.addPrecondition(new Precondition(brick_function, pre_frame, operator, brick_function_value));
            addBrickFunctionToFrame(precondition, frameLayout);
        }
    }

    public void show_dialog_choose_function(final Brick brick, final LinearLayout frameLayout) {
        final ArrayList<Brick_Function> brick_functions_array;
        final String frame_tag = frameLayout.getTag().toString();
        String frame_title;

        if(frame_tag.equals("Action")){
            frame_title = getString(R.string.dialog_select_action);
            brick_functions_array = db.getBrick_Functions_For_Actions(brick.getId());
            if (brick_functions_array.isEmpty()) {
                Toast.makeText(getApplicationContext(),getText(R.string.dialog_hint_no_action),Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else {
            frame_title = getString(R.string.dialog_select_trigger);
            brick_functions_array = db.getBrick_Functions_For_Triggers(brick.getId());
            if (brick_functions_array.isEmpty()) {
                Toast.makeText(getApplicationContext(),getText(R.string.dialog_hint_no_trigger),Toast.LENGTH_SHORT).show();
                return;
            }
        }
        ArrayAdapter brick_function_adapter = new ArrayAdapter<Brick_Function>(getApplicationContext(), R.layout.textview_select_brick_function, brick_functions_array);
        // setup the DIALOG
        new AlertDialog.Builder(this)
            .setTitle(frame_title)
            .setNegativeButton(R.string.button_cancel, null)
            .setAdapter(brick_function_adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Brick_Function brick_function = brick_functions_array.get(whichButton);
                    String type = brick_function.getFunction().getType().getName();
                    if (frame_tag.equals("Action")|type.equals("Switch")){
                        Operator operator = db.getOperator("==");
                        show_dialog_function_value(brick_function,operator,frameLayout);
                    }else
                        show_dialog_choose_operator(brick_function, frameLayout);
                }
            })
            .show();
    }
    public void show_dialog_choose_operator(final Brick_Function brick_function, final LinearLayout frameLayout) {
        final ArrayList<Operator> operators_array = db.getAllOperators();
        String frame_title;
        frame_title = "Wenn "+brick_function.getFunction().getName();
        ArrayAdapter operator_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.textview_select_brick_function, operators_array);
        new AlertDialog.Builder(this)
            .setTitle(frame_title)
            .setNegativeButton(R.string.button_back, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    show_dialog_choose_function(brick_function.getBrick(),frameLayout);
                    }
                })
            .setAdapter(operator_adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Operator operator = operators_array.get(whichButton);
                    show_dialog_function_value(brick_function,operator,frameLayout);
                }
            })
            .show();
    }
    public void show_dialog_function_value(final Brick_Function brick_function, final Operator operator, final LinearLayout frameLayout){
        final String frame_tag, type,frame_title;
        final EditText brick_function_input;
        final SeekBar seekbar_r, seekbar_g,seekbar_b;
        final LinearLayout show_color;
        frame_tag = frameLayout.getTag().toString();
        type = brick_function.getFunction().getType().getName();
        if (frame_tag.equals("Action")){
            frame_title = "Setze "+brick_function.getFunction().getName()+ " auf";
        }else {
            frame_title = "Wenn "+brick_function.getFunction().getName()+"  "+operator.getName();
        }
       final AlertDialog.Builder select_brick_function_builder = new AlertDialog.Builder(this)
            .setTitle(frame_title)
            .setNegativeButton(R.string.button_back, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (frame_tag.equals("Action")|type.equals("Switch")){
                        show_dialog_choose_function(brick_function.getBrick(),frameLayout);
                    }else {
                        show_dialog_choose_operator(brick_function, frameLayout);
                    }
                }
            });
        if(type.equals("Switch")){
            final ArrayList<String> switch_options_array = new ArrayList<>();
            switch_options_array.add(brick_function.getFunction().getMin_value());
            switch_options_array.add(brick_function.getFunction().getMax_value());
            switch_options_array.add("Switch");
            ArrayAdapter switch_options_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.textview_select_brick_function, switch_options_array);
            select_brick_function_builder.setAdapter(switch_options_adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String brick_function_value = switch_options_array.get(whichButton);
                    addNewBrickFunctionToDb(brick_function,brick_function_value, operator, frameLayout);
                }
            });
            select_brick_function_builder.create().show();
        }else if(type.equals("Clock")){
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View input_layout = layoutInflater.inflate(R.layout.relativelayout_timepicker, null);
            final TimePicker timepicker = input_layout.findViewById(R.id.timepicker);
            timepicker.setIs24HourView(true);
            select_brick_function_builder.setView(input_layout);
            select_brick_function_builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    int min = timepicker.getMinute();
                    int hour = timepicker.getHour();
                    String brick_function_value = hour+":"+min;
                    addNewBrickFunctionToDb(brick_function,brick_function_value, operator, frameLayout);
                }
            });
            select_brick_function_builder.create().show();
        }
        else if(type.equals("Colorpicker")){
            final int[] intvalue_rgb = new int[4];
            intvalue_rgb[0] = 255;
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View input_layout = layoutInflater.inflate(R.layout.relativelayout_colorpicker, null);
            show_color=input_layout.findViewById(R.id.show_color);
            final TextView show_color_text=show_color.findViewById(R.id.show_color_text);
            seekbar_r = input_layout.findViewById(R.id.seekbar_r);
            intvalue_rgb[1] =seekbar_r.getProgress();
            seekbar_g = input_layout.findViewById(R.id.seekbar_g);
            intvalue_rgb[2] =seekbar_g.getProgress();
            seekbar_b = input_layout.findViewById(R.id.seekbar_b);
            intvalue_rgb[3] =seekbar_b.getProgress();
            seekbar_r.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                 public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                     intvalue_rgb[1] = progress;
                     show_color.setBackgroundColor(Color.argb(intvalue_rgb[0],intvalue_rgb[1], intvalue_rgb[2], intvalue_rgb[3]));
                     String brick_function_value = "("+intvalue_rgb[1]+","+intvalue_rgb[2]+","+intvalue_rgb[3]+")";
                     show_color_text.setText(brick_function_value);
                 }
                 public void onStartTrackingTouch(SeekBar seekBar) {}
                 public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            seekbar_g.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    intvalue_rgb[2] = progress;
                    show_color.setBackgroundColor(Color.argb(intvalue_rgb[0],intvalue_rgb[1], intvalue_rgb[2], intvalue_rgb[3]));
                    String brick_function_value = "("+intvalue_rgb[1]+","+intvalue_rgb[2]+","+intvalue_rgb[3]+")";
                    show_color_text.setText(brick_function_value);
                }
                public void onStartTrackingTouch(SeekBar seekBar) {}
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            seekbar_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    intvalue_rgb[3] = progress;
                    show_color.setBackgroundColor(Color.argb(intvalue_rgb[0],intvalue_rgb[1], intvalue_rgb[2], intvalue_rgb[3]));
                    String brick_function_value = "("+intvalue_rgb[1]+","+intvalue_rgb[2]+","+intvalue_rgb[3]+")";
                    show_color_text.setText(brick_function_value);
                }
                public void onStartTrackingTouch(SeekBar seekBar) {}
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            select_brick_function_builder.setView(input_layout);
            select_brick_function_builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String brick_function_value = "("+intvalue_rgb[0]+","+intvalue_rgb[1]+","+intvalue_rgb[2]+")";
                    addNewBrickFunctionToDb(brick_function,brick_function_value, operator, frameLayout);
                }
            });
            select_brick_function_builder.create().show();
        }else if(type.equals("Seekbar")){
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View input_layout = layoutInflater.inflate(R.layout.relativelayout_seekbar, null);
            final SeekBar seekbar = input_layout.findViewById(R.id.seekbar);
            final TextView seekbar_progress = input_layout.findViewById(R.id.seekbar_progress);
            final int min_int,max_int,akt_progress;

            min_int = Integer.parseInt(brick_function.getFunction().getMin_value());
            max_int = Integer.parseInt(brick_function.getFunction().getMax_value());
            akt_progress = Math.round((min_int+max_int)/2);

            seekbar.setMax(max_int-min_int);
            seekbar.setProgress(akt_progress-min_int);
            seekbar_progress.setText(""+akt_progress);

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int akt_progress = min_int+progress;
                    seekbar_progress.setText(""+akt_progress);
                }
                public void onStartTrackingTouch(SeekBar seekBar) {}
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            select_brick_function_builder.setView(input_layout);
            select_brick_function_builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String brick_function_value = ""+(min_int+seekbar.getProgress());
                    addNewBrickFunctionToDb(brick_function,brick_function_value, operator, frameLayout);
                }
            });
            AlertDialog select_brick_function_dialog = select_brick_function_builder.create();
            select_brick_function_dialog.show();
        }else{
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View input_layout = layoutInflater.inflate(R.layout.relativelayout_brick_function_input, null);
            brick_function_input = input_layout.findViewById(R.id.brick_function_value);
            select_brick_function_builder.setView(input_layout);
            select_brick_function_builder.setPositiveButton(R.string.button_ok, null);
            AlertDialog select_brick_function_dialog = select_brick_function_builder.create();
            select_brick_function_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                public void onShow(DialogInterface dialog) {
                    Button bt_positive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    bt_positive.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String brick_function_value = brick_function_input.getEditableText().toString().trim();
                            if(brick_function_value.isEmpty()){
                                Toast.makeText(getApplicationContext(),getText(R.string.toast_hint_check_inputs),Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                addNewBrickFunctionToDb(brick_function,brick_function_value, operator, frameLayout);
                            }
                        }
                    });
                }
            });
            select_brick_function_dialog.show();
        }
    }

    public String generateRuleString(Rule rule_param){

        String generated_rule_string = "", generated_when = "", generated_if = "", generated_then = "";
        Boolean firstPrecondition = true, firstAction = true, firstFrame = true, first_if = true;
        ArrayList<Frame> frames;
        ArrayList<Action> actions;
        ArrayList<Precondition> preconditions;

        generated_rule_string = generated_rule_string + "rule '" + rule_param.toString() + "'\n";

        db = new DatabaseHelper(getApplicationContext());
        generated_when = generated_when + "when\n";

        frames = db.getAllFrames(rule_param.getId());
        actions = db.getAllActions(rule_param.getId());

        for (Frame f : frames) {
            preconditions = db.getAllPreconditions(f.getId());
            if (!preconditions.isEmpty()) {
                if (firstFrame) {
                    firstFrame = false;
                    generated_if = generated_if + "if(";
                } else {
                    generated_if = generated_if + " || \n \t";
                    first_if = true;
                }
            }
            for (Precondition p : preconditions) {
                if (firstPrecondition) {
                    firstPrecondition = false;
                    generated_when = generated_when + "\t";
                } else {
                    generated_when = generated_when + " or \n \t";

                }
                if (first_if) {
                    first_if = false;
                } else {
                    generated_if = generated_if + " && ";
                }

                String type = p.getBrick_function().getFunction().getType().getName();
                String value = p.getValue();
                String openhab_name = p.getBrick_function().getOpenhab_name();
                String operator = p.getOperator().getCharacter();
                generated_when = generated_when + " Item " + openhab_name + " changed";
                if (type.equals("Switch")) {
                    String min = p.getBrick_function().getFunction().getMin_value();
                    String max = p.getBrick_function().getFunction().getMax_value();
                    if (value.equals(max)) {
                        generated_when = generated_when + " from " + min + " to " + max;
                        generated_if = generated_if + openhab_name + ".state" + operator + value;
                    } else if (value.equals(min)) {
                        generated_when = generated_when + " from " + max + " to " + min;
                        generated_if = generated_if + openhab_name + ".state" + operator + value;
                    } else if (value.equals("Switch")) {
                        generated_if = generated_if + openhab_name + ".state" + operator + "(" + min + "||" + max + ")";
                    }
                } else {
                    generated_if = generated_if + openhab_name + ".state" + operator + value;
                }

            }
        }
        generated_if = generated_if + ")";
        generated_then = generated_then + "\nthen\n";
        for (Action a : actions) {
            if (firstAction) {
                firstAction = false;
                generated_then = generated_then + "\t" + generated_if + "{";
            }

            String type = a.getBrick_function().getFunction().getType().getName();
            String value = a.getValue();
            String openhab_name = a.getBrick_function().getOpenhab_name();
            boolean action_generated = false;
            if (type.equals("Switch")) {
                if (value.equals("Switch")) {
                    String min = a.getBrick_function().getFunction().getMin_value();
                    String max = a.getBrick_function().getFunction().getMax_value();
                    generated_then = generated_then + "\n\t\tif(" + openhab_name + ".state==" + min + "){" + openhab_name + ".sendCommand(" + max + ")}else{" + openhab_name + ".sendCommand(" + min + ")};";
                    action_generated = true;
                }
            }
            if (!action_generated) {
                generated_then = generated_then + "\n\t\t" + openhab_name + ".sendComand(" + value + ");";
            }
        }
        generated_then = generated_then + "\n\t}";
        generated_rule_string = generated_rule_string + generated_when + generated_then+"\n"+"end";
        return generated_rule_string;
    }
    public String generateServiceString(){
        String generated_service_string = "";
        db = new DatabaseHelper(getApplicationContext());

        ArrayList<Rule> rules_in_service = db.getAllRulesInService(selected_rule.getService().getId());

        for (Rule r : rules_in_service) {
            generated_service_string = generated_service_string + generateRuleString(r)+"\n\n";
        }

        return generated_service_string;
    }

    public void upload_service_file(){
        final ftpUploaderClient ftpclient;
        final File rule_file;
        final String fileName;
        try {

            fileName = selected_rule.getService().getName()+"_"+selected_rule.getName() +".rules";
            //fileName = selected_rule.getService().getName()+".rules";

            rule_file = new File(getApplicationContext().getCacheDir(), fileName);
            FileWriter fw = new FileWriter(rule_file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            String generated_service_string = generateRuleString(selected_rule);
            //String generated_service_string = generateServiceString();
            bw.write(generated_service_string);
            bw.close();
            ftpclient = new ftpUploaderClient();
            new Thread(new Runnable() {
                public void run() {
                    // host – your FTP address
                    // username & password – for your secured login
                    // 21 default gateway for FTP
                    boolean connectstatus = ftpclient.ftpConnect(getApplicationContext());
                    if(connectstatus) {
                        boolean uploadstatus = ftpclient.ftpUpload(rule_file, fileName);
                        if (uploadstatus){
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(EditRule.this).create();
                                    alertDialog.setTitle("Upload erfolgreich");
                                    alertDialog.setMessage("Service wurde erfolgreich hochgeladen");
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            });
                            ftpclient.ftpDisconnect();
                        }
                    }else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(EditRule.this).create();
                                alertDialog.setTitle("Verbindung konnte nicht hergestellt werden");
                                alertDialog.setMessage("WLAN-Verbindung oder Server-Einstellungen überprüfen");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
                    }

                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    }
