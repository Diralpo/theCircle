#!/usr/bin/env python
# coding=utf-8

from service.server import *
from service.web_server import *

if __name__ == '__main__':
    app.run(debug=True, port=80)
