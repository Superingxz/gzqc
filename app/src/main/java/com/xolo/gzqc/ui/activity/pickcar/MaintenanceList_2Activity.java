package com.xolo.gzqc.ui.activity.pickcar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.ReceiveInfo;
import com.xolo.gzqc.bean.postJson.Photo;
import com.xolo.gzqc.bean.postJson.Progress;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.PhotoActivity;
import com.xolo.gzqc.ui.dialog.BottomMenuDialog;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollGridView;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.Base64Utils;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.PhotoUtils;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import io.rong.push.notification.PushNotificationMessage;

public class MaintenanceList_2Activity extends BaseActivity {

    @BindView(R.id.carno)
    TextView carno;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.gv)
    ScrollGridView gv;
    @BindView(R.id.titleview)
    TitleView titleview;


    private List<Bitmap> bitmapList = new ArrayList<>();
    private CommenAdapter<Bitmap> gvAdapter;

    //选择图片
    private BottomMenuDialog dialog_select_photo;
    LoadDialog loadDialog;

    private List<Photo> urlList = new ArrayList<>();

    ReceiveInfo receiveInfo;

    private CommenAdapter<Progress> adapter;

    private List<Progress> list = new ArrayList<>();

    private int type;

    private CommenAdapter<Photo> photoCommenAdapter;

    private PhotoUtils photoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_list_2);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();

        type = intent.getIntExtra("type", 0);
        if (type == 1) {
            btnScan.setVisibility(View.GONE);
            btnSend.setVisibility(View.GONE);
        }

        receiveInfo = (ReceiveInfo) intent.getSerializableExtra(Key.OBJECT);

        carno.setText(receiveInfo.getCarno());

        initLv();
        initGv();
        initDialog();

        getlistrepairprojectbyreceiveid();

        if (type == 1) {
            getlistfinishedphoto();
        }


        initPhotoUtils();
    }

    private void initDialog() {
        //选择图片
        dialog_select_photo = new BottomMenuDialog(mContext, "拍照", "从本地导入");
        dialog_select_photo.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUtils.takePicture(mContext);
                dialog_select_photo.dismiss();
            }
        });
        dialog_select_photo.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUtils.selectPicture(mContext);
                dialog_select_photo.dismiss();
            }
        });
    }

    private void initPhotoUtils() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
