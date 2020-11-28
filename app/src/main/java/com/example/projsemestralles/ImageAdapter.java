package com.example.projsemestralles;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Animal> mAnimals;
    private OnItemClickListener mListener;
    public ImageAdapter(Context context, List<Animal> animals){
        mContext = context;
        mAnimals = animals;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.linha_model, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Animal animalCurrent = mAnimals.get(position);
        holder.txtEspecie.setText(animalCurrent.getEspecie());
        holder.txtComprimento.setText("COMPRIMENTO - "+animalCurrent.getComprimento()+"m");
        holder.txtPeso.setText("PESO - "+animalCurrent.getPeso()+"KG");
        Picasso.with(mContext)
            .load(animalCurrent.getLocal_foto())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imgAnimal);
    }

    @Override
    public int getItemCount() {
        return mAnimals.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        public TextView txtEspecie;
        public ImageView imgAnimal;
        public TextView txtPeso;
        public TextView txtComprimento;

        public ImageViewHolder(View itemView) {
            super(itemView);

            txtEspecie = itemView.findViewById(R.id.txtVEspecie);
            txtComprimento = itemView.findViewById(R.id.txtVComprimento);
            txtPeso = itemView.findViewById(R.id.txtVPeso);
            imgAnimal = itemView.findViewById(R.id.img_Animal);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("SELECIONE UMA OPÇÃO");
            MenuItem editar = menu.add(Menu.NONE, 1, 1, "EDITAR");
            MenuItem deletar = menu.add(Menu.NONE, 2, 2, "DELETAR");
            editar.setOnMenuItemClickListener(this);
            deletar.setOnMenuItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener!=null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION);{
                    switch (item.getItemId()){
                        case 1:
                            mListener.onEditarClick(position);
                            return true;
                        case 2:
                            mListener.onDeletarClick(position);
                            return true;
                    }

                }
            }
            return false;
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onEditarClick(int position);
        void onDeletarClick(int position);

    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

}
