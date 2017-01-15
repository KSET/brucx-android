package kset.ivan.rasus.brucx;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends ActionBarActivity {

    Button login;
    EditText username;
    EditText password;
    Context context = this;

    private static String ADMIN = "admin";
    private static String PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.id_username);
        password = (EditText) findViewById(R.id.id_password);
        login = (Button) findViewById(R.id.id_loginbtn);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = username.getText().toString();
                String pass = password.getText().toString();
                if(usr.equals(ADMIN) && pass.equals(PASSWORD)){
                    Toast.makeText(context, "Loggin successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, Options.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
