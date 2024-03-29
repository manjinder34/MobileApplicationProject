package com.unik.firebaseweather;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClickListener implements
		RecyclerView.OnItemTouchListener {

	private OnItemClickListener mListener;

	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	GestureDetector mGestureDetector;

	public RecyclerItemClickListener(Context context,
                                     OnItemClickListener listener) {
		mListener = listener;
		mGestureDetector = new GestureDetector(context,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						return true;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
		// TODO Auto-generated method stub
		View childView = view.findChildViewUnder(e.getX(), e.getY());
		if (childView != null && mListener != null
				&& mGestureDetector.onTouchEvent(e)) {
			mListener.onItemClick(childView, view.getChildPosition(childView));
			return true;
		}
		return false;
	}

	@Override
	public void onTouchEvent(RecyclerView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

}
