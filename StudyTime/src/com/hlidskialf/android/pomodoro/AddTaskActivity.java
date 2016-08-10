package com.hlidskialf.android.pomodoro;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskActivity extends Activity
	{
		
		EditText taskName;
		EditText numberOfSessions;
		Button saveButtion;
		StoreManager myStore;
		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.add_task);
				myStore = new StoreManager(this);
				
				taskName = (EditText) findViewById(R.id.taskNameeditText1);
				numberOfSessions = (EditText)findViewById(R.id.sessioneditText2);
				saveButtion = (Button) findViewById(R.id.saveButtonAddTask);
				
			
			}
		
		
		
		public void saveTaskFunction(View View)
		{
			String Name = taskName.getText().toString();
			String noOfSession = numberOfSessions.getText().toString();
			if(Name!=null & noOfSession!=null)
					{
						myStore.insertTasks(Name, noOfSession, "0");
						Intent intent = new Intent(this, MyTasksActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
		}
		
		public static String getCurrentDate()
			{
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				return date;
			}
		

	}
