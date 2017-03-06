package com.xolo.gzqc.configuration;

import android.content.Context;

import com.amap.api.maps2d.UiSettings;
import com.xolo.gzqc.bean.child.Area;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.utils.SPUtils;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SPManager {
    /**
     * user_name : 衢州市开化县宝宝树爱婴房连锁总部
     * sex : 0
     * dept_id : 100400020001000100060021
     * is_leader : 1
     * user_code : 1015180
     * role_count : 1
     * sign_pic :
     * phone_code : 13212345679
     * user_id : 005CA187-09AC-4C76-B7F8-1DE39BBFC21E
     * isfemp : 1
     * dept_name :
     */
    private static User  mUser;

    /**
     * 是否记住密码
     */
    public final static  String  IS_REMENBER = "isRemenber";

    /**
     * 登录用户
     */
    public final static  String  USER = "user";

    /**
     * 登录密码
     */
    public final static  String  PASSWORD = "password";
    /**
     * 登录密码
     */
    public final static  String  PASSWORD_2 = "password_2";

    /**
     * 是否自动登录
     */
    public final static  String  IS_AUTO_LOGIN = "isAutoLogin";

    public final static  String  TAKEN = "taken";

    public final static  String USER_NAME = "user_name";

    public final static  String SEX = "sex";

    public final  static String  DEPT_ID  =  "dept_id"   ;

    public final  static String     IS_LEADER = "is_leader" ;

    public final  static String  USER_CODE  =  "user_code"   ;

    public final  static String  ROLE_COUNT  =  "role_count"   ;

    public final  static String  SIGN_PIC  =   "sign_pic"  ;

    public final  static String  PHONE_CODE  =   "phone_code"  ;

    public final  static String  USER_ID  =   "user_id"  ;

    public final  static String  ISFEMP  =   "isfemp"  ;

    public final  static String  DEPT_NAME  =  "dept_name"   ;

    public final static  String  SUPPLYID= "supplyid";

    public final static  String  USER_VERSION= "user_version";

    /**
     * phone : 13812345678
     * remark : 测试的
     * street : 新港中路世港国际公寓A栋805
     * phone2 : 13552367891
     * factory_name : 接车部
     * password : C69D4E05A5E2019547976B57AF240F12
     * photo :
     * city : 广州市
     * drivingno : 452523236545565525
     * factory_id : 1004000200010003
     * area : 海珠区
     * bc_car_owner_id : 978691E5-E3AC-4A26-96F7-EFDF28910712
     * name : 张三
     * province : 广东省
     */
    public final  static String PHONE ="phone";
    public final  static String REMARK="remark";
    public final  static String STREET="street";
    public final  static String PHONE2="phone2";
    public final  static String FACTORY_NAME="factory_name";
    public final  static String PHOTO="photo";
    public final  static String CITY="city";
    public final  static String DRIVINGNO="drivingno";
    public final  static String FACTORY_ID="factory_id";
    public final  static String AREA="area";
    public final  static String BG_CAR_OWNER_ID="bc_car_owner_id";
    public final  static String NAME="name";
    public final  static String PROVINCE ="province";



    public static  void   saveUser(Context context , User user){
            SPUtils.put(context,USER_NAME,user.getUser_name());
            SPUtils.put(context,SEX,user.getSex());
            SPUtils.put(context,DEPT_ID,user.getDept_id());
            SPUtils.put(context,IS_LEADER,user.getIs_leader());
            SPUtils.put(context,USER_CODE,user.getUser_code());
            SPUtils.put(context,SIGN_PIC,user.getSign_pic());
            SPUtils.put(context,PHONE_CODE,user.getPhone_code());
            SPUtils.put(context,USER_ID,user.getUser_id());
            SPUtils.put(context,ISFEMP,user.getIsfemp());
            SPUtils.put(context,DEPT_NAME,user.getDept_name());
            SPUtils.put(context,PHONE,user.getPhone());
            SPUtils.put(context,REMARK,user.getRemark());
            SPUtils.put(context,STREET,user.getStreet());
            SPUtils.put(context,PHONE2,user.getPhone2());
            SPUtils.put(context,FACTORY_NAME,user.getFactory_name());
            SPUtils.put(context,PHOTO,user.getPhone());
            SPUtils.put(context,CITY,user.getCity());
            SPUtils.put(context,DRIVINGNO,user.getDrivingno());
            SPUtils.put(context,FACTORY_ID,user.getFactory_id());
            SPUtils.put(context, AREA,user.getArea());
            SPUtils.put(context,BG_CAR_OWNER_ID,user.getBc_car_owner_id());
            SPUtils.put(context,NAME,user.getName());
            SPUtils.put(context,PROVINCE,user.getProvince());
            SPUtils.put(context,PASSWORD,user.getPassword());
            SPUtils.put(context,ROLE_COUNT,user.getRole_count());
            SPUtils.put(context,SUPPLYID,user.getSupplyid());
            SPUtils.put(context,USER_VERSION,user.getUse_version());

        mUser = user;
    }

    public static  User   getUser(Context context){

        if (mUser == null){
            mUser = new User();
            mUser.setUser_name((String) SPUtils.get(context,USER_NAME,""));
            mUser.setSex((String) SPUtils.get(context,SEX,""));
            mUser.setDept_id((String) SPUtils.get(context,DEPT_ID,""));
            mUser.setIs_leader((String) SPUtils.get(context,IS_LEADER,""));
            mUser.setUser_code((String) SPUtils.get(context,USER_CODE,""));
            mUser.setSign_pic((String) SPUtils.get(context,SIGN_PIC,""));
            mUser.setPhone_code((String) SPUtils.get(context,PHONE_CODE,""));
            mUser.setUser_id((String) SPUtils.get(context,USER_ID,""));
            mUser.setIsfemp((String) SPUtils.get(context,ISFEMP,""));
            mUser.setDept_name((String) SPUtils.get(context,DEPT_NAME,""));
            mUser.setPhone((String) SPUtils.get(context,PHONE,""));
            mUser.setRemark((String) SPUtils.get(context,REMARK,""));
            mUser.setStreet((String) SPUtils.get(context,STREET,""));
            mUser.setPhone2((String) SPUtils.get(context,PHONE2,""));
            mUser.setFactory_name((String) SPUtils.get(context,FACTORY_NAME,""));
            mUser.setPassword((String) SPUtils.get(context,PASSWORD,""));
            mUser.setPhoto((String) SPUtils.get(context,PHOTO,""));
            mUser.setFactory_id((String) SPUtils.get(context,FACTORY_ID,""));
            mUser.setArea((String) SPUtils.get(context,AREA,""));
            mUser.setBc_car_owner_id((String) SPUtils.get(context,BG_CAR_OWNER_ID,""));
            mUser.setDrivingno((String) SPUtils.get(context,DRIVINGNO,""));
            mUser.setName((String) SPUtils.get(context,NAME,""));
            mUser.setProvince((String) SPUtils.get(context,PROVINCE,""));
            mUser.setRole_count((String) SPUtils.get(context,ROLE_COUNT,""));
            mUser.setSupplyid((String) SPUtils.get(context,SUPPLYID,""));
            mUser.setUse_version((String) SPUtils.get(context,USER_VERSION,""));
        }
        return mUser;

    }






}
