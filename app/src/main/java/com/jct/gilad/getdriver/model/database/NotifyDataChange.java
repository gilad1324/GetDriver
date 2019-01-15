package com.jct.gilad.getdriver.model.database;

public interface NotifyDataChange <T>{

        void OnDataChanged(T obj);

        void onFailure(Exception exception);
    }

