# \<notes\> CRUD приложение на Spring Boot

Приложение для ведения заметок - списка дел, с отметкой сделано или нет.
Реализована регистрация и аутентификация пользователей.
Каждый пользователь имеет доступ только к своему списку заметок.

С заметками можно выполнить следующие действия: 
* постраничный вывод списка, 
* фильтровать список, 
* сортировать список по дате создания,
* создать новую заметку,
* изменить заметку,
* удалить заметку. 

Администратор (пользователь с ролью ADMIN) может управлять списком пользователей.
Со списком пользователей возможны следущие действия:
 * постраничный вывод списка,
 * изменение роли и/или активности пользователя,
 * отключение пользователя,
 * добавление нового пользователя осуществляется пользователем самостоятельно через регистрацию.
 

### Используемые технологии
* Java 8
* Spring Boot, Spring MVC, Spring Data JPA, Spring Security
* MySql
* Thymeleaf
* Bootstrap
* Maven
* Tomcat
* JUnit 5, Mockito

### Шаги реализации
1. Взял за основу <a href="https://javarush.ru/groups/posts/497-zavoevanie-spring-boot">Завоевание Spring Boot</a>. 
Вот результат - <a href="https://github.com/ValeriyEmelyanov/notes/tree/v1_simple">вариант простой реализации</a>.  
2. Добавил пользователей и Security - [Пошаговая инструкция добавления Security](STEP-BY-STEP-SECURITY-ADDING.md) 
3. Заметки привязал к пользователям.
4. Реализовал работу со списком пользователей.

Собрать проект и запустить из командной строки:<br> 
`mvn spring-boot:run`

### Проблемы и их решения

* У меня MsSql 5.7. При подключении базы к EDEA была ошибка 08001. В настройках подключения необходимо в Driver выбрать "MySQL for 5.1".
* Вывод перечисления в виде списка радиокнопок:
<br> <i>пробный вариант, добавляю в модель атрибут done</i>

---
        <label class="radio-inline">
            <input th:value="${'all'}" type="radio" name="done" th:checked="${done=='all'}">All
        </label>
        <label class="radio-inline">
            <input th:value="${'plan'}" type="radio" name="done" th:checked="${done=='plan'}">Plan
        </label>
        <label class="radio-inline">
            <input th:value="${'done'}" type="radio" name="done" th:checked="${done=='done'}">Done
        </label>
---

<i>оптимизированный вариант, добавляю в модель атрибуты filterOptions (массив констант перечисления) и done</i>

---
        <div th:each="item : ${filterOptions}" class="radio-inline">
            <input th:value="${item}" th:id="${item.ordinal()}" type="radio" name="done" th:checked="${done==item.name()}">
            <label th:for="${item.ordinal()}" th:text="${item.description}">All
            </label>
        </div>
---

<i>и еще вариант, добавляю в модель атрибуты filterOptions (массив констант перечисления), done является полем объекта filterAdjuster</i>

---
    <form class="form-inline" th:object="${filterAdjuster}" th:action="@{/(searchText=*{searchText},done=*{done})}">
        <div th:each="option : ${filterOptions}" class="radio-inline">
            <input type="radio" th:value="${option}" th:field="*{done}" th:id="${option.ordinal()}">
            <label th:for="${option.ordinal()}" th:text="${option.description}">All
            </label>
        </div>
        <input th:value="*{searchText}" th:field="*{searchText}" id="searchText" class="form-control mr-sm-2" type="text" placeholder="Search">
        <button class="btn btn-outline-primary" type="submit">Search</button>
    </form>
---

* Ресурс интерпретируется как таблица стилей. 
После успешной аутентификации открывается содержимое страницы /static/css/bootstrap.min.css.
Нашел ответ <a href="https://coder-booster.ru/q/resource-interpreted-as-stylesheet-but-transferred-with-mime-type-text-html-see-18236/">здесь</a> - ответ @Rob Sedgwic.
Проблема происходит, когда запрос, в том числе и для статического контента проходит проверку подлинности.
Решение: добавил исключение в мою конфигурацию безопасности `.antMatchers("/css/**").permitAll()`.
* В UserService было необходимо преобразовать Page\<User\> в Page\<UserDto\>. 
Помогло это: <a href="https://stackoverflow.com/questions/37749559/conversion-of-list-to-page-in-spring">Conversion of List to Page in Spring</b></a>
* Чтобы добавить JUnit5 необходимо сначала из `spring-boot-starter-test` исключть JUnit4:
---
            <exclusions>
                <exclusion>
                    <groupId>junit</groupId>
                    <artifactId>junit</artifactId>
                </exclusion>
            </exclusions>
---
* Как в JUnit проверить ожидаемые исключения? 
Ответы: <a href="http://barancev.github.io/junit-catch-throwable/">...как в JUnit проверять ожидаемые исключения?</b></a> 
и <a href="https://stackoverflow.com/questions/40268446/junit-5-how-to-assert-an-exception-is-thrown">JUnit 5: How to assert an exception is thrown?</b></a> 
* Проблема Mockito в заглушке нельзя применить void метод - решение: 
<a href="https://stackoverflow.com/questions/25249902/mockito-issue-whenjava-lang-void-in-stubber-cannot-be-applied-to-void">Mockito issue - when(java.lang.Void) in Stubber cannot be applied to void</a>
* Как при тестировании подтасовать аутентификацию?:
<a href="https://www.javacodegeeks.com/2017/05/mocking-spring-security-context-unit-testing.html">Mocking Spring Security Context for Unit Testing</a>
* Чтобы при удалении данных избежать использования http-метода GET, 
нашел следующее решение: 
<br> a) в application.properties добавляю строку <br>
`spring.mvc.hiddenmethod.filter.enabled=true`
<br> б) на странице использую конструкцию  
---
    <form th:action="@{'/delete/{id}'(id=${note.id})}" th:method="delete">
        <button type="submit" class="btn-link">
            <i class="fa fa-trash" style="font-size: 20px;"></i>
        </button>
    </form>
---
которая превратиться в соответствующий html-код:

---
    <form action="/delete/12" method="post">
        <input type="hidden" name="_method" value="delete"/>
        <button type="submit" class="btn-link">
            <i class="fa fa-trash" style="font-size: 20px;"></i>
        </button>
    </form>
---
в) в контроллере для метода-обработчика использую соответствующую аннотацию @DeleteMapping.

 
### Полезные ссылки

MySql
* <a href="http://stasyak.ru/?p=51">Пользователи и их права в MySQL на примерах</a>
* <a href="https://gahcep.github.io/blog/2013/01/05/mysql-utf8/">Корректная настройка MySQL для работы с UTF8</a>

CRUD
* <a href="https://www.youtube.com/watch?v=e7swABdqOS4">Создание CRUD приложения на языке Java с помощью Spring <b>Eugene Suleimanov</b></a>
* <a href="https://www.logicbig.com/tutorials/spring-framework/spring-data/pagination-with-thymeleaf.html">Spring Data JPA - Pagination With Thymeleaf View</b></a>
 
 Thymeleaf
 * <a href="http://itutorial.thymeleaf.org/exercise/12">Thymeleaf Interactive Tutorial <i>Exercise 12: forms</i></b></a>
 
 Прочее
 * <a href="https://fontawesome.ru/all-icons/">Полная сборка из 675 иконок Font Awesome 4.7.0</b></a>
 
 Тестирование
 * <a href="http://java-online.ru/junit-mockito.xhtml">JUnit и фреймворк Mockito</b></a>
 
  