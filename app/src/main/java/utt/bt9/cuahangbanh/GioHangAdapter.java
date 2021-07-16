package utt.bt9.cuahangbanh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GioHangAdapter extends BaseAdapter {
    Context context;
    ArrayList<GioHang> arraygiohang;

    public GioHangAdapter(Context context, ArrayList<GioHang> arraygiohang) {
        this.context = context;
        this.arraygiohang = arraygiohang;
    }

    @Override
    public int getCount() {
        return arraygiohang.size();
    }

    @Override
    public Object getItem(int i) {
        return arraygiohang.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder{
        public TextView txttengiohang , txtgiagiohang;
        public ImageView imggiagiohang;
        public Button btnvalues, btnplus, btnminus;

    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
                if (convertView == null){
                    viewHolder = new ViewHolder();
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.dong_giohang,null);
                    viewHolder.txttengiohang = convertView.findViewById(R.id.textviewtengiohang);
                    viewHolder.txtgiagiohang = convertView.findViewById(R.id.textviewgiagiohang);
                    viewHolder.imggiagiohang = convertView.findViewById(R.id.imgviewgiohang);
                    viewHolder.btnminus = convertView.findViewById(R.id.buttonminus);
                    viewHolder.btnplus = convertView.findViewById(R.id.buttonplus);
                    viewHolder.btnvalues = convertView.findViewById(R.id.buttonvalues);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                GioHang gioHang = (GioHang) getItem(i);
                viewHolder.txttengiohang.setText(gioHang.getTensp());
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.txtgiagiohang.setText(decimalFormat.format(gioHang.getGiasp())+"D");
                Picasso.get().load(gioHang.getHinhsp())
                        .placeholder(R.drawable.error1)
                        .error(R.drawable.error2)
                        .into(viewHolder.imggiagiohang);
                viewHolder.btnvalues.setText(gioHang.getSoluongsp()+"");
                int sl= Integer.parseInt(viewHolder.btnvalues.getText().toString());
                if (sl>=10){
                    viewHolder.btnplus.setVisibility(View.INVISIBLE);
                    viewHolder.btnminus.setVisibility(View.VISIBLE);
                }else if (sl<=1){
                    viewHolder.btnminus.setVisibility(View.INVISIBLE);

                }else if (sl>=1){
                    viewHolder.btnminus.setVisibility(View.VISIBLE);
                    viewHolder.btnplus.setVisibility(View.VISIBLE);
                }
        ViewHolder finalViewHolder = viewHolder;
        ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.btnplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int sln =Integer.parseInt(finalViewHolder.btnvalues.getText().toString())+1;
                        int slr = MainActivity.manggiohang.get(i).getSoluongsp();
                        long giaR= MainActivity.manggiohang.get(i).getGiasp();
                        MainActivity.manggiohang.get(i).setSoluongsp(sln);
                        long giaN = (giaR * sln)/slr;
                        MainActivity.manggiohang.get(i).setGiasp(giaN);
                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                        finalViewHolder.txtgiagiohang.setText(decimalFormat.format(giaN)+"VND");
                        GioHangActivity.EvenUltil();
                        if (sln > 9){
                            finalViewHolder1.btnplus.setVisibility(View.INVISIBLE);
                            finalViewHolder1.btnminus.setVisibility(View.VISIBLE);
                            finalViewHolder.btnvalues.setText(String.valueOf(sln));
                        }else {
                            finalViewHolder.btnminus.setVisibility(View.VISIBLE);
                            finalViewHolder.btnplus.setVisibility(View.VISIBLE);
                            finalViewHolder.btnvalues.setText(String.valueOf(sln));
                        }
                    }
                });
        viewHolder.btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sln =Integer.parseInt(finalViewHolder.btnvalues.getText().toString())-1;
                int slr = MainActivity.manggiohang.get(i).getSoluongsp();
                long giaR= MainActivity.manggiohang.get(i).getGiasp();
                MainActivity.manggiohang.get(i).setSoluongsp(sln);
                long giaN = (giaR * sln)/slr;
                MainActivity.manggiohang.get(i).setGiasp(giaN);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.txtgiagiohang.setText(decimalFormat.format(giaN)+"D");
                GioHangActivity.EvenUltil();
                if (sln < 2){
                    finalViewHolder.btnplus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnminus.setVisibility(View.INVISIBLE);
                    finalViewHolder.btnvalues.setText(String.valueOf(sln));
                }else {
                    finalViewHolder.btnminus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnplus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnvalues.setText(String.valueOf(sln));
                }

            }
        });
        return convertView;
    }
}
