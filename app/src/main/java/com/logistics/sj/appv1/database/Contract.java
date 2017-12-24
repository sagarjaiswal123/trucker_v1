package com.logistics.sj.appv1.database;

import android.provider.BaseColumns;

/**
 * Created by AJ on 03-05-2017.
 */

public class Contract {


    public static final class TableColumns implements BaseColumns {

        private  TableColumns(){}

        public static  final String TRUCK_TABLE="TRUCK_TABLE";
        public static  final String TRUCK_NUMBER="TRUCK_NUMBER";
        public static  final String TRUCK_OWNER_NAME="TRUCK_OWNER_NAME";
        public static  final String TRUCK_OWNER_NUMBER_ARRAY="PHONE_NUMBER";
        public static  final String ACCOUNT_NUMBER_ARRAY="PHONE_NUMBER";
    }

}
