package com.xolo.gzqc.bean;

import com.xolo.gzqc.bean.child.Address;
import com.xolo.gzqc.bean.child.Area;
import com.xolo.gzqc.bean.child.Brand;
import com.xolo.gzqc.bean.child.Car;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.CarOwner;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.bean.child.CarOwner_SendCar_info;
import com.xolo.gzqc.bean.child.CarOwner_service;
import com.xolo.gzqc.bean.child.CarState;
import com.xolo.gzqc.bean.child.CarStateRead;
import com.xolo.gzqc.bean.child.CarnoInfo;
import com.xolo.gzqc.bean.child.CarnoInfoConfirm;
import com.xolo.gzqc.bean.child.ChildHistroyPrice;
import com.xolo.gzqc.bean.child.CoAccessories;
import com.xolo.gzqc.bean.child.CoCarOrder;
import com.xolo.gzqc.bean.child.CoEngineOilRemind;
import com.xolo.gzqc.bean.child.CoHistoryRecord;
import com.xolo.gzqc.bean.child.CoHistroyMaintainState;
import com.xolo.gzqc.bean.child.CoHistroyPrice;
import com.xolo.gzqc.bean.child.CoInfo;
import com.xolo.gzqc.bean.child.CoMainImg;
import com.xolo.gzqc.bean.child.CoMaintainDetails;
import com.xolo.gzqc.bean.child.CoMaintainDetile;
import com.xolo.gzqc.bean.child.CoMaintainInfo;
import com.xolo.gzqc.bean.child.CoMaintainPrograss;
import com.xolo.gzqc.bean.child.CoProject;
import com.xolo.gzqc.bean.child.CoRemind;
import com.xolo.gzqc.bean.child.CoRescue;
import com.xolo.gzqc.bean.child.CoSenderCarInfo;
import com.xolo.gzqc.bean.child.CoServiceChild;
import com.xolo.gzqc.bean.child.CoServiceContent;
import com.xolo.gzqc.bean.child.CoSheduliTotlePrice;
import com.xolo.gzqc.bean.child.CoTotalworkamt;
import com.xolo.gzqc.bean.child.Comfault;
import com.xolo.gzqc.bean.child.CompanypPhone;
import com.xolo.gzqc.bean.child.Complete;
import com.xolo.gzqc.bean.child.ComponentProgress;
import com.xolo.gzqc.bean.child.ConsumerMain;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.child.CreatOrder;
import com.xolo.gzqc.bean.child.DataPermission;
import com.xolo.gzqc.bean.child.Dept;
import com.xolo.gzqc.bean.child.Employee;
import com.xolo.gzqc.bean.child.GoodInfo;
import com.xolo.gzqc.bean.child.GoodMore;
import com.xolo.gzqc.bean.child.InquiryInfo;
import com.xolo.gzqc.bean.child.InquriyOrder;
import com.xolo.gzqc.bean.child.ItemVerhaul;
import com.xolo.gzqc.bean.child.Itemt;
import com.xolo.gzqc.bean.child.ConsumerLogistics;
import com.xolo.gzqc.bean.child.LoginType;
import com.xolo.gzqc.bean.child.Logistics;
import com.xolo.gzqc.bean.child.Maintain_Record;
import com.xolo.gzqc.bean.child.Inquiry;
import com.xolo.gzqc.bean.child.Maintainwarn;
import com.xolo.gzqc.bean.child.MaintenanceInfo;
import com.xolo.gzqc.bean.child.NewFriend;
import com.xolo.gzqc.bean.child.News;
import com.xolo.gzqc.bean.child.Notice;
import com.xolo.gzqc.bean.child.OfferHistroy;
import com.xolo.gzqc.bean.child.Order;
import com.xolo.gzqc.bean.child.Ordermbuyparts;
import com.xolo.gzqc.bean.child.OwnerInfo;
import com.xolo.gzqc.bean.child.PayOrder;
import com.xolo.gzqc.bean.child.PickUpOrder;
import com.xolo.gzqc.bean.child.PurchaseOrder;
import com.xolo.gzqc.bean.child.ReceiveInfo;
import com.xolo.gzqc.bean.child.Role;
import com.xolo.gzqc.bean.child.SearchResult;
import com.xolo.gzqc.bean.child.SaveGoodType;
import com.xolo.gzqc.bean.child.SaveGoodsResult;
import com.xolo.gzqc.bean.child.SelectShipper;
import com.xolo.gzqc.bean.child.SendMan;
import com.xolo.gzqc.bean.child.ShopFitment;
import com.xolo.gzqc.bean.child.ShoppingCart;

