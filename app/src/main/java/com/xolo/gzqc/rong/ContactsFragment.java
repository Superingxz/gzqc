package com.xolo.gzqc.rong;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.App;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.CarOwnersActivity;
import com.xolo.gzqc.ui.activity.RoleActivity;
import com.xolo.gzqc.ui.activity.pickcar.FuntionActivity;
import com.xolo.gzqc.ui.activity.procurement.ProcurementMainActivity;
import com.xolo.gzqc.ui.activity.team.TeamMainActivity;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.mention.SideBar;
import io.rong.imlib.model.UserInfo;

/**
 * 联系人
 */
public class ContactsFragment extends BaseFragment {

    @BindView(R.id.search)
    EditText mSearchEditText;
    @BindView(R.id.show_no_friend)
    TextView mNoFriends;
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.group_dialog)
    TextView mDialogTextView;
    @BindView(R.id.sidrbar)
    SideBar mSidBar;
    private View mHeadView;

    private PinyinComparator mPinyinComparator;

    private Control   control;

    private List<FriendInfo> mSourceFriendList;
    private List<FriendInfo> mFriendInfoList;
    private List<FriendInfo> mFilteredFriendList;
    /**
     * 好友列表的 mFriendListAdapter
     */
    private FriendListAdapter mFriendListAdapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */

    private String mId;
    private String mCacheName;
    private TextView tvUnread;



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_address, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();

        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        mHeadView = mLayoutInflater.inflate(R.layout.item_contact_list_header,
                null);
        RelativeLayout newFriendsLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_newfriends);
        tvUnread = ((TextView) mHeadView.findViewById(R.id.tv_unread));
        newFriendsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (control!=null){
                    EventBus.getDefault().removeStickyEvent(control);
                    tvUnread.setVisibility(View.GONE);
                }
                startActivityForResult(new Intent(getContext(),NewFriendActivity.class),11);
            }
        });
