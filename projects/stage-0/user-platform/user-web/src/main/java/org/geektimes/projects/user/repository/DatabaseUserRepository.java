package org.geektimes.projects.user.repository;


import org.geektimes.function.ThrowableFunction;
import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.sql.DBConnectionManager;
import org.geektimes.projects.user.sql.Insert;
import org.geektimes.projects.user.sql.Select;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang.ClassUtils.wrapperToPrimitive;

public class DatabaseUserRepository implements UserRepository, InvocationHandler {

    private static Logger logger = Logger.getLogger(DatabaseUserRepository.class.getName());

    /**
     * 通用处理方式
     */
    private static Consumer<Throwable> COMMON_EXCEPTION_HANDLER = e -> logger.log(Level.SEVERE, e.getMessage());

    public static final String INSERT_USER_DML_SQL =
            "INSERT INTO users(name,password,email,phoneNumber) VALUES " +
                    "(?,?,?,?)";
    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";
    public static final String QUERY_ALL_USERS_DML_SQL = "SELECT id,name,password,email,phoneNumber FROM users";

    private final DBConnectionManager dbConnectionManager;

    public DatabaseUserRepository() {
        this.dbConnectionManager = ComponentContext.getInstance().getComponent("bean/DBConnectionManager");
    }

    private Connection getConnection() {
        String databaseURL ="jdbc:derby:TEST/db/user-platform;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(databaseURL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return connection;
    }

    @Override
    public boolean save(User user) {
        return false;
//        return executeQuery(INSERT_USER_DML_SQL, resultSet -> {
//            // TODO
//            return true;}, COMMON_EXCEPTION_HANDLER, user);

    }

    @Override
    public boolean deleteById(Long userId) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User getById(Long userId) {
        return null;
    }

