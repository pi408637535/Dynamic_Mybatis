package com.study.general.mapper.helper;

import com.study.general.mapper.mapper.Mapper;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by piguanghua on 2017/4/4.
 */
public class MapperHelper {

    /**
     * 缓存skip结果
     * method,result
     */
    private final Map<String, Boolean> msIdSkip = new HashMap<String, Boolean>();

    //基础可配置项
    private class Config {
        private String UUID = "";
        private String IDENTITY = "";  //主键自增回写方法,默认值CALL IDENTITY(),适应于大多数数据库
        private boolean BEFORE = false; //主键自增回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
    }

    private Config config = new Config();

    /**
     * 定义要拦截的方法名
     */
    public final String[] METHODS = {
            "select",
            "insert",
            "delete",
            "updateByPrimaryKey",};

    public static final String DYNAMIC_SQL = "dynamicSQL";

    public String dynamicSQL(Object record) {
        return DYNAMIC_SQL;
    }

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    /**
     * 反射对象，利用Mybatis提供的对象
     *
     * @param object 反射对象
     * @return
     */
    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object,
                DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
    }


    public void setIDENTITY(String IDENTITY) {
        config.IDENTITY = IDENTITY;
    }

    public void setBEFORE(String BEFORE) {
        config.BEFORE = "BEFORE".equalsIgnoreCase(BEFORE);
    }

    /**
     * 接口类是否为Mapper子接口
     *
     * @param mapperClass
     * @return
     */
    private boolean ifExtendsMapper(Class mapperClass) {
        return Mapper.class.isAssignableFrom(mapperClass);
    }

    /**
     * 根据msId获取接口类
     *
     * @param msId
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> getMapperClass(String msId) throws ClassNotFoundException {
        String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));
        return Class.forName(mapperClassStr);
    }

    /**
     * 判断当前的接口方法是否需要进行拦截
     * 流程：通过传入的全限定名：①判断当前的mapper是否继承Mapper类，②如果继承Mapper则判断当前的方法是否
     * 需要拦截
     * @param msId  namespace + id = com.study.general.mapper.mapper.UserMapper.select
     * @return
     */
    public boolean ifMapperMethodNeedIntercepted(String msId) {
        if (msIdSkip.get(msId) != null) {
            return msIdSkip.get(msId);
        }
        try {
            String methodName = msId.substring(msId.lastIndexOf(".") + 1);
            boolean rightMethod = false;
            for (String method : METHODS) {
                if (method.equals(methodName)) {
                    rightMethod = true;
                    break;
                }
            }
            if (!rightMethod) {
                return false;
            }
            //通过传入的全限定名:  msId = com.study.general.mapper.mapper.UserMapper.select
            Boolean skip = ifExtendsMapper(getMapperClass(msId));
            msIdSkip.put(msId, skip);
            return skip;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
