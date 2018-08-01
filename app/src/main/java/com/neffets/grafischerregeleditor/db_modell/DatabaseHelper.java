package com.neffets.grafischerregeleditor.db_modell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "RegelManager";

    //Tabellen Namen
    private static final String TABLE_ICON = "Icons";
    private static final String TABLE_GROUP = "Groups";
    private static final String TABLE_BRICK = "Bricks";
    private static final String TABLE_BRICK_FUNCTION = "Brick_Functions";
    private static final String TABLE_FUNCTION = "Functions";
    private static final String TABLE_TYPE = "Types";
    private static final String TABLE_FRAME = "Frames";
    private static final String TABLE_PRECONDITION = "Preconditions";
    private static final String TABLE_OPERATOR = "Operators";
    private static final String TABLE_ACTION = "Actions";
    private static final String TABLE_RULE = "Rules";
    private static final String TABLE_SERVICE = "Services";

    //Column names
    private static final String KEY_ID = "id";
    private static final String KEY_RULE_ID = "rule_id";
    private static final String KEY_VALUE = "value";
    private static final String KEY_BRICK_ID = "brick_id";
    private static final String KEY_FUNCTION_ID = "function_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ICON_ID = "icon_id";
    private static final String KEY_BRICK_FUNCTION_ID = "brick_function_id";
    private static final String KEY_FRAME_ID = "frame_id";
    private static final String KEY_OPERATOR_ID = "operator_id";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_FILENAME = "filename";
    private static final String KEY_CHARACTER = "character";
    private static final String KEY_TYPE_ID = "type_id";
    private static final String KEY_MIN_VALUE = "min_value";
    private static final String KEY_MAX_VALUE = "max_value";
    private static final String KEY_SERVICE_ID = "service_id";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_GROUP_ID = "group_id";
    private static final String KEY_OPENHAB_NAME = "openhab_name";
    private static final String KEY_IS_TRIGGER = "is_trigger";
    private static final String KEY_IS_ACTION = "is_action";



    //Table Create Statements
    private static final String CREATE_TABLE_GROUP = "CREATE TABLE "
            + TABLE_GROUP +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT "
            + ")";
    private static final String CREATE_TABLE_ICON = "CREATE TABLE "
            + TABLE_ICON +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT, "
            + KEY_FILENAME+" TEXT"
            + ")";
    private static final String CREATE_TABLE_OPERATOR = "CREATE TABLE "
            + TABLE_OPERATOR +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT, "
            + KEY_CHARACTER+" TEXT"
            + ")";
    private static final String CREATE_TABLE_TYPE = "CREATE TABLE "
            + TABLE_TYPE +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT "
            + ")";
    private static final String CREATE_TABLE_FUNCTION = "CREATE TABLE "
            + TABLE_FUNCTION +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT, "
            +KEY_MIN_VALUE+ " TEXT, "+KEY_MAX_VALUE+" TEXT, "+ KEY_TYPE_ID + " INTEGER"
            + ")";
    private static final String CREATE_TABLE_BRICK = "CREATE TABLE "
            + TABLE_BRICK +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT, "
            + KEY_GROUP_ID+" INTEGER, "+KEY_ICON_ID+" INTEGER"
            + ")";
    private static final String CREATE_TABLE_BRICK_FUNCTION = "CREATE TABLE "
            + TABLE_BRICK_FUNCTION +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +KEY_FUNCTION_ID+" INTEGER, "+ KEY_BRICK_ID+" INTEGER, "+KEY_OPENHAB_NAME+" TEXT, "
            + KEY_IS_ACTION +" INTEGER, "+ KEY_IS_TRIGGER +" INTEGER "
            + ")";
    private static final String CREATE_TABLE_RULE = "CREATE TABLE "
            + TABLE_RULE +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT, "
            +KEY_SERVICE_ID+" INTEGER, " + KEY_ACTIVE+" INTEGER"
            + ")";
    private static final String CREATE_TABLE_SERVICE = "CREATE TABLE "
            + TABLE_SERVICE +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT"
            + ")";
    private static final String CREATE_TABLE_ACTION = "CREATE TABLE "
            + TABLE_ACTION +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_RULE_ID+" INTEGER, "
            + KEY_BRICK_FUNCTION_ID +" INTEGER, "+KEY_VALUE+" TEXT"
            + ")";
    private static final String CREATE_TABLE_FRAME = "CREATE TABLE "
            + TABLE_FRAME +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_RULE_ID+" INTEGER, "
            + KEY_NUMBER +" INTEGER"
            + ")";
    private static final String CREATE_TABLE_PRECONDITION = "CREATE TABLE "
            + TABLE_PRECONDITION +"("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ KEY_BRICK_FUNCTION_ID +" INTEGER, "
            + KEY_FRAME_ID+" INTEGER, " + KEY_OPERATOR_ID+" INTEGER, " +KEY_VALUE+" TEXT"
            + ")";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACTION);
        db.execSQL(CREATE_TABLE_BRICK_FUNCTION);
        db.execSQL(CREATE_TABLE_PRECONDITION);
        db.execSQL(CREATE_TABLE_FRAME);
        db.execSQL(CREATE_TABLE_GROUP);
        db.execSQL(CREATE_TABLE_ICON);
        db.execSQL(CREATE_TABLE_OPERATOR);
        db.execSQL(CREATE_TABLE_FUNCTION);
        db.execSQL(CREATE_TABLE_RULE);
        db.execSQL(CREATE_TABLE_SERVICE);
        db.execSQL(CREATE_TABLE_BRICK);
        db.execSQL(CREATE_TABLE_TYPE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRICK_FUNCTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRECONDITION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ICON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPERATOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUNCTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRICK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        onCreate(db);
    }


    ///////////////////////////////////////////CRUD Operations for SERVICE
    public Service addService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, service.getName());
        long id = db.insertWithOnConflict(TABLE_SERVICE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getService(id);
    }
    public void deleteService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_SERVICE+" WHERE "+KEY_ID+" = "+service.getId());
        db.close();
    }
    public Service getService(long service_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_SERVICE + " WHERE "
                + KEY_ID + " = " + service_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Service service = new Service();
        service.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        service.setName(c.getString((c.getColumnIndex(KEY_NAME))));
        db.close();
        c.close();
        return service;
    }

    public ArrayList<Service> getAllServices() {
        ArrayList<Service> services = new ArrayList<Service>();
        String selectQuery = "SELECT  * FROM " + TABLE_SERVICE +" ORDER BY "+KEY_NAME;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                services.add(getService(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return services;
    }
    public void deleteAllServices() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_SERVICE);
        db.close();
    }

    ///////////////////////////////////////////CRUD Operations for RULE/////////////////////////////
    public Rule addRule(Rule rule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, rule.getName());
        values.put(KEY_SERVICE_ID, rule.getService().getId());
        if(rule.isActive()==true)
            values.put(KEY_ACTIVE,1);
        else
            values.put(KEY_ACTIVE,0);
        long rule_id = db.insertWithOnConflict(TABLE_RULE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        rule = getRule(rule_id);
        addFrame(new Frame(rule));
        return rule;
    }
    public void deleteRule(Rule rule) {
        ArrayList<Frame> frames = getAllFrames(rule.getId());
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_RULE+" WHERE "+KEY_ID+" = "+rule.getId());
        db.close();
        for(Frame f:frames){
            deleteAllPreconditions(f.getId());
        }
        deleteAllFrames(rule.getId());
        deleteAllActions(rule.getId());

    }
    public Rule getRule(long rule_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_RULE + " WHERE "
                + KEY_ID + " = " + rule_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Rule rule = new Rule();
        rule.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        rule.setName(c.getString((c.getColumnIndex(KEY_NAME))));
        rule.setService(getService(c.getLong(c.getColumnIndex(KEY_SERVICE_ID))));
        if(c.getLong(c.getColumnIndex(KEY_ACTIVE))==1)
            rule.setActive(true);
        else
            rule.setActive(false);
        db.close();
        c.close();
        return rule;
    }
    public ArrayList<Rule> getAllRules() {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        String selectQuery = "SELECT  * FROM " + TABLE_RULE+" ORDER BY "+KEY_NAME;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                rules.add(getRule(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return rules;
    }
    public ArrayList<Rule> getAllRulesInService(long service_id) {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        String selectQuery = "SELECT  * FROM " + TABLE_RULE+" WHERE "+KEY_SERVICE_ID+ " = "+ service_id+" ORDER BY "+KEY_NAME;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                rules.add(getRule(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return rules;
    }
    public void deleteAllRules() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Rule> rules = getAllRules();
        for(Rule r:rules)
            deleteRule(r);
        db.close();
    }

    ///////////////////////////////////////////CRUD Operations for Frames/////////////////////////////
    public Frame addFrame(Frame frame) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RULE_ID, frame.getRule().getId());
        long id = db.insertWithOnConflict(TABLE_FRAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getFrame(id);
    }
    public Frame getFrame(long frame_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FRAME + " WHERE "
                + KEY_ID + " = " + frame_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Frame frame = new Frame();
        frame.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        frame.setRule(getRule(c.getLong(c.getColumnIndex(KEY_RULE_ID))));
        db.close();
        c.close();
        return frame;
    }
    public void deleteFrame(long frame_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_FRAME+" WHERE "+KEY_ID+" = "+frame_id);
        db.close();
    }
    public void deleteAllFrames(long rule_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_FRAME+" WHERE "+KEY_RULE_ID+" = "+rule_id);
        db.close();
    }
    public ArrayList<Frame> getAllFrames(long rule_id) {
        ArrayList<Frame> frames = new ArrayList<Frame>();
        String selectQuery = "SELECT  * FROM " + TABLE_FRAME+" WHERE "+KEY_RULE_ID+" = "+rule_id;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                frames.add(getFrame(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return frames;
    }
    ///////////////////////////////////////////CRUD Operations for Actions/////////////////////////////
    public Action addAction(Action action) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RULE_ID, action.getRule().getId());
        values.put(KEY_BRICK_FUNCTION_ID, action.getBrick_function().getId());
        values.put(KEY_VALUE, action.getValue());
        long id = db.insertWithOnConflict(TABLE_ACTION, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getAction(id);
    }
    public Action getAction(long action_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ACTION + " WHERE "
                + KEY_ID + " = " + action_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Action action = new Action();
        action.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        action.setRule(getRule(c.getLong(c.getColumnIndex(KEY_RULE_ID))));
        action.setBrick_function(getBrick_Function(c.getLong((c.getColumnIndex(KEY_BRICK_FUNCTION_ID)))));
        action.setValue(c.getString((c.getColumnIndex(KEY_VALUE))));
        db.close();
        c.close();
        return action;
    }
    public void deleteAction(long action_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_ACTION+" WHERE "+KEY_ID+" = "+action_id);
        db.close();
    }
    public void deleteAllActions(long rule_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_ACTION+" WHERE "+KEY_RULE_ID+" = "+rule_id);
        db.close();
    }
    public ArrayList<Action> getAllActions(long rule_id) {
        ArrayList<Action> actions = new ArrayList<Action>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACTION+" WHERE "+KEY_RULE_ID+" = "+rule_id;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                actions.add(getAction(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return actions;
    }

    ///////////////////////////////////////////CRUD Operations for Preconditions/////////////////////////////
    public Precondition addPrecondition(Precondition precondition) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FRAME_ID, precondition.getFrame().getId());
        values.put(KEY_BRICK_FUNCTION_ID, precondition.getBrick_function().getId());
        values.put(KEY_OPERATOR_ID,precondition.getOperator().getId());
        values.put(KEY_VALUE, precondition.getValue());
        long id = db.insertWithOnConflict(TABLE_PRECONDITION, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getPrecondition(id);
    }
    public Precondition getPrecondition(long precondition_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PRECONDITION + " WHERE "
                + KEY_ID + " = " + precondition_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Precondition precondition = new Precondition();
        precondition.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        precondition.setFrame(getFrame(c.getLong(c.getColumnIndex(KEY_FRAME_ID))));
        precondition.setOperator(getOperator(c.getLong(c.getColumnIndex(KEY_OPERATOR_ID))));
        precondition.setBrick_function(getBrick_Function(c.getLong((c.getColumnIndex(KEY_BRICK_FUNCTION_ID)))));
        precondition.setValue(c.getString((c.getColumnIndex(KEY_VALUE))));
        db.close();
        c.close();
        return precondition;
    }
    public void deletePrecondition(long precondition_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_PRECONDITION+" WHERE "+KEY_ID+" = "+precondition_id);
        db.close();
    }
    public void deleteAllPreconditions(long frame_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_PRECONDITION+" WHERE "+KEY_FRAME_ID+" = "+frame_id);
        db.close();
    }
    public ArrayList<Precondition> getAllPreconditions(long frame_id) {
        ArrayList<Precondition> preconditions = new ArrayList<Precondition>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRECONDITION+" WHERE "+KEY_FRAME_ID+" = "+frame_id;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                preconditions.add(getPrecondition(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return preconditions;
    }

    ///////////////////////////////////////////CRUD Operations for ICONS/////////////////////////////
    public Icon addIcon(Icon icon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, icon.getName());
        values.put(KEY_FILENAME, icon.getFilename());
        long id = db.insertWithOnConflict(TABLE_ICON, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getIcon(id);
    }
    public Icon getIcon(long icon_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ICON + " WHERE "
                + KEY_ID + " = " + icon_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Icon icon = new Icon();
        icon.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        icon.setName(c.getString((c.getColumnIndex(KEY_NAME))));
        icon.setFilename(c.getString((c.getColumnIndex(KEY_FILENAME))));
        db.close();
        c.close();
        return icon;
    }
    public void deleteAllIcons() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_ICON);
        db.close();
    }
    ///////////////////////////////////////////CRUD Operations for GROUP/////////////////////////////
    public Group addGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, group.getName());
        long id = db.insertWithOnConflict(TABLE_GROUP, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getGroup(id);
    }
    public Group getGroup(long group_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP + " WHERE "
                + KEY_ID + " = " + group_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Group group = new Group();
        group.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        group.setName(c.getString((c.getColumnIndex(KEY_NAME))));
        db.close();
        c.close();
        return group;
    }
    public void deleteAllGroups() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_GROUP);
        db.close();
    }
    public ArrayList<Group> getAllGroups() {
        ArrayList<Group> groups = new ArrayList<Group>();
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP+" ORDER BY "+KEY_NAME;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                groups.add(getGroup(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return groups;
    }
    ///////////////////////////////////////////CRUD Operations for BRICK/////////////////////////////
    public Brick addBrick(Brick brick) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, brick.getName());
        values.put(KEY_GROUP_ID, brick.getGroup().getId());
        values.put(KEY_ICON_ID, brick.getIcon().getId());
        long id = db.insertWithOnConflict(TABLE_BRICK, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getBrick(id);
    }
    public Brick getBrick(long brick_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_BRICK + " WHERE "
                + KEY_ID + " = " + brick_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Brick brick = new Brick();
        brick.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        brick.setName(c.getString((c.getColumnIndex(KEY_NAME))));
        brick.setGroup(getGroup(c.getLong((c.getColumnIndex(KEY_GROUP_ID)))));
        brick.setIcon(getIcon(c.getLong((c.getColumnIndex(KEY_ICON_ID)))));
        db.close();
        c.close();
        return brick;
    }
    public void deleteAllBricks() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_BRICK);
        db.close();
    }
    public ArrayList<Brick> getAllBricks() {
        ArrayList<Brick> bricks = new ArrayList<Brick>();
        String selectQuery = "SELECT  * FROM " + TABLE_BRICK+" ORDER BY "+KEY_NAME;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                bricks.add(getBrick(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return bricks;
    }

    public ArrayList<Brick> getAllBricksInGroup(long group_id) {
        ArrayList<Brick> bricks = new ArrayList<Brick>();
        String selectQuery = "SELECT  * FROM " + TABLE_BRICK+" WHERE "+KEY_GROUP_ID+" = "+group_id+" ORDER BY "+KEY_NAME;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                bricks.add(getBrick(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return bricks;
    }
    ///////////////////////////////////////////CRUD Operations for BRICK_FUNCTIONS/////////////////////////////
    public Brick_Function addBrick_Function(Brick_Function brick_function) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OPENHAB_NAME, brick_function.getOpenhab_name());
        values.put(KEY_BRICK_ID,brick_function.getBrick().getId());
        values.put(KEY_FUNCTION_ID,brick_function.getFunction().getId());
        if(brick_function.isAction()==true)
            values.put(KEY_IS_ACTION,1);
        else
            values.put(KEY_IS_ACTION,0);
        if(brick_function.isTrigger()==true)
            values.put(KEY_IS_TRIGGER,1);
        else
            values.put(KEY_IS_TRIGGER,0);
        long id = db.insertWithOnConflict(TABLE_BRICK_FUNCTION, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getBrick_Function(id);
    }
    public Brick_Function getBrick_Function(long brick_function_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_BRICK_FUNCTION + " WHERE "
                + KEY_ID + " = " + brick_function_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Brick_Function brick_function = new Brick_Function();
        brick_function.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        brick_function.setOpenhab_name(c.getString((c.getColumnIndex(KEY_OPENHAB_NAME))));
        brick_function.setBrick(getBrick(c.getLong((c.getColumnIndex(KEY_BRICK_ID)))));
        brick_function.setFunction(getFunction(c.getLong((c.getColumnIndex(KEY_FUNCTION_ID)))));
        if(c.getInt(c.getColumnIndex(KEY_IS_ACTION))==1)
            brick_function.setAction(true);
        else
            brick_function.setAction(false);
        if(c.getInt(c.getColumnIndex(KEY_IS_TRIGGER))==1)
            brick_function.setTrigger(true);
        else
            brick_function.setTrigger(false);
        db.close();
        c.close();
        return brick_function;
    }
    public void deleteAllBrick_Function() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_BRICK_FUNCTION);
        db.close();
    }
    public ArrayList<Brick_Function> getBrick_Functions_For_Actions(long brick_id) {
        ArrayList<Brick_Function> brick_functions = new ArrayList<Brick_Function>();
        String selectQuery = "SELECT  * FROM " + TABLE_BRICK_FUNCTION +" WHERE "+KEY_BRICK_ID+" = "+brick_id +" AND "+ KEY_IS_ACTION+" = 1";
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                brick_functions.add(getBrick_Function(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return brick_functions;
    }
    public ArrayList<Brick_Function> getBrick_Functions_For_Triggers(long brick_id) {
        ArrayList<Brick_Function> brick_functions = new ArrayList<Brick_Function>();
        String selectQuery = "SELECT  * FROM " + TABLE_BRICK_FUNCTION +" WHERE "+KEY_BRICK_ID+" = "+brick_id +" AND "+KEY_IS_TRIGGER+" = 1";
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                brick_functions.add(getBrick_Function(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return brick_functions;
    }

    ///////////////////////////////////////////CRUD Operations for FUNCTIONS/////////////////////////////
    public Function addFunction(Function function) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, function.getName());
        values.put(KEY_TYPE_ID,function.getType().getId());
        values.put(KEY_MIN_VALUE, function.getMin_value());
        values.put(KEY_MAX_VALUE,function.getMax_value());
        long id = db.insertWithOnConflict(TABLE_FUNCTION, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getFunction(id);
    }
    public Function getFunction(long function_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FUNCTION + " WHERE "
                + KEY_ID + " = " + function_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Function function = new Function();
        function.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        function.setName(c.getString((c.getColumnIndex(KEY_NAME))));
        function.setType(getType(c.getLong(c.getColumnIndex(KEY_TYPE_ID))));
        function.setMin_value(c.getString((c.getColumnIndex(KEY_MIN_VALUE))));
        function.setMax_value(c.getString((c.getColumnIndex(KEY_MAX_VALUE))));
        db.close();
        c.close();
        return function;
    }
    public void deleteAllFunctions() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_FUNCTION);
        db.close();
    }

    ///////////////////////////////////////////CRUD Operations for Types/////////////////////////////
    public Type addType(Type type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, type.getName());
        long id = db.insertWithOnConflict(TABLE_TYPE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getType(id);
    }
    public Type getType(long type_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_TYPE + " WHERE "
                + KEY_ID + " = " + type_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Type type = new Type();
        type.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        type.setName(c.getString((c.getColumnIndex(KEY_NAME))));
        db.close();
        c.close();
        return type;
    }
    public void deleteAllTypes() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_TYPE);
        db.close();
    }

    ///////////////////////////////////////////CRUD Operations for Operators/////////////////////////////
    public Operator addOperator(Operator operator) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, operator.getName());
        values.put(KEY_CHARACTER, operator.getCharacter());
        long id = db.insertWithOnConflict(TABLE_OPERATOR, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return getOperator(id);
    }
    public Operator getOperator(long operator_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_OPERATOR + " WHERE "
                + KEY_ID + " = " + operator_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Operator operator = new Operator();
        operator.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        operator.setName(c.getString((c.getColumnIndex(KEY_NAME))));
        operator.setCharacter(c.getString((c.getColumnIndex(KEY_CHARACTER))));
        db.close();
        c.close();
        return operator;
    }
    public Operator getOperator(String character) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_OPERATOR + " WHERE "
                + KEY_CHARACTER + " = '" + character+"'";
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Operator operator = new Operator();
        operator.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        operator.setName(c.getString((c.getColumnIndex(KEY_NAME))));
        operator.setCharacter(c.getString((c.getColumnIndex(KEY_CHARACTER))));
        db.close();
        c.close();
        return operator;
    }
    public void deleteAllOperators() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_OPERATOR);
        db.close();
    }
    public ArrayList<Operator> getAllOperators() {
        ArrayList<Operator> operators = new ArrayList<Operator>();
        String selectQuery = "SELECT  * FROM " + TABLE_OPERATOR;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                operators.add(getOperator(c.getLong(c.getColumnIndex(KEY_ID))));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return operators;
    }
}



