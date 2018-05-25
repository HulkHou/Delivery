package com.hulk.delivery.entity;

public class TCollect {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 类型 1-shop 2-food
     */
    private Integer collectType;

    /**
     * 收藏内容ID
     */
    private String collectItem;

    /**
     * 收藏商铺名称
     */
    private String shopName;

    /**
     * 收藏菜品名称
     */
    private String foodName;

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
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户编号
     *
     * @return user_id - 用户编号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户编号
     *
     * @param userId 用户编号
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取类型 1-shop 2-food
     *
     * @return collect_type - 类型 1-shop 2-food
     */
    public Integer getCollectType() {
        return collectType;
    }

    /**
     * 设置类型 1-shop 2-food
     *
     * @param collectType 类型 1-shop 2-food
     */
    public void setCollectType(Integer collectType) {
        this.collectType = collectType;
    }

    /**
     * 获取收藏内容ID
     *
     * @return collect_item - 收藏内容ID
     */
    public String getCollectItem() {
        return collectItem;
    }

    /**
     * 设置收藏内容ID
     *
     * @param collectItem 收藏内容ID
     */
    public void setCollectItem(String collectItem) {
        this.collectItem = collectItem;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
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
}