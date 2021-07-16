package utt.bt9.cuahangbanh;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTietSanPham extends AppCompatActivity {
    Toolbar toolbarchitiet;
    ImageView imgChiTiet;
    TextView txtten,txtgia,txtmota;
    Spinner spinner;
    Button btndatmua;
    int id = 0;
    String TenChiTiet = "";
    int GiaChiTiet = 0;
    String HinhChiTiet = "";
    String MotaChiTiet = "";
    int idloai = 0;
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.chitietsanpham);
        AX();
        ActionToolbar();
        GetInfo();
        CatchEvenSpinner();
        EventButton();
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

    private void EventButton() {
        btndatmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.manggiohang.size() >0){
                    int sl = Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exists = false;
                    for (int i= 0; i<MainActivity.manggiohang.size();i++){
                        if (MainActivity.manggiohang.get(i).getIdsp()==id){
                            MainActivity.manggiohang.get(i).setSoluongsp(MainActivity.manggiohang.get(i).getSoluongsp()+sl);
                            if (MainActivity.manggiohang.get(i).getSoluongsp() >= 10){
                                MainActivity.manggiohang.get(i).setSoluongsp(10);
                            }
                            MainActivity.manggiohang.get(i).setGiasp(GiaChiTiet+MainActivity.manggiohang.get(i).getSoluongsp());
                            exists = true;
                        }
                    }
                    if (exists == false){
                        int soluong =Integer.parseInt(spinner.getSelectedItem().toString());
                        long Giamoi = soluong * GiaChiTiet;
                        MainActivity.manggiohang.add(new GioHang(id,TenChiTiet,Giamoi,HinhChiTiet,soluong));
                    }
                }else {
                    int soluong =Integer.parseInt(spinner.getSelectedItem().toString());
                    long Giamoi = soluong * GiaChiTiet;
                    MainActivity.manggiohang.add(new GioHang(id,TenChiTiet,Giamoi,HinhChiTiet,soluong));
                }
                Intent intent = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CatchEvenSpinner() {
        Integer[] soluong = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapter= new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,soluong);
        spinner.setAdapter(arrayAdapter);
    }

    private void GetInfo() {

        Sanpham sanpham = (Sanpham) getIntent().getSerializableExtra("thongtinsanpham");
        id= sanpham.getID();
        TenChiTiet = sanpham.getTensanpham();
        GiaChiTiet = sanpham.getGiasanpham();
        HinhChiTiet = sanpham.getHinhanhsanpham();
        MotaChiTiet = sanpham.getMotasanpham();
        idloai = sanpham.getIDSanpham();
        txtten.setText(TenChiTiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtgia.setText("Giá: " + decimalFormat.format(GiaChiTiet)+ "VND");
        txtmota.setText(MotaChiTiet);
        Picasso.get().load(HinhChiTiet)
                .placeholder(R.drawable.error1)
                .error(R.drawable.error2)
                .into(imgChiTiet);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarchitiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarchitiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AX() {
        toolbarchitiet=findViewById(R.id.idtoolbarchitiet);
        imgChiTiet = findViewById(R.id.imgchitietsp);
        txtten = findViewById(R.id.textviewtenchitietsp);
        txtgia= findViewById(R.id.textviewgiachitietsp);
        txtmota = findViewById(R.id.textviewmotachitietsp);
        spinner =findViewById(R.id.spinner);
        btndatmua = findViewById(R.id.buttomdatmua);

    }
}
