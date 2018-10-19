#!/usr/bin/env python
# coding=utf-8

import pymysql
import re

from config import const
from common import mail_sent
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


###############
#  向table中加入新元组
def insert_tuple(db, cursor, sql_insert):
    try:
        cursor.execute(sql_insert)
        db.commit()
        return 0
    except KeyError:
        return -1


###############
#  在table中更新某元组
def update_tuple(db, cursor, sql_update):
    try:
        cursor.execute(sql_update)
        db.commit()
        return 0
    except KeyError:
        return -1


#####################
#  在table中删除某元组
def del_tuple(db, cursor, sql_del):
    try:
        cursor.execute(sql_del)
        db.commit()
        return 0
    except KeyError:
        return -1


###############
#  在table中查找元素
def select_tuple(db, cursor, sql_select):
    try:
        cursor.execute(sql_select)
        res = cursor.fetchall()
        return res
    except :
        return None
