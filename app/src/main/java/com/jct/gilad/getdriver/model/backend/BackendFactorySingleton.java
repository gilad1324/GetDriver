package com.jct.gilad.getdriver.model.backend;

import com.jct.gilad.getdriver.model.database.FireBase_DbManager;

public final class BackendFactorySingleton {
    private static Backend instance = null;
    public static Backend  getBackend()
    {
        if(instance == null)
        {
            instance = new FireBase_DbManager();
        }
        return instance;
    }
}
