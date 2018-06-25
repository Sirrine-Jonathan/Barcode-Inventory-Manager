package byui_cs246.barcodeinventorymanager;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

/*
    hopefully this class can be removed after migrating to database v2
 */
public class IntTypeConverter {

    @TypeConverter
    public static int toInt(String st){
        // not the best way of converting
        return (st == null) ? null : (st.length());
    }

    @TypeConverter
    public static String toString(int num){
        return Integer.toString(num);
    }
}
