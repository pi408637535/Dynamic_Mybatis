package com.study.mybatis.page.study.metaobject;

import com.study.mybatis.page.domain.User;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_FACTORY;
import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

/**
 * Created by piguanghua on 2017/2/23.
 */
public class StudyMeteObject {
    @Test
    public void MetaObjectStudyJavaBean() {
        User user = new User();
        user.setName("test");
        Object javaBean = new  User().getName();
        MetaObject javaBeanMeta = MetaObject.forObject(javaBean,
                DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());

        System.out.println(javaBeanMeta.getValue("name"));
        javaBeanMeta.setValue("name", "world");
        System.out.println(javaBeanMeta.getValue("name"));
    }
    @Test
    public void MetaObjectStudyCollection() {
        List<String> collection = new ArrayList<String>();
        MetaObject collectionMeta = MetaObject.forObject(collection,
                DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        collectionMeta.add("hello world");
        System.out.println(collection.size());
    }


    @Test
    public void MetaObjectStudyMap() {
        Map<String,String> map = new HashMap<String, String>();
        map.put("hello","hello world");

        MetaObject mapMeta = MetaObject.forObject(map,
                DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        System.out.println(mapMeta.getValue("hello"));
    }
}
