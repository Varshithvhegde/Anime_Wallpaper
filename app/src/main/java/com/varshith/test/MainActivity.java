package com.varshith.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.varshith.test.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {

    public String url;
    public String base_url;
    public String[] itemsarray;
    public GridView gridView;
    int page_number = 1;
    int last_page = 1;
    public Content content;
    public TextView index;
    public EditText page_picker;
    InputMethodManager inputMethodManager;

    public void load_thumbs(int pn) {
        url = base_url + "&page=" + String.valueOf(pn);

        content.cancel(true);

        content = new Content();
        content.execute();
        page_picker.setText(String.valueOf(pn));
        index.setText("/ " + String.valueOf(last_page));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView search_field = (SearchView) findViewById(R.id.search_field);
        gridView = findViewById(R.id.grid_view);

        final Button prev_button = findViewById(R.id.previous);
        final Button next_button = findViewById(R.id.next);

        LinearLayout Main = (LinearLayout) findViewById(R.id.Main);
        LinearLayout bar = (LinearLayout) findViewById(R.id.bottom_bar);
        index = (TextView) findViewById(R.id.pagenbr);
        page_picker = (EditText) findViewById(R.id.page_select);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        search_field.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = null;
                try {
                    query = URLEncoder.encode(search_field.getQuery().toString(), StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                url = "https://wall.alphacoders.com/search.php?search=" + query;
                Main.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                Main.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

                gridView.setVisibility(View.VISIBLE);
                bar.setVisibility(View.VISIBLE);

                page_number = 1;

                content = new Content();
                content.execute();
                page_picker.setText("1");


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }


        });


        prev_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (page_number > 1) {
                    page_number--;
                    load_thumbs(page_number);

                }
            }
        });

//

        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (page_number < last_page) {
                    page_number++;
                    load_thumbs(page_number);

                }
            }
        });


        page_picker.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    int n = Integer.parseInt(String.valueOf(page_picker.getText()));
                    if (n >= 1 && n <= last_page) {
                        page_number = n;
                        load_thumbs(page_number);
                        page_picker.setSelection(page_picker.getText().length());
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        handled = true;
                    }
                }
                return handled;
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                // passing array index
                i.putExtra("id", position);
                i.putExtra("Array", itemsarray);
                startActivity(i);
            }
        });


    }


    private class Content extends AsyncTask<String, Void, Document> {


        @Override
        protected Document doInBackground(String... strings) {

            if (isCancelled()) {
                return null;
            }

            try {

                Document doc = Jsoup.connect(url).get();
                if (page_number == 1) {
                    base_url = doc.baseUri();
                }

                return doc;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(Document document) {
            if (document != null) {


                ArrayList<String> links_thumbs = new ArrayList<String>();
                Elements images = document.select("source");
                String link;

                for (int i = 0; i < images.size(); i++) {
                    link = images.get(i).attr("srcset");
                    if (link.contains("thumb-350-") && (link.contains("jpg") || link.contains("png"))) {
                        links_thumbs.add(link);
                    }
                }

                itemsarray = links_thumbs.toArray(new String[links_thumbs.size()]);

                ImageAdapter baseAdapter = new ImageAdapter(MainActivity.this, itemsarray);
                gridView.setAdapter(baseAdapter);


                if (page_number == 1 && itemsarray.length == 30) {


                    Elements pages = document.getElementsByTag("script");

                    for (int i = 0; i < pages.size(); i++) {

                        String text = pages.get(i).data();

                        Pattern pattern = Pattern.compile("last_page = (\\d+);");
                        Matcher matcher = pattern.matcher(text);

                        while (matcher.find()) {

                            last_page = Integer.parseInt(matcher.group(1));

                        }
                    }


                }

                if (page_number == 1 && itemsarray.length != 30) {
                    last_page = 1;

                }

                index.setText("/ " + String.valueOf(last_page));


            }
        }
    }

}