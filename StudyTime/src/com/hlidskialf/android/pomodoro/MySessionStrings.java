package com.hlidskialf.android.pomodoro;

public class MySessionStrings
	{
		private String taskName;
		private String taskSessionState;

		public MySessionStrings(String taskName, String taskSessionState)
			{
				// TODO Auto-generated constructor stub
				this.taskName = taskName;
				this.taskSessionState = taskSessionState;
			}

		public String getTaskName()
			{
				return taskName;
			}

		public void setTaskName(String taskName)
			{
				this.taskName = taskName;
			}

		public String getTaskSessionState()
			{
				return taskSessionState;
			}

		public void setTaskSessionState(String taskSessionState)
			{
				this.taskSessionState = taskSessionState;
			}

	}