//                Bitmap bitmap = PhotoUtils.compressFile(uri.getPath(), 6);
                Bitmap bitmap = null;
                try {
                    bitmap = PhotoUtils.getBitmapFormUri(mContext,uri);
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mContext,"获取图片文件失败");
                }
                bitmapList.add(bitmap);
                gvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }

    private void initGv() {
        if (type == 0) {
            gvAdapter = new CommenAdapter<>(R.layout.item_photo, mContext, bitmapList, new CommenAdapter.AdapterCallback() {
                @Override
                public void setView(ViewHolder holder, int position) {
                    holder.setImage(R.id.item1, bitmapList.get(position));
                }
            });

            gv.setAdapter(gvAdapter);

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bitmap bitmap = bitmapList.get(position);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
                    byte[] bytes = bos.toByteArray();

                    Intent intent1 = new Intent(mContext, PhotoActivity.class);
                    intent1.putExtra(Key.OBJECT, bytes);

                    startActivity(intent1);
                }
            });
        }

        if (type == 1) {
            photoCommenAdapter = new CommenAdapter<>(R.layout.item_photo, mContext, urlList, new CommenAdapter.AdapterCallback() {
                @Override
                public void setView(ViewHolder holder, int position) {
                    ImageView view = holder.getView(R.id.item1);
                    x.image().bind(view, urlList.get(position).getPhoto_path());
                }
            });

            gv.setAdapter(photoCommenAdapter);

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String photo_path = urlList.get(position).getPhoto_path();

                    if (!TextUtils.isEmpty(photo_path)) {
                        Intent intent1 = new Intent(mContext, PhotoActivity.class);
                        intent1.putExtra("url", photo_path);
                        startActivity(intent1);
                    }
                }
            });

        }
    }


    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_maintenance_list, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, final int position) {
                final Progress maintenanceInfo1 = list.get(position);

                holder.setImage(R.id.iv,R.mipmap.item_iv);
                holder.setText(R.id.item1, "维修项目：" + maintenanceInfo1.getItemt_name());
                String set_finished = maintenanceInfo1.getSet_finished();

                TextView tv = holder.getView(R.id.item3);
                tv.setCompoundDrawables(null, null, null, null);

                holder.setText(R.id.item3, maintenanceInfo1.getTeam_name());

                CheckBox cb = holder.getView(R.id.item6);

                if (maintenanceInfo1.getSet_finished().equals("1")) {
                    cb.setChecked(true);
                }

                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            maintenanceInfo1.setSet_finished("1");
                        } else {
                            maintenanceInfo1.setSet_finished("0");
                        }
                    }
                });

                holder.getView(R.id.item2).setVisibility(View.GONE);
                holder.getView(R.id.item4).setVisibility(View.GONE);
                holder.getView(R.id.item5).setVisibility(View.GONE);
                holder.getView(R.id.item6).setVisibility(View.VISIBLE);

            }
        });
        lv.setAdapter(adapter);
    }

    /**
     * 2-57 根据接车单ID获取该车对应的维修项目清单接口 getlistrepairprojectbyreceiveid(userid,receiver_id)
     * 当前用户ID:userid,接车单ID:receiver_id
     */
    private void getlistrepairprojectbyreceiveid() {
        RequestParams params = creatParams("getlistrepairprojectbyreceiveid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.RECRIVE_ID, receiveInfo.getBf_receive_id());

        loadDialog.show(mContext);
        HttpUtil.getInstance().post( params, ORMBean.ProgressBean.class, new HttpUtil.HttpCallBack<ORMBean.ProgressBean>() {
            @Override
            public void onSuccess(ORMBean.ProgressBean result) {
                if (result.getRes().equals("1")){
                    List<Progress> data = result.getData();
                    list.addAll(data);
                    adapter.notifyDataSetChanged();
                }

                loadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContext);
            }
        });
    }


    /**
     * 2-58 发送给客户接口 sendscheduletoclient(userid,receiver_id,json_photo,json_projets,dept_id)
     * 当前用户ID:userid,接车单ID:receiver_id,照片列表:json_photo,维修项目清单:json_projets,维修厂ID:dept_id
     */
    private void sendscheduletoclient() {
        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i < bitmapList.size(); i++) {
            Bitmap bitmap = bitmapList.get(i);
            if (i == 0) {
                buffer.append(Base64Utils.Bitmap2StrByBase64(bitmap));
            } else {
                buffer.append("," + Base64Utils.Bitmap2StrByBase64(bitmap));
            }
        }

        RequestParams params = creatParams("sendscheduletoclient");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.RECRIVE_ID, receiveInfo.getBf_receive_id());
        params.addBodyParameter("json_photo", buffer.toString());
        params.addBodyParameter("json_projets", "{\"data\":" + list.toString() + "}");
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        loadDialog.show(mContext);

        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")){
                    rongImSend(receiveInfo.getMobile());
                }
                ToastUtil.showShort(mContext, result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                  loadDialog.dismiss(mContext);
            }
        });

    }


    @OnClick({R.id.btn_scan, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                dialog_select_photo.show();
                break;
            case R.id.btn_send:
                sendscheduletoclient();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoUtils.onActivityResult(mContext, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 进入图片详情
     */
    private void interPhoto(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();

        Intent intent1 = new Intent(mContext, PhotoActivity.class);
        intent1.putExtra(Key.OBJECT, bytes);

        startActivity(intent1);
    }


    /**
     * 2-57A 根据接车单ID获取该车对应的照片列表单接口 getlistschedulephoto(userid,receiver_id)
     * 当前用户ID:userid,接车单ID:receiver_id
     */
    private void getlistfinishedphoto() {
        RequestParams params = creatParams("getlistschedulephoto");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("receiver_id", receiveInfo.getBf_receive_id());

        loadDialog.show(mContext);
        HttpUtil.getInstance().post(params, ORMBean.PhotoBean.class, new HttpUtil.HttpCallBack<ORMBean.PhotoBean>() {
            @Override
            public void onSuccess(ORMBean.PhotoBean result) {
                if (result.getRes().equals("1")) {
                    List<Photo> data = result.getData();
                    urlList.addAll(data);
                    photoCommenAdapter.notifyDataSetChanged();
                }
                loadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContext);
            }
        });
    }


    private void rongImSend(String phone) {

        TextMessage myTextMessage = TextMessage.obtain("您好，您在【"+user.getDept_name()+"】维修厂维修的车辆已经完工，请尽快去确认哦");

//"7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
//Conversation.ConversationType.PRIVATE 为会话类型。
        Message myMessage = Message.obtain(phone, Conversation.ConversationType.PRIVATE, myTextMessage);
        /**
         * <p>发送消息。
         * 通过 {@link IRongCallback.ISendMessageCallback}
         * 中的方法回调发送的消息状态及消息体。</p>
         *
         * @param message     将要发送的消息体。
         * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
         *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
         *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
         * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link PushNotificationMessage#getPushData()} 方法获取。
         * @param callback    发送消息的回调，参考 {@link IRongCallback.ISendMessageCallback}。
         */
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
                loadDialog.dismiss(mContext);
                finish();
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回
                loadDialog.dismiss(mContext);
                ToastUtil.showShort(mContext, "提醒车主报价失败");
            }

        });
    }

}
