package cn.cnbuilder.smallapp.entity.base;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@Data

public class BaseEntity<T extends Model> extends Model<T> {

    public Long id;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
