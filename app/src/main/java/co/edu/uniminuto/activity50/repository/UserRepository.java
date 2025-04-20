package co.edu.uniminuto.activity50.repository;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import co.edu.uniminuto.activity50.dataacces.ManagerDataBase;
import co.edu.uniminuto.activity50.entities.User;

public class UserRepository {
    private ManagerDataBase dataBase;
    private Context context;
    private View view;
    private User user;
    private TextView etDocumento;
    /// Constructor inicializa context, view y dataBase
    public UserRepository(Context context, View view) {
        this.context = context;
        this.view = view;
        this.dataBase = new ManagerDataBase(context);
    }
    ///  Metodo para insertar un usuario new
    public void insertUser(User user) {
        try {
            SQLiteDatabase dataBaseSql = dataBase.getWritableDatabase();//obtiene la db en mood escritura
            if (dataBaseSql != null) {
                ContentValues values = new ContentValues();
                values.put("use_document", user.getDocument());
                values.put("use_name", user.getName());
                values.put("use_lastname", user.getLastName());
                values.put("use_user", user.getUser());
                values.put("use_pass", user.getPass());
                values.put("use_status", "1");//asigna estado en 1 automaticamente
                long response = dataBaseSql.insert("users", null, values);//insert user
                String message = (response > 1) ? "Se registro correctamente" //si se registro?
                        : "No se pudo registrar";//lo valida..
                Snackbar.make(this.view, message, Snackbar.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            Log.i("Error en bases de datos", "insertUser: "+e.getMessage());
            e.printStackTrace();
        }
    }
    ///  para llenar la lista -- lista de usuarios
    public ArrayList<User> getUserList() {
        SQLiteDatabase dataBaseSql = dataBase.getReadableDatabase();//lee la db
        String query = "SELECT * FROM users WHERE use_status = 1;";// consulta a la db
        ArrayList<User> users = new ArrayList<>(); //crea una lista
        Cursor cursor = dataBaseSql.rawQuery(query, null); //ejecuta la consulta
        if (cursor.moveToFirst()) {
            do {
                User user = new User();//crea un nuevo user los demas obtienen datos y los asigna
                user.setDocument(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
                user.setUser(cursor.getString(3));
                user.setPass(cursor.getString(4));
                user.setStatus(cursor.getString(5));
                users.add(user);
            } while (cursor.moveToNext());
        }
            cursor.close();
            dataBaseSql.close();
            return users;
        }//fin
        /// Busqueda de el numero de documento
    public User getUserByDocument(int document) {

        SQLiteDatabase dataBaseSql = dataBase.getReadableDatabase();// Se lee la BD
        User user = null;
        //Consulta que busca al usuario en la db con el # documento
        //String query = "SELECT * FROM users WHERE use_document = ? AND use_status = 1";//se valida el status
        String query = "SELECT * FROM users WHERE use_document = ?";
        Cursor cursor = null;
        try{
            //Ejecutar consulta con el numero de documento
            cursor = dataBaseSql.rawQuery(query, new String[]{String.valueOf(document)});
            //Si hay resultados
            if (cursor.moveToFirst()) {
                user = new User();
                user.setDocument(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
                user.setUser(cursor.getString(3));
                user.setPass(cursor.getString(4));
                user.setStatus(cursor.getString(5));
            }
        } catch (SQLException e) {//Si hay un error se muestra el log
            Log.i("Error en bases de datos", "getUserByDocument: "+e.getMessage());
            e.printStackTrace();
        } finally {
            //cerrar la conexion
            if (cursor != null) cursor.close();
            dataBaseSql.close();
        }return user;// x si no se encuentra el usuario
    }// fin busqda

    /// metodo para eliminar un usuario (cambiar a 0)
    public boolean borrarUsuarioDco(int document) {
        SQLiteDatabase db  = dataBase.getWritableDatabase();
        boolean actualizado = false;// si se actualiza..

        try {
            ContentValues values = new ContentValues();
            values.put("use_status", "0");///se actualiza el status a 0 no se borra
            int filas = db.update("users", values, "use_document = ?", new String[]{String.valueOf(document)});
            /// si se actualiza la infor..
            if (filas > 0) {
                actualizado = true;
                Snackbar.make(view, "Usuario eliminado (inactivo)", Snackbar.LENGTH_LONG).show();
            } else{
                Snackbar.make(view, "Usuario no encotrado", Snackbar.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            Log.i("DB ERROR", "Error en bases de datos"+e.getMessage());
            Snackbar.make(view,
                    "Error al borrar", Snackbar.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return actualizado;
    }//FIN BORRAR
    }///fin code

