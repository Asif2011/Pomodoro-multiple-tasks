package com.hlidskialf.android.pomodoro;

import java.util.Calendar;



import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class PomodoroActivity extends Activity
	{

		private TextView textViewShowTime;
		private TextView mStatusText;
		//private ViewGroup mTomatoBar;
		private Button mStartButton, mStopButton, mTaskButton;
		TextView tomatoCount;
		TextView taskDetailTextView;
		

		private SharedPreferences mPrefs;

		private int mCurAlarmType, mTomatoCount;
		

		private static final int REQUEST_PREFERENCES = 1;

		// ////////////////////////////////////////
		private ProgressBar mProgressBar;
		private long totalTimeCountInMilliseconds; // total count down time in
		// milliseconds
		private long timeBlinkInMilliseconds; // start time of start blinking
		private boolean blink;
		private long timeLeftInMilliseconds;
		private CountDownTimer countDownTimer;
		private TextView taskNameView;
		private String taskName;
		private int sessionCompleted;
		private int sessionTotal;
		private StoreManager myStoreTasks;
		boolean isTimerRunning;
		boolean startedBefore;

		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				
				SharedPreferences pref = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
				startedBefore = pref.getBoolean("STARTED_BEFORE", false);
				if (!startedBefore)
					{
						//Launch help activity
						/////////////////////////////////////////
					/////////////////////////////////
						SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
						SharedPreferences.Editor editor = preference.edit();
						editor.putBoolean("STARTED_BEFORE", true);
						editor.commit();
					}
				
				
				super.onCreate(savedInstanceState);
				setContentView(R.layout.main);
				myStoreTasks = new StoreManager(this);
				

				// /////////////////////
				blink = false;
				isTimerRunning = false;
				taskName = null;
				sessionCompleted = 0;
				sessionTotal = 0;
				taskNameView = (TextView) findViewById(R.id.taskNametextView1);
				timeBlinkInMilliseconds = 10 * 1000;
				tomatoCount = (TextView) findViewById(R.id.tomato_count);
				mTaskButton = (Button)findViewById(R.id.add_task_button);
				taskDetailTextView = (TextView)findViewById(R.id.taskDetailTextView);
				textViewShowTime = (TextView) findViewById(R.id.tvTimeCount);

				mStatusText = (TextView) findViewById(R.id.tomato_status);
				//mTomatoBar = (ViewGroup) findViewById(R.id.tomato_bar);
				
				mStartButton = (Button) findViewById(R.id.tomato_start);
				mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
				mStartButton.setOnClickListener(new Button.OnClickListener()
					{
						public void onClick(View b)
							{
								mPrefs = getSharedPreferences(
										Pomodoro.PREFERENCES, 0);
								int duration = mPrefs.getInt(Pomodoro.PREF_TOMATO_DURATION,
										Pomodoro.PREF_TOMATO_DURATION_DEFAULT);
								setTimer(duration);

								startTimer();
								Pomodoro.startTomato(PomodoroActivity.this);
								// mTimerView.start(duration*60000);

								mCurAlarmType = Pomodoro.ALARM_TYPE_TOMATO;

								update_tomato_bar();
								update_status();
							}
					});

				mStopButton = (Button) findViewById(R.id.tomato_stop);
				mStopButton.setOnClickListener(new Button.OnClickListener()
					{
						public void onClick(View b)
							{
								Pomodoro.stopTomato(PomodoroActivity.this);
								// mTimerView.stop();

								countDownTimer.cancel();
								if (mCurAlarmType == Pomodoro.ALARM_TYPE_REST)
									mTomatoCount = Pomodoro.setTomatoCount(
											PomodoroActivity.this, -1);

								mCurAlarmType = Pomodoro.ALARM_TYPE_NONE;

								update_tomato_bar();
								update_status();

							}
					});
				
				
				
				
						
						
					

			}
		
		public void myTaskFunction(View view)
		{
			Intent intentActivity = new Intent(this, MyTasksActivity.class);
			startActivity(intentActivity);
			
		}

		@Override
		public void onResume()
			{
				super.onResume();
				taskName = getTaskSelected(this);
						if(taskName!=null)
							{
								sessionCompleted = myStoreTasks.getcompleted_sessions(taskName);
								
								
								sessionTotal = myStoreTasks.getTotal_sessions(taskName);
								//Toast.makeText(this, ""+sessionTotal+" compltede", Toast.LENGTH_SHORT).show();
								
								taskNameView.setText(taskName);
								
								taskDetailTextView.setText(""+sessionCompleted+"/"+sessionTotal);
								saveTask(this, taskName);
								
							}
						
			
						
				mPrefs = getSharedPreferences(Pomodoro.PREFERENCES, 0);
				long start = mPrefs.getLong(Pomodoro.PREF_ALARM_START, 0);
				long duration = mPrefs.getLong(Pomodoro.PREF_ALARM_DURATION, 0);
				mCurAlarmType = mPrefs.getInt(Pomodoro.PREF_ALARM_TYPE, 0);
				mTomatoCount = mPrefs.getInt(Pomodoro.PREF_TOMATO_COUNT, 0);

				
				
				if (mCurAlarmType == Pomodoro.ALARM_TYPE_TOMATO)
					{
						// mTimerView.start(duration,start);
						long endingTime = start+duration;
						 long leftTime = endingTime - System.currentTimeMillis();
						 setTimer(leftTime, (mPrefs.getInt(Pomodoro.PREF_TOMATO_DURATION,
									Pomodoro.PREF_TOMATO_DURATION_DEFAULT)));
						 startTimer();
						

					} else if (mCurAlarmType == Pomodoro.ALARM_TYPE_REST)
					{
						// mTimerView.start(duration,start);
						long endingTime = start+duration;
						 long leftTime = endingTime - System.currentTimeMillis();
						 setTimer(leftTime, Pomodoro.PREF_REST_DURATION_DEFAULT);
						 startTimer();
						
					}

				update_tomato_bar();
				update_status();
			}

		@Override
		public void onPause()
			{
				super.onPause();
				if(isTimerRunning)
					{
						countDownTimer.cancel();
					}
			}

		@Override
		public boolean onCreateOptionsMenu(Menu menu)
			{
				super.onCreateOptionsMenu(menu);
				getMenuInflater().inflate(R.menu.options, menu);

				return true;
			}

		@Override
		public boolean onPrepareOptionsMenu(Menu menu)
			{
				MenuItem reset_item = menu.findItem(R.id.options_menu_reset);
				reset_item.setVisible(Pomodoro.getTomatoCount(this) > 0);
				return super.onPrepareOptionsMenu(menu);
			}

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
			{
				switch (item.getItemId())
					{
					case R.id.options_menu_preferences:
						startActivityForResult(new Intent(this,
								PomodoroPreferences.class), REQUEST_PREFERENCES);
						return true;
					case R.id.options_menu_about:
						View layout = getLayoutInflater().inflate(
								R.layout.about, null);
						AlertDialog dia = new AlertDialog.Builder(this)
								.setTitle(R.string.about_title).setView(layout)
								.setPositiveButton(android.R.string.ok, null)
								.show();
						return true;
					case R.id.options_menu_reset:
						Pomodoro.stopTomato(PomodoroActivity.this);
						mTomatoCount = Pomodoro.setTomatoCount(
								PomodoroActivity.this, 0);
						
						update_tomato_bar();
						update_status();
						return true;
					}
				return super.onOptionsItemSelected(item);
			}

		private void update_status()
			{
				taskNameView.setText(taskName);
				if (mCurAlarmType == Pomodoro.ALARM_TYPE_TOMATO)
					{
						//mStatusText.setText(R.string.status_tomato);
						mStartButton.setVisibility(View.GONE);
						mStopButton.setVisibility(View.VISIBLE);
					} else if (mCurAlarmType == Pomodoro.ALARM_TYPE_REST)
					{
						//mStatusText.setText(R.string.status_rest);
						mStartButton.setVisibility(View.GONE);
						mStopButton.setVisibility(View.VISIBLE);
					} else
					{
						//mStatusText.setText(R.string.status_none);
						mStartButton.setEnabled(true);
						mStartButton.setVisibility(View.VISIBLE);
						mStopButton.setVisibility(View.GONE);
					}

			}

		private void update_tomato_bar()
			{
				int count = myStoreTasks.getTotalSessiontoday();
				
				
				tomatoCount.setText("Session = "+String.valueOf(mTomatoCount)+"/"+count);
				/*mTomatoBar.removeAllViews();
				int i;
				int c = 0;
				LinearLayout holder = null;
				for (i = 0; i < mTomatoCount; i++)
					{
						if (c++ % 6 == 0)
							{
								holder = new LinearLayout(this);
								holder.setGravity(Gravity.CENTER);
								mTomatoBar.addView(holder);
							}
						ImageView iv = new ImageView(this);
						iv.setImageResource(R.drawable.tomato);
						holder.addView(iv);
					}
				if (c++ % 6 == 0)
					{
						holder = new LinearLayout(this);
						holder.setGravity(Gravity.CENTER);
						mTomatoBar.addView(holder);
					}
				if (mCurAlarmType == Pomodoro.ALARM_TYPE_TOMATO)
					{
						ImageView iv = new ImageView(this);
						iv.setImageResource(R.drawable.greentomato);
						holder.addView(iv);
					}
				if (mCurAlarmType == Pomodoro.ALARM_TYPE_REST)
					{
						ImageView iv = new ImageView(this);
						iv.setImageResource(R.drawable.tomato);
						holder.addView(iv);
					} */
			}

		private void startTimer()
			{

				countDownTimer = new CountDownTimer(
						totalTimeCountInMilliseconds, 500)

					{

						@Override
						public void onTick(long leftTimeInMilliseconds)
							{
								// TODO Auto-generated method stub
								long seconds = leftTimeInMilliseconds / 1000;
								// i++;
								// Setting the Progress Bar to decrease wih the
								// timer
								mProgressBar
										.setProgress((int) (leftTimeInMilliseconds / 1000));
								// textViewShowTime.setTextAppearance(getApplicationContext(),
								// R.style.Base_TextAppearance_AppCompat_Large);
								// textViewShowTime.setTextSize(60);

								if (leftTimeInMilliseconds < timeBlinkInMilliseconds)
									{

										textViewShowTime
												.setTextColor(Color.RED);
										// change the style of the textview ..
										// giving a red
										// alert style

										if (blink)
											{
												textViewShowTime
														.setVisibility(View.VISIBLE);
												AudioManager audioManager = (AudioManager) getApplicationContext()
														.getSystemService(
																Context.AUDIO_SERVICE);
												audioManager
														.playSoundEffect(
																audioManager.FX_KEYPRESS_STANDARD,
																2);

												// if blink is true, textview
												// will be visible
											} else
											{
												textViewShowTime
														.setVisibility(View.INVISIBLE);
											}

										blink = !blink; // toggle the value of
														// blink
										

									}

								textViewShowTime.setText(String.format("%02d",
										seconds / 60)
										+ ":"
										+ String.format("%02d", seconds % 60));

								// format the textview to show the easily
								// readable format

							}

						@Override
						public void onFinish()
							{
								// TODO Auto-generated method stub
								textViewShowTime.setText("Time up!");
								textViewShowTime.setVisibility(View.VISIBLE);
								isTimerRunning = false;
								// buttonStartTime.setVisibility(View.VISIBLE);
								// buttonStopTime.setVisibility(View.GONE);
								// edtTimerValue.setVisibility(View.VISIBLE);

							}
					}.start();
					isTimerRunning = true;
			}

		private void setTimer(int time)
			{

				//mPrefs = getSharedPreferences(Pomodoro.PREFERENCES, 0);
				
				totalTimeCountInMilliseconds = 60 * time * 1000;

				mProgressBar.setMax(60 * time);

				totalTimeCountInMilliseconds = 60 * time * 1000;

				timeBlinkInMilliseconds = 10 * 1000;
			}
		private void setTimer(long time, int maxTime)
		{
			mPrefs = getSharedPreferences(Pomodoro.PREFERENCES, 0);
			mProgressBar.setMax(maxTime*60);
			totalTimeCountInMilliseconds = time;
			timeBlinkInMilliseconds = 10 * 1000;
			
		}
		
		
		
		
		@Override
		protected void onNewIntent(Intent intent)
			{
				super.onNewIntent(intent);
				setIntent(intent);
			}
		
		
		public static String getTaskSelected(Context context)
			{
				SharedPreferences mSharedPreference = context.getSharedPreferences("active_task", 0);
				return mSharedPreference.getString("active_task",null);
			}
		public static void saveTask(Context context, String value)
			{
				SharedPreferences mSharedPreference = context.getSharedPreferences("active_task", 0);
				SharedPreferences.Editor editor = mSharedPreference.edit();
				editor.putString("active_task", value);
				editor.commit();
			}
		public void startAlarm(Context context, int hour, int minute)
			{
				AlarmManager alarmMgr = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				Intent intent = new Intent(context, PomodoroActivity.class);
				PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
						0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				cal.set(cal.HOUR_OF_DAY, hour);
				cal.set(cal.MINUTE, minute);

				alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						alarmIntent);
				Toast.makeText(context, "alarm is reset", Toast.LENGTH_SHORT)
						.show();
			}

	}
