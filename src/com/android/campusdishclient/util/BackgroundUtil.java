package com.android.campusdishclient.util;

import android.os.AsyncTask;

public class BackgroundUtil {
	public static void doInBackGround(PreDeliver preDeliver, Deliver deliver,
			AftDeliver aftDeliver) {
		if (deliver == null) {
			throw new NullPointerException("deliver不能为null");
		}
		BackGroundTask m = new BackGroundTask(preDeliver, aftDeliver);
		m.execute(deliver);
	}

	private static class BackGroundTask extends
			AsyncTask<Deliver, Integer, Object> {
		private PreDeliver mPreDeliver;
		private AftDeliver mAftDeliver;

		private BackGroundTask(PreDeliver preDeliver, AftDeliver aftDeliver) {
			mPreDeliver = preDeliver;
			mAftDeliver = aftDeliver;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (mPreDeliver != null) {
				mPreDeliver.todo();
			}

		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mAftDeliver != null) {
				mAftDeliver.todo(result);
			}
		}

		@Override
		protected Object doInBackground(Deliver... params) {
			return params[0].todo();
		}

	}

	public interface PreDeliver {
		public void todo();
	}

	public interface Deliver {
		public Object todo();
	}

	public interface AftDeliver {
		public void todo(Object result);
	}
}
