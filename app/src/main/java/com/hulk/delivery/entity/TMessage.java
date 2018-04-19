package com.hulk.delivery.entity;

import java.util.Date;

public class TMessage {
    /**
     * 主键ID
     */
    private Integer messageId;

    /**
     * 消息类型 0-站内信；1-公告
     */
    private Integer messageType;

    /**
     * 发送者ID
     */
    private String sendUserId;

    /**
     * 接收者ID
     */
    private String receiveUserId;

    /**
     * 消息状态。0，未读；1，已读；
     */
    private Boolean messageStatus;

    /**
     * 发送时间
     */
    private String createTime;

    /**
     * 数据有效性 0，无效；1，有效；
     */
    private Boolean dataFlag;

    /**
     * 消息标题
     */
    private String messageTitle;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 获取主键ID
     *
     * @return message_id - 主键ID
     */
    public Integer getMessageId() {
        return messageId;
    }

    /**
     * 设置主键ID
     *
     * @param messageId 主键ID
     */
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取消息类型 0-站内信；1-公告
     *
     * @return message_type - 消息类型 0-站内信；1-公告
     */
    public Integer getMessageType() {
        return messageType;
    }

    /**
     * 设置消息类型 0-站内信；1-公告
     *
     * @param messageType 消息类型 0-站内信；1-公告
     */
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    /**
     * 获取发送者ID
     *
     * @return send_user_id - 发送者ID
     */
    public String getSendUserId() {
        return sendUserId;
    }

    /**
     * 设置发送者ID
     *
     * @param sendUserId 发送者ID
     */
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     * 获取接收者ID
     *
     * @return receive_user_id - 接收者ID
     */
    public String getReceiveUserId() {
        return receiveUserId;
    }

    /**
     * 设置接收者ID
     *
     * @param receiveUserId 接收者ID
     */
    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    /**
     * 获取消息状态。0，未读；1，已读；
     *
     * @return message_status - 消息状态。0，未读；1，已读；
     */
    public Boolean getMessageStatus() {
        return messageStatus;
    }

    /**
     * 设置消息状态。0，未读；1，已读；
     *
     * @param messageStatus 消息状态。0，未读；1，已读；
     */
    public void setMessageStatus(Boolean messageStatus) {
        this.messageStatus = messageStatus;
    }

    /**
     * 获取发送时间
     *
     * @return create_time - 发送时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置发送时间
     *
     * @param createTime 发送时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取数据有效性 0，无效；1，有效；
     *
     * @return data_flag - 数据有效性 0，无效；1，有效；
     */
    public Boolean getDataFlag() {
        return dataFlag;
    }

    /**
     * 设置数据有效性 0，无效；1，有效；
     *
     * @param dataFlag 数据有效性 0，无效；1，有效；
     */
    public void setDataFlag(Boolean dataFlag) {
        this.dataFlag = dataFlag;
    }

    /**
     * 获取消息标题
     *
     * @return message_title - 消息标题
     */
    public String getMessageTitle() {
        return messageTitle;
    }

    /**
     * 设置消息标题
     *
     * @param messageTitle 消息标题
     */
    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    /**
     * 获取消息内容
     *
     * @return message_content - 消息内容
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * 设置消息内容
     *
     * @param messageContent 消息内容
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}