package cn.cnbuilder.smallapp.entity;

import cn.cnbuilder.smallapp.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "User")
public class User extends BaseEntity {

    private  String name;
    private Integer age;

}
