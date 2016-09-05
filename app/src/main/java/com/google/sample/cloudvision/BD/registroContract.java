package com.google.sample.cloudvision.BD;

import android.provider.BaseColumns;

/**
 * Created by Vane on 31/08/2016.
 */
public class registroContract {
     public static abstract class registroEntry implements BaseColumns {
        public static final String TABLE_NAME = "registro";

        public static final String INDICE = "indice";
        public static final String TEXTO = "texto";
        public static final String CALIDAD = "calidad";

    }

}
