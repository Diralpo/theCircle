#!/usr/bin/env python
# coding=utf-8

import time

from flask import Flask, json
from flask import request
from flask_cors import *
import hashlib

from common import db_helper
from config import const

app = Flask(__name__, template_folder='../Webpage/')
CORS(app, supports_credentials=True)


@app.route('/signup', methods=['GET', 'POST'])
def get_the_ip():
    if request.method == 'POST':
        ip = request.remote_addr
        user_data = request.get_json()
        user_data['ip'] = str(ip)

        tran_str = user_data['username']
        hl = hashlib.md5()
        hl.update(tran_str.encode(encoding='utf-8'))
        verified_url = hl.hexdigest()
        tran_str = str(time.time())
        hl.update(tran_str.encode(encoding='utf-8'))
        verified_url = verified_url + hl.hexdigest()
        tran_str = user_data['email']
        hl.update(tran_str.encode(encoding='utf-8'))
        verified_url = verified_url + hl.hexdigest()

        user_data['verified_url'] = verified_url
        user_data['signup_time'] = time.time()
        print('验证邮箱的url为： ' + verified_url)

        db_helper.insert_user(db, cursor, user_data)
        return '注册成功，请前往邮箱确认'


@app.route('/verification/<verified_url>/', methods=['GET'])
def user_verify(verified_url):
    #  邮箱验证
    if db_helper.user_verify(db, cursor, verified_url) == 0:
        return "注册成功"
    else:
        return "404 <br/> NOT FOUND"


@app.route('/search', methods=['GET', 'POST'])
def search():
    data = [{'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'},
            {'姓名': 'xx', '学号': 1234, '性别': 'male', '联系方式': '29xxx'}]
    return json.dumps(data, encoding='utf-8')


if __name__ == '__main__':
    db, cursor = db_helper.login_db(const.DB_USER, const.DB_PASS, const.DB_NAME)
    app.run(host=const.SERVER_IP, port=const.SERVER_PORT)
