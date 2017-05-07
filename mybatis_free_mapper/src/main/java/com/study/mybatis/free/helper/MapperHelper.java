package com.study.mybatis.free.helper;

import com.study.mybatis.free.mapper.Mapper;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_FACTORY;
import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

/**
 * Created by piguanghua on 2017/5/7.
 */
public class MapperHelper  {
    public static final String DYNAMIC_SQL = "dynamicSQL";

    //基础可配置项
    private class Config {
        private String UUID = "";
        private String IDENTITY = "";
        private boolean BEFORE = false;
    }

    private Config config = new Config();

    public static Set<String> InterceptorMethod = new HashSet<>();
    static {
        InterceptorMethod.add("insert");
        InterceptorMethod.add("selectByPrimaryKey");
        InterceptorMethod.add("select");
        InterceptorMethod.add("delete");
        InterceptorMethod.add("deleteByPrimaryKey");
        InterceptorMethod.add("updateByPrimaryKey");
    }
    private final Map<String, Class<?>> entityType = new HashMap<String, Class<?>>();


    public String  getMethodByMapperStatementId(String msId){
        return msId.substring(msId.lastIndexOf(".") + 1, msId.length()) ;
    }



    public void addDynamicSql(MappedStatement ms){
        String id = ms.getId();
        String method = this.getMethodByMapperStatementId(id);
        switch (method){
            case "insert":
                DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), getInsertSqlNode(ms));
                setSqlSource(ms, dynamicSqlSource);
                break;
            case "selectByPrimaryKey":
                break;
        }
    }


    /**
     * 生成动态insert语句
     *
     * @param ms
     * @return
     */
    private MixedSqlNode getInsertSqlNode(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO " + EntityHelper.getTableName(entityClass)));

        List<EntityHelper.EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        Boolean hasIdentityKey = false;
        //用于进行 insert column,column,
        for (EntityHelper.EntityColumn column : columnList) {
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                //直接序列加进去
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
            } else if (column.isIdentity()) {
                if (hasIdentityKey) {
                    throw new RuntimeException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                //新增一个selectKey-MS
                newSelectKeyMappedStatement(ms, column);
                hasIdentityKey = true;
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_cache", column.getProperty()));
            } /*else if (column.isUuid()) {
                sqlNodes.add(new VarDeclSqlNode(column.getProperty() + "_bind", getUUID()));
                ifNodes.add(new StaticTextSqlNode(column.getColumn() + ","));
            }*/ else {
                SqlNode ifSqlNode = new StaticTextSqlNode(column.getColumn() + ",");
                String test = column.getProperty() + " != null " ;
                ifNodes.add(new IfSqlNode(ifSqlNode, test));
            }
        }
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "(", null, ")", ","));

        ifNodes = new ArrayList<SqlNode>();
        //用于进行 value(column, column,)
        for (EntityHelper.EntityColumn column : columnList) {
            //当参数中的属性值不为空的时候,使用传入的值
            //自增的情况下,如果默认有值,就会备份到property_cache中
            if (column.isIdentity()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "_cache },"), column.getProperty() + "_cache != null "));
            } else {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "},"), column.getProperty() + " != null "));
            }
            if (column.getSequenceName() != null && column.getSequenceName().length() > 0) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode(column.getProperty() + ".nextval ,"), column.getProperty() + " == null "));
            } else if (column.isIdentity()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + " },"), column.getProperty() + " == null "));
            } else if (column.isUuid()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode("#{" + column.getProperty() + "_bind },"), column.getProperty() + " == null "));
            }
        }
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "VALUES (", null, ")", ","));
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 获取返回值类型
     *
     * @param ms
     * @return
     */
    public Class<?> getSelectReturnType(MappedStatement ms) {
        String msId = ms.getId();
        if (entityType.get(msId) != null) {
            return entityType.get(msId);
        }
        Class<?> mapperClass = null;
        try {
            mapperClass = getMapperClass(msId);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("无法获取Mapper接口信息:" + msId);
        }
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType() == Mapper.class) {
                    Class<?> returnType = (Class) t.getActualTypeArguments()[0];
                    entityType.put(msId, returnType);
                    return returnType;
                }
            }
        }
        throw new RuntimeException("无法获取Mapper<T>泛型类型:" + msId);
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
     * 新建SelectKey节点 - 只对mysql的自动增长有效，Oracle序列直接写到列中
     *
     * @param ms
     * @param column
     */
    private void newSelectKeyMappedStatement(MappedStatement ms, EntityHelper.EntityColumn column) {
        String keyId = ms.getId() + SelectKeyGenerator.SELECT_KEY_SUFFIX;
        if (ms.getConfiguration().hasKeyGenerator(keyId)) {
            return;
        }
        Class<?> entityClass = getSelectReturnType(ms);
        //defaults
        Configuration configuration = ms.getConfiguration();
        KeyGenerator keyGenerator = new NoKeyGenerator();
        Boolean executeBefore = getBEFORE();
        String IDENTITY = (column.getGenerator() == null || column.getGenerator().equals("")) ? getIDENTITY() : column.getGenerator();
        SqlSource sqlSource = new RawSqlSource(configuration, IDENTITY, entityClass);

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, keyId, sqlSource, SqlCommandType.SELECT);
        statementBuilder.resource(ms.getResource());
        statementBuilder.fetchSize(null);
        statementBuilder.statementType(StatementType.STATEMENT);
        statementBuilder.keyGenerator(keyGenerator);
        statementBuilder.keyProperty(column.getProperty());
        statementBuilder.keyColumn(null);
        statementBuilder.databaseId(null);
        statementBuilder.lang(configuration.getDefaultScriptingLanuageInstance());
        statementBuilder.resultOrdered(false);
        statementBuilder.resulSets(null);
        statementBuilder.timeout(configuration.getDefaultStatementTimeout());

        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        ParameterMap.Builder inlineParameterMapBuilder = new ParameterMap.Builder(
                configuration,
                statementBuilder.id() + "-Inline",
                entityClass,
                parameterMappings);
        statementBuilder.parameterMap(inlineParameterMapBuilder.build());

        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(
                configuration,
                statementBuilder.id() + "-Inline",
                int.class,
                new ArrayList<ResultMapping>(),
                null);
        resultMaps.add(inlineResultMapBuilder.build());
        statementBuilder.resultMaps(resultMaps);
        statementBuilder.resultSetType(null);

        statementBuilder.flushCacheRequired(false);
        statementBuilder.useCache(false);
        statementBuilder.cache(null);

        MappedStatement statement = statementBuilder.build();
        configuration.addMappedStatement(statement);

        MappedStatement keyStatement = configuration.getMappedStatement(keyId, false);
        configuration.addKeyGenerator(keyId, new SelectKeyGenerator(keyStatement, executeBefore));
        //keyGenerator
        try {
            MetaObject msObject = forObject(ms);
            msObject.setValue("keyGenerator", configuration.getKeyGenerator(keyId));
        } catch (Exception e) {
            //ignore
        }
    }

    private boolean getBEFORE() {
        return config.BEFORE;
    }

    /**
     * 反射对象，增加对低版本Mybatis的支持
     *
     * @param object 反射对象
     * @return
     */
    public static MetaObject forObject(Object object) {
        MetaObject metaObject = MetaObject.forObject(object,
                DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());

        return metaObject;
    }

    private String getIDENTITY() {
        if (config.IDENTITY != null && config.IDENTITY.length() > 0) {
            return config.IDENTITY;
        }
        return "CALL IDENTITY()";
    }

    /**
     * 重新设置SqlSource
     *
     * @param ms
     * @param sqlSource
     */
    private void setSqlSource(MappedStatement ms, SqlSource sqlSource) {
        MetaObject msObject = forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
    }

    public void setIDENTITY(String IDENTITY) {
        config.IDENTITY = IDENTITY;
    }

}
