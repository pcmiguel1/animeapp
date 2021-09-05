package com.pcmiguel.anime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ProfileFragment extends Fragment {

    private View view;
    private TextView days, daysM, score, score2, watching, wantToWatch, onHold, dropped, completed, totalEntries, rewatched, episodes, reading, wantToRead, onHoldM, droppedM, completedM, totalEntriesM, reread, volumes;
    private RelativeLayout barW, barWtw, barH, barD, barC, barR, barWtr, barHM, barDM, barCM;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());

        days = view.findViewById(R.id.days);
        daysM = view.findViewById(R.id.days2);
        score = view.findViewById(R.id.mean_score);
        score2 = view.findViewById(R.id.mean_score2);
        watching = view.findViewById(R.id.watching);
        wantToWatch = view.findViewById(R.id.wantToWatch);
        onHold = view.findViewById(R.id.onHold);
        dropped = view.findViewById(R.id.dropped);
        completed = view.findViewById(R.id.completed);
        totalEntries = view.findViewById(R.id.totalEntries);
        rewatched = view.findViewById(R.id.rewatched);
        episodes = view.findViewById(R.id.episodes);
        reading = view.findViewById(R.id.reading);
        wantToRead = view.findViewById(R.id.wantToRead);
        onHoldM = view.findViewById(R.id.onHoldManga);
        droppedM = view.findViewById(R.id.droppedManga);
        completedM = view.findViewById(R.id.completedManga);
        totalEntriesM = view.findViewById(R.id.totalEntriesManga);
        reread = view.findViewById(R.id.reread);
        volumes = view.findViewById(R.id.volumes);

        barW = view.findViewById(R.id.barWatching);
        barWtw = view.findViewById(R.id.barWantToWatch);
        barH = view.findViewById(R.id.barOnHold);
        barD = view.findViewById(R.id.barDropped);
        barC = view.findViewById(R.id.barCompleted);

        barR = view.findViewById(R.id.barWatchingM);
        barWtr = view.findViewById(R.id.barWantToWatchM);
        barHM = view.findViewById(R.id.barOnHoldM);
        barDM = view.findViewById(R.id.barDroppedM);
        barCM = view.findViewById(R.id.barCompletedM);

        getValuesAnime();
        getAllAnimes();
        getValuesManga();
        getAllMangas();

        return view;
    }

    private void getValuesAnime() {

        String url = MainActivity.HOST_URL + "/api/animes/libraries";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {

                            try {

                                int w = 0;
                                int wtw = 0;
                                int h = 0;
                                int d = 0;
                                int c = 0;

                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    if (data.getInt("id") == 1) {
                                        watching.setText(data.getString("size"));
                                        w = data.getInt("size");
                                    }

                                    if (data.getInt("id") == 2) {
                                        wantToWatch.setText(data.getString("size"));
                                        wtw = data.getInt("size");
                                    }

                                    if (data.getInt("id") == 3) {
                                        onHold.setText(data.getString("size"));
                                        h = data.getInt("size");
                                    }

                                    if (data.getInt("id") == 4) {
                                        dropped.setText(data.getString("size"));
                                        d = data.getInt("size");
                                    }

                                    if (data.getInt("id") == 5) {
                                        completed.setText(data.getString("size"));
                                        c = data.getInt("size");
                                    }

                                }

                                int sum = w + wtw + h + d + c;

                                float wi = (float) (w*100)/sum;
                                barW.setLayoutParams(new TableRow.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, wi));

                                float wtwi = (float) (wtw*100)/sum;
                                barWtw.setLayoutParams(new TableRow.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, wtwi));

                                float hi = (float) (h*100)/sum;
                                barH.setLayoutParams(new TableRow.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, hi));

                                float di = (float) (d*100)/sum;
                                barD.setLayoutParams(new TableRow.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, di));

                                float ci = (float) (c*100)/sum;
                                barC.setLayoutParams(new TableRow.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, ci));

                                if (wi != 0 && wtwi == 0 && hi == 0 && di == 0 && ci == 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_4);
                                }
                                else if (wtwi != 0 && wi == 0 && hi == 0 && di == 0 && ci == 0) {
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_4);
                                }
                                else if (hi != 0 && wi == 0 && wtwi == 0 && di == 0 && ci == 0) {
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_4);
                                }
                                else if (di != 0 && wi == 0 && hi == 0 && wtwi == 0 && ci == 0) {
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_4);
                                }
                                else if (ci != 0 && wi == 0 && hi == 0 && wtwi == 0 && di == 0) {
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_4);
                                } //uma
                                else if (wi != 0 && wtwi != 0 && hi == 0 && di == 0 && ci == 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi == 0 && hi != 0 && di == 0 && ci == 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi == 0 && hi == 0 && di != 0 && ci == 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi == 0 && hi == 0 && di == 0 && ci != 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi != 0 && hi != 0 && di == 0 && ci == 0) {
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi != 0 && hi == 0 && di != 0 && ci == 0) {
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi != 0 && hi == 0 && di == 0 && ci != 0) {
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi == 0 && hi != 0 && di != 0 && ci == 0) {
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi == 0 && hi != 0 && di == 0 && ci != 0) {
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi == 0 && hi == 0 && di != 0 && ci != 0) {
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                } //duas
                                else if (wi != 0 && wtwi != 0 && hi != 0 && di == 0 && ci == 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi == 0 && hi != 0 && di != 0 && ci == 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi == 0 && hi != 0 && di == 0 && ci != 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi != 0 && hi == 0 && di != 0 && ci == 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi != 0 && hi == 0 && di == 0 && ci != 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                } //tres
                                else if (wi != 0 && wtwi != 0 && hi != 0 && di != 0 && ci == 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi != 0 && hi != 0 && di == 0 && ci != 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi != 0 && hi != 0 && di != 0 && ci != 0) {
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi != 0 && hi != 0 && di != 0 && ci != 0) {
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                //4
                                else if (wi != 0 && wtwi != 0 && hi != 0 && di != 0 && ci != 0) {
                                    barW.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtw.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barD.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barH.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barC.setBackgroundResource(R.drawable.custom_satus_bar_3);
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

    private void getAllAnimes() {

        String url = MainActivity.HOST_URL + "/api/animes/all";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {

                            try {

                                JSONArray jsonArray = new JSONArray(response);

                                totalEntries.setText(String.valueOf(jsonArray.length()));
                                int countEpisodes = 0;
                                int s = 0;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    countEpisodes += data.getInt("anime_episodio");
                                    s += data.getInt("anime_rating");

                                }

                                episodes.setText(String.valueOf(countEpisodes));

                                int meanScore = s / jsonArray.length();
                                score.setText(String.valueOf(meanScore));

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

    private void getValuesManga() {

        String url = MainActivity.HOST_URL + "/api/mangas/libraries";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {

                            try {

                                int w = 0;
                                int wtw = 0;
                                int h = 0;
                                int d = 0;
                                int c = 0;

                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    if (data.getInt("id") == 1) {
                                        reading.setText(data.getString("size"));
                                        w = data.getInt("size");
                                    }

                                    if (data.getInt("id") == 2) {
                                        wantToRead.setText(data.getString("size"));
                                        wtw = data.getInt("size");
                                    }

                                    if (data.getInt("id") == 3) {
                                        onHoldM.setText(data.getString("size"));
                                        h = data.getInt("size");
                                    }

                                    if (data.getInt("id") == 4) {
                                        droppedM.setText(data.getString("size"));
                                        d = data.getInt("size");
                                    }

                                    if (data.getInt("id") == 5) {
                                        completedM.setText(data.getString("size"));
                                        c = data.getInt("size");
                                    }

                                }

                                int sum = w + wtw + h + d + c;

                                float wi = (float) (w*100)/sum;
                                barR.setLayoutParams(new TableRow.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, wi));

                                float wtwi = (float) (wtw*100)/sum;
                                barWtr.setLayoutParams(new TableRow.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, wtwi));

                                float hi = (float) (h*100)/sum;
                                barHM.setLayoutParams(new TableRow.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, hi));

                                float di = (float) (d*100)/sum;
                                barDM.setLayoutParams(new TableRow.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, di));

                                float ci = (float) (c*100)/sum;
                                barCM.setLayoutParams(new TableRow.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, ci));

                                if (wi != 0 && wtwi == 0 && hi == 0 && di == 0 && ci == 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_4);
                                }
                                else if (wtwi != 0 && wi == 0 && hi == 0 && di == 0 && ci == 0) {
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_4);
                                }
                                else if (hi != 0 && wi == 0 && wtwi == 0 && di == 0 && ci == 0) {
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_4);
                                }
                                else if (di != 0 && wi == 0 && hi == 0 && wtwi == 0 && ci == 0) {
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_4);
                                }
                                else if (ci != 0 && wi == 0 && hi == 0 && wtwi == 0 && di == 0) {
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_4);
                                } //uma
                                else if (wi != 0 && wtwi != 0 && hi == 0 && di == 0 && ci == 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi == 0 && hi != 0 && di == 0 && ci == 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi == 0 && hi == 0 && di != 0 && ci == 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi == 0 && hi == 0 && di == 0 && ci != 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi != 0 && hi != 0 && di == 0 && ci == 0) {
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi != 0 && hi == 0 && di != 0 && ci == 0) {
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi != 0 && hi == 0 && di == 0 && ci != 0) {
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi == 0 && hi != 0 && di != 0 && ci == 0) {
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi == 0 && hi != 0 && di == 0 && ci != 0) {
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi == 0 && hi == 0 && di != 0 && ci != 0) {
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                } //duas
                                else if (wi != 0 && wtwi != 0 && hi != 0 && di == 0 && ci == 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi == 0 && hi != 0 && di != 0 && ci == 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi == 0 && hi != 0 && di == 0 && ci != 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi != 0 && hi == 0 && di != 0 && ci == 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi != 0 && hi == 0 && di == 0 && ci != 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                } //tres
                                else if (wi != 0 && wtwi != 0 && hi != 0 && di != 0 && ci == 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi != 0 && wtwi != 0 && hi != 0 && di == 0 && ci != 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi != 0 && hi != 0 && di != 0 && ci != 0) {
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                else if (wi == 0 && wtwi != 0 && hi != 0 && di != 0 && ci != 0) {
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_3);
                                }
                                //4
                                else if (wi != 0 && wtwi != 0 && hi != 0 && di != 0 && ci != 0) {
                                    barR.setBackgroundResource(R.drawable.custom_satus_bar_1);
                                    barWtr.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barDM.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barHM.setBackgroundResource(R.drawable.custom_satus_bar_2);
                                    barCM.setBackgroundResource(R.drawable.custom_satus_bar_3);
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

    private void getAllMangas() {

        String url = MainActivity.HOST_URL + "/api/mangas/all";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {

                            try {

                                JSONArray jsonArray = new JSONArray(response);

                                totalEntries.setText(String.valueOf(jsonArray.length()));
                                int countEpisodes = 0;
                                int s = 0;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    countEpisodes += data.getInt("manga_volume");
                                    s += data.getInt("manga_rating");

                                }

                                volumes.setText(String.valueOf(countEpisodes));

                                int meanScore = s / jsonArray.length();
                                score2.setText(String.valueOf(meanScore));

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