import com.xolo.gzqc.bean.child.SumByType;
import com.xolo.gzqc.bean.child.SumDetail;
import com.xolo.gzqc.bean.child.SupplierGoods;
import com.xolo.gzqc.bean.child.SupplierMainGoods;

import com.xolo.gzqc.bean.child.Store;

import com.xolo.gzqc.bean.child.SupplierSellGoods;
import com.xolo.gzqc.bean.child.Team;
import com.xolo.gzqc.bean.child.TypeCode;
import com.xolo.gzqc.bean.child.UpDate;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.bean.child.WordBeachDealed;
import com.xolo.gzqc.bean.child.Workamt;
import com.xolo.gzqc.bean.postJson.InquiryPart;
import com.xolo.gzqc.bean.postJson.Offer;
import com.xolo.gzqc.bean.postJson.Part;
import com.xolo.gzqc.bean.postJson.PartVerhaul;
import com.xolo.gzqc.bean.postJson.Photo;
import com.xolo.gzqc.bean.postJson.Progress;
import com.xolo.gzqc.bean.postJson.Repairlist;
import com.xolo.gzqc.rong.FriendInfo;

import java.util.List;


/**
 * Created by sa on 2016/4/28.
 */
public class ORMBean {

    /**
     * 登陆
     */
    public class LoginBean extends BaseBean<List<User>> {
    };

    /**
     * 登陆
     */
    public class LoginTypeBean extends BaseBean<List<LoginType>> {
    };


    /**
     * 角色
     */
    public class RoleBean extends BaseBean<List<Role>> {
    }

    ;


    /**
     * 地区
     */
    public class AreaBean extends BaseBean<List<Area>> {
    }

    ;

    /**
     * 车主
     */
    public class CarOwnerBean extends BaseBean<List<CarOwner>> {
    }

    ;

    /**
     * 车辆列表
     */
    public class CarBean extends BaseBean<List<Car>> {
    }

    ;

    /**
     * 根据类别获取对应的数据字典接口。1为燃料，2为操控，3为驱动，4为排量 ,5为油量
     */
    public class ControlBean extends BaseBean<List<Control>> {
    }

    ;

    /**
     * 车辆列表
     */
    public class CarInfoBean extends BaseBean<List<CarInfo>> {
    }

    ;


    /**
     * 通过车牌号获取该车的车辆信息接口
     */
    public class OwnerInfoBean extends BaseBean<List<OwnerInfo>> {
    }

    ;

    /**
     * 通过车牌号获取该车的车主信息接口
     */
    public class SendManBean extends BaseBean<List<SendMan>> {
    }

    ;

    /**
     * 车辆列表
     */
    public class ComfaultBean extends BaseBean<List<Comfault>> {
    }

    ;

    /**
     * 预约构件进度
     */
    public class OrdermbuypartsBean extends BaseBean<List<Ordermbuyparts>> {
    }

    ;

    /**
     * 车主个人信息
     */
    public class CoInfoBean extends BaseBean<List<CoInfo>> {
    }

    ;

    /**
     * 车辆信息
     */
    public class CoCarInfo extends BaseBean<List<CarOwner_CarInfo>> {
    }

    ;

    /**
     * 送修人信息
     */
    public class CoSenderCar extends BaseBean<List<CarOwner_SendCar_info>> {
    }

    ;

    /**
     * 搜素详情
     */
    public class MaintenanceInfoBean extends BaseBean<List<MaintenanceInfo>> {
    }

    ;

    /**
     * 询价配件
     */
    public class InquiryPartBean extends BaseBean<List<InquiryPart>> {
    }

    ;

    /**
     * 询价单
     */
    public class InquiryBean extends BaseBean<List<Inquiry>> {
    }

    ;

    /**
     * 购件开单
     */
    public class PurchaseOrderBean extends BaseBean<List<PurchaseOrder>> {
    }

    ;

    /**
     * 拆检配件
     */
    public class PartVerhaulBean extends BaseBean<List<PartVerhaul>> {
    }

    ;

    /**
     * 接车信息
     */
    public class ReceiveInfoBean extends BaseBean<List<ReceiveInfo>> {
    }

    ;

    /**
     * 预约维修进度
     */
    public class CoMaintainRecord extends BaseBean<List<Maintain_Record>> {
    }

    ;

    /**
     * 车辆服务大类
     */
    public class CoService extends BaseBean<List<CarOwner_service>> {
    }

    ;


    /**
     * 车辆服务小类
     */
    public class CoSvChild extends BaseBean<List<CoServiceChild>> {
    }

    ;

    /**
     * 车辆服务小类内容
     */
    public class CoServiceContentBean extends BaseBean<List<CoServiceContent>> {
    }

