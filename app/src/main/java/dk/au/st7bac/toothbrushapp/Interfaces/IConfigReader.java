package dk.au.st7bac.toothbrushapp.Interfaces;

import android.content.Context;

import dk.au.st7bac.toothbrushapp.Model.Configs;

public interface IConfigReader {

    Configs getConfigSettings(Context context);
}
