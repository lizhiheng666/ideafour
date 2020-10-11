package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName("tb_user")
@Data
@Accessors(chain = true)
public class User extends BasePojo implements Serializable {
    @TableId(type = IdType.AUTO)
    private long id;
    private  String username;
    private String password;
    private String phone;
    private String email;
}
