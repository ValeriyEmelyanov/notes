# \<notes\> CRUD приложение на Spring Boot

Приложение для ведения заметок - списка дел, с отметкой сделано или нет.

### Используемые технологии
* Spring Boot, Spring MVC, Spring Data JPA
* MySql
* Thymeleaf
* Bootstrap
* Maven
* Tomcat


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


### Полезные ссылки

Взято за основу <a href="https://javarush.ru/groups/posts/497-zavoevanie-spring-boot">Завоевание Spring Boot</a>

MySql
* <a href="http://stasyak.ru/?p=51">Пользователи и их права в MySQL на примерах</a>
* <a href="https://gahcep.github.io/blog/2013/01/05/mysql-utf8/">Корректная настройка MySQL для работы с UTF8</a>

CRUD
* <a href="https://www.youtube.com/watch?v=e7swABdqOS4">Создание CRUD приложения на языке Java с помощью Spring <b>Eugene Suleimanov</b></a>
* <a href="https://www.logicbig.com/tutorials/spring-framework/spring-data/pagination-with-thymeleaf.html">Spring Data JPA - Pagination With Thymeleaf View</b></a>
 
 Thymeleaf
 * <a href="http://itutorial.thymeleaf.org/exercise/12">Thymeleaf Interactive Tutorial <i>Exercise 12: forms</i></b></a>
 