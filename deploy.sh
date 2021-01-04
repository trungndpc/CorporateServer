
local_dir=./
server_dir=/home/insee/webapp

server=washup@103.147.186.58
jar_file=CorporateServer-0.0.1-SNAPSHOT.jar

rsync -a $local_dir/target/$jar_file $server:$server_dir/jar/
rsync -avr $local_dir/cmd $server:$server_dir/
