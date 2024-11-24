### index.html and its Special Meaning

- Question: Notice how the index.html has a special meaning to the server. What is this "special meaning"?

- Answer: The file named index.html is recognized by web servers as the default file to serve when a directory is requested. When you navigate to http://localhost:3000/, the server looks for index.html in the root directory to serve as the default page.

- Question: Does it have a special meaning to the browser?

- Answer: No, the browser does not assign any special meaning to index.html. The browser simply sends a request to the server for a specific URL, and it is the server that determines which file to serve in response.