    @Override
    public User getByNameAndPassword(String userName, String password) {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users WHERE name=? and password=?",
                resultSet -> {
                    // TODO
                    return new User();
                }, COMMON_EXCEPTION_HANDLER, userName, password);
    }

    @Override
    public Collection<User> getAll() {
        System.out.println("execute insert: " + "SELECT id,name,password,email,phoneNumber FROM users");

        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users", resultSet -> {
            // BeanInfo -> IntrospectionException
            BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) { // 如果存在并且游标滚动 // SQLException
                User user = new User();
                for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                    String fieldName = propertyDescriptor.getName();
                    Class fieldType = propertyDescriptor.getPropertyType();
                    String methodName = resultSetMethodMappings.get(fieldType);
                    // 可能存在映射关系（不过此处是相等的）
                    String columnLabel = mapColumnLabel(fieldName);
                    Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                    // 通过放射调用 getXXX(String) 方法
                    Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);
                    // 获取 User 类 Setter方法
                    // PropertyDescriptor ReadMethod 等于 Getter 方法
                    // PropertyDescriptor WriteMethod 等于 Setter 方法
                    Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                    // 以 id 为例，  user.setId(resultSet.getLong("id"));
                    setterMethodFromUser.invoke(user, resultValue);
                    System.out.println(user.toString());
                }
            }

            return users;
        }, e -> {
            // 异常处理
        });
    }

    /**
     * @param sql
     * @param function
     * @param <T>
     * @return
     */
    protected <T> T executeQuery(String sql, ThrowableFunction<ResultSet, T> function,
                                 Consumer<Throwable> exceptionHandler, Object... args) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Class argType = arg.getClass();

                Class wrapperType = wrapperToPrimitive(argType);

                if (wrapperType == null) {
                    wrapperType = argType;
                }

                // Boolean -> boolean
                String methodName = preparedStatementMethodMappings.get(argType);
                Method method = PreparedStatement.class.getMethod(methodName, wrapperType);
                method.invoke(preparedStatement, i + 1, args);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            // 返回一个 POJO List -> ResultSet -> POJO List
            // ResultSet -> T
            return function.apply(resultSet);
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        }
        return null;
    }
    private Object executeInsertSql(String sqlTemplate, Object[] args) throws IllegalAccessException, SQLException {
        System.out.println("execute insert: " + sqlTemplate);
        StringBuilder cols = new StringBuilder();
        StringBuilder values = new StringBuilder();

        Object data = args[0];
        Field[] fields = data.getClass().getDeclaredFields();
        for (Field field: fields) {
            if (field.get(data) == null) {
                continue;
            }
            cols.append(field.getName()).append(",");
            String sp= field.get(data).toString();
            sp="'"+sp+"'";
            values.append(sp+",");
        }

        String sql = String.format(sqlTemplate, cols.toString().substring(0,cols.length()-1), values.toString().substring(0,values.length()-1));
        System.out.println("sql string: " + sql);

//        Connection conn = database.getConnection();
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
//        statement.execute("")
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resq = meta.getTables(null, null, null, new String[]{"TABLE"});
        HashSet<String> set=new HashSet<String>();

        while (resq.next()) {
            System.out.println("sql string: " + resq.getString("TABLE_NAME"));
            set.add(resq.getString("TABLE_NAME"));
        }
        System.out.println(set);
        if(!set.contains("users".toUpperCase())){
            statement.execute(CREATE_USERS_TABLE_DDL_SQL);
        }


        statement.execute(sql);

//        String querySql = "select * from users";
        Collection<User> all = getAll();
//        ResultSet res = statement.executeQuery(querySql);
//        while (res.next()) {
//            System.out.println(res.toString());
//        }
     for (User user:all){
                     System.out.println(user.toString());

     }


        statement.close();

        return true;
    }

    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }
    private Object executeQuerySql(String querySql, String returnType) throws SQLException, ClassNotFoundException, IllegalAccessException, IntrospectionException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        List<Object> result = new ArrayList<>();

        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet res = statement.executeQuery(querySql);
        // BeanInfo
        BeanInfo userBeanInfo = Introspector.getBeanInfo(Class.forName(returnType), Object.class);

        while (res.next()) {
            Object object = Class.forName(returnType).newInstance();
            for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                String fieldName = propertyDescriptor.getName();
                Class fieldType = propertyDescriptor.getPropertyType();
                String methodName = resultSetMethodMappings.get(fieldType);
                // 可能存在映射关系（不过此处是相等的）
                String columnLabel = fieldName;
                Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                // 通过放射调用 getXXX(String) 方法
                Object resultValue = resultSetMethod.invoke(res, columnLabel);
                // 获取 User 类 Setter方法
                // PropertyDescriptor ReadMethod 等于 Getter 方法
                // PropertyDescriptor WriteMethod 等于 Setter 方法
                Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                // 以 id 为例，  user.setId(resultSet.getLong("id"));
                setterMethodFromUser.invoke(object, resultValue);
            }
            result.add(object);

            System.out.println(object.toString());
        }
        statement.close();
        return result;
    }
    /**
     * 数据类型与 ResultSet 方法名映射
     */
    static Map<Class, String> resultSetMethodMappings = new HashMap<>();

    static Map<Class, String> preparedStatementMethodMappings = new HashMap<>();

    static {
        resultSetMethodMappings.put(Long.class, "getLong");
        resultSetMethodMappings.put(String.class, "getString");

        preparedStatementMethodMappings.put(Long.class, "setLong"); // long
        preparedStatementMethodMappings.put(String.class, "setString"); //


    }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation: annotations) {
                if (annotation instanceof Insert) {
                    try {
                        return executeInsertSql(((Insert) annotation).value(), args);
                    } catch (IllegalAccessException | SQLException e) {
                        e.printStackTrace();
                        return false;
                    }
//                    save(args);
                }

                if (annotation instanceof Select) {
                    try {
                        return executeQuerySql(((Select) annotation).value(), ((Select) annotation).returnType());
                    } catch (SQLException | ClassNotFoundException | IllegalAccessException | IntrospectionException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }

}