    ;

    /**
     * 班主
     */
    public class TeamBean extends BaseBean<List<Team>> {
    }

    ;

    /**
     * C车主消息
     */
    public class CoRemindBean extends BaseBean<List<CoRemind>> {
    }

    ;

    /**
     * 维修清单父级目录
     */
    public class CoProjectBean extends BaseBean<List<CoProject>> {
    }

    ;


    /**
     * g根据车牌获取相关信息
     */
    public class CarnoInfoBean extends BaseBean<List<CarnoInfo>> {
    }

    ;

    /**
     * 配件清单
     */
    public class CoAccessoriesBean extends BaseBean<List<CoAccessories>> {
    }

    ;

    /**
     * 工时总计
     */
    public class CoCoTotalworkamtBean extends BaseBean<List<CoTotalworkamt>> {
    }

    ;

    /**
     * 历史报价
     */
    public class CoHistroyBean extends BaseBean<List<CoHistroyPrice>> {
    }

    ;


    /**
     * 历史报价二级目录
     */
    public class CoHistroyChildBean extends BaseBean<List<ChildHistroyPrice>> {
    }

    ;


    /**
     * 保险公司电话
     */
    public class CoCompanyPhoneBean extends BaseBean<List<CompanypPhone>> {
    }

    ;


    /**
     * 提醒换机油
     */
    public class CoCoEngineOilRemindBean extends BaseBean<List<CoEngineOilRemind>> {
    }

    ;


    /**
     * 道路抢修救援
     */
    public class CoRescueBean extends BaseBean<List<CoRescue>> {
    }

    ;


    /**
     * 未付结账详细信息
     */
    public class PayOrderBean extends BaseBean<List<PayOrder>> {
    }

    ;

    /**
     * 报价
     */
    public class OfferBean extends BaseBean<List<Offer>> {
    }

    ;

    /**
     * 配件清单
     */
    public class RepairlistBean extends BaseBean<List<Repairlist>> {
    }

    ;

    /**
     * 进度查询
     */
    public class ProgressBean extends BaseBean<List<Progress>> {
    }

    /**
     * 配件
     */
    public class PartBean extends BaseBean<List<Part>> {
    }

    ;

    /**
     * 历史报价
     */
    public class OfferHistroyBean extends BaseBean<List<OfferHistroy>> {
    }

    /**
     * 历史报价
     */
    public class NewsBean extends BaseBean<List<News>> {
    }

    ;
    ;
    ;

    /**
     * 历史维系记录
     */
    public class CoHistoryRecordBean extends BaseBean<List<CoHistoryRecord>> {
    }

    ;

    /**
     * 购件进度配件状态
     */
    public class CoHistroyMaintainStateBean extends BaseBean<List<CoHistroyMaintainState>> {
    }

    ;

    /**
     * 购件进度信息
     */
    public class CoComponentProgressBean extends BaseBean<List<ComponentProgress>> {
    }

    ;

    /**
     * 购件进度总金额
     */
    public class CoSheduliTotlePriceBean extends BaseBean<List<CoSheduliTotlePrice>> {
    }

    ;

    /**
     * 购件进度总金额
     */
    public class CoMaintainPrograssBean extends BaseBean<List<CoMaintainPrograss>> {
    }

    ;

    /**
     * 购件进度详情
     */
    public class CoMaintainDetileBean extends BaseBean<List<CoMaintainDetile>> {
    }

    /**
     * 完工
     */
    public class CompleteBean extends BaseBean<List<Complete>> {
    }

    /**
     * 接车单
     */
    public class PickUpOrderBean extends BaseBean<List<PickUpOrder>> {
    }

    /**
     * 接车单详情
     */
    public class CoCarOrderBean extends BaseBean<List<CoCarOrder>> {
    }

    /**
     * 接车单
     */
    public class CarStateBean extends BaseBean<List<CarState>> {
    }

    /**
     * 接车单
     */
    public class CarStateReadBean extends BaseBean<List<CarStateRead>> {
    }

    /**
     * 保养提醒
     */
    public class MaintainwarnBean extends BaseBean<List<Maintainwarn>> {
    }

    /**
     * 我的维修详情
     */
    public class CoMaintainDetailsBean extends BaseBean<List<CoMaintainDetails>> {
    }

    /**
     * 车主首页
     */
    public class CoMainImgBean extends BaseBean<List<CoMainImg>> {
    }

    /**
     * 维修厂公告信息
     */
    public class CoMaintainInfoBean extends BaseBean<List<CoMaintainInfo>> {
    }

    /**
     * 价格确认获取车牌
     */
    public class CarnoInfoConfirmBean extends BaseBean<List<CarnoInfoConfirm>> {
    }

