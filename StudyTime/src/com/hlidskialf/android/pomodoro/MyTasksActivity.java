package com.hlidskialf.android.pomodoro;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MyTasksActivity extends Activity
	{
		
		ListView taskListView;
		StoreManager myTaskstore;
		ArrayList<String> tasksArray;
		ArrayList<Integer> total_sessionArray;
		ArrayList<String> totalSessionArrayState;
		String taskNameTemp;
		ArrayList< MySessionStrings> taskSessionObjects;

		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.my_tasks);
				taskListView = (ListView) findViewById(R.id.tasks_listView1);
				run();
				
			}
		public void resetTaskButtonFunction(View view)
			{
				myTaskstore.resetAllCompletedTasks();
			}
		
		
		public void run()
		{
			myTaskstore = new StoreManager(this);
			tasksArray = myTaskstore.getAlltasks();
			total_sessionArray = myTaskstore.getAlltotal_sessionss();
			totalSessionArrayState = myTaskstore.getAllSessionStates();
			taskSessionObjects = myTaskstore.getAllSessionState();
			
			
			
			showList();
			
			//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			
			taskListView.setOnItemClickListener(new ListView.OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> adapter,
							View view, int position, long id)
						{
							
							 MySessionStrings i =   (MySessionStrings) adapter.getItemAtPosition(position);
							 String itemValue= i.getTaskName();
							Intent myIntent = new Intent(getBaseContext(), PomodoroActivity.class);
							PomodoroActivity.saveTask(getApplicationContext(), itemValue);
							
							myIntent.putExtra("nameSelected", itemValue);
							startActivity(myIntent);
							
							
						}
				});
			taskListView.setOnItemLongClickListener(new OnItemLongClickListener()
				{

					@Override
					public boolean onItemLongClick(AdapterView<?> adapter,
							View arg1, int position, long id)
						{
								{
									// TODO Auto-generated method
									// stub
									MySessionStrings i =   (MySessionStrings) adapter.getItemAtPosition(position);
									taskNameTemp= i.getTaskName();
									
									AlertDialog.Builder builder = new AlertDialog.Builder(
											MyTasksActivity.this);
									builder.setMessage(
											"Delete Task...?")
											.setPositiveButton(
													"Delete",
													new DialogInterface.OnClickListener()
														{
															public void onClick(
																	DialogInterface dialog,
																	int id)
																{
																	if(!PomodoroActivity.getTaskSelected(getApplicationContext()).equals(taskNameTemp))
																		{
																			myTaskstore.deletetasks(taskNameTemp);
																			Toast.makeText(
																					getApplicationContext(),
																					"Deleted Successfully",
																					Toast.LENGTH_SHORT)
																					.show();
																			run();
																		}
																	else{
																		Toast.makeText(
																				getApplicationContext(),
																				"Selected Task can't be deleted",
																				Toast.LENGTH_SHORT)
																				.show();
																	}
																	
																	
																}
														})
											.setNegativeButton(
													"No",
													new DialogInterface.OnClickListener()
														{
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int id)
																{
																	// User
																	// cancelled
																	// the
																	// dialog

																}
														});
									AlertDialog dialog = builder.create();
									// d.setTitle("Are you sure");
									dialog.show();
									
									return false;
								}
							// TODO Auto-generated method stub
							//return false;
								
						}
				});
		}
		
		
		protected void showList()
			{
				//ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
					//	R.layout.list_item_layout, R.id.task_item, this.tasksArray);
			//taskListView.setAdapter(myAdapter);
			//taskListView.invalidateViews();
			ListViewAdapter myListAdapter = new ListViewAdapter(this, R.layout.list_item_layout, taskSessionObjects);
			taskListView.setAdapter(myListAdapter);
			taskListView.invalidate();
			
			}
		
		public void addTaskFunction(View view)
		{
			Intent intentActivity = new Intent(this, AddTaskActivity.class);
			startActivity(intentActivity);
		}
		
		

	}
