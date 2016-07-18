package com.game.jieluo.crazygame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobads.BaiduManager;
import com.game.jieluo.crazygame.baiduad.basevideo.PrerollActivity;
import com.game.jieluo.crazygame.baiduad.feeds.HTMLFeedChuChuangActivity;
import com.game.jieluo.crazygame.baiduad.feeds.HTMLFeedLunBoActivity;
import com.game.jieluo.crazygame.baiduad.feeds.ListViewActivity;
import com.game.jieluo.crazygame.baiduad.feeds.NativeOriginActivity;
import com.game.jieluo.crazygame.baiduad.feeds.ReconfirmActivity;
import com.game.jieluo.crazygame.baiduad.feeds.VideoFeedActivity;
import com.game.jieluo.crazygame.math.MathGame1;

public class CrazyActivity extends Activity implements TextWatcher {

    private MathGame1 game;
    private EditText allEdt;
    private EditText lineEdt;
    private EditText rmEdt;
    private EditText rEdt;
    private Button confirmBtn;
    private TextView tvWinner;
    private EditText bEdt;
    private int maxNumber = 0;

    Button simpleCoding;
    Button simpleVideo;
    Button simpleInter;
    Button simpleInterForVideoApp;
    Button prerollVideo;
    Button simpleIcon;
    Button simpleRecomAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crazy);
        init();
        initBdAd();
    }

    private void init()
    {
        allEdt = (EditText) findViewById(R.id.allnumber);
        lineEdt = (EditText) findViewById(R.id.linenumber);
        rmEdt = (EditText) findViewById(R.id.maxremovenumber);
        rEdt = (EditText) findViewById(R.id.anumber);
        bEdt = (EditText) findViewById(R.id.bnumber);
        tvWinner = (TextView) findViewById(R.id.tv_winner);
        confirmBtn = (Button) findViewById(R.id.confirm);
        game = new MathGame1("math",100);
        allEdt.addTextChangedListener(this);
        lineEdt.addTextChangedListener(this);
        rmEdt.addTextChangedListener(this);
        confirmBtn.setOnClickListener(confirmListener);
    }

    View.OnClickListener confirmListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            String numAStr = rEdt.getText().toString();
            String numBStr = bEdt.getText().toString();
            int numA = numAStr!=null&&!numAStr.equals("")?Integer.parseInt(numAStr):0;
            int numB = numBStr!=null&&!numBStr.equals("")?Integer.parseInt(numBStr):0;
            if(numA>maxNumber||numB>maxNumber)
            {
                Toast.makeText(CrazyActivity.this,"The number is too big.",Toast.LENGTH_SHORT).show();
            }
            if(numA>0&&numA<=maxNumber)
            {
                game.remove(numA);
                game.judgeGame();
                Toast.makeText(CrazyActivity.this,"A remove:"+numA+",remain:"+game.getRemainNumber(),Toast.LENGTH_SHORT).show();

            }else if(numB>0&&numB<=maxNumber)
            {
                game.remove(numB);
                game.judgeGame();
                Toast.makeText(CrazyActivity.this,"B remove:"+numB+",remain:"+game.getRemainNumber(),Toast.LENGTH_SHORT).show();
            }
            tvWinner.setText("remain:"+game.getRemainNumber());
            if(game.getRemainNumber()<=0)
            {
                if(numA>0)
                {
                    tvWinner.setText("Winner:A");
                }else if(numB>0)
                {
                    tvWinner.setText("Winner:B");
                }
                allEdt.setEnabled(true);
                lineEdt.setEnabled(true);
                rmEdt.setEnabled(true);
            }
            rEdt.setText("");
            bEdt.setText("");
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String allstr = allEdt.getText().toString();
        String linestr = lineEdt.getText().toString();
        String maxstr = rmEdt.getText().toString();
        int allNum = allstr!=null&&!allstr.equals("")?Integer.parseInt(allstr):0;
        int lineNum = linestr!=null&&!linestr.equals("")?Integer.parseInt(linestr):0;
        int maxNum = maxstr!=null&&!maxstr.equals("")?Integer.parseInt(maxstr):0;
        if(allNum>0&&lineNum>0&&maxNum>0)
        {
            game.setInitParams(lineNum,allNum,maxNum);
            boolean boo = game.judgeGame();
            maxNumber = maxNum;
            allEdt.setEnabled(false);
            lineEdt.setEnabled(false);
            rmEdt.setEnabled(false);
            if(boo)
            {
                game.playGame();
//                int machine = game.getNextNumber();
//                if(machine==0)
//                {
//                    Toast.makeText(this,"Your first",Toast.LENGTH_SHORT).show();
//                }else
//                {
//                    Toast.makeText(this,"I first",Toast.LENGTH_SHORT).show();
//                    game.remove(machine);
//                }
            }

        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }
    @Override
    protected void onDestroy() {
        com.baidu.mobads.production.BaiduXAdSDKContext.exit();
        super.onDestroy();
    }
    private void initBdAd()
    {
        BaiduManager.init(this);

        Button btnListView = (Button) this.findViewById(R.id.btnList);
        btnListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启信息流广告形式
                startActivity(new Intent(CrazyActivity.this, ListViewActivity.class));
            }

        });
        Button btnOrigin = (Button) this.findViewById(R.id.btnOrigin);
        btnOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启原生字段形式
                startActivity(new Intent(CrazyActivity.this, NativeOriginActivity.class));
            }

        });
        Button btnReconfirm = (Button) this.findViewById(R.id.btnReconfirm);
        btnReconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启非WiFi下二次确认下载activity
                startActivity(new Intent(CrazyActivity.this, ReconfirmActivity.class));
            }
        });

        Button btnVideoFeed = (Button) this.findViewById(R.id.btnVideoFeed);
        btnVideoFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启非WiFi下二次确认下载activity
                startActivity(new Intent(CrazyActivity.this, VideoFeedActivity.class));
            }
        });

        Button btnHtmlFeedLunBo = (Button) this.findViewById(R.id.btnHTMLFeedLunBo);
        btnHtmlFeedLunBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrazyActivity.this, HTMLFeedLunBoActivity.class);
                startActivity(intent);
            }

        });

        Button btnHtmlFeedChuChuang = (Button) this.findViewById(R.id.btnHTMLFeedChuChuang);
        btnHtmlFeedChuChuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrazyActivity.this, HTMLFeedChuChuangActivity.class);
                startActivity(intent);
            }

        });




        simpleCoding = (Button) findViewById(R.id.simple_coding);
        simpleCoding.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(simpleCoding.getContext(), BannerAdActivity.class);
                startActivity(intent);
            }
        });

        simpleInter = (Button) findViewById(R.id.simple_inters);
        simpleInter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(simpleInter.getContext(), InterstitialAdActivity.class);
                startActivity(intent);
            }
        });

        simpleInterForVideoApp = (Button) findViewById(R.id.simple_inters_for_videoapp);
        simpleInterForVideoApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(simpleInterForVideoApp.getContext(),
                        InterstitialAdForVideoAppActivity.class);
                startActivity(intent);
            }
        });

        prerollVideo = (Button) findViewById(R.id.preroll);
        prerollVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://211.151.146.65:8080/wlantest/shanghai_sun/Cherry/dahuaxiyou.mp4";
                Intent intent = new Intent(simpleInterForVideoApp.getContext(), PrerollActivity.class);
                intent.putExtra(PrerollActivity.EXTRA_CONTENT_VIDEO_URL, url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Button simpleNRLM = (Button) findViewById(R.id.nrlm);
        simpleNRLM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(simpleInterForVideoApp.getContext(), CpuAdActivity.class);
                startActivity(intent);
            }
        });
    }
}
