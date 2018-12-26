package com.jct.gilad.getdriver.model.database;

import com.jct.gilad.getdriver.model.backend.Backend;

public class FireBase_DbManager implements Backend {
    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }
}
