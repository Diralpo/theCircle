#!/usr/bin/env python
# coding=utf-8

import pymysql
import re

from Webserver.config import const
from Webserver.common import mail_sent
from email.mime.text import MIMEText

#  查找是否具有某数据库
def database_exists(cursor, db_name):
    sql = "show databases;"
    cursor.execute(sql)
    tables = [cursor.fetchall()]
    table_list = re.findall('(\'.*?\')', str(tables))
    table_list = [re.sub("'", '', each) for each in table_list]
    if db_name in table_list:
        # 存在返回1
        return 1
    else:
        return 0


#  这个函数用来判断表是否存在
def table_exists(cursor, table_name):
    sql = "show tables;"
    cursor.execute(sql)
    tables = [cursor.fetchall()]
    table_list = re.findall('(\'.*?\')', str(tables))
    table_list = [re.sub("'", '', each) for each in table_list]
    if table_name in table_list:
        # 存在返回1
        return 1
    else:
        return 0


#  创建新的库
def create_database(cursor, db_name):
    # 首先检测此数据库是否存在
    if database_exists(cursor, db_name) is 0:
        sql_create = "create database {}".format(db_name)
        cursor.execute(sql_create)


#  删除某数据库
def del_database(cursor, db_name):
    # 首先检测此数据库是否存在
    if database_exists(cursor, db_name) is 1:
        sql_create = "drop database {}".format(db_name)
        cursor.execute(sql_create)


# 提供账户及密码登陆数据库
def login_db(user, pwd, db_name):
    try:
        db = pymysql.connect(host=const.DB_HOST, port=const.DB_PORT, user=user, passwd=pwd, db=db_name, charset="utf8", cursorclass = pymysql.cursors.DictCursor)
        # 获取游标对象
        cursor = db.cursor()
        return db, cursor
    except pymysql.err.InternalError:
        # 若该数据库不存在
        db = pymysql.connect(host=const.DB_HOST, port=const.DB_PORT, user=user, passwd=pwd, db='mysql', charset="utf8")
        # 获取游标对象
        cursor = db.cursor()
        create_database(cursor, db_name)

        db = pymysql.connect(host=const.DB_HOST, port=const.DB_PORT, user=user, passwd=pwd, db=db_name, charset="utf8", cursorclass = pymysql.cursors.DictCursor)
        # 获取游标对象
        cursor = db.cursor()
        return db, cursor


############################
#  加入新用户
#  需传入一个字典
def insert_user(db, cursor, user_data):
    try:
        print(user_data)
        nickname = user_data['username']
        status = 0
        power = 0
        email = user_data['email']
        the_ip = user_data['ip']
        signup_time = user_data['signup_time']
        pwd = user_data['pwd']
        verified_url = user_data['verified_url']
        school = user_data['school']
        sql_insert = '''insert into users(nickname, status, power,ip, signup_time, email) values ("{}", {}, {},"{}",{},"{}")'''.format(
            nickname, status, power, the_ip, signup_time, email)
    except KeyError:
        # 不可缺少的字段缺失，结束程序
        # mail_sent.mail('加入学生信息', "表单要素缺失\n{}".format(str(user_data)))
        return -1
    try:
        cursor.execute(sql_insert)
        db.commit()
        sql = 'select * from users where nickname="{}"'.format(nickname)
        cursor.execute(sql)
        res = cursor.fetchall()
        user_id = res[0]['user_id']
        sql_insert = '''insert into user_verified(user_id, verified_url, begin_time) values ({}, "{}", {})'''.format(
            user_id, verified_url, signup_time
        )
        cursor.execute(sql_insert)
        db.commit()
        # 发送注册邮件

        ahtml = """
        <html> 
          <head></head> 
          <body>
                <table style="width: 538px; background-color: #393836;" align="center" cellspacing="0" cellpadding="0">
                    <tbody>
                        <tr>
                            <td style="height: 65px; background-color: #171a21; border-bottom: 1px solid #4d4b48; padding: 0px;">
                
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#17212e">
                
                                <table width="470" border="0" align="center" cellpadding="0" cellspacing="0" style="padding-left: 5px; padding-right: 5px; padding-bottom: 10px;">
                
                                    <tbody><tr bgcolor="#17212e">
                                        <td style="padding-top: 32px; padding-bottom: 16px;">
                                        <span style="font-size: 24px; color: #66c0f4; font-family: Arial, Helvetica, sans-serif; font-weight: bold;">
                                            敬爱的 {}，
                                        </span><br>
                                        </td>
                                    </tr>
                                    <tr bgcolor="#121a25">
                                        <td style="padding: 20px; font-size: 12px; line-height: 17px; color: #c6d4df; font-family: Arial, Helvetica, sans-serif;">
                                            <p>若您注册了我们网站的账户，可点击如下链接确认；若没有，可忽略此邮件</p>
                                            <p><a style="color: #c6d4df;" href="http://{}:{}/verification/{}/" rel="noopener" target="_blank">点击此处来验证您的电子邮件地址。</a></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="font-size: 12px; color: #6d7880; padding-top: 16px; padding-bottom: 60px;">
                                            <p>感谢您协助我们维护帐户的安全。<br><br><br><br>
                                            <a style="color: #8f98a0;" href="{}" rel="noopener" target="_blank">联系我们</a><br>
                                        </p></td>
                                    </tr>
                                </tbody></table>
                            </td>
                        </tr>
                    </tbody>
                </table>
          </body> 
        </html> 
        """.format(nickname, const.SERVER_IP, const.SERVER_PORT, verified_url, const.CONTACT_US)
        #  Record the MIME types of both parts - text/plain and text/html.
        part2 = MIMEText(ahtml, 'html')
        sent_list = [part2]

        mail_sent.mail("验证邮箱账户", sent_list, recipient=email, recipient_nickname=nickname, sender_nickname='账户验证')

        sql_insert = '''insert into user_auths(user_id, identity_type, identifier, credential) 
                values ({}, "{}", "{}", "{}")'''.format(
            user_id, 'nickname', nickname, pwd
        )
        cursor.execute(sql_insert)
        db.commit()

        sql = 'select * from schools where sc_name="{}"'.format(school)
        cursor.execute(sql)
        res = cursor.fetchall()
        sc_id = res[0]['sc_id']

        sql_insert = '''insert into user_school(user_id, school) values ({}, {})'''.format(
            user_id, sc_id
        )
        cursor.execute(sql_insert)
        db.commit()


        return 0
    except :
        db.rollback()
    return -1


#  邮箱验证
def user_verify(db, cursor, verified_url):
    try:
        sql = 'select * from user_verified where verified_url ="{}"'.format(verified_url)
        cursor.execute(sql)
        res = cursor.fetchall()
        user_id = res[0]['user_id']
        sql_insert = '''update users set status ={},power = {} where user_id={}'''.format(
            1, 1, user_id)
        cursor.execute(sql_insert)
        db.commit()
        return 0
    except :
        return -1


