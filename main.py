#!/usr/bin/env python
# coding=utf-8

from service.server import *

if __name__ == '__main__':
    db, cursor = db_helper.login_db(const.DB_USER, const.DB_PASS, const.DB_NAME)
    app.run(host=const.SERVER_IP, port=const.SERVER_PORT)

