package com.aervon.app.regioncacheproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.aervon.app.regioncache.Region;
import com.aervon.app.regioncache.core.RegionCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @RegionCache(field1 = "msg")
    public RegionData data = new RegionData();

    @RegionCache(field1 = "text")
    public TextView textView;

    public TextView textView2;

    @RegionCache(from = 0, to = 1)
    public List<String> dataList = new ArrayList<>();

    @RegionCache
    public Map<String, String> map = new HashMap<>();

    @RegionCache
    public String[] stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        textView2 = findViewById(R.id.text2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Hello World");
                map.put("11", "11");
                map.put("22", "22");
                map.put("33", "33");
                dataList.add("11");
                dataList.add("22");
                dataList.add("33");
                Region.with(MainActivity.this).cacheRegion(MainActivity.this, "tag");
                textView.setText("Cache!");
//                if (map.size() != 0) {
//                    for (Map.Entry<String, String> entry : map.entrySet()) {
//                        textView.setText(textView.getText() + "\n" + "key : " + entry.getKey() + " value : " + entry.getValue());
//                    }
//                }
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Region.with(MainActivity.this).readRegion(MainActivity.this, "tag");
//                if (dataList != null && dataList.size() > 0) {
//                    for (String s : dataList) {
//                        Log.e("region", "\n" + s);
//                    }
//                }
            }
        });
    }
}
