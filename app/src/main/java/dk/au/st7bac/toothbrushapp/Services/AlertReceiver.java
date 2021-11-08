package dk.au.st7bac.toothbrushapp.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dk.au.st7bac.toothbrushapp.Model.UpdateDataCtrl;


//https://www.youtube.com/watch?v=yrpimdBRk5Q
public class AlertReceiver extends BroadcastReceiver {

    private UpdateDataCtrl updateDataCtrl;
    @Override
    public void onReceive(Context context, Intent intent) {


        updateDataCtrl = UpdateDataCtrl.getInstance();

        updateDataCtrl.initUpdateTbData(); //kaldes fra main



    }
}
