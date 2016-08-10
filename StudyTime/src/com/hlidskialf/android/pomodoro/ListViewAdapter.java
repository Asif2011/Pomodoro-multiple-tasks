package com.hlidskialf.android.pomodoro;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends ArrayAdapter<MySessionStrings>
	{
		
		//Activity context;
	    //ArrayList<String> title;
	    //ArrayList<String> description;
	   // ViewHolder holder;
	    private ArrayList<MySessionStrings> objects;

		public ListViewAdapter(Context context, int textViewResourceId, ArrayList<MySessionStrings> objects)
			{
				// TODO Auto-generated constructor stub
				super(context, textViewResourceId, objects);
				this.objects = objects;
				
			}

		@Override
		public int getCount()
			{
				// TODO Auto-generated method stub
				return objects.size();
			}

		@Override
		public MySessionStrings getItem(int position)
			{
				// TODO Auto-generated method stub
				return objects.get(position);
			}

		@Override
		public long getItemId(int position)
			{
				// TODO Auto-generated method stub
				return position;
			}
		
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
			{
				
				// TODO Auto-generated method stub
				View v = convertView;
				if (v == null) {
					LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = inflater.inflate(R.layout.list_item_layout, null);
					 MySessionStrings i = objects.get(position);
					 if (i != null) {

							// This is how you obtain a reference to the TextViews.
							// These TextViews are created in the XML files we defined.

							TextView tt = (TextView) v.findViewById(R.id.task_item);
							TextView ttd = (TextView) v.findViewById(R.id.task_session_item);
						

							// check to see if each individual textview is null.
							// if not, assign some text!
							if (tt != null){
								tt.setText(i.getTaskName());
							}
							if (ttd != null){
								ttd.setText(i.getTaskSessionState());
							}
							
						}

						// the view must be returned to our activity
						
				}
				return v;
			}
		
		 

	}
