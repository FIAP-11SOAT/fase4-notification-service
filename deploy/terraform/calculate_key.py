import hmac
import hashlib
import base64

def calculate_key(secret_access_key, region):
    message = "SendRawEmail"
    version = 0x04

    signature = hmac.new(
        ("AWS4" + secret_access_key).encode('utf-8'),
        "11111111".encode('utf-8'),
        hashlib.sha256
    ).digest()

    signature = hmac.new(signature, region.encode('utf-8'), hashlib.sha256).digest()
    signature = hmac.new(signature, "ses".encode('utf-8'), hashlib.sha256).digest()
    signature = hmac.new(signature, "aws4_request".encode('utf-8'), hashlib.sha256).digest()
    signature = hmac.new(signature, message.encode('utf-8'), hashlib.sha256).digest()
    signature_and_version = bytes([version]) + signature
    smtp_password = base64.b64encode(signature_and_version)
    return smtp_password.decode('utf-8')