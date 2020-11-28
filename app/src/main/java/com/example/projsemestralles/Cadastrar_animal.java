package com.example.projsemestralles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cadastrar_animal extends AppCompatActivity {
    private Button cadastrar_animal, selecionar_foto;
    private EditText especie, peso, comprimento, descricao;
    private Uri caminhoImagem;


    private static final int PICK_IMAGE_REQUEST=1;
    StorageReference mStorageReference;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private List<Animal> animalList = new ArrayList<>();
    private ArrayAdapter<Animal> animalArrayAdapter;
    private String nomeArquivo=null;
    private EditText edIndice;
    private ImageView imUpload;
    private StorageTask uploadTask;
    Animal ani = new Animal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_animal);
        cadastrar_animal = (Button)findViewById(R.id.btnCadastrar_animal);
        selecionar_foto = (Button)findViewById(R.id.btnSelecionar_foto);
        especie = (EditText) findViewById(R.id.txtEspecie);
        peso = (EditText)findViewById(R.id.txtPeso);
        comprimento = (EditText)findViewById(R.id.txtComprimento);
        descricao = (EditText)findViewById(R.id.txtDescricao);
        imUpload = (ImageView)findViewById(R.id.imgAnimal);
        mStorageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Animals");
        iniciarFirebase();
        botoes();
    }

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
        Intent tela = new Intent(Cadastrar_animal.this, IndexUser.class);
        startActivity(tela);
    }

    public void abreCadastrarAnimal(){
        Intent tela = new Intent(Cadastrar_animal.this, Cadastrar_animal.class);
        startActivity(tela);
    }

    public void abreComparar(){
        Intent tela = new Intent(Cadastrar_animal.this, Comparar_animais.class);
        startActivity(tela);
    }

    private void iniciarFirebase(){
        FirebaseApp.initializeApp(Cadastrar_animal.this);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        pesquisa("");
    }

    private void botoes(){
        selecionar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscaFoto();
            }
        });

        cadastrar_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask !=null && uploadTask.isInProgress()){
                    Toast.makeText(Cadastrar_animal.this, "Aguarde o fim do upload atual", Toast.LENGTH_LONG).show();
                }else {
                    uploadFoto();
                }
            }
        });
    }

    private void buscaFoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            caminhoImagem=data.getData();
            Picasso.with(this).load(caminhoImagem).into(imUpload);
        }
    }
    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFoto(){
        final StorageReference ref = mStorageReference.child(System.currentTimeMillis()+"."+getExtension(caminhoImagem));
        uploadTask= ref.putFile(caminhoImagem)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri){
                                Uri downloadurl = uri;
                                String download = downloadurl.toString();
                                Toast.makeText(getApplicationContext(), "Carregou com sucesso", Toast.LENGTH_LONG).show();
                                Animal ani = new Animal(especie.getText().toString().trim(), peso.getText().toString().trim(), comprimento.getText().toString().trim(), descricao.getText().toString().trim(), download);
                                String uploadId = databaseReference.push().getKey();
                                databaseReference.child(uploadId).setValue(ani);
                            }
                        }

                        );}
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Algum erro", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void pesquisa(String nome){
        Query query;
        query = FirebaseDatabase.getInstance().getReference("animals");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animalList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(dataSnapshot1.exists()){
                        Animal animal1 = dataSnapshot1.getValue(Animal.class);
                        animalList.add(animal1);
                    }
                }
                animalArrayAdapter = new ArrayAdapter<Animal>(Cadastrar_animal.this, android.R.layout.simple_list_item_1, animalList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}