#!/usr/bin/env python
# coding=utf-8

from email.mime.text import MIMEText

from config import const
from common import mail_sent
from common import db_helper


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
        if db_helper.insert_tuple(db, cursor, sql_insert) < 0:
            return -2

        sql_select = 'select * from users where nickname="{}"'.format(nickname)
        res = db_helper.select_tuple(db, cursor, sql_select)
        if len(res) <= 0:
            return -3
        user_id = res[0]['user_id']

        sql_insert = '''insert into user_verified(user_id, verified_url, begin_time) values ({}, "{}", {})'''.format(
            user_id, verified_url, signup_time
        )
        if db_helper.insert_tuple(db, cursor, sql_insert) < 0:
            return -4

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
                                            亲爱的 {}，
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
        part2 = MIMEText(ahtml, 'html')
        sent_list = [part2]
        mail_sent.mail("验证邮箱账户", sent_list, recipient=email, recipient_nickname=nickname, sender_nickname='账户验证')

        sql_insert = '''insert into user_auths(user_id, identity_type, identifier, credential) 
                values ({}, "{}", "{}", "{}")'''.format(
            user_id, 'nickname', nickname, pwd
        )
        if db_helper.insert_tuple(db, cursor, sql_insert) < 0:
            return -5

        sql_select = 'select * from schools where sc_name="{}"'.format(school)
        res = db_helper.select_tuple(db, cursor, sql_select)
        if len(res) <= 0:
            return -6
        sc_id = res[0]['sc_id']

        sql_insert = '''insert into user_school(user_id, school) values ({}, {})'''.format(
            user_id, sc_id
        )
        if db_helper.insert_tuple(db, cursor, sql_insert) < 0:
            return -7
        return 0
    except KeyError:
        db.rollback()
    return -1


#  邮箱验证
def verify_user(db, cursor, verified_url):
    try:
        #  在保存验证信息的表中查找该验证url
        sql_select = 'select * from user_verified where verified_url ="{}"'.format(verified_url)
        res = db_helper.select_tuple(db, cursor, sql_select)
        if len(res) <= 0:
            return -1
        user_id = res[0]['user_id']
        #  更新用户表中的用户状态和权限
        sql_update = '''update users set status ={},power = {} where user_id={}'''.format(
            1, 1, user_id)
        db_helper.update_tuple(db, cursor, sql_update)
        sql_del = "delete from user_verified where verified_url='{}'".format(verified_url)
        #  从验证表中删去此url
        db_helper.del_tuple(db, cursor, sql_del)
        return 0
    except :
        return -2


