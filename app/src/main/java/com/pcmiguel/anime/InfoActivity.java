package com.pcmiguel.anime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoActivity extends AppCompatActivity {

    private ImageView anime_img, seriesVolumes;
    private TextView anime_nome, anime_generos, anime_ano, anime_episodios, anime_classificacao, anime_text;
    private Button btnLibrary;

    private int id;
    private String nome;
    private String image_url;
    private String totalEpisodios;
    private int genero;
    private int library;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        requestQueue = Volley.newRequestQueue(this);

        anime_img = findViewById(R.id.anime_info_img);
        anime_nome = findViewById(R.id.anime_info_nome);
        anime_generos = findViewById(R.id.anime_info_generos);
        anime_ano = findViewById(R.id.anime_info_ano);
        anime_episodios = findViewById(R.id.anime_info_episodios);
        anime_classificacao = findViewById(R.id.anime_info_classificacao);
        anime_text = findViewById(R.id.text_info_anime);
        seriesVolumes = findViewById(R.id.series_volumes);

        btnLibrary = findViewById(R.id.btn_library);

        if (getIntent().hasExtra("id")) {
            id = getIntent().getIntExtra("id", 0);
            getInfo(id);

        }
        getInfoAnimeDB();

        if (!MainActivity.ANIME) {
            seriesVolumes.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }


    }

    private void getInfoAnimeDB() {

        String url = MainActivity.HOST_URL + "/api/animes/"+id;

        if (MainActivity.ANIME) {

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {

                                try {

                                    JSONObject object = new JSONObject(response);

                                    library = object.getInt("anime_library");

                                    if (object.getInt("anime_library") == 1) {
                                        btnLibrary.setText("Watching");
                                    }
                                    else if (object.getInt("anime_library") == 2) {
                                        btnLibrary.setText("Want To Watch");
                                    }
                                    else if (object.getInt("anime_library") == 3) {
                                        btnLibrary.setText("On Hold");
                                    }
                                    else if (object.getInt("anime_library") == 4) {
                                        btnLibrary.setText("Dropped");
                                    }
                                    else if (object.getInt("anime_library") == 5) {
                                        btnLibrary.setText("Completed");
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(request);

        } else {
            url = MainActivity.HOST_URL + "/api/mangas/"+id;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {

                                try {

                                    JSONObject object = new JSONObject(response);

                                    library = object.getInt("manga_library");

                                    if (object.getInt("manga_library") == 1) {
                                        btnLibrary.setText("Reading");
                                    }
                                    else if (object.getInt("manga_library") == 2) {
                                        btnLibrary.setText("Want To Read");
                                    }
                                    else if (object.getInt("manga_library") == 3) {
                                        btnLibrary.setText("On Hold");
                                    }
                                    else if (object.getInt("manga_library") == 4) {
                                        btnLibrary.setText("Dropped");
                                    }
                                    else if (object.getInt("manga_library") == 5) {
                                        btnLibrary.setText("Completed");
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(request);
        }

    }

    private void getInfo(final int id) {

        String url = "https://api.jikan.moe/v3/anime/"+id;

        if (!MainActivity.ANIME) {
            url = "https://api.jikan.moe/v3/manga/"+id;

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {

                                try {

                                    JSONObject object = new JSONObject(response);

                                    nome = object.getString("title");
                                    image_url = object.getString("image_url");


                                    totalEpisodios = (object.getString("volumes").equals("null")) ? "0" : object.getString("volumes");
                                    genero = 1;

                                    Picasso.with(getApplicationContext()).load(image_url).into(anime_img);
                                    anime_nome.setText(nome);
                                    anime_ano.setText(object.getString("title"));
                                    anime_episodios.setText(String.valueOf(totalEpisodios));
                                    anime_classificacao.setText(object.getString("score"));
                                    anime_text.setText(object.getString("synopsis"));

                                    getGeneros(id);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(request);

        } else {

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {

                                try {

                                    JSONObject object = new JSONObject(response);

                                    nome = object.getString("title");
                                    image_url = object.getString("image_url");
                                    totalEpisodios = (object.getString("episodes").equals("null")) ? "0" : object.getString("episodes");
                                    genero = 1;

                                    Picasso.with(getApplicationContext()).load(image_url).into(anime_img);
                                    anime_nome.setText(nome);
                                    anime_ano.setText(object.getString("title"));
                                    anime_episodios.setText(String.valueOf(totalEpisodios));
                                    anime_classificacao.setText(object.getString("score"));
                                    anime_text.setText(object.getString("synopsis"));

                                    getGeneros(id);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(request);

        }
    }

    private void openChooseLibrary() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                InfoActivity.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_library,
                        (LinearLayout) findViewById(R.id.bottomSheetLibrary)
                );

        LinearLayout nevermind, watching, wantToWatch, onHold, dropped, completed, removeLibrary, reading, wantToRead;

        nevermind = bottomSheetView.findViewById(R.id.nevermind);
        watching = bottomSheetView.findViewById(R.id.watching);
        wantToWatch = bottomSheetView.findViewById(R.id.wantToWatch);
        onHold = bottomSheetView.findViewById(R.id.onHold);
        dropped = bottomSheetView.findViewById(R.id.dropped);
        completed = bottomSheetView.findViewById(R.id.completed);
        removeLibrary = bottomSheetView.findViewById(R.id.removeLibrary);
        reading = bottomSheetView.findViewById(R.id.reading);
        wantToRead = bottomSheetView.findViewById(R.id.wantToRead);

        if (MainActivity.ANIME) {
            reading.setVisibility(View.GONE);
            wantToRead.setVisibility(View.GONE);
        } else {
            reading.setVisibility(View.VISIBLE);
            wantToRead.setVisibility(View.VISIBLE);
            watching.setVisibility(View.GONE);
            wantToWatch.setVisibility(View.GONE);
        }

        if (library == 1) {
            watching.setVisibility(View.GONE);
            reading.setVisibility(View.GONE);
        }
        else if (library == 2) {
            wantToWatch.setVisibility(View.GONE);
            wantToRead.setVisibility(View.GONE);
        }
        else if (library == 3) {
            onHold.setVisibility(View.GONE);
        }
        else if (library == 4) {
            dropped.setVisibility(View.GONE);
        }
        else if (library == 5) {
            completed.setVisibility(View.GONE);
        }
        else {
            removeLibrary.setVisibility(View.GONE);
        }

        removeLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InfoActivity.this, "Successfully removed from the list!", Toast.LENGTH_SHORT).show();
                removeAnimeLibrary(id);
                bottomSheetDialog.dismiss();
                btnLibrary.setText("Add to Library");
            }
        });

        reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InfoActivity.this, "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                addAnimeLibrary(1, id);
                bottomSheetDialog.dismiss();
                btnLibrary.setText("Reading");
            }
        });

        wantToRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InfoActivity.this, "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                addAnimeLibrary(2, id);
                bottomSheetDialog.dismiss();
                btnLibrary.setText("Want To Read");
            }
        });

        watching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InfoActivity.this, "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                addAnimeLibrary(1, id);
                bottomSheetDialog.dismiss();
                btnLibrary.setText("Watching");
            }
        });

        wantToWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InfoActivity.this, "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                addAnimeLibrary(2, id);
                bottomSheetDialog.dismiss();
                btnLibrary.setText("Want To Watch");
            }
        });

        onHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InfoActivity.this, "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                addAnimeLibrary(3, id);
                bottomSheetDialog.dismiss();
                btnLibrary.setText("On Hold");
            }
        });

        dropped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InfoActivity.this, "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                addAnimeLibrary(4, id);
                bottomSheetDialog.dismiss();
                btnLibrary.setText("Dropped");
            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InfoActivity.this, "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                addAnimeLibrary(5, id);
                bottomSheetDialog.dismiss();
                btnLibrary.setText("Completed");
            }
        });

        nevermind.setOnClickListener(new View.OnClickListener() {
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

    public void back(View view) {
        finish();
    }

    public void openLibrary(View view) {
        openChooseLibrary();
    }

    public void removeAnimeLibrary(int animeId) {

        String url = MainActivity.HOST_URL + "/api/animes/"+animeId+"/remove";

        if (!MainActivity.ANIME) {

            url = MainActivity.HOST_URL + "/api/mangas/"+animeId+"/remove";

        }

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);

    }

    public void addAnimeLibrary(int library, int animeId) {

        String url = MainActivity.HOST_URL + "/api/animes/"+animeId+"/addlibrary";

        if (MainActivity.ANIME) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("anime_nome", nome);
                jsonObject.put("anime_url", image_url);
                jsonObject.put("anime_total_episodios", totalEpisodios);
                jsonObject.put("anime_genero", genero);
                jsonObject.put("anime_library", library);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(request_json);

        } else {

            url = MainActivity.HOST_URL + "/api/mangas/"+animeId+"/addlibrary";

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("manga_nome", nome);
                jsonObject.put("manga_url", image_url);
                jsonObject.put("manga_total_volumes", totalEpisodios);
                jsonObject.put("manga_genero", genero);
                jsonObject.put("manga_library", library);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(request_json);

        }

    }

    private void getGeneros(int id) {

        String url = "https://api.jikan.moe/v3/anime/" + id;

        if (!MainActivity.ANIME) {
            url = "https://api.jikan.moe/v3/manga/" + id;
        }

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {

                            try {

                                JSONObject object = new JSONObject(response);
                                JSONArray jsonArray = object.getJSONArray("genres");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);


                                    if (anime_generos.getText() == "") {
                                        anime_generos.setText(data.getString("name"));
                                    } else {
                                        anime_generos.setText(anime_generos.getText() + ", " + data.getString("name"));
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);

    }
}