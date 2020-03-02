# Пошаговая инструкция добавления Security

1. В pom.xml добавить зависимость<br>
	`<groupId>org.springframework.boot</groupId>` <br>
	`<artifactId>spring-boot-starter-security</artifactId>`

2. В пакете `persist.entities` создаем перечисление `Role` и класс `User`.
В классе `User` создаем конструктор без параметров, конструктор со всеми параметрами, геттеры и сеттеры, билдер.

3. Подготовить SQL-запрос для создания таблицы users. Добавить в пользователи админа.

4. Создать `UserRepository` репозиторий в пакете `persist.repositories`, 
унаследовать от `JpaRepository<User, Integer>`. 
Определить метод `Optional<User> findOneByUsername(String username)`.

5. Создать пакет `security`. 
В пакете `security` создаем пакет `details`.
В пакете `details` создаем реализацию интерфейсов `UserDetails` и `UserDetailsService` - 
`UserDetailsImpl` и `UserDetailsServiceImpl`. 

6. Доработать класс приложения `NotesApplication`.
Добавить аннотации для указания где искать репозитории, сущности.
Добавить бин `PasswordEncoder`.

7. В пакет `security` добавить пакет `config`.
В пакете `config` создать класс `SecurityConfig`,
унаследовать от `WebSecurityConfigurerAdapter`.
Переопределить методы `void configure(HttpSecurity http)` и `void configure(AuthenticationManagerBuilder auth)`.

8. Создать пакет `transfer` и в нем создать класс `UserRegDto` для передачи данных пользователя. 

9. Добавить контроллер `SignupController` в пакет `controllers` 
и страницу `signup.html` в папку `templates`.

10. Добавить котроллер `LoginController` в пакет `controllers` 
и страницу `login.html` в папку `templates`.