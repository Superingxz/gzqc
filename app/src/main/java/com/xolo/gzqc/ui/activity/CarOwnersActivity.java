package com.xolo.gzqc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwners_Adapter;
import com.xolo.gzqc.bean.child.CarOwnerMenu;
import com.xolo.gzqc.rong.MainActivity;
import com.xolo.gzqc.ui.fragment.CarOwnerCenterFragment;
import com.xolo.gzqc.ui.fragment.CoRemindFragment;
import com.xolo.gzqc.ui.fragment.MainCarOwnerFragment;
import com.xolo.gzqc.ui.view.LoadDialog;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 车主首页
 * Created by Administrator on 2016/9/23.
 */
public class CarOwnersActivity extends BaseActivity {
    public   static  boolean isLogout=false;
    boolean first;
    MainCarOwnerFragment mainCarOwnerFragment;
    CarOwnerCenterFragment carOwnerCenterFragment;
    CoRemindFragment coRemindFragment;
    FragmentManager fm;
    LinearLayout content;
    GridView carowners_gv;
    List<CarOwnerMenu> menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carowner);
        initView();


    }


    private static boolean isExit = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        exit();

    }

    CarOwners_Adapter carOwners_adapter;
    protected LoadDialog mLoad;

    void initView() {
//        Intent intent = getIntent();
//        String name = intent.getStringExtra("name");
        String user_name = user.getUser_name();

        Toast toast = Toast.makeText(this, "亲爱的"+user_name+"车主，欢迎您回来！", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

/*        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle("")
                .setMessage("亲爱的" + user_name + "车主，欢迎您回来！")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dialog!=null){
                            dialog.dismiss();
                        }
                    }
                })
                .create();
        dialog.show();*/

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP
        };
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, conversationTypes);
            }
        }, 500);
        fm = getSupportFragmentManager();
        menu = new ArrayList<CarOwnerMenu>();
        menu.add(new CarOwnerMenu("首页", R.mipmap.main02, R.mipmap.main01, false));
        menu.add(new CarOwnerMenu("消息", R.mipmap.main04, R.mipmap.main03, false));
        menu.add(new CarOwnerMenu("我的提醒", R.mipmap.main06, R.mipmap.main05, false));
        menu.add(new CarOwnerMenu("个人中心", R.mipmap.main08, R.mipmap.main07, false));
        content = (LinearLayout) findViewById(R.id.content);
        carowners_gv = (GridView) findViewById(R.id.carowners_gv);
        carOwners_adapter = new CarOwners_Adapter(this, R.layout.carowner_menu, menu);
        carowners_gv.setAdapter(carOwners_adapter);
        carowners_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (menu.get(position).getIsOnclick()) {
                    return;
                }
                FragmentTransaction ft = fm.beginTransaction();
                if (position != 1) {
                    for (CarOwnerMenu m : menu) {
                        m.setIsOnclick(false);
                    }
                    menu.get(position).setIsOnclick(true);
                }
                carOwners_adapter.notifyDataSetChanged();
                switch (position) {
                    case 0:
                        if (mainCarOwnerFragment == null || !mainCarOwnerFragment.isAdded()) {
                            mainCarOwnerFragment = new MainCarOwnerFragment();
                            ft.add(R.id.content, mainCarOwnerFragment);
                        }
                        switchFragment(ft, mainCarOwnerFragment);
                        break;
                    case 1:
                        startActivity(new Intent(mContext, MainActivity.class));
                        break;
                    case 2:
                        if (coRemindFragment == null || !coRemindFragment.isAdded()) {
                            coRemindFragment = new CoRemindFragment();
                            ft.add(R.id.content, coRemindFragment);
                        }
                        switchFragment(ft, coRemindFragment);
                        break;
                    case 3:
                        if (carOwnerCenterFragment == null || !carOwnerCenterFragment.isAdded()) {
                            carOwnerCenterFragment = new CarOwnerCenterFragment();
                            ft.add(R.id.content, carOwnerCenterFragment);
                        }
                        switchFragment(ft, carOwnerCenterFragment);
                        break;
                }
                ft.commitAllowingStateLoss();


            }

        });
        if (!first) {
            FragmentTransaction ft = fm.beginTransaction();
            if (mainCarOwnerFragment == null || !mainCarOwnerFragment.isAdded()) {
                mainCarOwnerFragment = new MainCarOwnerFragment();
                ft.add(R.id.content, mainCarOwnerFragment);

            }
            switchFragment(ft, mainCarOwnerFragment);
            first = true;
            menu.get(0).setIsOnclick(true);
            carOwners_adapter.notifyDataSetChanged();
            ft.commitAllowingStateLoss();
        }

    }

    public  RongIM.OnReceiveUnreadCountChangedListener   mCountListener = new RongIM.OnReceiveUnreadCountChangedListener()   {
        @Override
        public void onMessageIncreased(int count)  {
            if(isLogout){
                return;
            }
            if (count == 0) {
                menu.get(1).setMessageNum("");
            } else if (count > 0 && count < 100) {
                menu.get(1).setMessageNum(String.valueOf(count));
            } else {
                menu.get(1).setMessageNum("100+");
            }
            carOwners_adapter.notifyDataSetChanged();

        }
    };

    private void switchFragment(FragmentTransaction ft, Fragment f) {
        if (mainCarOwnerFragment != null) {
            ft.hide(mainCarOwnerFragment);
        }
        if (carOwnerCenterFragment != null) {
            ft.hide(carOwnerCenterFragment);
        }
        if (coRemindFragment != null) {
            ft.hide(coRemindFragment);
        }
        ft.show(f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isLogout=false;

    }
}
