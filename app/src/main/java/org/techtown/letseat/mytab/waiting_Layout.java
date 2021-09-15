package org.techtown.letseat.mytab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.techtown.letseat.R;
import org.techtown.letseat.restaurant.rest_recycle_adapter;

public class waiting_Layout extends AppCompatActivity {

    private rest_recycle_adapter adapter = new rest_recycle_adapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytab_waiting);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //아이템 로드
        adapter.setItems(new waiting_status_data().getItems());
    }
}