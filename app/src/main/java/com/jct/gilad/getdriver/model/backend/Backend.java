package com.jct.gilad.getdriver.model.backend;

import com.jct.gilad.getdriver.model.database.FireBase_DbManager;
import com.jct.gilad.getdriver.model.entities.Driver;
import com.jct.gilad.getdriver.model.entities.Ride;

import java.util.List;

public interface Backend {
    List<String> getDriversNames(FireBase_DbManager.Action<String> action);
    //void addDriver(Driver driver, FireBase_DbManager.Action)
}
