package com.kairlec.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kairlec.utils.Network;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @JSONField(name = "username")
    private String username;

    @JSONField(serialize = false)
    private String password;

    @JSONField(name = "IP")
    private String IP;

    @JSONField(name = "lastLoginTime")
    private Date lastLoginTime;

    @JSONField(name = "TokenId")
    private String lastSessionId;

    public static User getUser(HttpServletRequest request) {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setIP(Network.getIpAddress(request));
        user.setLastLoginTime(new Date());
        user.setLastSessionId(request.getSession(true).getId());
        return user;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
