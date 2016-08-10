package com.hlidskialf.android.pomodoro;

import java.util.ArrayList;
import java.util.HashMap;









import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class StoreManager extends SQLiteOpenHelper
	{
		public static final String DATABASE_NAME = "MyDBName.db";
		////////////////////////////////////////////////////////////////
		public static final String tasks_TABLE_NAME = "tasks";
		public static final String tasks_COLUMN_NAME = "name";
		public static final String tasks_COLUMN_total_sessions = "total_sessions";
		public static final String tasks_COLUMN_completed_sessions = "completed_sessions";
		/////////////////////////////////////////////////////////////////


		//private HashMap hp;

		public StoreManager(Context context)
			{
				super(context, DATABASE_NAME, null, 1);
				// TODO Auto-generated constructor stub
			}

		@Override
		public void onCreate(SQLiteDatabase db)
			{
				// TODO Auto-generated method stub
				
				db.execSQL("CREATE TABLE tasks(name text primary key,total_sessions text, completed_sessions text)");
				

			}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
			{
				// TODO Auto-generated method stub
				db.execSQL("DROP TABLE IF EXISTS tasks");
				onCreate(db);
			}

		
		
		public boolean insertTasks(String name, String total_sessions,
				String completed_sessions)
			{
				
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues contentValues = new ContentValues();
				contentValues.put("name", name);
				contentValues.put("total_sessions", total_sessions);
				contentValues.put("completed_sessions", completed_sessions);
				db.insert("tasks", null, contentValues);
				return true;
			}
		

		
		public boolean updateTasksName(String oldName, String newName, String total_sessions,
				String completed_sessions)
			{
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues contentValues = new ContentValues();
				contentValues.put("total_sessions", total_sessions);
				contentValues.put("completed_sessions", completed_sessions);
				contentValues.put("name", newName);
				db.update("tasks", contentValues, "name = ? ", new String[]
					{oldName});
				
				return true;
			}
		
		public boolean updateTasksCompletedSession(String name,
				String completed_sessions)
			{
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues contentValues = new ContentValues();
				//contentValues.put("total_sessions", total_sessions);
				contentValues.put("completed_sessions", completed_sessions);
				//contentValues.put("name", newName);
				db.update("tasks", contentValues, "name = ? ", new String[]
					{name});
				
				return true;
	
			}
		public void resetAllCompletedTasks()
		{
			
			SQLiteDatabase db = this.getReadableDatabase();
			ContentValues contentValues = new ContentValues();
			Cursor res = db.rawQuery("SELECT * FROM tasks", null);
			String name;
			if(res.moveToFirst())
				{
					while (res.isAfterLast() == false)
						{
							name = res.getString(res
									.getColumnIndex(tasks_COLUMN_NAME));
							contentValues.put("completed_sessions", 0);
							db.update("tasks", contentValues, "name = ? ", new String[]
										{name});
							 res.moveToNext();
						}
					
				}
			
		}
		
		
		public int deletetasks(String name)
			{
				SQLiteDatabase db = this.getWritableDatabase();
				String[] selectionArgs ={ name };
				
				 int rowdeleted =db.delete("tasks", "name like ?", selectionArgs);
				 db.close();
				 return rowdeleted;
			}
		
		public ArrayList<String> getAlltasks()
		{
			ArrayList<String> array_list = new ArrayList<String>();
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor res = db.rawQuery("SELECT * FROM tasks", null);
			if(res.moveToFirst())
				{
					while (res.isAfterLast() == false)
						{
							array_list.add(res.getString(res
									.getColumnIndex(tasks_COLUMN_NAME)));
							res.moveToNext();
						}
					
				}
			return array_list;
		}
		
		
		
		
		
		public ArrayList<Integer> getAlltotal_sessionss()
		{
			ArrayList<Integer> array_list = new ArrayList<Integer>();
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor res = db.rawQuery("SELECT * FROM tasks", null);
			if(res.moveToFirst())
				{
					while (res.isAfterLast() == false)
						{
							array_list.add(Integer.valueOf(res.getString(res
									.getColumnIndex(tasks_COLUMN_total_sessions))));
							res.moveToNext();
						}
					
				}
			return array_list;
		}
		
		public int getTotalSessiontoday()
		{
			//ArrayList<Integer> array_list = new ArrayList<Integer>();
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor res = db.rawQuery("SELECT * FROM tasks", null);
			int count = 0;
			if(res.moveToFirst())
				{
					while (res.isAfterLast() == false)
						{
							count += Integer.valueOf(res.getString(res
									.getColumnIndex(tasks_COLUMN_total_sessions)));
							res.moveToNext();
						}
					
				}
			return count;
		}
		
		public ArrayList<MySessionStrings> getAllSessionState()
		{
			ArrayList<MySessionStrings> array_list = new ArrayList<MySessionStrings>();
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor res = db.rawQuery("SELECT * FROM tasks", null);
			MySessionStrings taskString;
			String taskName;
			String taskSessionState;
			if(res.moveToFirst())
				{
					while (res.isAfterLast() == false)
						{
							taskName = res.getString(res
									.getColumnIndex(tasks_COLUMN_NAME));
							taskSessionState = res.getString(res
									.getColumnIndex(tasks_COLUMN_completed_sessions))+"/"+res.getString(res
											.getColumnIndex(tasks_COLUMN_total_sessions));
							
							taskString = new MySessionStrings(taskName, taskSessionState);
							
							array_list.add(taskString);
							res.moveToNext();
						}
					
				}
			return array_list;
		}
		
		public ArrayList<String> getAllSessionStates()
		{
			ArrayList<String> array_list = new ArrayList<String>();
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor res = db.rawQuery("SELECT * FROM tasks", null);
			if(res.moveToFirst())
				{
					while (res.isAfterLast() == false)
						{
							array_list.add(res.getString(res
									.getColumnIndex(tasks_COLUMN_completed_sessions))+"/"+res.getString(res
											.getColumnIndex(tasks_COLUMN_total_sessions)));
							res.moveToNext();
						}
					
				}
			return array_list;
		}
		
		
	
		   public int numberOfRowsOftasks(){
			      SQLiteDatabase db = this.getReadableDatabase();
			      int numRows = (int) DatabaseUtils.queryNumEntries(db, tasks_COLUMN_NAME);
			      return numRows;
			   }
		   
		public Cursor getDataOftasks(String name)
			{
				SQLiteDatabase db = this.getReadableDatabase();
				String[] idName = {name};
				Cursor res = db.rawQuery("SELECT * FROM tasks WHERE name LIKE ?", idName );
				return res;
			}
		
		public int getcompleted_sessions(String nameID)
			{
				SQLiteDatabase db = this.getReadableDatabase();
				String[] name = {nameID};
				Cursor res = db.rawQuery("SELECT * FROM tasks WHERE name LIKE ?", name);
				int completed_sessions =Integer.valueOf( res.getString(res.getColumnIndex(StoreManager.tasks_COLUMN_completed_sessions)));
				return completed_sessions;
				
			}
		
		public int getTotal_sessions(String nameID)
			{
				SQLiteDatabase db = this.getReadableDatabase();
				String[] name = {nameID};
				Cursor res = db.rawQuery("SELECT * FROM tasks WHERE name LIKE ?", name);
				int total_Sessions =Integer.valueOf( res.getString(res.getColumnIndex(StoreManager.tasks_COLUMN_total_sessions)));
				return total_Sessions;
				
			}
		

	}
