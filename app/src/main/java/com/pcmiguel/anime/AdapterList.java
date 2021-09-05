package com.pcmiguel.anime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {

    private List<AnimeList> animeList;
    final private AdapterList.ListItemClickListener mOnClickListener;
    private int rValue;

    public AdapterList(List<AnimeList> animeList, AdapterList.ListItemClickListener onClickListener) {
        this.animeList = animeList;
        this.mOnClickListener = onClickListener;

    }


    @NonNull
    @Override
    public AdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_design, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterList.ViewHolder holder, int position) {

        String image = animeList.get(position).getImagem();
        String nome = animeList.get(position).getNome();
        int totalEpisodios = animeList.get(position).getTotalEpisodios();
        int episodiosVistos = animeList.get(position).getEpisodiosVistos();
        int rated = animeList.get(position).getRated();
        int library = animeList.get(position).getLibrary();

        holder.setData(image, nome, totalEpisodios, episodiosVistos, rated, library);

    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img, rateImg, previous, next;
        private TextView text_nome, rateText, currentEpisode;
        private ProgressBar progressBar;
        private LinearLayout btnRate, optionsLibrary, status;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.anime_image);
            text_nome = itemView.findViewById(R.id.anime_nome);
            progressBar = itemView.findViewById(R.id.progressBar_anime_status);
            btnRate = itemView.findViewById(R.id.btn_rate);
            rateImg = itemView.findViewById(R.id.rate_img);
            rateText = itemView.findViewById(R.id.rate_text);
            currentEpisode = itemView.findViewById(R.id.current_episode);
            previous = itemView.findViewById(R.id.previous);
            next = itemView.findViewById(R.id.next);
            optionsLibrary = itemView.findViewById(R.id.optionsLibrary);
            status = itemView.findViewById(R.id.status);

        }

        public void setData(String image, String nome, int totalEpisodios, int episodiosVistos, final int rated, int library) {

            Picasso.with(img.getContext()).load(image).into(img);

            text_nome.setText(nome);

            progressBar.setMax(totalEpisodios);
            progressBar.setProgress(episodiosVistos);

            if (library != 5) {
                status.setVisibility(View.VISIBLE);
            }

            if (rated == 0) {
                rateImg.setImageResource(R.drawable.ic_round_radio_button_unchecked_24);
                rateText.setText("Not Rated");
                rateText.setTextColor(rateText.getResources().getColor(R.color.colorAccent));
            }
            else if (rated == 1) {
                rateImg.setImageResource(R.drawable.surprised_100px);
                rateText.setText("AWFUL");
                rateText.setTextColor(rateText.getResources().getColor(R.color.colorYellow));
            }
            else if (rated == 2) {
                rateImg.setImageResource(R.drawable.boring_100px);
                rateText.setText("MEH");
                rateText.setTextColor(rateText.getResources().getColor(R.color.colorYellow));
            }
            else if (rated == 3) {
                rateImg.setImageResource(R.drawable.lol_100px);
                rateText.setText("GOOD");
                rateText.setTextColor(rateText.getResources().getColor(R.color.colorYellow));
            }
            else if (rated == 4) {
                rateImg.setImageResource(R.drawable.smiling_100px);
                rateText.setText("GREAT");
                rateText.setTextColor(rateText.getResources().getColor(R.color.colorYellow));
            }

            currentEpisode.setText(episodiosVistos + " of " + totalEpisodios);

            btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                            v.getContext(), R.style.BottomSheetDialogTheme
                    );
                    View bottomSheetView = LayoutInflater.from(v.getContext())
                            .inflate(
                                    R.layout.layout_bottom_rate,
                                    (RelativeLayout) v.findViewById(R.id.bottomSheetRate)
                            );

                    TextView btnCancel, btnDone;
                    final ImageView rate0, rate1, rate2, rate3, rate4;


                    btnCancel = bottomSheetView.findViewById(R.id.btn_cancel);
                    btnDone = bottomSheetView.findViewById(R.id.btn_done);
                    rate0 = bottomSheetView.findViewById(R.id.rate0);
                    rate1 = bottomSheetView.findViewById(R.id.rate1);
                    rate2 = bottomSheetView.findViewById(R.id.rate2);
                    rate3 = bottomSheetView.findViewById(R.id.rate3);
                    rate4 = bottomSheetView.findViewById(R.id.rate4);

                    rValue = rated;

                    if (rated == 1) {
                        rate1.animate().alpha(1);
                    } else if (rated == 2) {
                        rate2.animate().alpha(1);
                    } else if (rated == 3) {
                        rate3.animate().alpha(1);
                    } else if (rated == 4) {
                        rate4.animate().alpha(1);
                    } else {
                        rate0.animate().alpha(1);
                    }

                    rate0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (rValue != 0) {
                                rValue = 0;
                                rate0.animate().alpha(1);
                                rate1.animate().alpha(0.3f);
                                rate2.animate().alpha(0.3f);
                                rate3.animate().alpha(0.3f);
                                rate4.animate().alpha(0.3f);
                            }
                        }
                    });

                    rate1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (rValue != 1) {
                                rValue = 1;
                                rate1.animate().alpha(1);
                                rate0.animate().alpha(0.3f);
                                rate2.animate().alpha(0.3f);
                                rate3.animate().alpha(0.3f);
                                rate4.animate().alpha(0.3f);
                            }
                        }
                    });

                    rate2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (rValue != 2) {
                                rValue = 2;
                                rate2.animate().alpha(1);
                                rate1.animate().alpha(0.3f);
                                rate0.animate().alpha(0.3f);
                                rate3.animate().alpha(0.3f);
                                rate4.animate().alpha(0.3f);
                            }
                        }
                    });

                    rate3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (rValue != 3) {
                                rValue = 3;
                                rate3.animate().alpha(1);
                                rate1.animate().alpha(0.3f);
                                rate2.animate().alpha(0.3f);
                                rate0.animate().alpha(0.3f);
                                rate4.animate().alpha(0.3f);
                            }
                        }
                    });

                    rate4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (rValue != 4) {
                                rValue = 4;
                                rate4.animate().alpha(1);
                                rate1.animate().alpha(0.3f);
                                rate2.animate().alpha(0.3f);
                                rate3.animate().alpha(0.3f);
                                rate0.animate().alpha(0.3f);
                            }
                        }
                    });

                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            int animeId = animeList.get(position).getId();
                            animeList.get(position).setRated(rValue);
                            mOnClickListener.onItemClick(position, rValue, animeId);
                            bottomSheetDialog.dismiss();
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.dismiss();
                        }
                    });

                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.setCanceledOnTouchOutside(false);
                    bottomSheetDialog.setCancelable(false);
                    bottomSheetDialog.show();

                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (animeList.get(position).getEpisodiosVistos() > 0) {
                        int animeId = animeList.get(position).getId();
                        animeList.get(position).setEpisodiosVistos(animeList.get(position).getEpisodiosVistos() - 1);
                        mOnClickListener.pnext(position, false, animeId);
                    }
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (animeList.get(position).getEpisodiosVistos() < animeList.get(position).getTotalEpisodios()) {
                        int animeId = animeList.get(position).getId();
                        animeList.get(position).setEpisodiosVistos(animeList.get(position).getEpisodiosVistos() + 1);
                        mOnClickListener.pnext(position, true, animeId);
                    }
                }
            });

            optionsLibrary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    int animeId = animeList.get(position).getId();
                    mOnClickListener.loadOptionsLibrary(position, animeId);
                }
            });

        }

    }

    interface ListItemClickListener {
        void onItemClick(int position, int rate, int animeId);
        void pnext(int position, boolean next, int animeId);
        void loadOptionsLibrary(int position, int animeId);
    }
}
