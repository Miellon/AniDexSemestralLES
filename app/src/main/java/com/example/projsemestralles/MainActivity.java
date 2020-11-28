package com.example.projsemestralles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = firebaseDatabase.getReference();
    Button cadastrar, logar;
    EditText login, senha;
    private FirebaseDatabase banco;
    private static final String nuser="user";
    private DatabaseReference mBanco;
    private FirebaseAuth mAuth;
    private User user;
    private static final String TAG = "Cadastrar_usuario";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cadastrar = (Button)findViewById(R.id.btnCadastrar);
        logar = (Button)findViewById(R.id.btnLogar);
        login = (EditText)findViewById(R.id.txtLogin);
        senha = (EditText)findViewById(R.id.txtSenha);

        banco = FirebaseDatabase.getInstance();
        mBanco = banco.getReference(nuser);
        mAuth = FirebaseAuth.getInstance();

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreCadastrar();
            }
        });
        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String varlogin = login.getText().toString();
                String varsenha = senha.getText().toString();
                if(TextUtils.isEmpty(varlogin) || TextUtils.isEmpty(varsenha)){
                    Toast.makeText(getApplicationContext(), "INSIRA O LOGIN E A SENHA CORRETAMENTE", Toast.LENGTH_SHORT).show();

                }

                funcLogar(varlogin, varsenha);
            }
        });

    }

    public void abreCadastrar(){
        Intent tela = new Intent(MainActivity.this, Cadastrar_usuario.class);
        startActivity(tela);
    }

    public void funcLogar(String vLog, String vSenha){
        mAuth.signInWithEmailAndPassword(vLog, vSenha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "FALHA NA AUTENTICAÇÃO.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            updateUI(currentUser);
        }
    }

    public void updateUI(FirebaseUser userAtual){
        Intent tela = new Intent(MainActivity.this, IndexUser.class);
        tela.putExtra("email", userAtual.getEmail());
        startActivity(tela);
    }

}