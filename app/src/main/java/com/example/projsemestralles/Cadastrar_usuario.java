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

import org.w3c.dom.Text;

public class Cadastrar_usuario extends AppCompatActivity {
    Button cadastrar;
    EditText nome, login, senha;
    private FirebaseDatabase banco;
    private static final String nuser="user";
    private DatabaseReference mBanco;
    private FirebaseAuth mAuth;
    private User user;
    private static final String TAG = "Cadastrar_usuario";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);
        cadastrar = (Button)findViewById(R.id.btnCadastrar);
        nome = (EditText)findViewById(R.id.txtNome);
        login = (EditText)findViewById(R.id.txtLogin);
        senha = (EditText)findViewById(R.id.txtSenha);
        banco = FirebaseDatabase.getInstance();
        mBanco = banco.getReference(nuser);
        mAuth = FirebaseAuth.getInstance();



        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String varlogin = login.getText().toString();
                String varsenha = senha.getText().toString();
                if(TextUtils.isEmpty(varlogin) || TextUtils.isEmpty(varsenha)){
                    Toast.makeText(getApplicationContext(), "INSIRA O LOGIN E A SENHA CORRETAMENTE", Toast.LENGTH_SHORT).show();

                }
                String varnome = nome.getText().toString();
                user = new User(varnome, varlogin, varsenha);
                Novo_user(varlogin, varsenha);
            }
        });


    }

    public void Novo_user(String vLog, String vSen){
        mAuth.createUserWithEmailAndPassword(vLog, vSen)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Cadastro bem sucedido!");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Cadastrar_usuario.this, "Falha no cadastro.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser userAtual){
        String KeyID = mBanco.push().getKey();
        mBanco.child(KeyID).setValue(user);
        Intent tela = new Intent(Cadastrar_usuario.this, MainActivity.class);
        startActivity(tela);
    }

}