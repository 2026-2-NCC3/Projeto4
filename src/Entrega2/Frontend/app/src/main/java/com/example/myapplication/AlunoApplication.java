package com.example.myapplication;

import android.app.Application;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AlunoApplication extends Application {
  public static final ExecutorService IO = Executors.newFixedThreadPool(3);
}