    /**
     * 查询维修厂
     */
    public class DeptBean extends BaseBean<List<Dept>> {
    }

    /**
     * 查询送修人车辆
     */
    public class CoSenderCarInfoBean extends BaseBean<List<CoSenderCarInfo>> {
    }

    /**
     * 查询送修人车辆
     */
    public class PhotoBean extends BaseBean<List<Photo>> {
    }

    /**
     * 自动更新
     */
    public class UpDateBean extends BaseBean<List<UpDate>> {
    }

    /**
     */
    public class ItemtBean extends BaseBean<List<Itemt>> {
    }

    /**
     */
    public class WorkamtBean extends BaseBean<List<Workamt>> {
    }

    /**
     */
    public class ItemVerhaulBean extends BaseBean<List<ItemVerhaul>> {
    }


    /**
     * 消费者首页
     */
    public class ConsumerMainBean extends BaseBean<List<ConsumerMain>> {
    }

    /**
     */
    public class InquiryInfoBean extends BaseBean<List<InquiryInfo>> {
    }


    /**
     * 融云聊天联系人
     */
    public class FriendInfoBean extends BaseBean<List<FriendInfo>> {
    }


    /**
     * 工作邰就近三天
     */
    public class WordBeachDealedBean extends BaseBean<List<WordBeachDealed>> {
    }


    /**
     * 询价单详情
     */
    public class InquriyOrderBean extends BaseBean<List<InquriyOrder>> {
    }

    /**
     * 供应商店铺首页商品顺序列表
     */
    public class ShopFimentBean extends BaseBean<List<ShopFitment>> {
    }

    /**
     */
    public class BrandBean extends BaseBean<List<Brand>> {
    }

    /**
     * 店铺公告
     */
    public class NoticeBean extends BaseBean<List<Notice>> {
    }

    /**
     * 商品类别
     */
    public class SaveGoodTypeBean extends BaseBean<List<SaveGoodType>> {
    }

    /**
     * 上传商品结果
     */
    public class SaveGoodsResultBean extends BaseBean<List<SaveGoodsResult>> {
    }

    /**
     */
    public class TypeCodeBean extends BaseBean<List<TypeCode>> {
    }

    /**
     * 消费者搜索
     */
    public class SearchResultBean extends BaseBean<List<SearchResult>> {
    }

    /**
     * 消费者更多
     */
    public class GoodMoreBean extends BaseBean<List<GoodMore>> {
    }

    /**
     * 消费者购物车
     */
    public class ShoppingCartBean extends BaseBean<List<ShoppingCart>> {
    }

    /**
     * 产品详情
     */
    public class GoodInfoBean extends BaseBean<List<GoodInfo>> {
    }

    /**
     * 收货地址
     */
    public class AddressBean extends BaseBean<List<Address>> {
    }


    /**
     * 供应商订单
     */
    public class SupplierOrderListBean extends BaseBean<List<SupplierGoods>> {
    }

    /**
     * 收货地址
     */
    public class OrderBean extends BaseBean<List<Order>> {
    }

    /**
     * 供应商首页
     */
    public class SupplierMainGoodsBean extends BaseBean<List<SupplierMainGoods>> {
    }

    /**
     * 快递接口
     */
    public class SelectShipperBean extends BaseBean<List<SelectShipper>> {
    }

    /**
     * 物流接口
     */
    public class LogisticsBean extends BaseBean<List<Logistics>> {
    }
    /**
     * 店铺首页
     */
    public class StoreBean extends BaseBean<List<Store>> {
    }
    /**
     * 物流
     */
    public class ConsumerLogisticsBean extends BaseBean<List<ConsumerLogistics>> {
    }

    /**
     * 消费者生成订单
     */
    public class CreatOrderBean extends BaseBean<List<CreatOrder>> {
    }

    /**
     * 新的好友
     */
    public class NewFriendBean extends BaseBean<List<NewFriend>> {
    }

    /**
     * 消费者生成订单
     */
    public class SupplierSellGoodsBean extends BaseBean<List<SupplierSellGoods>> {
    }

    /**
     * 老板数据
     */
    public class SumByTypeBean extends BaseBean<List<SumByType>> {
    }

    /**
     * 老板页面点进去的明细
     */
    public class SumDetailBean extends BaseBean<List<SumDetail>> {
    }

    /**
     * 管理员首页-获取本部门的员工列表接口
     */
    public class EmployeeBean extends BaseBean<List<Employee>> {
    }

    /**
     * 获取该员工已存在的数据权限列表接口
     */
    public class DataPermissionBean extends BaseBean<List<DataPermission>> {
    }

}