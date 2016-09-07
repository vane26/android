package com.google.sample.cloudvision.BD;

import android.provider.BaseColumns;

/**
 * Created by Vane on 31/08/2016.
 */
public class registroContract {
     public static abstract class registroEntry implements BaseColumns {
        public static final String table_name = "registro";

        public static final String indice = "indice";
        public static final String texto = "texto";
        public static final String calidad = "calidad";

    }

}
