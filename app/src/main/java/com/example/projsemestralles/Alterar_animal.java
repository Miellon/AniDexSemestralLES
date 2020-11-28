package com.example.projsemestralles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Alterar_animal extends AppCompatActivity {
    EditText textoEspecie, textoPeso, textoComprimento, textoDescricao;
    ImageView teste;
    Button btnSalvar;
    DatabaseReference consulta;
    Context nContext;
    String Key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_animal);
        textoEspecie = (EditText)findViewById(R.id.edtEspecie);
        textoPeso = (EditText)findViewById(R.id.edtPeso);
        textoComprimento = (EditText)findViewById(R.id.edtComprimento);
        textoDescricao = (EditText)findViewById(R.id.edtDescricao);
        teste = (ImageView)findViewById(R.id.fotoBicho);
        btnSalvar= (Button)findViewById(R.id.btnSalvar_Alteracoes);

        Intent intent = getIntent();
        Key = intent.getStringExtra("keyConsulta");

        consulta = FirebaseDatabase.getInstance().getReference().child("Animals/"+Key);
        consulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String especie = dataSnapshot.child("especie").getValue().toString();
                String peso = dataSnapshot.child("peso").getValue().toString();
                String comprimento = dataSnapshot.child("comprimento").getValue().toString();
                String descricao = dataSnapshot.child("descricao").getValue().toString();
                String imgAnimal = dataSnapshot.child("local_foto").getValue().toString();
                textoEspecie.setText(especie);
                textoPeso.setText(peso);
                textoComprimento.setText(comprimento);
                textoDescricao.setText(descricao);
                Picasso.with(nContext)
                        .load(imgAnimal)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(teste);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botoes(Key, textoEspecie.getText().toString(), textoPeso.getText().toString(), textoComprimento.getText().toString(), textoDescricao.getText().toString());
                voltaIndex();
            }
        });

    }
    public void botoes(String chave, String esp, String pes, String comp, String desc){
        Map<String, Object> updateNovo = new HashMap<>();
        updateNovo.put("/comprimento", comp);
        updateNovo.put("/descricao", desc);
        updateNovo.put("/especie", esp);
        updateNovo.put("/peso", pes);
        consulta.updateChildren(updateNovo);
    }
    public void voltaIndex(){
        Intent tela = new Intent(Alterar_animal.this, IndexUser.class);
        startActivity(tela);
    }
}