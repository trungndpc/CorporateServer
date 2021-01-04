
local_dir=./
server=washup@103.147.186.58

rsync -a $local_dir/insee-promotion-client/ $server:/var/www/insee-client/html

