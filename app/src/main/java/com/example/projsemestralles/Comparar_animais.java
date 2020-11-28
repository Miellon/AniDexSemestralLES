package com.example.projsemestralles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Comparar_animais extends AppCompatActivity {
Spinner SpinAnimal1, SpinAnimal2;
private DatabaseReference comparando;
String animalseleiconado="";
LinearLayout layoutbicho1, layoutbicho2;
TextView ExEspecie1, ExPeso1, ExComprimento1, ExDescricao1, ExEspecie2, ExPeso2, ExComprimento2, ExDescricao2;
ImageView ImagemBicho1, ImagemBicho2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparar_animais);
        SpinAnimal1 = (Spinner)findViewById(R.id.spnAnimal1);
        SpinAnimal2 = (Spinner)findViewById(R.id.spnAnimal2);
        comparando = FirebaseDatabase.getInstance().getReference();
        layoutbicho1 = (LinearLayout) findViewById(R.id.bicho1Info) ;
        layoutbicho2 = (LinearLayout)findViewById(R.id.bicho2Info);
        carregaAnimais();

        ExEspecie1 = (TextView)findViewById(R.id.txtEspecie1);
        ExPeso1 = (TextView)findViewById(R.id.txtPeso1);
        ExComprimento1 = (TextView)findViewById(R.id.txtComprimento1);
        ExDescricao1 = (TextView)findViewById(R.id.txtDescricao1);
        ImagemBicho1 = (ImageView)findViewById(R.id.imgBicho1);

        ExEspecie2 = (TextView)findViewById(R.id.txtEspecie2);
        ExPeso2 = (TextView)findViewById(R.id.txtPeso2);
        ExComprimento2 = (TextView)findViewById(R.id.txtComprimento2);
        ExDescricao2 = (TextView)findViewById(R.id.txtDescricao2);
        ImagemBicho2 = (ImageView)findViewById(R.id.imgBicho2);
    }

    public void carregaAnimais(){
        final List<Animal> animais = new ArrayList<>();
        animais.add(new Animal("","","","",""));
        String id1, id2;
        comparando.child("Animals").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){

                        String especie = ds.child("especie").getValue().toString();
                        String peso = ds.child("peso").getValue().toString();
                        String comprimento = ds.child("comprimento").getValue().toString();
                        String descricao = ds.child("descricao").getValue().toString();
                        String image = ds.child("local_foto").getValue().toString();
                        animais.add(new Animal(especie, peso, comprimento, descricao, image ));
                    }

                    final ArrayAdapter<Animal> arrayAdapter = new ArrayAdapter<>(Comparar_animais.this, android.R.layout.simple_dropdown_item_1line, animais);
                    SpinAnimal1.setAdapter(arrayAdapter);


                    SpinAnimal1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            animalseleiconado=adapterView.getItemAtPosition(i).toString();
                            if(animais.get(i).getEspecie().toString() == ""){
                                layoutbicho1.setVisibility(View.GONE);
                            }
                            else{
                                layoutbicho1.setVisibility(View.VISIBLE);
                                ExEspecie1.setText(animais.get(i).getEspecie());
                                ExPeso1.setText("PESO: "+animais.get(i).getPeso()+"KG");
                                ExComprimento1.setText("COMPRIMENTO: "+animais.get(i).getComprimento()+"m");
                                ExDescricao1.setText("DESCRIÇÃO: "+animais.get(i).getDescricao());
                                Picasso.with(Comparar_animais.this)
                                        .load(animais.get(i).getLocal_foto())
                                        .placeholder(R.mipmap.ic_launcher)
                                        .into(ImagemBicho1);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            layoutbicho1.setVisibility(View.GONE);
                        }
                    });
                    SpinAnimal2.setAdapter(arrayAdapter);
                    SpinAnimal2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            animalseleiconado=parent.getItemAtPosition(position).toString();
                            if(animais.get(position).getEspecie().toString() == ""){
                                layoutbicho2.setVisibility(View.GONE);
                            }else {
                                layoutbicho2.setVisibility(View.VISIBLE);
                                ExEspecie2.setText(animais.get(position).getEspecie());
                                ExPeso2.setText("PESO: "+animais.get(position).getPeso()+"KG");
                                ExComprimento2.setText("COMPRIMENTO: "+animais.get(position).getComprimento()+"m");
                                ExDescricao2.setText("DESCRIÇÃO: "+animais.get(position).getDescricao());
                                Picasso.with(Comparar_animais.this)
                                        .load(animais.get(position).getLocal_foto())
                                        .placeholder(R.mipmap.ic_launcher)
                                        .into(ImagemBicho2);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            layoutbicho2.setVisibility(View.GONE);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}