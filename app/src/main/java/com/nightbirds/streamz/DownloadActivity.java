package com.nightbirds.streamz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class DownloadActivity extends AppCompatActivity {

    ListView downList;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_download);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        downList = findViewById(R.id.downlist);

        hashMap = new HashMap<>();
        hashMap.put("title", "Toofan");
        hashMap.put("actor", "Sakib Khan | Mim");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "K.G.F");
        hashMap.put("actor", "Yash | Jani na");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Bandia");
        hashMap.put("actor", "Seikh Hasina | Modi");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Hitman");
        hashMap.put("actor", "Sakib Khan | Apo Biswas");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("title", "Power");
        hashMap.put("actor", "Jet | Jani Na");
        arrayList.add(hashMap);




        DownAdapter downAdapter = new DownAdapter();

        downList.setAdapter(downAdapter);


    }//================= on create end

    private class DownAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View downView = inflater.inflate(R.layout.search_item, parent, false);

            LinearLayout searchItem;
            TextView srcmovietitle, srcmovieactor;

            searchItem = downView.findViewById(R.id.searchItem);
            srcmovietitle = downView.findViewById(R.id.srcmovietitle);
            srcmovieactor = downView.findViewById(R.id.srcmovieactor);


            HashMap<String, String> hashMap = arrayList.get(position);

            srcmovietitle.setText(hashMap.get("title"));
            srcmovieactor.setText(hashMap.get("actor"));

            searchItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DownloadActivity.this, PlayerActivity.class);
                    startActivity(intent);
                }
            });


            srcmovietitle.setSelected(true);
            return downView;
        }
    }
}