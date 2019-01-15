package com.jct.gilad.getdriver.model.backend;

import android.content.Context;

import com.jct.gilad.getdriver.model.database.FireBase_DbManager;

public final class BackendFactorySingleton {
    private static Backend instance = null;
    public static Backend  getBackend(Context context)
    {
        if(instance == null)
        {
            instance = new FireBase_DbManager(context);
        }
        return instance;
    }




}
