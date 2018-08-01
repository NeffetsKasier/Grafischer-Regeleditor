package com.neffets.grafischerregeleditor;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neffets.grafischerregeleditor.db_modell.Brick;

import java.util.ArrayList;

public class Adapter_Brick extends BaseAdapter {
    Context mContext;
    // ArrayList-Variablen für Autocomplete initialisieren
    ArrayList<Brick> brick_array;

    public Adapter_Brick(Context context, ArrayList<Brick> brick_array) {
        this.mContext = context;
        this.brick_array = brick_array;
    }

    public int getCount() {
        return brick_array.size();
    }

    public long getItemId(int position) {
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Brick brick = this.brick_array.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_brick, null);
        }

        LinearLayout brick_layout = (LinearLayout)convertView.findViewById(R.id.brick_layout);
        ImageView brick_icon = (ImageView)convertView.findViewById(R.id.brick_icon);
        TextView brick_title = (TextView)convertView.findViewById(R.id.brick_title);


        int drawable_id = mContext.getResources().getIdentifier(brick.getIcon().getFilename(),"drawable",mContext.getPackageName());
        brick_icon.setImageResource(drawable_id);
        brick_layout.setTag("brick_"+brick.getId());
        brick_layout.setOnTouchListener(new OnBrickTouchListener());
        brick_title.setText(brick.getName());


        return convertView;
    }

    private final class OnBrickTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                //view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }




}
