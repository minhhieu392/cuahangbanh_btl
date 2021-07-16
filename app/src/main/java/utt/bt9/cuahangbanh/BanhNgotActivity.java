package utt.bt9.cuahangbanh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
//import android.widget.Toolbar;

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

public class BanhNgotActivity extends AppCompatActivity {
    Toolbar toolbarbn;
    ListView lvbn;
    BanhNgotAdapter banhNgotAdapter;
    ArrayList<Sanpham> mangbn;
    int idbn = 0;
    int page= 1;
    View footerview;
    boolean isLoading = false;
    boolean limitadata = false;
    mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banh_ngot);
        AX();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            GetIDloaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();
        }else {
            CheckConnection.ShowToast_short(getApplicationContext(),"kiem tra lai internet");
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
        lvbn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(getApplicationContext(),ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangbn.get(i));
                startActivity(intent);
            }
        });
        lvbn.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdanbanhngot+String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id =0;
                String Tenbanh="";
                int GiaBanh=0;
                String Hinhanhbanh="";
                String Motabanh = "";
                int idloai = 0;
                if (response != null && response.length()!=2){
                    lvbn.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tenbanh = jsonObject.getString("tensanpham");
                            GiaBanh = jsonObject.getInt("giasanpham");
                            Hinhanhbanh = jsonObject.getString("hinhsanpham");
                            Motabanh = jsonObject.getString("motasp");
                            idloai =  jsonObject.getInt("idloai");
                            mangbn.add(new Sanpham(id,Tenbanh,GiaBanh,Hinhanhbanh,Motabanh,idloai));
                            banhNgotAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    limitadata = true;
                    lvbn.removeFooterView(footerview);
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
                param.put("idloai",String.valueOf(idbn));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarbn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarbn.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GetIDloaisp() {
        idbn = getIntent().getIntExtra("idloaisanpham",-1);
        Log.d("giatriloaisanpham",idbn+"");
    }

    private void AX() {
        toolbarbn = findViewById(R.id.toobarbanhngot);
        lvbn = findViewById(R.id.listviewbanhngot);
        mangbn = new ArrayList<>();
        banhNgotAdapter = new BanhNgotAdapter(getApplicationContext(),mangbn);
        lvbn.setAdapter(banhNgotAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        mHandler = new mHandler();
    }
    public class mHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvbn.addFooterView(footerview);
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