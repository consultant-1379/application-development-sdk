#!/usr/bin/env python

import os
import sys
import unittest
import requests
import time
from subprocess import Popen

CHECKS = 15
SECONDS_DELAY = 10
HTTP_OK = 200

# Disable output buffering to receive the output instantly
sys.stdout = os.fdopen(sys.stdout.fileno(), "w", 0)
sys.stderr = os.fdopen(sys.stderr.fileno(), "w", 0)

def is_url_available(url):
    num_check = CHECKS
    while num_check > 0:
        try:
            request = requests.get(url, allow_redirects=False, verify=False)
            status_code = request.status_code
            break
        except requests.ConnectionError, ce:
            print '%d. %s => [%s]' % (num_check, url, ce)

        time.sleep(SECONDS_DELAY)
        num_check -= 1
    return status_code == HTTP_OK

def get_redirect_url(url):
    redirect_url = None
    num_check = CHECKS
    while num_check > 0:
        try:
            request = requests.get(url, allow_redirects=False, verify=False)
            redirect_url = request.headers['location']
            break
        except KeyError, ke:
            print '%d. %s => KeyError [%s] not found yet' % (num_check, url, ke)
        except requests.ConnectionError, ce:
            print '%d. %s => [%s]' % (num_check, url, ce)

        time.sleep(SECONDS_DELAY)
        num_check -= 1
    print "redirect: %s =>> %s" % (url, redirect_url)
    return redirect_url

def run_shell_command(cmd):
        print ">> %s" % (' '.join(cmd))
        p = Popen(cmd, shell=False)
        p.communicate()
        if p.returncode != 0:
            print 'error code [%s]' % (p.returncode)
        return p.returncode


class TestApplicationLauncher(unittest.TestCase):
    setup_ok = True

    @classmethod
    def setUpClass(cls):
        """This is ran once before any test methods in the class"""
        print "==> Rebuild images checking for updated base images"
        cmd = ["docker-compose", "-f", "docker/docker-compose.yml", "build",
               "--force-rm", "--pull", "--no-cache"]
        result = run_shell_command(cmd)
        if result != 0:
            TestApplicationLauncher.setup_ok = False
            return result

        print "==> Bringing up test docker containers"
        cmd = ["docker-compose", "-f", "docker/docker-compose.yml", "up", "-d"]
        result = run_shell_command(cmd)
        if result != 0:
            TestApplicationLauncher.setup_ok = False
        return result

    @classmethod
    def tearDownClass(cls):
        """This is ran once after all test methods in the class regardless of
        whether they succeeded or failed"""
        print "==> Bringing down test docker containers"
        cmd = ["docker-compose", "-f", "docker/docker-compose.yml",
               "down", "-v"]
        return run_shell_command(cmd)

    def test01_docker_test_environment_setup_ok(self):
        self.assertTrue(TestApplicationLauncher.setup_ok, "Error setting up "
                                                 "docker test environment")

    def test02_enm_application_launcher_is_available(self):
        url = "http://localhost:8008/"
        self.assertTrue(is_url_available(url))

    def test03_sample_external_application_json_redirection_ok(self):
        url = "http://localhost:8008/rest/apps/web/externalapp"
        redirect_url = "https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/sites/tor/application-launcher/latest/index.html"
        self.assertEqual(get_redirect_url(url), redirect_url)

    def test04_sample_internal_application_json_redirection_ok(self):
        url = "http://localhost:8008/rest/apps/web/sampleapp"
        redirect_url = "http://localhost/#launcher/favorites"
        self.assertEqual(get_redirect_url(url), redirect_url)


