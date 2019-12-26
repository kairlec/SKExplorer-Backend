package com.kairlec.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;

    @JSONField(serialize = false)
    private String password;

    @JSONField(name = "IP")
    private String IP;

    private LocalDateTime lastLoginTime;

    private String email;

    private String lastSessionId;

    public void updatePassword(String password) {
        this.password = DigestUtils.md5DigestAsHex(password.getBytes());
    }

    public boolean equalsPassword(String password){
        return this.password.equals(DigestUtils.md5DigestAsHex(password.getBytes()));
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
