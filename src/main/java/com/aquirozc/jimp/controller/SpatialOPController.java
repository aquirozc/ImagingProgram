package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.init.FXApp;

import javafx.scene.Parent;
import javafx.scene.control.Button;

public class SpatialOPController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private Button rotate90DBtn = (Button) parent.lookup("#perspective_tab").lookup("#rotate90_btn");
    private Button rotate270DBtn = (Button) parent.lookup("#perspective_tab").lookup("#rotate270_btn");
    private Button mirrorXAxisBtn = (Button) parent.lookup("#perspective_tab").lookup("#mirrorx_btn");
    private Button mirrorYAxisBtn = (Button) parent.lookup("#perspective_tab").lookup("#mirrory_btn");

    public SpatialOPController (){
        System.out.println(rotate90DBtn);
        System.out.println(rotate270DBtn);
        System.out.println(mirrorXAxisBtn);
        System.out.println(mirrorYAxisBtn);
    }
    
}
