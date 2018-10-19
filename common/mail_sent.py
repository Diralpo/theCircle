#!/usr/bin/env python
# coding=utf-8

import smtplib
#from email.mime.text import MIMEText
from email.utils import formataddr
from email.mime.multipart import MIMEMultipart

from config import const


def mail(err_title, text_content_list, sender_nickname='Program', recipient_nickname='Programmer', recipient= const.EMAIL_LIST):
    ret = True
    try:
        msg = MIMEMultipart('alternative')
        msg['From'] = formataddr([sender_nickname, const.EMAIL_USER])  # 括号里的对应发件人邮箱昵称、发件人邮箱账号
        msg['To'] = formataddr([recipient_nickname, recipient])  # 括号里的对应收件人邮箱昵称、收件人邮箱账号
        msg['Subject'] = err_title
        for i in text_content_list:
            msg.attach(i)

        # 发件人邮箱中的SMTP服务器
        server = smtplib.SMTP_SSL(const.EMAIL_SMTP, const.EMAIL_PORT)
        # 括号中对应的是发件人邮箱账号、邮箱密码
        server.login(const.EMAIL_USER, const.EMAIL_PWD)
        # 括号中对应的是发件人邮箱账号、收件人邮箱账号、发送邮件
        server.sendmail(const.EMAIL_USER, [recipient, ], msg.as_string())
        server.quit()  # 关闭连接
    except :  # 如果 try 中的语句没有执行，则会执行下面的 ret=False
        ret = False
    return ret
