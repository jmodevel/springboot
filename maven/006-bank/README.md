# MySQL Docker
<!--This is a Docker command that runs a MySQL container.

Here's a breakdown of the options used:

docker run: This command runs a new container from the specified image.
-p 3306:3306: This option maps port 3306 on the host machine to port 3306 inside the container. This allows you to access the MySQL database from outside the container.
--name accountsdb: This option gives the container a name, which can be used to identify it later.
-e MYSQL_ROOT_PASSWORD=root: This option sets the root password for the MySQL database to "root".
-e MYSQL_DATABASE=accountsdb: This option creates a new database named "accountsdb" inside the MySQL container.
-d mysql: This option runs the container in detached mode (i.e., in the background) and specifies the image to use (in this case, the official MySQL image).
When you run this command, Docker will:

Pull the official MySQL image from Docker Hub (if it's not already cached locally).
Create a new container from the image.
Map port 3306 on the host machine to port 3306 inside the container.
Set the root password for the MySQL database to "root".
Create a new database named "accountsdb" inside the MySQL container.
Run the container in detached mode.
You can then access the MySQL database by connecting to localhost:3306 using a MySQL client (e.g., the mysql command-line tool).

Note that this command uses the official MySQL image, which is a good practice. However, you may want to consider using a custom image or a different database management system depending on your specific needs.-->
- docker run -p 3306:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=accountsdb -d mysql
- docker run -p 3307:3306 --name loansdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=loansdb -d mysql
- docker run -p 3308:3306 --name cardsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cardsdb -d mysql

- docker network ls

# SQLECTRON
- DB Client for Relational Databases
