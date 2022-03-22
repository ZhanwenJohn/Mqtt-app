package com.example.mqttpro.user;

import com.example.mqttpro.dbHelper.DBHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    /**
     * 测试MySQL是否正常连接
     * @return MySQL表中的用户总数
     */
    public int getUserSize(){
        DBHelper dbHelper = new DBHelper();
        String sql = "SELECT COUNT(1) AS s1 FROM Mqtt_android";
        int i = 0;
        ResultSet resultSet;
        try {
            resultSet = dbHelper.executeQuery(sql);
            System.out.println("数据库连接开始");
            while(resultSet.next()){
                i = resultSet.getInt("s1");
                System.out.println("SQL语句执行成功!!!");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            System.out.println("数据库连接失败");
        }finally {
            try{
                dbHelper.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("数据库关闭失败");
            }
        }
        System.out.println("返回值成功"+i);
        return i;
    }

    /**
     * 用户注册方法
     * @param stu 学号
     * @param studentName 姓名
     * @param userName 用户名
     * @param passWord2 密码
     * @param studentEmail 邮箱
     */
    public int addInformation(String stu, String studentName, String userName, String passWord2, String studentEmail) {
        DBHelper dbHelper = new DBHelper();
        int i = 0;
        String sql = "INSERT INTO Mqtt_android(stu_android,studentname_android,username_android,password_android,email_android) VALUES (?,?,?,?,?)";
        try {
            i = dbHelper.executeUpdate(sql,stu,studentName,userName,passWord2,studentEmail);
            System.out.println("有" + i + "条语句执行成功");
            System.out.println("数据连接，插入用户信息");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                dbHelper.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("数据库关闭失败");
            }
            System.out.println("返回值执行语句数量" + "\t" + i + "条");
            return i;
        }
    }

    /**
     *查询用户填写的信息用户名是否重复
     * @param userName 用户名
     */
    public boolean selectRepeatInformation(String userName) {
        DBHelper dbHelper = new DBHelper();
        String sql = "SELECT COUNT(1) AS s1 FROM Mqtt_android WHERE username_android=?";
        int i = 0;
        ResultSet resultSet;
        try {
            resultSet = dbHelper.executeQuery(sql,userName);
            System.out.println("数据库连接,开始查询用户名是否重复");
            while(resultSet.next()){
                i = resultSet.getInt("s1");
                if (i!=0){
                    return true;
                }
                System.out.println("SQL语句执行成功!!!");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            System.out.println("数据库连接失败");
        }finally {
            try{
                dbHelper.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("数据库关闭失败");
            }
        }
        System.out.println("返回值成功"+i);
        return false;
    }


    public User findByNameAndPassword(String adminName) {
        DBHelper dbHelper = new DBHelper();
        User user = null;
        String sql = "SELECT * FROM Mqtt_android WHERE username_android=?";
        try {
            ResultSet resultSet = dbHelper.executeQuery(sql,adminName);
            System.out.println("连接数据库，查询用户信息");
            while(resultSet.next()){
                user = new User();
                user.setId(resultSet.getInt("id_android"));
                user.setStudentStu(resultSet.getString("stu_android"));
                user.setStudentName(resultSet.getString("studentname_android"));
                user.setPassword(resultSet.getString("password_android"));
                user.setStudentEmail(resultSet.getString("email_android"));
                System.out.println("查询用户密码");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            System.out.println("数据库连接失败");
        }finally {
            try{
                dbHelper.close();
                System.out.println("数据库关闭连接");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("数据库关闭失败");
            }
        }
        return user;
    }
}
