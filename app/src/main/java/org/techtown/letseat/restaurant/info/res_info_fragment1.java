package org.techtown.letseat.restaurant.info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.R;
import org.techtown.letseat.menu.MenuAdapter;
import org.techtown.letseat.menu.MenuData;
import org.techtown.letseat.menu.OnMenuItemClickListner;
import org.techtown.letseat.pay_test.Kakao_pay_test;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.PhotoSave;

import java.util.ArrayList;

public class res_info_fragment1 extends Fragment {

    ArrayList<String> priceList = new ArrayList<>();
    ArrayList list = new ArrayList<>();
    RecyclerView recyclerView;
    private org.techtown.letseat.menu.MenuAdapter adapter = new MenuAdapter();
    private int sum;
    private CheckBox checkBox;
    private ExtendedFloatingActionButton basketBtn;

    int resId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.res_info_fragment1, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //결제버튼
        basketBtn = view.findViewById(R.id.basket_button2);
        basketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Kakao_pay_test.class);
                intent.putExtra("Price",String.valueOf(sum));
                intent.putExtra("resId",resId);
                startActivity(intent);
            }
        });


        Bundle extra = this.getArguments();
        if (extra != null) {
            resId = extra.getInt("send_resId");     //레스토랑 정보
            get_MenuData();
        }


        return view;
    }

    public void start(){
        //recycleView 초기화

        adapter.setItems(new MenuData().getItems(list));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListner(new OnMenuItemClickListner() {
            @Override
            public void OnItemClick(MenuAdapter.ViewHolder holder, View view, int position) {

            }
        });

        //클릭 이벤트


    }

    // 메뉴 리스트 가져오기
    void get_MenuData() {
        String url = "http://125.132.62.150:8000/letseat/store/menu/load?resId="+resId;

        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String menuPrice,menuName,image;
                            Bitmap bitmap;

                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                menuPrice = jsonObject.getString("price");
                                menuName = jsonObject.getString("name");
                                image = jsonObject.getString("photo");
                                bitmap = PhotoSave.StringToBitmap(image);

                                list.add(bitmap);
                                list.add(menuName);
                                list.add(menuPrice);

                                priceList.add(menuPrice);
                            }

                            start();

                            Log.d("응답", response.toString());
                        } catch (JSONException e) {
                            Log.d("예외", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("에러", error.toString());
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(getActivity()); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }

}