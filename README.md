Diversity Readme

Requirements:

- Eclipse Mars 2 Java EE environment
- MySQL server 5.7 
- MySQL Workbench 6.3 CE 
- Tomcat 8.0


Setting up DataBase:

1 - Clone or sync project to local computer  
2 - Open MySQLWorkbench and connect to local database  
3 - On the top bar select Server then Data Import  
4 - Select "Import from Self-Contained File" and select the file DB.sql from inside the project folder  
5 - Click Start Import  

Setting up Workspace and testing Environment:

1 - Clone or sync project to local computer  
2 - Open Eclipse  
3 - From the top bar select File>Import and Existing Projects into Workspace  
4 - Root directory enter de path to the local copy of the project  
5 - Select Diversity Project and click Finish  
6 - Right-Click Project and Select Run As Server  
7 - If asked locate Tomcat folder  


Known issues:

If after all procedures on eclipse the programs fails to compile due to errors, right click project
then navigate to Build Path>Configure Build Path, Libraries tab, double click JRE System and select workspace default,
also delete all .jar present and Add External JARs from inside the project folder WebContent/WEB-INF/lib
 
