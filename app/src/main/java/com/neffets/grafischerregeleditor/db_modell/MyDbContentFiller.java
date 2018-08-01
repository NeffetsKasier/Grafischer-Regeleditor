package com.neffets.grafischerregeleditor.db_modell;

public class MyDbContentFiller {
    DatabaseHelper db;

    public MyDbContentFiller(DatabaseHelper db) {
        this.db = db;
    }

    public void FillServicesAndRules() {
        this.db.deleteAllServices();
        Service ser1 = this.db.addService(new Service("Arbeitsweg"));
        Service ser2 = this.db.addService(new Service("Anwesenheit"));
        Service ser3 = this.db.addService(new Service("Beleuchtung"));

        this.db.deleteAllRules();
        Rule rule1 = this.db.addRule(new Rule("Heimweg von der Arbeit", ser1));
        Rule rule2 = this.db.addRule(new Rule("Zur Arbeit gehen", ser1));
        Rule rule3 = this.db.addRule(new Rule("Heimkommen", ser2));
        Rule rule4 = this.db.addRule(new Rule("Aus dem Haus gehen", ser2));
        Rule rule5 = this.db.addRule(new Rule("Keiner Zuhause", ser2));
        Rule rule6 = this.db.addRule(new Rule("Jemand zuhause", ser2));
        Rule rule7 = this.db.addRule(new Rule("Alle Zuhause", ser2));
        Rule rule8 = this.db.addRule(new Rule("Morgens Schlafzimmerlicht", ser3));
        Rule rule9 = this.db.addRule(new Rule("Abend Schlafzimmerlicht", ser3));
        Rule rule10 = this.db.addRule(new Rule("Gardarobenbeleuchtung", ser3));
        Rule rule11 = this.db.addRule(new Rule("Gartenlampe", ser3));
    }
    public void FillBackend(){
        this.db.deleteAllIcons();
        Icon icon1 = this.db.addIcon(new Icon("ic_sb_clock_black","Uhrzeit"));
        Icon icon2 = this.db.addIcon(new Icon("ic_sb_cloud_black","Wolke"));
        Icon icon3 = this.db.addIcon(new Icon("ic_sb_electric_plug_black","Steckdose"));
        Icon icon4 = this.db.addIcon(new Icon("ic_sb_fan_black","Lüfter"));
        Icon icon5 = this.db.addIcon(new Icon("ic_sb_fire_black","Feuer"));
        Icon icon6 = this.db.addIcon(new Icon("ic_sb_fridge_black","Kühlschrank"));
        Icon icon7 = this.db.addIcon(new Icon("ic_sb_heartbeat_black","Herzschlag"));
        Icon icon8 = this.db.addIcon(new Icon("ic_sb_laptop_black","Laptop"));
        Icon icon9 = this.db.addIcon(new Icon("ic_sb_lightbulb_black","Glühbirne"));
        Icon icon10 = this.db.addIcon(new Icon("ic_sb_smartphone_black","Smartphone"));
        Icon icon11 = this.db.addIcon(new Icon("ic_sb_tablet_black","Tablet"));
        Icon icon12 = this.db.addIcon(new Icon("ic_sb_temperature_black","Termometer"));
        Icon icon13 = this.db.addIcon(new Icon("ic_sb_window_black","Fenster"));
        Icon icon14 = this.db.addIcon(new Icon("ic_sb_flashlight_black","Flashlight"));
        Icon icon15 = this.db.addIcon(new Icon("ic_sb_alarm_black","Alarm"));
        Icon icon16 = this.db.addIcon(new Icon("ic_sb_button_black","Taster"));
        Icon icon17 = this.db.addIcon(new Icon("ic_sb_house_roof_black","Hausdach"));
        Icon icon18 = this.db.addIcon(new Icon("ic_sb_door_black","Türe"));

        this.db.deleteAllGroups();
        Group group1 = this.db.addGroup(new Group("Beleuchtung"));
        Group group2 = this.db.addGroup(new Group("Strom"));
        Group group3 = this.db.addGroup(new Group("Belüftung"));
        Group group4 = this.db.addGroup(new Group("Alarm"));
        Group group5 = this.db.addGroup(new Group("Sensoren"));
        Group group6 = this.db.addGroup(new Group("Haushaltsgerät"));
        Group group7 = this.db.addGroup(new Group("Schalter"));
        Group group8 = this.db.addGroup(new Group("Zeit"));
        Group group9 = this.db.addGroup(new Group("Webdienste"));
        Group group10 = this.db.addGroup(new Group("Mobile Geräte"));

        this.db.deleteAllBricks();
        Brick brick1 = this.db.addBrick(new Brick("RGB Lampe",group1,icon9));
        Brick brick2 = this.db.addBrick(new Brick("Beleuchtung Treppe",group1,icon9));
        Brick brick3 = this.db.addBrick(new Brick("LED Flashlight",group1,icon14));
        Brick brick4 = this.db.addBrick(new Brick("Alarm Pieper",group4,icon15));
        Brick brick5 = this.db.addBrick(new Brick("Taster",group7,icon16));
        Brick brick6 = this.db.addBrick(new Brick("Klopf Sensor",group5,icon18));
        Brick brick7 = this.db.addBrick(new Brick("Hausdach",group5,icon17));
        Brick brick8 = this.db.addBrick(new Brick("Temperatur",group5,icon12));
        Brick brick9 = this.db.addBrick(new Brick("Flammen Detektor",group5,icon5));
        Brick brick10 = this.db.addBrick(new Brick("Fenster",group5,icon13));
        Brick brick11 = this.db.addBrick(new Brick("Steckdosen Aussen",group2,icon3));
        Brick brick12 = this.db.addBrick(new Brick("Lüfter",group3,icon4));
        Brick brick13 = this.db.addBrick(new Brick("Kühlschrank",group6,icon6));
        Brick brick14 = this.db.addBrick(new Brick("Herzschlag",group5,icon7));
        Brick brick15 = this.db.addBrick(new Brick("Uhr",group8,icon1));
        Brick brick16 = this.db.addBrick(new Brick("Smartphone",group10,icon10));
        Brick brick17 = this.db.addBrick(new Brick("Laptop",group10,icon8));
        Brick brick18 = this.db.addBrick(new Brick("Tablet",group10,icon11));
        //Brick brick19 = this.db.addBrick(new Brick("Wetter Service",group9,icon2));

        this.db.deleteAllOperators();
        Operator operator1 = this.db.addOperator(new Operator("Gleich","=="));
        Operator operator2 = this.db.addOperator(new Operator("Ungleich","!="));
        Operator operator3 = this.db.addOperator(new Operator("Größer",">"));
        Operator operator4 = this.db.addOperator(new Operator("Kleiner","<"));

        this.db.deleteAllTypes();
        Type type1 = this.db.addType(new Type("Clock"));
        Type type2 = this.db.addType(new Type("Colorpicker"));
        Type type3 = this.db.addType(new Type("Seekbar"));
        Type type4 = this.db.addType(new Type("Switch"));
        Type type5 = this.db.addType(new Type("Value"));

        this.db.deleteAllFunctions();
        Function function1 = this.db.addFunction(new Function("Power",type4,"Aus","An"));
        Function function2 = this.db.addFunction(new Function("Signal",type4,"Nein","Ja"));
        Function function3 = this.db.addFunction(new Function("Farbe",type2,"",""));
        Function function4 = this.db.addFunction(new Function("Farb-Temperatur",type3,"0","100"));
        Function function5 = this.db.addFunction(new Function("Helligkeit",type3,"0","100"));
        Function function6 = this.db.addFunction(new Function("Temperatur",type3,"-20","60"));
        Function function7 = this.db.addFunction(new Function("Geschwindigkeit",type3,"0","100"));
        Function function8 = this.db.addFunction(new Function("Lautstärke",type3,"0","100"));
        Function function9 = this.db.addFunction(new Function("Uhrzeit",type1,"",""));
        Function function10 = this.db.addFunction(new Function("Puls ",type3,"0","250"));
        Function function11 = this.db.addFunction(new Function("Zustand",type4,"Geschlossen","Geöffnet"));
        Function function12 = this.db.addFunction(new Function("Verbindung",type4,"Nicht verbunden","Verbunden"));
        this.db.deleteAllBrick_Function();
        Brick_Function brick_function1 = this.db.addBrick_Function(new Brick_Function(brick1,"Item_Yeelight_color ", function3,false,true));
        Brick_Function brick_function2 = this.db.addBrick_Function(new Brick_Function(brick1,"Item_Yeelight_switch", function1,true,true));
        Brick_Function brick_function3 = this.db.addBrick_Function(new Brick_Function(brick1,"Item_Yeelight_brightness ", function5,true,true));
        Brick_Function brick_function4 = this.db.addBrick_Function(new Brick_Function(brick1,"Item_Yeelight_color_temperature", function4,true,true));
        Brick_Function brick_function5 = this.db.addBrick_Function(new Brick_Function(brick2,"Item_LED_Stair_Switch", function1,true,true));
        Brick_Function brick_function6 = this.db.addBrick_Function(new Brick_Function(brick3,"Item_LED_Flashlight_Switch", function1,true,true));
        Brick_Function brick_function7 = this.db.addBrick_Function(new Brick_Function(brick4,"Item_Alarm_Buzzer_Switch", function1,true,true));
        Brick_Function brick_function8 = this.db.addBrick_Function(new Brick_Function(brick5,"Item_Button_Signal", function2,true,false));
        Brick_Function brick_function9 = this.db.addBrick_Function(new Brick_Function(brick6,"Item_Knock_Signal", function2,true,false));
        Brick_Function brick_function10 = this.db.addBrick_Function(new Brick_Function(brick7,"Item_Roof_State", function11,true,false));
        Brick_Function brick_function11 = this.db.addBrick_Function(new Brick_Function(brick8,"Item_Temperatur_Signal", function2,true,false));
        Brick_Function brick_function12 = this.db.addBrick_Function(new Brick_Function(brick8,"Item_Temperatur_Slider", function6,true,false));
        Brick_Function brick_function13 = this.db.addBrick_Function(new Brick_Function(brick9,"Item_Flame_Signal", function2,true,false));
        Brick_Function brick_function14 = this.db.addBrick_Function(new Brick_Function(brick10,"Item_Window_Signal", function11,true,false));
        Brick_Function brick_function15 = this.db.addBrick_Function(new Brick_Function(brick11,"Item_Plug_Outside_Power", function1,true,true));
        Brick_Function brick_function16 = this.db.addBrick_Function(new Brick_Function(brick12,"Item_Fan_Power", function1,true,true));
        Brick_Function brick_function17 = this.db.addBrick_Function(new Brick_Function(brick12,"Item_Fan_Speed", function7,true,true));
        Brick_Function brick_function18 = this.db.addBrick_Function(new Brick_Function(brick13,"Item_Fridge_Power", function1,true,true));
        Brick_Function brick_function19 = this.db.addBrick_Function(new Brick_Function(brick15,"Item_Time", function9,true,false));
        Brick_Function brick_function20 = this.db.addBrick_Function(new Brick_Function(brick16,"Item_Smartphone_Connected", function12,true,false));
        Brick_Function brick_function21 = this.db.addBrick_Function(new Brick_Function(brick17,"Item_Laptop_Connected", function12,true,false));
        Brick_Function brick_function22 = this.db.addBrick_Function(new Brick_Function(brick18,"Item_Tablet_Connected", function12,true,false));
        Brick_Function brick_function23 = this.db.addBrick_Function(new Brick_Function(brick14,"Item_Puls_Signal", function10,true,false));


    }
}
