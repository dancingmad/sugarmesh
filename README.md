# sugarmesh

## How to Build
mvn clean install (to install into local repository)

## How to Run
1) as a library 

add dependency to pom.xml:
```
<dependency>
  <groupId>com.irongrp</groupId>
  <artifactId>sugarmesh</artifactId>
  <version>1.0.0</version>
</dependency>
```
Provide credentials in the application.properties:

```
spring.data.neo4j.uri=bolt://localhost:7687
spring.data.neo4j.username=
spring.data.neo4j.password=
```

Inject Test to existing Spring Context

```
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableNeo4jRepositories(basePackages = "com.irongrp")
@EntityScan("com.irongrp.sugarmesh")
public class MainAppTests {

	@Autowired
	private BeansEndpoint endpoint;

	private DependencyUserService userService;
	private DependencyService dependencyService;

	@Autowired
    private UserRepository userRepository;

	@Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

	@Test
	public void updateDependencyGraph() {
	    DependencyUserService userService = new DependencyUserService(userRepository, passwordRepository);
		User user = userService.getUser("application");
		if (user == null) {
			user = userService.register("application","");
		}
		DependencyService dependencyService = new DependencyService(applicationRepository);
		dependencyService.createDependencyGraph(user,endpoint.beans(),"com.irongrp");
	}

}
```
