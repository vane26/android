package com.google.sample.cloudvision.BD;

import android.provider.BaseColumns;

/**
 * Created by Vane on 31/08/2016.
 */
public class RegistroContract {
     public static abstract class RegistroEntry implements BaseColumns {
        public static final String table_name = "registro";

        //public static final int id = 0 ;
        public static final String indice = "indice";
        public static final String texto = "texto";
        public static final String calidad = "calidad";
        public static final String imagen = "imagen";

    }

}
