# textSearchEngine
Prerequisites-
1.Mongo Db should be up and running at 127.0.0.1:27017(to enable the use of mongo db as the underlying db set the property use.mongodb in applicatiion.properties to true).


#Points to note-

1.The project is based on an inverted index based search.

2.A separate thread is responsible for creating the inverted index which checks for file in the folder location specied in application.properties as
 files.dir

3.This particular project can read files in the format- date time:level:class-message
 There will be subtle code changes required to handle a separate format.

4.There is mongo store added as well but the use of the same degrades performance exponentially.
 The default version uses an in memory hashmap storing all the token and their corresponding files with line numbers.
 To activate the mongodb based store set the property use.mongodb in application.properties to true.

5.There are two API's-
 POST-
 curl -v -HContent-Type:application/json -X POST --data-binary '{"searchQuery": "GET_CART"}' http://127.0.0.1:8080/findwordinfile | python -m json.tool
 GET-
 curl -v -HContent-Type:application/json -X GET  http://127.0.0.1:8080/findwordinfile?word=GET_CART | python -m json.tool

#Running the project-
1.The project has been exported as a jar.
2.To export it as a war change the packaging to war in pom.xml and also exclude the dependency of apache tomcat from-
 <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
3.If you want to use the version without mongo please checkout the branch dev-without-mongo and on the contrary checkout dev-with-mongo.
4.