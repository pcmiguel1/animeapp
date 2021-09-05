package com.pcmiguel.anime;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SearchFragment extends Fragment implements Adapter.ListItemClickListener {

    private View view;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<Anime> animeList;
    private Adapter adapter;
    private ProgressBar progressBar;

    private EditText search;
    private LinearLayout anime, manga, notfound;
    private ImageView anime_img, manga_img;
    private TextView anime_text, manga_text;

    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());
        progressBar = view.findViewById(R.id.progressBar);

        search = view.findViewById(R.id.search_input);
        anime = view.findViewById(R.id.anime_op);
        manga = view.findViewById(R.id.manga_op);
        anime_img = view.findViewById(R.id.anime_op_img);
        anime_text = view.findViewById(R.id.anime_op_text);
        manga_img = view.findViewById(R.id.manga_op_img);
        manga_text = view.findViewById(R.id.manga_op_text);
        notfound = view.findViewById(R.id.notfound);

        initData();
        initRecyclerView();

        if (MainActivity.ANIME) {
            anime_img.setColorFilter(getResources().getColor(R.color.colorWhite));
            anime_text.setTextColor(getResources().getColor(R.color.colorWhite));

            manga_img.setColorFilter(getResources().getColor(R.color.colorAccent));
            manga_text.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            manga_img.setColorFilter(getResources().getColor(R.color.colorWhite));
            manga_text.setTextColor(getResources().getColor(R.color.colorWhite));

            anime_img.setColorFilter(getResources().getColor(R.color.colorAccent));
            anime_text.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ANIME = true;

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new SearchFragment()).commit();


            }
        });

        manga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ANIME = false;

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new SearchFragment()).commit();

            }
        });



        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 3) {
                    getAnimes(s.toString());
                }

            }
        });

        return view;

    }

    private void getAnimes(String anime) {

        animeList.clear();

        String url = "https://api.jikan.moe/v3/search/anime?q="+anime;

        progressBar.setVisibility(View.VISIBLE);
        notfound.setVisibility(View.GONE);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {

                            try {

                                JSONObject object = new JSONObject(response);
                                JSONArray jsonArray = object.getJSONArray("results");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    animeList.add(new Anime(data.getInt("mal_id"), data.getString("image_url"), data.getString("title"), data.getString("start_date").substring(0, 4), data.getString("episodes"), data.getString("score")));

                                }

                                progressBar.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    int status = networkResponse.statusCode;
                    if (status == 404) {
                        progressBar.setVisibility(View.GONE);
                        notfound.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        requestQueue.add(request);

    }

    private void initData() {

        animeList = new ArrayList<>();

        animeList.clear();

        String url = "https://api.jikan.moe/v3/search/anime?q=&order_by=members&sort=desc&page=1";

        progressBar.setVisibility(View.VISIBLE);
        notfound.setVisibility(View.GONE);

        if (!MainActivity.ANIME) {
            url = "https://api.jikan.moe/v3/search/manga?q=&order_by=members&sort=desc&page=1";

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {

                                try {

                                    JSONObject object = new JSONObject(response);
                                    JSONArray jsonArray = object.getJSONArray("results");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);

                                        animeList.add(new Anime(data.getInt("mal_id"), data.getString("image_url"), data.getString("title"), data.getString("start_date").substring(0, 4), data.getString("volumes"), data.getString("score")));

                                    }

                                    progressBar.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();

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
                                    JSONArray jsonArray = object.getJSONArray("results");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);

                                        animeList.add(new Anime(data.getInt("mal_id"), data.getString("image_url"), data.getString("title"), data.getString("start_date").substring(0, 4), data.getString("episodes"), data.getString("score")));

                                    }

                                    progressBar.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();

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

    private void initRecyclerView() {

        recyclerView = view.findViewById(R.id.list_animes);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(animeList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onListItemClick(int position) {
        Intent intent = new Intent(getActivity(), InfoActivity.class);
        intent.putExtra("id", animeList.get(position).getId());
        intent.putExtra("data", animeList.get(position).getAno());
        startActivity(intent);
    }

}