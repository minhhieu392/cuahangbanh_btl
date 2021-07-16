package utt.bt9.cuahangbanh;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BanhNgotAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraybanhngot;

    public BanhNgotAdapter(Context context, ArrayList<Sanpham> arraybanhngot) {
        this.context = context;
        this.arraybanhngot = arraybanhngot;
    }

    @Override
    public int getCount() {
        return arraybanhngot.size();
    }

    @Override
    public Object getItem(int i) {
        return arraybanhngot.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class Viewholder{
        public TextView txttenbanhngot, txtgiabanhngot, txtmotabanhngot;
        public ImageView imgbanhngot;

    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Viewholder viewholder = null;
        if (convertView == null){
            viewholder = new Viewholder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_banhngot,null);
            viewholder.txttenbanhngot = convertView.findViewById(R.id.textviewbanhngot);
            viewholder.txtgiabanhngot= convertView.findViewById(R.id.textviewgiabanhngot);
            viewholder.txtmotabanhngot = convertView.findViewById(R.id.textviewmotabanh);
            viewholder.imgbanhngot= convertView.findViewById(R.id.imagebanhngot);
            convertView.setTag(viewholder);
             //
        }else  {
            viewholder = (Viewholder) convertView.getTag();
        }

        Sanpham sanpham = (Sanpham) getItem(i);
        viewholder.txttenbanhngot.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewholder.txtgiabanhngot.setText("Giá :" + decimalFormat.format(sanpham.getGiasanpham())+"VND");
        viewholder.txtmotabanhngot.setMaxLines(2);
        viewholder.txtmotabanhngot.setEllipsize(TextUtils.TruncateAt.END);
        viewholder.txtmotabanhngot.setText(sanpham.getMotasanpham());
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.error1)
                .error(R.drawable.error2)
                .into(viewholder.imgbanhngot);
        return convertView;
    }
}
