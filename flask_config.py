#!/usr/bin/env python
# coding=utf-8

from flask import Flask
from flask_bootstrap import Bootstrap

app = Flask(__name__)
bootstrap = Bootstrap(app)