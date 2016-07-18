package com.game.jieluo.crazygame.baiduad.feeds;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.baidu.mobad.feeds.BaiduNative;
import com.baidu.mobad.feeds.BaiduNative.BaiduNativeNetworkListener;
import com.baidu.mobad.feeds.NativeErrorCode;
import com.baidu.mobad.feeds.NativeResponse;
import com.baidu.mobad.feeds.RequestParameters;
import com.game.jieluo.crazygame.R;

/**
 * 背景：在开发者不满足已有的下载确认接口为前提
 * 目的：开发者自行实现二次确认弹窗实例，此demo以信息流广告非WiFi环境下是否下载为例
 *
 */
public class ReconfirmActivity extends Activity {
    /**
     * 非WiFi环境下下载确认开关
     */
    private static boolean CONFIRM_DL_NOT_WIFI = true;
    ListView listView;
    MyAdapter adapter;
    List<NativeResponse> nrAdList = new ArrayList<NativeResponse>();
    private RequestParameters reuqestParameters;
    private static String YOUR_AD_PLACE_ID = "2611082"; // 双引号中填写自己的广告位ID
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        Log.i("demo", "ListViewActivity.onCreate");
        setContentView(R.layout.listview_activity);
        listView = (ListView) this.findViewById(R.id.native_list_view);
        adapter = new MyAdapter(this);
        listView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                    int position, long id) {
                 // 在此处创建cm虽然耗能存，但可以在每次点击做出实时网络状态判断
                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                final NetworkInfo anInfo = cm.getActiveNetworkInfo();
                
                Log.i("demo", "ListViewActivity.onItemClick");
                final NativeResponse nrAd = nrAdList.get(position);
                
                if (anInfo == null) {
                    Toast.makeText(ReconfirmActivity.this, "当前没有可用网络，请检查网络状态", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (anInfo.getType() == ConnectivityManager.TYPE_WIFI) { // 网络类型为WIFI
                    nrAd.handleClick(view);
                } else if (CONFIRM_DL_NOT_WIFI) { // 非WiFi环境，可再次自定义下载确认弹窗
                    Log.i("ReconfirmActivity", "CONFIRM_DL_NOT_WIFI");
                    if (nrAd.isDownloadApp()) { // 下载类应用
                        Log.i("ReconfirmActivity", "isDownloadApp");
                        Builder builder = new Builder(ReconfirmActivity.this);
                        builder.setMessage("当前为非WiFi网络，确认下载吗？");
                        builder.setPositiveButton("确认", new OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                nrAd.handleClick(view);
                            }
                        });
                        builder.setNegativeButton("取消", new OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                
                            }
                        });
                        builder.show();
                        
                       
                    } else {  
                        nrAd.handleClick(view);
                    }
                }
            }
        });
        fetchAd(this); 
    }

    public void showAdList() {
        listView.setAdapter(adapter);
    }
    
    public void fetchAd(Activity activity) {
        /**
         * Step 1. 创建BaiduNative对象，参数分别为：
         * 上下文context，广告位ID, BaiduNativeNetworkListener监听（监听广告请求的成功与失败）
         */
        BaiduNative baidu = new BaiduNative(activity, YOUR_AD_PLACE_ID,
                new BaiduNativeNetworkListener() {

                    @Override
                    public void onNativeFail(NativeErrorCode arg0) {
                        Log.w("ListViewActivity", "onNativeFail reason:"
                                + arg0.name());
                    }

                    @Override
                    public void onNativeLoad(List<NativeResponse> arg0) {
                        if (arg0 != null && arg0.size() > 0) {
                            nrAdList = arg0;
                            showAdList();
                        }
                    }

                });

        reuqestParameters = new RequestParameters.Builder()
 //                .confirmDownloading(true)// 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
                .build();

        baidu.makeRequest(reuqestParameters);
    }

    class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;

        public MyAdapter(Context context) {
            super();
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return nrAdList.size();
        }

        @Override
        public NativeResponse getItem(int position) {
            return nrAdList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NativeResponse nrAd = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.native_ad_row, null);
            }
            AQuery aq = new AQuery(convertView);
            aq.id(R.id.native_icon_image).image(nrAd.getIconUrl(),
                    false, true);
            aq.id(R.id.native_main_image).image(nrAd.getImageUrl(),
                    false, true);
            aq.id(R.id.native_text).text(nrAd.getDesc());
            aq.id(R.id.native_title).text(nrAd.getTitle());
            
            aq.id(R.id.native_cta).text(nrAd.isDownloadApp() ? "下载" : "查看");
            nrAd.recordImpression(convertView);
            return convertView;
        }

    }
}
