package com.example.mqttpro.analysis;

import com.example.mqttpro.user.UserDao;

public class RegisterAnalysis {

    /**
     * 用户信息注册方法
     * @param stu 学号
     * @param studentName 姓名
     * @param userName 用户名
     * @param passWord1 密码
     * @param passWord2 确认密码
     * @param studentEmail 邮箱
     * @return 信息错误类型analysis
     */
    public static int addInformation(String stu, String studentName, String userName, String passWord1, String passWord2, String studentEmail) {
        int analysis = 0 ;
        UserDao userDao = new UserDao();
        boolean i = userDao.selectRepeatInformation(userName);
        if (stu.equals("")){
            analysis = 1;
        }else if (studentName.equals("")){
            analysis = 2;
        }else if(userName.equals("")){
            analysis = 3;
        }else if (passWord1.equals("")){
            analysis = 4;
        }else if (passWord2.equals("")){
            analysis = 5;
        }else if (studentEmail.equals("")){
            analysis = 6;
        }else if (!passWord1.equals(passWord2)){
            analysis = 7;
        }else if (userName.length()<4){
            analysis = 8;
        }else if (passWord2.length()<4){
            analysis = 9;
        }else if (i){
            analysis = 10;
        }else if (userDao.addInformation(stu,studentName,userName,passWord2,studentEmail)>0){
            analysis = 11;
        }
        return analysis;
    }
}
