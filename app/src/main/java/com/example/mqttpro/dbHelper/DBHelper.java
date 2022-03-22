package com.example.mqttpro.dbHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
    //通用层，初始化对象，链接字符串等信息
    String url = "jdbc:mysql://1.1.1.1:3306/Mqtt_app?&serverTimeZone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&&useSSL=false";
    String sqlName = "root";
    String sqlPass = "root";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;


    /**
     * 创建一个方法专门用来加载驱动和链接数据库
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void getConnection() throws ClassNotFoundException, SQLException {
        //1.加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //2.建立连接
        connection = DriverManager.getConnection(url,sqlName,sqlPass);
    }

    /**
     * 创建一个方法专门用来执行增删改的sql
     * @param sql
     * @param objects
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int executeUpdate(String sql,Object...objects) throws SQLException, ClassNotFoundException {
        //在外部调用executeUpdate()方法时就连接数据库
        getConnection();
        //预编译执行sql,创建语句发送器
        preparedStatement = connection.prepareStatement(sql);
        //怎么样去为sql语句中的？赋值
        setPreparedValues(objects);
        int i = preparedStatement.executeUpdate();
        return i;
    }

    public ResultSet executeQuery(String sql,Object...objects) throws SQLException, ClassNotFoundException {
        getConnection();
        preparedStatement = connection.prepareStatement(sql);
        setPreparedValues(objects);
        rs = preparedStatement.executeQuery();
        return rs;
    }

    /**
     * 创建一个为sql语句动态赋值的方法
     * 用可变参数动态的为sql赋值
     * @param objects
     */
    public void setPreparedValues(Object...objects) throws SQLException {
        if(objects!=null&&objects.length>0){
            for (int i = 0;i<objects.length;i++){
                //把可变参数本质上是数组，这里需要把数组中的第n位赋值给sql中的第n+1列
                //数组时从0开始，而sql位置从1开始
                preparedStatement.setObject(i+1,objects[i]);
            }
        }
    }

    /**
     * 关闭
     * @throws SQLException
     */
    public void close() throws SQLException {
        if(rs!=null){
            rs.close();
        }
        if(preparedStatement!=null){
            preparedStatement.close();
        }
        if(connection!=null){
            connection.close();
        }
    }
}
