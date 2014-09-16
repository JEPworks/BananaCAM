package com.ripeyet.bananacam;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.parse.Parse;
import com.parse.PushService;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "IfpakLFfSGFAPk1OXeIDH6XEbBP3yX9zoQIsrPvE", "52lXNqpn9bAqu3kQSYW9IoWNhsUapSQBmxfYT0Iu");
        PushService.setDefaultPushCallback(this, RoomSelectionActivity.class);
        PushService.subscribe(this, "Kitchen", RoomSelectionActivity.class);
        PushService.subscribe(this, "Suite300",RoomSelectionActivity.class);
        PushService.subscribe(this, "Suite350",RoomSelectionActivity.class);
    }
}