package com.reply.hackaton.biotech.chipitsafe;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<DeviceViewHolder>{

    List<BluetoothDevice> items;

    public BluetoothDeviceAdapter(List<BluetoothDevice> items) {
        this.items = items;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row,parent,false);
        DeviceViewHolder viewHolder = new DeviceViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        BluetoothDevice item = items.get(position);
        holder.txtName.setText(item.getAddress()+" - "+item.getName());
    }

    @Override
    public int getItemCount() {
        return (items!=null)?items.size():0;
    }

    void update(){
        notifyDataSetChanged();
    }

    void addElement(BluetoothDevice dev){
        this.items.add(dev);
        update();
    }

}

class DeviceViewHolder extends RecyclerView.ViewHolder {
    TextView txtName;
    public DeviceViewHolder(View itemView) {
        super(itemView);
        txtName = (TextView) itemView.findViewById(R.id.txt_name);
    }
}

/*public class BluetoothDeviceAdapter extends ArrayAdapter<String> {

    public BluetoothDeviceAdapter(Context c, List<String> devices){
        super(c, 0, devices);

    }

    // called once per list item
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            convertView = inflater.inflate(R.layout.row, parent);
            TextView tv = (TextView) convertView.findViewById(R.id.textViewList);
            tv.setText(getItem(position));
        }
        return convertView;
    }

    void update(){
        notifyDataSetInvalidated();
    }

    void addElement(BluetoothDevice dev){
        this.add(dev.getName() + " " + dev.getAddress());
        update();
    }
}*/
