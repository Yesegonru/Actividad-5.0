package co.edu.uniminuto.activity50;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import co.edu.uniminuto.activity50.entities.User;
import co.edu.uniminuto.activity50.repository.UserRepository;

public class MainActivity extends AppCompatActivity {
    //declaración de campos de la vista
    private Context context;
    private EditText etDocumento;
    private EditText etUsuario;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etContrasena;

    private ListView listView;

    private ListView listUsers;
    private SQLiteDatabase sqLiteDatabase;

    private Button btnGuardar;

    private Button btnBorrar;
    private Button btnListUsers;
    private int documento;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String pass;

    /// /busqueda
    private Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.context = this;
        begin();
        btnGuardar.setOnClickListener(this::createUser); //crear user (hecho en clase)
        btnListUsers.setOnClickListener(this::listUsers);//listar usuarios (hecho en clase)
        btnBuscar.setOnClickListener(this::buscarUsuario); //buscar usuario
        btnBorrar.setOnClickListener(this::borrarUsuario); //borrar usuario
    }

    /// crear usuario
    private void createUser(View view) {
        capData();///captura datos
        User user = new User(documento, nombres, apellidos, usuario, pass);//crea objeto
        UserRepository userRepository = new UserRepository(context, view);
        userRepository.insertUser(user);//llama al metodo insertUser
        /// invocar el metodo para listar los usuarios
        listUsers(view);
    }

    //captura de datos
    private void capData(){
        //validaciones de los campos -- regex
        this.documento = Integer.parseInt(this.etDocumento.getText().toString());//cap documento
        this.nombres = this.etNombres.getText().toString();//captura el nombre
        this.apellidos = this.etApellidos.getText().toString();//captura el apellido
        this.usuario = this.etUsuario.getText().toString();//captura el usuario
        this.pass = this.etContrasena.getText().toString();//capt la contraseñaa
    }
    /// Buscar usuario - metodo
    private void buscarUsuario(View view) {
        int documento = Integer.parseInt(etDocumento.getText().toString());//buscar por documento
        UserRepository userRepository = new UserRepository(context, view);
        User userEncontrado = userRepository.getUserByDocument(documento);// busca en la bn

        if (userEncontrado != null) {
            /// los datos que se muestran en los campos - al encontrarlo
            this.etNombres.setText(userEncontrado.getName());
            this.etApellidos.setText(userEncontrado.getLastName());
            this.etUsuario.setText(userEncontrado.getUser());
            this.etContrasena.setText(userEncontrado.getPass());
        } else{
            /// x si no esta en la db
            Snackbar.make(view, "No se encontro el usuario", Snackbar.LENGTH_LONG).show();//error
        }
    }
    /// Borrar usuario (cambio de status de 1 a 0)
    private void borrarUsuario(View view) {
        try {
            // Obtiene el documento ingresado y jala información
            int documento = Integer.parseInt(etDocumento.getText().toString());//Obtiene el documento
            UserRepository userRepository = new UserRepository(context, view);//ayuda a llamar el metodo borrar user
            boolean actualizado = userRepository.borrarUsuarioDco(documento);//llama metodo para actualizar el status
            // Si se actualizó correctamente, actualiza la list
            if (actualizado) {
                listUsers(view);  // actualiza la listuser
            }
        } catch (NumberFormatException e) {//error con # doc no validos
            Snackbar.make(view, "Ingrese un # documento valido", Snackbar.LENGTH_LONG).show();//error
        } catch (Exception e) {
            Snackbar.make(view, "Error al intentar borrar el usuario", Snackbar.LENGTH_LONG).show();//en caso de errores
        }
    }//fin borrar usuario
    private void begin (){
        this.etNombres = findViewById(R.id.etNombres);
        this.etApellidos = findViewById(R.id.etApellidos);
        this.etDocumento = findViewById(R.id.etDocumento);
        this.etUsuario = findViewById(R.id.etUsuario);
        this.etContrasena = findViewById(R.id.etContrasena);
        this.listUsers = findViewById(R.id.lvLista);
        this.btnGuardar = findViewById(R.id.btnRegister);
        this.btnListUsers = findViewById(R.id.btnListar);
        this.btnBuscar = findViewById(R.id.btnBuscar);
        this.btnBorrar = findViewById(R.id.btnBorrar);
        this.context = this;
    }//fin begin

    //Listar los usuarios para mostrarlos en la lista
    private void listUsers(View view) {
        UserRepository userRepository = new UserRepository(context, view);
        ArrayList<User> usuariosList = userRepository.getUserList();
        ArrayAdapter<User> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, usuariosList);
        listUsers.setAdapter(adapter);
    }///fin listUsers
    //Limpiar campos
    private void limpiarCampos() {
        etDocumento.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        etUsuario.setText("");
        etContrasena.setText("");
    }
}// fin codigo