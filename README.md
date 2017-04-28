# Webapp Solution
A simple way to pack webapp into a single standalone exe file.

## Quick Usage
1. `mvn package` or download compiled result [packer\_and\_starters.zip](http://www.jar2exe.com/download/sites/jar2exe/files/download/solutions/packer_and_starters.zip), you will get:
	* jetty9-starter.jar
	* tomcat7-starter.jar
	* tomcat8-starter
	* packer.jar

2. Pre-compile your webapp and wrap into a .war file, for example `web.war`.

3. Make sure Jar2Exe is installed and `j2ewiz` is reachable in PATH. Right now, Jar2Exe itself only runs on Windows.

4. Run `packer.jar` with `web.war` and `xxxx-starter.jar` as arguments.

		java -jar packer.jar web.war tomcat8-starter.jar

5. Other parameters after `java -jar packer.jar` will be passed to `j2ewiz`, for example:

		java -jar packer.jar web.war xxx-starter.jar /amd64 /platform linux
