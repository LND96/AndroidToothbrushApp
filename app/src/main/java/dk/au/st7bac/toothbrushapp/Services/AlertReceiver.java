package dk.au.st7bac.toothbrushapp.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dk.au.st7bac.toothbrushapp.Constants;
import dk.au.st7bac.toothbrushapp.Controllers.UpdateDataCtrl;

// inspiration for alarm manager and alert receiver: https://www.youtube.com/watch?v=yrpimdBRk5Q
public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        UpdateDataCtrl updateDataCtrl = UpdateDataCtrl.getInstance();
        updateDataCtrl.initUpdateTbData(Constants.FROM_ALERT_RECEIVER);
    }
}
