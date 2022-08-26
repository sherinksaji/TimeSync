# TimeSync
TimeSync is an app that syncs Timetables. 
It was made using Android Studio.
This app uses Firebase RealtimeDatabase.        
When pressing build, in values.xml file there may be an error.
If there is, here is a quick fix:
	in line 1577, just change the hyphen in <overlayable name="rotary-ui"> to underscore.
	in line 961, just change the hyphens in <overlayable name="car-ui-lib"> to underscores.
Then press build, unfortuantely you will be taken to home activity straight away. Just press logout.
You can register for the account by pressing the Register textview.
