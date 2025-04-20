package co.edu.uniminuto.activity50.dataacces;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import kotlin.text.UStringsKt;
public class ManagerDataBase extends SQLiteOpenHelper {
    private static final String DATA_BASE = "db_users";
    private static final int VERSION = 1;
    private static final String TABLE_USERS = "users";
    public ManagerDataBase(@Nullable Context context) {
        super(context, DATA_BASE, null , VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_USERS = "CREATE TABLE " + TABLE_USERS + "(use_document INTEGER PRIMARY KEY, " +
                "use_name varchar (200) NOT NULL, " +
                "use_lastname varchar (200) NOT NULL, use_user varchar (35) NOT NULL, use_pass varchar (35) NOT NULL," +
                "use_status varchar (1));";
        db.execSQL(CREATE_USERS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
