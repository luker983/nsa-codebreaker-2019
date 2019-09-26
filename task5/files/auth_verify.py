# uncompyle6 version 3.4.0
# Python bytecode 2.7 (62211)
# Decompiled from: Python 3.7.4 (default, Sep  7 2019, 18:27:02) 
# [Clang 10.0.1 (clang-1001.0.46.4)]
# Embedded file name: auth_verify.py
# Compiled at: 2019-09-22 01:57:55
import urllib2, pprint, json, logging, sys
logger = logging.getLogger('oauth_verify')

def check_token(token, url):
    try:
        data = 'token=' + token
        urllib2.install_opener(
            urllib2.build_opener(
                urllib2.ProxyHandler({'http': '127.0.0.1:8080'})
            )
        )
        req = urllib2.Request(url, data=data)
        resp = urllib2.urlopen(req)
    except:
        logger.exception('Token introspect request failed')
        return False
    else:
        if resp.getcode() != 200:
            logger.error('Bad HTTP response code: %d', resp.getcode())
            return False
        try:
            j = json.loads(resp.read())
        except:
            logger.exception('Malformed response data')
            return False

        logger.debug('Token Verification Response: %s', pprint.pformat(j))
        try:
            if j['active'] and 'chat' in j['scope'] and j['token_type'] == 'access_token':
                return True
        except:
            logger.exception('Exception in token response logic')
            return False

    return False


def main(argv=None):
    logging.basicConfig(level='DEBUG')
    if argv is None:
        argv = sys.argv
    if len(sys.argv) < 2:
        print >> sys.stderr, 'Usage: %s <token> [url]' % sys.argv[0]
        return 2
    else:
        token = sys.argv[1]
        if len(sys.argv) > 2:
            url = sys.argv[2]
        else:
            url = 'http://localhost:4445/oauth2/introspect'
        logger.info('check_token token: %r', token)
        logger.info('check_token url: %r', url)
        r = check_token(token, url)
        logger.info('check_token result: %r', r)
        if r:
            return 0
        return 1


if __name__ == '__main__':
    sys.exit(main())
# okay decompiling auth_verify.pyc
