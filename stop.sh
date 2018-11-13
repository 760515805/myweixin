APPDIR=`pwd`
PIDFILE=$APPDIR/phoneDataClean.pid
if [ ! -f "$PIDFILE" ] || ! kill -0 "$(cat "$PIDFILE")"; then
echo "phoneDataClean not running..."
else
echo "stopping phoneDataClean..."
PID="$(cat "$PIDFILE")"
kill -9 $PID
rm "$PIDFILE"
echo "...phoneDataClean stopped"
fi


