HttpMock
========

HttpMock was designed in order to mock endpoints, either for frontend code, or collaborating services.

During my time writing code (especially in a more microservices approach) I've seen lots of people mocking out the client inside their service / front end to disastrous effect.
Most of the bugs have been seen to be in how the client handles errors.  
I then got to thinking about having a mock that was a real server that could be asserted upon, and primed as part of the test.
You can then begin to do all sorts of interesting things like cancelling / shutting down the server mid reply, and see how the service / client handles it.

So, HttpMock is a small library, with one dependency (hamcrest) that makes a server that you can prime as part of a testcase.

HttpMock is also meant to be extensible - it uses hamcrest matchers at the core of it to decide on whether an incoming request 
matches.  

#### How to Build
```bash
cd <the dir where this project is>
ant
cd dist
unzip HttpMock.zip
```
This has the built library, and the version of hamcrest I sued bundled with it.


#### Examples
##### Example of collaborating service test

```java
@Test
public void exampleServiceTest() throws Exception {
    HttpMock mockUserService = new HttpMock();
    mockUserService.expects()
            .when(pathStartsWith("/users/authorise"))
            .and(methodEq(POST))
        .replyWith(reply("{\"feature\":{\"chat\":\"authorised\"}}", 200));
    
    mockUserService.play();

    final RestServiceUnderTest unit = new RestServiceUnderTest(mockUserService.url().toString() + "/users/authorise");
    unit.start();

    ResponseToClient response = send(unit.serviceUrl(), "{\"user_id\":\"1234567890\", \"feature\":\"chat\"}", header("Accept", "application/json"), POST);

    mockUserService.validate();
    
    assertThat(response.getBody(), is("{\"feature\":{\"chat\":\"authorised\"}}"));
}
```



##### Example of a front end test with mocked service
```java
@Test
public void frontEndTest() throws Exception {
    HttpMock mockUserService = new HttpMock();
    mockUserService.expects()
            .when(pathStartsWith("/users/authorise"))
            .and(methodEq(POST))
            .replyWith(reply("", 401));
    mockUserService.play();
    final FrontEndRestService unit = new FrontEndRestService(mockUserService.url().toString() + "/users/authorise");
    unit.start();
    

    //obviously don't write ugly webdriver code like this
    WebDriver driver = WebDriverInstance.get();
    driver.get(unit.url());
    driver.findElement(By.id("username")).sendKeys("banned_user");
    driver.findElement(By.id("password")).sendKeys("banned_password");
    driver.findElement(By.id("submit")).click();
    
    String errorMessage = driver.findElement(By.id("banner_error")).getText();
    
    assertThat(errorMessage, is("That user and / or password combination cannot be found"));
}
```