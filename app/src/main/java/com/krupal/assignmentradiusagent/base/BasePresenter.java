package com.krupal.assignmentradiusagent.base;

import android.content.Intent;

public interface BasePresenter<T> {

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void setView(T view);
}