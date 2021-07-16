package utt.bt9.cuahangbanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoUongActivity extends AppCompatActivity {
    Toolbar toolbardu;
    ListView lvdu;
    DoUongAdapter doUongAdapter;
    ArrayList<Sanpham> mangdu;
    int iddu =2;
    int page= 1;
    View footerview;
    boolean isLoading = false;
    boolean limitadata = false;
    mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_uong);

        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            AX();
            GetIDloaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();
        }else {
            CheckConnection.ShowToast_short(getApplicationContext(),"kiem tra internet");
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void LoadMoreData() {
        lvdu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(getApplicationContext(),ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangdu.get(i));
                startActivity(intent);
            }
        });
        lvdu.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstitem, int visibleitem, int totalItem) {
                //
                if (firstitem + visibleitem == totalItem && totalItem != 0 && isLoading == false&& limitadata == false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void AX() {
        toolbardu = findViewById(R.id.toobardouong);
        lvdu = findViewById(R.id.listviewdouong) ;
        mangdu = new ArrayList<>();
        doUongAdapter = new DoUongAdapter(getApplicationContext(),mangdu);
        lvdu.setAdapter(doUongAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        mHandler = new mHandler();

    }
    private void GetIDloaisp() {
        iddu = getIntent().getIntExtra("idloaisanpham",-1);
    }
    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdanbanhngot+String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id =0;
                String Tendouong="";
                int Giadouong=0;
                String Hinhdouong="";
                String Motadouong = "";
                int idloai = 0;
                if (response != null && response.length()!=2){
                    lvdu.removeFooterView(footerview);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tendouong = jsonObject.getString("tensanpham");
                            Giadouong = jsonObject.getInt("giasanpham");
                            Hinhdouong = jsonObject.getString("hinhsanpham");
                            Motadouong = jsonObject.getString("motasp");
                            idloai =  jsonObject.getInt("idloai");
                            mangdu.add(new Sanpham(id,Tendouong,Giadouong,Hinhdouong,Motadouong,idloai));
                            doUongAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    limitadata = true;
                    lvdu.removeFooterView(footerview);
                    CheckConnection.ShowToast_short(getApplicationContext(),"da het du lieu");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("idloai",String.valueOf(iddu));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void ActionToolbar() {
        setSupportActionBar(toolbardu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbardu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public class mHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvdu.addFooterView(footerview);
                    break;
                case 1:
                    GetData(++page);
                    isLoading = false;
                    break;

            }
            super.handleMessage(msg);
        }
    }
    public class ThreadData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }


}