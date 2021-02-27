# 1337 Homepage Downloader
An application for downloading homepages. Developed as part of the tretton37 code assignment.

## Running the application
The system have been built to be able to run on Windows 10 and Java 15. To run the application simply download the repository and compile the code using Maven ('mvn clean install').
The jar-file should be generated in the target folder if the compilation was successful.
To run it simply write 'java -jar 1337-webpage-downloader-0.0.1-SNAPSHOT.jar'.
I might recommend to pipe the output to a file as the output is quite verbose.

The website will be stored in a folder named 'tretton37.com' located in the same folder as you are currently located.
If you want to rerun the application it is recommended to delete all content in the folder storing website (including the folder).

## Design
### Overview
The System consists of six main parts: the Application, the Downloader Executor Service, the Http Request Handler, the Asyncronous Response Processor, the Webpage Parser and the File Service:
- The **Application** is the main class of the software, responsible for initializing the other classes and to handle the shutdown of the application.
  The Application is run in its own thread.
- The **DownloaderExecutorService** is responsible for handling the main execution of the code.
  This is done through a thread pool to which execution requests can be submited.
  The service also keeps track of the state of the execution requests to the service.
  This is done through a queue of Futures. A future represents a state of an execution request.
- The **HttpRequestHandler** is responsible for managing the connections and http requests. The handler takes a URI, representing the requested resource, and a response handler,
  representing the code which should be used to process the response.
- the **AsyncResponseProcessor** is responsible for the process of parsing and storing responses, and generate new requests to the requestHandler.
- the **WebpageParser** is responsible for parsing the webpages and looking for links to other parts of the site.
  The parser is constructed so it ignores links to external resources (other websites), this is to hinder the application for downloading the whole internet.
- the **FileService** is responsible for handling the file monitoring and storing.

### Performance Improving Concepts
The file system of application is used to keep track of which resource already has been downloaded and which to request from the server.
This is done to keep these checks non-blocking and asyncronous. If a race condition would appear that causes a resources to be downloaded twice, the new one will simply overwrite the old one.

When the requestHandler requests a webpage it generates a future connected to a post-processing task. Put this task is not sent to the exectorService until the response has been received.
This is to hinder the task to block and therefore slow down the application.
These generated futures are used to keep track of when the download is complete and the application can be shut down.

The parser seperates its links into two different parts. The part which should be parsed and the one who shouldn't.
This is to not spend unneccessary time parsing images, videos etc. looking for linked resources.

## Improvement
This section cover some the improvements which can be done to the code.
- **Error Handling -** the application currently has quite primitive error handling.
- **Logging -** the application lacks a good logging system which would improve the error handling.
- **More JUnit tests -** the current tests does not fully explore the full execution tree, and therfore doesn't achieve full code coverage.
- **Parsing -** the parser is built to only be able to handle the tretton37 website and changes would need to be made to extend the functionality to other websites.
  The parser also can only handle http and could be improved to handle other protocols and scripts, for example like javascipt.
- **OS Independency -** the application is built and tested on a Windows 10 machine and might not work 100% on other Operating Systems.
  This could be improved through the use of the System.seperator.
- **Reloading -** the application could be improved to allow the user to download only the missing resources instead of the whole website, even if the time saved would be minimal.