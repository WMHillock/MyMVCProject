package ru.wmh.springcourse.dao;

import org.springframework.stereotype.Component;
import ru.wmh.springcourse.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/EC_DataHolder";
    public static String USERNAME ="postgres";
    public static String PASSWORD ="azazel";
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Person> index() {
        //Список в который выгружаем из бащы людей
        List<Person> people = new ArrayList<>();
        //Тут соответственно запрос
        String SQL = "SELECT * from person";
        //Это объект соединения
        Statement statement = null;
        try {
            statement = connection.createStatement();
            //А вот таким образом обрабатывается результат запроса
            ResultSet resultSet = statement.executeQuery(SQL);
            //Обходим значения выгруженные из базы
            while (resultSet.next()) {
                //Создаем пустой объект для заполнения
                Person person = new Person();
                //Заполняем его из базы
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setEmail(resultSet.getString("email"));
                person.setAge(resultSet.getInt("age"));
                //Запихиваем объект в наш список
                people.add(person);
            }

        } catch (SQLException e) {
            e.printStackTrace();;
        }


        return people;
    }

    public Person show(int id) {

        Person person = null;
        //Подготавливаем ресурсы - PrepareStatement
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM Person WHERE id=?")) {
            //Передаем входные данные для PrepareStatement
            preparedStatement.setInt(1, id);
            //Проводим изменения
            preparedStatement.executeQuery();
            //Так как мы получаем данные обратно, забираем их в резальт
            ResultSet resultSet = preparedStatement.executeQuery();
            //Переключаемся на первый полученый резальт (у нас тут не показ одного участника)
            resultSet.next();
            //Создаем болванку для полученного объекта
            person = new Person();
            //Заполняем болванку
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Отдаем полученный результат
    return person;
    }

    public void save(Person person)  {
        /*
        Есть statment и prepareStatement
        второй вариант лучше:
         быстрей,
         SQL injection safe,
         кэшируется базой данных
         компилируется 1 раз
         */
        //Надо ли оборачивать prepareStatement в ресурсы трая?
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO Person VALUES(1, ?, ?, ?)")) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, Person updatedPerson) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE Person SET name=?, age=?, email=? WHERE id=?")){

            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {

    try (PreparedStatement preparedStatement =
            connection.prepareStatement("DELETE FROM Person WHERE id=?")){
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
}
