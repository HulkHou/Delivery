package com.hulk.delivery.event;


import com.hulk.delivery.entity.TAddress;
import com.hulk.delivery.entity.TMessage;
import com.hulk.delivery.entity.User;

import java.util.List;

/**
 * Created by hulk-out on 2017/11/6.
 */

public class Event {

    public static class UserInfoEvent {
        public User user;
    }

    public static class AddressInfoEvent {
        public TAddress tAddress;
    }

    public static class MessageInfoEvent {
        public TMessage tMessage;
    }

}
