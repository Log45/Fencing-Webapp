import boto3
import os

s3 = boto3.client(
    "s3",
    endpoint_url=os.getenv("S3_ENDPOINT", "localhost:9000"),
    aws_access_key_id=os.getenv("S3_ACCESS_KEY"),
    aws_secret_access_key=os.getenv("S3_SECRET_KEY"),
)

def download_video(bucket, key, local_path):
    s3.download_file(bucket, key, local_path)

