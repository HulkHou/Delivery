package com.hulk.delivery.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TFood {

    private Integer foodId;

    /**
     * 店家id，同t_shop的id
     */
    private Integer shopId;

    /**
     * 菜品名称
     */
    private String foodName;

    /**
     * 菜品单价
     */
    private BigDecimal foodPrice;

    /**
     * 菜品图片
     */
    private String foodImg;

    /**
     * 菜品简介
     */
    private String foodDesc;

    /**
     * 商铺名称
     */
    private String shopName;

    /**
     * 起送标准
     */
    private String shippingStartFee;

    /**
     * 配送费用
     */
    private String shippingFee;


    /**
     * 满免运费额度
     */
    private String shippingFreeFee;


    /**
     * 门店简介
     */
    private String shopDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * @return food_id
     */
    public Integer getFoodId() {
        return foodId;
    }

    /**
     * @param foodId
     */
    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    /**
     * 获取店家id，同t_shop的id
     *
     * @return shop_id - 店家id，同t_shop的id
     */
    public Integer getShopId() {
        return shopId;
    }

    /**
     * 设置店家id，同t_shop的id
     *
     * @param shopId 店家id，同t_shop的id
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * 获取菜品名称
     *
     * @return food_name - 菜品名称
     */
    public String getFoodName() {
        return foodName;
    }

    /**
     * 设置菜品名称
     *
     * @param foodName 菜品名称
     */
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    /**
     * 获取菜品单价
     *
     * @return food_price - 菜品单价
     */
    public BigDecimal getFoodPrice() {
        return foodPrice;
    }

    /**
     * 设置菜品单价
     *
     * @param foodPrice 菜品单价
     */
    public void setFoodPrice(BigDecimal foodPrice) {
        this.foodPrice = foodPrice;
    }

    /**
     * 获取菜品图片
     *
     * @return food_img - 菜品图片
     */
    public String getFoodImg() {
        return foodImg;
    }

    /**
     * 设置菜品图片
     *
     * @param foodImg 菜品图片
     */
    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

    /**
     * 获取菜品简介
     *
     * @return food_desc - 菜品简介
     */
    public String getFoodDesc() {
        return foodDesc;
    }

    /**
     * 设置菜品简介
     *
     * @param foodDesc 菜品简介
     */
    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShippingStartFee() {
        return shippingStartFee;
    }

    public void setShippingStartFee(String shippingStartFee) {
        this.shippingStartFee = shippingStartFee;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getShippingFreeFee() {
        return shippingFreeFee;
    }

    public void setShippingFreeFee(String shippingFreeFee) {
        this.shippingFreeFee = shippingFreeFee;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取更新人
     *
     * @return update_by - 更新人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新人
     *
     * @param updateBy 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}