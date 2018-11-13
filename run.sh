APPDIR=`pwd`
PIDFILE=$APPDIR/phoneDataClean.pid
if [ -f "$PIDFILE" ] && kill -0 $(cat "$PIDFILE"); then
echo "phoneDataClean is already running..."
exit 1
fi
nohup java -jar $APPDIR/phoneDataClean.jar >/dev/null 2>&1 &
echo $! > $PIDFILE
echo "start phoneDataClean..."


