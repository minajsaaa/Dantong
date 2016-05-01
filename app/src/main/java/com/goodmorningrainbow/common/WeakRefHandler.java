package com.goodmorningrainbow.common;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

public class WeakRefHandler extends Handler {

	private final WeakReference< IOnHandlerMessage > mHandlerObject;

    public WeakRefHandler( IOnHandlerMessage object )
    {
        mHandlerObject = new WeakReference< IOnHandlerMessage >( object );
    }

    @Override
    public void handleMessage( Message msg )
    {
        super.handleMessage( msg );

        IOnHandlerMessage object = ( IOnHandlerMessage ) mHandlerObject.get();

        if ( object == null )
            return;

        object.handleMessage( msg );
    }
}
