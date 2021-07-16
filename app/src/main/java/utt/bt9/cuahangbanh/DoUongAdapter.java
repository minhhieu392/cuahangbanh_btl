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

public class DoUongAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraydouong;

    public DoUongAdapter(Context context, ArrayList<Sanpham> arraydouong) {
        this.context = context;
        this.arraydouong = arraydouong;
    }

    @Override
    public int getCount() {
        return arraydouong.size();
    }

    @Override
    public Object getItem(int i) {
        return arraydouong.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class Viewholder{
        public TextView txttendouong, txtgiadouong, txtmotadouong;
        public ImageView imgdouong;

    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Viewholder viewholder = null;
        if (convertView == null){
            viewholder = new Viewholder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_douong,null);
            viewholder.txttendouong = convertView.findViewById(R.id.textviewdouong);
            viewholder.txtgiadouong= convertView.findViewById(R.id.textviewgiadouong);
            viewholder.txtmotadouong = convertView.findViewById(R.id.textviewmotadouong);
            viewholder.imgdouong= convertView.findViewById(R.id.imagedouong);
            convertView.setTag(viewholder);
            //
        }else  {
            viewholder = (Viewholder) convertView.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewholder.txttendouong.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewholder.txtgiadouong.setText("Giá :" + decimalFormat.format(sanpham.getGiasanpham())+"VND");
        viewholder.txtmotadouong.setMaxLines(2);
        viewholder.txtmotadouong.setEllipsize(TextUtils.TruncateAt.END);
        viewholder.txtmotadouong.setText(sanpham.getMotasanpham());
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.error1)
                .error(R.drawable.error2)
                .into(viewholder.imgdouong);
        return convertView;
    }
}
