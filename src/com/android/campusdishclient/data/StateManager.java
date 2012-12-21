package com.android.campusdishclient.data;

import android.content.Context;

public class StateManager {
	private static StateManager mState;

	private StateManager(Context pContext){
		
	}
	public static StateManager getStateManager(Context pContext){
		if(mState==null){
			mState=new StateManager(pContext);
		}
		return mState;
	}
	public State getNetworkState(){
		return State.NET_OK;
	}
	public enum State{
		NET_NOCONNECTION,
		NET_OK,
		
	}
}
