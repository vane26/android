package com.google.sample.cloudvision.BD;

import android.provider.BaseColumns;

/**
 * Created by Vane on 30/08/2016.
 */
public class personaContract {
    public static abstract class personaEntry implements BaseColumns{
        public static final String TABLE_NAME = "persona";

        public static final String RUN = "run";
        public static final String APELLIDOS = "apellidos";
        public static final String NOMBRES = "nombres";

    }



}
