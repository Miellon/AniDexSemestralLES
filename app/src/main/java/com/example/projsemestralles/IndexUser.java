package com.example.projsemestralles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class IndexUser extends AppCompatActivity implements ImageAdapter.OnItemClickListener {

    RecyclerView rec;
    private ImageAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Animal> mAnimals;

    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private ValueEventListener mDBListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_user);
        rec = findViewById(R.id.RecView);
        rec.setHasFixedSize(true);

        rec.setLayoutManager(new LinearLayoutManager(this));

        mAnimals = new ArrayList<>();
        mStorage = FirebaseStorage.getInstance();
        mAdapter = new ImageAdapter(IndexUser.this, mAnimals);
        rec.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(IndexUser.this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Animals");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mAnimals.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Animal animal = postSnapshot.getValue(Animal.class);
                    animal.setKey(postSnapshot.getKey());
                    mAnimals.add(animal);

                }
                mAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(IndexUser.this, "ALGUM ERRO!", Toast.LENGTH_SHORT).show();
            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.Index:
                abreIndex();
                return(true);
            case R.id.CadastrarAnimal:
                abreCadastrarAnimal();
                return(true);
            case R.id.CompararAnimais:
                abreComparar();
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    public void abreIndex(){
        Intent tela = new Intent(IndexUser.this, IndexUser.class);
        startActivity(tela);
    }

    public void abreCadastrarAnimal(){
        Intent tela = new Intent(IndexUser.this, Cadastrar_animal.class);
        startActivity(tela);
    }

    public void abreComparar(){
        Intent tela = new Intent(IndexUser.this, Comparar_animais.class);
        startActivity(tela);
    }

    @Override
    public void onItemClick(int position) {
        Animal selectedItem = mAnimals.get(position);
        final String selectedKey = selectedItem.getKey();
    }

    @Override
    public void onEditarClick(int position) {
        Animal selectedItem = mAnimals.get(position);
        String selectedKey = selectedItem.getKey();
        Intent tela = new Intent(IndexUser.this, Alterar_animal.class);
        tela.putExtra("keyConsulta", selectedKey);
        startActivity(tela);
    }

    @Override
    public void onDeletarClick(int position) {
        Animal selectedItem = mAnimals.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getLocal_foto());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(IndexUser.this, "IMAGEM DELETADA COM SUCESSO", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}