#!/usr/bin/env python
# coding=utf-8

from flask_config import *
from flask import render_template


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/user/<name>/')
def show_user(name):
    return render_template('user.html', name=name)
