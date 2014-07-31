package com.okamipride.scanlanip;

import java.io.BufferedOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.okamipride.scanlanip.scan.ScanIp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ScanMainActivity extends Activity implements OnClickListener{
	
	static final String TAG = "ScanMainActivity";
	private final static int SOCKET_CONNECT_TIMEOUT = 1000;
	public static final int SERVERPORT = 45405;
	private List<InetAddress> scanResult = null;
	ListIpAdapter ipAdpater = null;
	ListView lstview;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_main);
        scanResult = new ArrayList<InetAddress>();
        ipAdpater = new ListIpAdapter(scanResult);
        lstview = (ListView) findViewById(R.id.lsv_ips);
        lstview.setAdapter(ipAdpater);
        Button discover = (Button) findViewById(R.id.btn_search);
        discover.setOnClickListener(this);
    }
    
    @Override
	public void onClick(View v) {
		new ScanIpAsyncTask().execute(this);
		
	}
    
    class ScanIpAsyncTask extends AsyncTask <Context, String, Boolean> {
		private ProgressDialog syncProgress = null;
		private List<InetAddress> lanIpList;
		private long taketime = 0;
		private ScanIp scan;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			syncProgress = new ProgressDialog(ScanMainActivity.this);
			syncProgress.setTitle(R.string.sync_prepare_title);
			syncProgress.setCancelable(true);
			syncProgress.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					Log.e(TAG, "onKey KeyCode: " + String.valueOf(keyCode));
					dialog.cancel();
					if (null != scan)
						scan.onCancell();
					cancel(true);
					return true;
				}
			});
			
			syncProgress.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					Log.e(TAG, "onDismiss()");
			
					if (null != scan)
						scan.onCancell();
					cancel(true);
					Thread.interrupted();
				}
			});
			// dialog show 
			syncProgress.show();
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Log.d(TAG, "ScanIpAsyncTask Cancel");
			if (syncProgress != null && syncProgress.isShowing()) {
				syncProgress.dismiss();
				syncProgress = null;
			}
			cancel(true);
		}

		@Override
		protected Boolean doInBackground(Context... arg0) {
			long scanStart = 0; 
			long scanEnd = 0;
			boolean dataSend = false;
			scanStart = System.currentTimeMillis();
			scan = new ScanIp();
			Log.d(TAG, "startScan");
			
			if (!isCancelled()) {
			   	publishProgress (getString(R.string.sync_start_status_discover));
				lanIpList =  scan.startScan(arg0[0]);
				if (lanIpList != null) {
					dataSend = true;
				}
			}else {
				lanIpList = null;
			}
			
			scanEnd = System.currentTimeMillis();
			taketime = scanEnd - scanStart;
			Log.d(TAG, "endScan taketime = " + Long.toString(taketime));
			
			return dataSend;			
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (syncProgress != null && syncProgress.isShowing()) {
				syncProgress.dismiss();
				syncProgress = null;
			}
			if (result) {
				scanResult.clear();
				scanResult.addAll(lanIpList);
				Log.e(TAG,"scanResult size =" +Integer.toString(scanResult.size()));//finish();
				Log.e(TAG,"lanIpList size =" +Integer.toString(lanIpList.size()));//finish();
			} else {
				scanResult.clear();
				Log.e(TAG,"false scanResult size =" +Integer.toString(scanResult.size()));//finish();
				//finish();
			}
			ipAdpater.notifyDataSetChanged();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			syncProgress.setMessage(values[0]);
		}	
	}
}
