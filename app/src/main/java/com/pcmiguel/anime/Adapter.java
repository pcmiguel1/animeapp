package com.pcmiguel.anime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Anime> animeList;
    final private ListItemClickListener mOnClickListener;
    public Adapter (List<Anime> animeList, ListItemClickListener onClickListener) {
        this.animeList = animeList;
        this.mOnClickListener = onClickListener;

    }


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);

        ImageView img = view.findViewById(R.id.epi_vol);
        if (!MainActivity.ANIME) img.setImageResource(R.drawable.ic_baseline_bookmark_24);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        String image = animeList.get(position).getImagem();
        String nome = animeList.get(position).getNome();
        String ano = animeList.get(position).getAno();
        String episodios = animeList.get(position).getEpisodios();
        String classificacao = animeList.get(position).getClassificacao();

        holder.setData(image, nome, ano, episodios, classificacao);

    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView img;
        private TextView text_nome, text_ano, text_episodios, text_classificacao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.anime_image);
            text_nome = itemView.findViewById(R.id.anime_nome);
            text_ano = itemView.findViewById(R.id.anime_ano);
            text_episodios = itemView.findViewById(R.id.anime_episodios);
            text_classificacao = itemView.findViewById(R.id.anime_classificacao);

            itemView.setOnClickListener(this);

        }

        public void setData(String image, String nome, String ano, String episodios, String classificacao) {

            Picasso.with(img.getContext()).load(image).into(img);

            text_nome.setText(nome);
            text_ano.setText(ano);
            text_episodios.setText(episodios);
            text_classificacao.setText(classificacao);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }

    interface ListItemClickListener{
        void onListItemClick(int position);
    }

}
