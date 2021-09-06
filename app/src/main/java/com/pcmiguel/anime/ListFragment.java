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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements AdapterCategory.ListItemClickListener, AdapterList.ListItemClickListener {

    private View view;

    private LinearLayout anime, manga, empty, notfound;
    private ImageView anime_img, manga_img;
    private TextView anime_text, manga_text;

    private RecyclerView categories_recyclerView, recyclerView;
    private LinearLayoutManager layoutManager;
    private List<Category> categoriesList;
    private List<AnimeList> animeList;
    private AdapterCategory adapterCategory;
    private AdapterList adapterList;
    private ProgressBar progressBar;

    private EditText search;

    private RequestQueue requestQueue;

    private int library = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());
        progressBar = view.findViewById(R.id.progressBar);

        initDataCategories();
        initRecyclerViewCategories();

        search = view.findViewById(R.id.search_input);
        anime = view.findViewById(R.id.anime_op);
        manga = view.findViewById(R.id.manga_op);
        anime_img = view.findViewById(R.id.anime_op_img);
        anime_text = view.findViewById(R.id.anime_op_text);
        manga_img = view.findViewById(R.id.manga_op_img);
        manga_text = view.findViewById(R.id.manga_op_text);
        empty = view.findViewById(R.id.empty);
        notfound = view.findViewById(R.id.notfound);

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

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListFragment()).commit();


            }
        });

        manga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ANIME = false;

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListFragment()).commit();

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

                getAnimes(s.toString());

            }
        });

        return view;
    }

    private void getAnimes(String anime) {

        animeList.clear();

        String url = MainActivity.HOST_URL + "/api/animes?nome="+anime + "&library=" + library;

        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        notfound.setVisibility(View.GONE);

        if (MainActivity.ANIME) {

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {

                                try {

                                    JSONArray jsonArray = new JSONArray(response);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);

                                        animeList.add(new AnimeList(data.getInt("anime_id"), data.getString("anime_url"), data.getString("anime_nome"), data.getInt("anime_total_episodios"), data.getInt("anime_episodio"), data.getInt("anime_rating"), data.getInt("anime_library")));

                                    }

                                    progressBar.setVisibility(View.GONE);
                                    adapterList.notifyDataSetChanged();

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

        } else {
            url = MainActivity.HOST_URL + "/api/mangas?nome="+anime + "&library=" + library;

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {

                                try {

                                    JSONArray jsonArray = new JSONArray(response);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);

                                        animeList.add(new AnimeList(data.getInt("manga_id"), data.getString("manga_url"), data.getString("manga_nome"), data.getInt("manga_total_volumes"), data.getInt("manga_volume"), data.getInt("manga_rating"), data.getInt("manga_library")));

                                    }

                                    progressBar.setVisibility(View.GONE);
                                    adapterList.notifyDataSetChanged();

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

    }

    private void initDataCategories() {

        categoriesList = new ArrayList<>();

        categoriesList.clear();

        String url = MainActivity.HOST_URL + "/api/animes/libraries";

        if (!MainActivity.ANIME) {
            url = MainActivity.HOST_URL + "/api/mangas/libraries";
        }

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {

                            try {

                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    categoriesList.add(new Category(data.getString("nome"), data.getInt("size")));

                                }

                                progressBar.setVisibility(View.GONE);
                                adapterCategory.notifyDataSetChanged();

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

    private void initRecyclerViewCategories() {

        categories_recyclerView = view.findViewById(R.id.list_categories);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        categories_recyclerView.setLayoutManager(layoutManager);
        adapterCategory = new AdapterCategory(categoriesList, this);
        categories_recyclerView.setAdapter(adapterCategory);
        adapterCategory.notifyDataSetChanged();

    }

    private void initData(int library) {

        animeList = new ArrayList<>();

        animeList.clear();

        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        notfound.setVisibility(View.GONE);

        String url = MainActivity.HOST_URL + "/api/animes?library=" + library;

        if (!MainActivity.ANIME) {
            url = MainActivity.HOST_URL + "/api/mangas?library=" + library;

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {

                                try {

                                    JSONArray jsonArray = new JSONArray(response);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);

                                        animeList.add(new AnimeList(data.getInt("manga_id"), data.getString("manga_url"), data.getString("manga_nome"), data.getInt("manga_total_volumes"), data.getInt("manga_volume"), data.getInt("manga_rating"), data.getInt("manga_library")));

                                    }

                                    progressBar.setVisibility(View.GONE);
                                    adapterList.notifyDataSetChanged();

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
                            empty.setVisibility(View.VISIBLE);
                        }
                    }
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

                                    JSONArray jsonArray = new JSONArray(response);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);

                                        animeList.add(new AnimeList(data.getInt("anime_id"), data.getString("anime_url"), data.getString("anime_nome"), data.getInt("anime_total_episodios"), data.getInt("anime_episodio"), data.getInt("anime_rating"), data.getInt("anime_library")));

                                    }

                                    progressBar.setVisibility(View.GONE);
                                    adapterList.notifyDataSetChanged();

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
                            empty.setVisibility(View.VISIBLE);
                        }
                    }
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
        adapterList = new AdapterList(animeList, this);
        recyclerView.setAdapter(adapterList);
        adapterList.notifyDataSetChanged();

    }

    @Override
    public void onListItemClick(int position) {
        library = position + 1;
        initData(library);
        initRecyclerView();

    }

    @Override
    public void onItemClick(int position, int rate, int animeId) {
        addRatAnime(rate, animeId);
        adapterList.notifyItemChanged(position);
    }

    @Override
    public void pnext(int position, boolean next, int animeId) {
        previousNext(next, animeId);
        adapterList.notifyItemChanged(position);
    }

    @Override
    public void loadOptionsLibrary(final int position, final int animeId) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                getActivity(), R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getActivity())
                .inflate(
                        R.layout.layout_bottom_library,
                        (LinearLayout) getActivity().findViewById(R.id.bottomSheetLibrary)
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
                Toast.makeText(getActivity(), "Successfully removed from the list!", Toast.LENGTH_SHORT).show();
                removeAnimeLibrary(animeId);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListFragment()).commit();
                bottomSheetDialog.dismiss();
            }
        });

        reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                updateAnimeLibrary(1, animeId);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListFragment()).commit();
                bottomSheetDialog.dismiss();
            }
        });

        wantToRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                updateAnimeLibrary(2, animeId);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListFragment()).commit();
                bottomSheetDialog.dismiss();
            }
        });

        watching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                updateAnimeLibrary(1, animeId);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListFragment()).commit();
                bottomSheetDialog.dismiss();
            }
        });

        wantToWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                updateAnimeLibrary(2, animeId);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListFragment()).commit();
                bottomSheetDialog.dismiss();
            }
        });

        onHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                updateAnimeLibrary(3, animeId);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListFragment()).commit();
                bottomSheetDialog.dismiss();
            }
        });

        dropped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                updateAnimeLibrary(4, animeId);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListFragment()).commit();
                bottomSheetDialog.dismiss();
            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Successfully added to your list!", Toast.LENGTH_SHORT).show();
                updateAnimeLibrary(5, animeId);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListFragment()).commit();
                bottomSheetDialog.dismiss();
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

    @Override
    public void onItemClickInfo(int position) {
        Intent intent = new Intent(getActivity(), InfoActivity.class);
        intent.putExtra("id", animeList.get(position).getId());
        startActivity(intent);
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

    public void updateAnimeLibrary(int library, int animeId) {

        String url = MainActivity.HOST_URL + "/api/animes/"+animeId+"/updateinfo";

        if (!MainActivity.ANIME) {
            url = MainActivity.HOST_URL + "/api/mangas/"+animeId+"/updateinfo";
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ulibrary", library);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
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

    public void addRatAnime(int rate, int animeId) {

        String url = MainActivity.HOST_URL + "/api/animes/"+animeId+"/updateinfo";

        if (MainActivity.ANIME) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("anime_rating", rate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
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

            url = MainActivity.HOST_URL + "/api/mangas/"+animeId+"/updateinfo";

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("manga_rating", rate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
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

    public void previousNext(boolean next, int animeId) {

        String url = MainActivity.HOST_URL + "/api/animes/"+animeId+"/updateinfo";

        if (!MainActivity.ANIME) {
            url = MainActivity.HOST_URL + "/api/mangas/"+animeId+"/updateinfo";
        }

        JSONObject jsonObject = new JSONObject();
        try {
            if (next) {
                jsonObject.put("next", true);
            } else {
                jsonObject.put("previous", true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
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