//        RelativeLayout groupLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_chatroom);
//        groupLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RongIM.getInstance().startGroupChat(getContext(),"123",null);
//            }
//        });
        mListView.addHeaderView(mHeadView);


        if (mSourceFriendList != null && mSourceFriendList.size() > 0) {
            mFriendInfoList = labelSourceFriendList(mSourceFriendList); //过滤数据为有字母的字段  现在有字母 别的数据没有
        } else {
            mNoFriends.setVisibility(View.VISIBLE);
        }

        //还原除了带字母字段的其他数据
        for (int i = 0; i < mSourceFriendList.size(); i++) {
            mFriendInfoList.get(i).setName(mSourceFriendList.get(i).getName());
            mFriendInfoList.get(i).setUserId(mSourceFriendList.get(i).getUserId());
            mFriendInfoList.get(i).setPortraitUri(mSourceFriendList.get(i).getPortraitUri());
            mFriendInfoList.get(i).setDisplayName(mSourceFriendList.get(i).getDisplayName());
        }

        // 根据a-z进行排序源数据
        Collections.sort(mFriendInfoList, mPinyinComparator);

        mFriendListAdapter = new FriendListAdapter(getActivity(), mFriendInfoList);
        mListView.setAdapter(mFriendListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView.getHeaderViewsCount() > 0) {
                    startFriendDetailsPage(mFriendInfoList.get(position-1));
                } else {
                    startFriendDetailsPage(mFilteredFriendList.get(position));
                }
            }
        });

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String s) {
                for (FriendInfo info:mFriendInfoList) {
                    if (info.getUserId().equals(s)){
                        return   new UserInfo(info.getUserId(),info.getName(), Uri.parse(info.getPortraitUri()));
                    }
                }
                return  null;
            }

        },true);

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    if (mListView.getHeaderViewsCount() > 0) {
                        mListView.removeHeaderView(mHeadView);
                    }
                } else {
                    if (mListView.getHeaderViewsCount() == 0) {
                        mListView.addHeaderView(mHeadView);
                    }
                }
            }
        });

        return view;
    }


    private void startFriendDetailsPage(FriendInfo friendInfo) {
        RongIM.getInstance().startPrivateChat(getContext(), friendInfo.getUserId(), friendInfo.getDisplayName());
    }


    private void initView() {
        mSidBar.setTextView(mDialogTextView);
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mFriendListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });
    }


    /**
     * 为ListView填充数据
     */
    private List<FriendInfo> labelSourceFriendList(List<FriendInfo> list) {
        List<FriendInfo> mFriendInfoList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            FriendInfo friendInfoModel = new FriendInfo();
            friendInfoModel.setName(list.get(i).getName());
            //汉字转换成拼音
            String pinyin = mCharacterParser.getSpelling(list.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                friendInfoModel.setLetters(sortString.toUpperCase());
            } else {
                friendInfoModel.setLetters("#");
            }

            mFriendInfoList.add(friendInfoModel);
        }
        return mFriendInfoList;

    }

    private void initData() {
        mSourceFriendList = new ArrayList<>();
        mFriendInfoList = new ArrayList<>();
        mFilteredFriendList = new ArrayList<>();

        List<FriendInfo> friendInfoList = App.getFriendInfoList();
        mSourceFriendList.addAll(friendInfoList);

        //实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        mPinyinComparator = PinyinComparator.getInstance();
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr 需要过滤的 String
     */
    private void filterData(String filterStr) {
        List<FriendInfo> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mFriendInfoList;
        } else {
            filterDateList.clear();
            for (FriendInfo friendInfoModel : mFriendInfoList) {
                String name = friendInfoModel.getName();
                String displayName = friendInfoModel.getDisplayName();
                if (!TextUtils.isEmpty(displayName)) {
                    if (name.contains(filterStr) || mCharacterParser.getSpelling(name).startsWith(filterStr) || displayName.contains(filterStr) || mCharacterParser.getSpelling(displayName).startsWith(filterStr)) {
                        filterDateList.add(friendInfoModel);
                    }
                } else {
                    if (name.contains(filterStr) || mCharacterParser.getSpelling(name).startsWith(filterStr)) {
                        filterDateList.add(friendInfoModel);
                    }
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mPinyinComparator);
        mFilteredFriendList = filterDateList;
        mFriendListAdapter.updateListView(filterDateList);
    }


    /**
     * 1-6 获取本维修厂的用户列表(含车主)接口 getlistdeptuser(userid,dept_id)
     * 当前用户ID：userid,维修厂ID：dept_id
     */
    private void getlistdeptuser( final int i) {
        RequestParams params = new RequestParams(Constant.BASE_URL);
        params.addBodyParameter(Key.ACTION,"getlistdeptuser");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter("type",String.valueOf(i));

        HttpUtil.getInstance().postLoading(getActivity(),params, ORMBean.FriendInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.FriendInfoBean>() {
            @Override
            public void onSuccess(ORMBean.FriendInfoBean result) {
                    App.setFriendInfoList(result.getData());
                    updateUI(result.getData());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
             if (requestCode == 11){
              getlistdeptuser(App.getType_role_chat());
             }
    }

    @Override
    protected void init() {

    }

    private void updateUI(List<FriendInfo>  list) {
        if (list != null) {
            if (mSourceFriendList != null) {
                mSourceFriendList.clear();
            }
            if (mFriendInfoList != null) {
                mFriendInfoList.clear();
            }
            for (FriendInfo friend : list) {
                mSourceFriendList.add(new FriendInfo(friend.getUserId(), friend.getName(), friend.getPortraitUri(), friend.getDisplayName()));
            }

        }
        if (mSourceFriendList != null) {
            mFriendInfoList = labelSourceFriendList(mSourceFriendList); //过滤数据为有字母的字段  现在有字母 别的数据没有
        } else {
            mNoFriends.setVisibility(View.VISIBLE);
        }

        //还原除了带字母字段的其他数据
        for (int i = 0; i < mSourceFriendList.size(); i++) {
            mFriendInfoList.get(i).setName(mSourceFriendList.get(i).getName());
            mFriendInfoList.get(i).setUserId(mSourceFriendList.get(i).getUserId());
            mFriendInfoList.get(i).setPortraitUri(mSourceFriendList.get(i).getPortraitUri());
            mFriendInfoList.get(i).setDisplayName(mSourceFriendList.get(i).getDisplayName());
        }

        // 根据a-z进行排序源数据
        Collections.sort(mFriendInfoList, mPinyinComparator);
        mFriendListAdapter.updateListView(mFriendInfoList);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public  void onEvent(Control c){
        LogUtil.i(c.getDisplay_data());
        control = c;
               tvUnread.setVisibility(View.VISIBLE);
    }

}
