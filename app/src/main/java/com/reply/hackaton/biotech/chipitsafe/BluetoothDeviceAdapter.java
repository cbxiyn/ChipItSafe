package com.reply.hackaton.biotech.chipitsafe;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<DeviceViewHolder>{

    //List<BluetoothDevice> items;
    Map<String,BluetoothDevice> items;

    public BluetoothDeviceAdapter( Map<String,BluetoothDevice> items) {
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
        int i = 0;
        for(BluetoothDevice item : items.values()){
            if(i == position) {
                holder.txtName.setText(item.getAddress() + " - " + item.getName());
                return;
            }
            i++;
        }

        //BluetoothDevice item = items.get(position);


    }

    @Override
    public int getItemCount() {
        return (items!=null)?items.size():0;
    }

    void update(){
        notifyDataSetChanged();
    }

    void addElement(BluetoothDevice dev){
        this.items.put(dev.getAddress(),dev);
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

