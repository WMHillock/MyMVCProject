package ru.wmh.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.wmh.springcourse.models.Person;

import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jbdcTemplate;
    @Autowired
    public PersonDAO(JdbcTemplate jbdcTemplate) {
        this.jbdcTemplate = jbdcTemplate;
    }

    /*
    Оставил для наглядности, самописный PersonMaper в index
    и готовое решение в методе show
    Суть в сопоставлении названий полей и столбцов
     */
    public List<Person> index() {
        return jbdcTemplate.query("SELECT * FROM Person", new PersonMapper());
    }

    public Person show(int id) {
        return jbdcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class)).stream()
                .findAny().orElse(null);
    }

    public void save(Person person)  {
       jbdcTemplate.update("INSERT INTO Person VALUES(1, ?, ?, ?)",
               person.getName(),
               person.getAge(),
               person.getEmail());
    }

    public void update(int id, Person updatedPerson) {
        jbdcTemplate.update("UPDATE Person SET name=?, age=?, email=? WHERE id=?",
                updatedPerson.getName(),
                updatedPerson.getAge(),
                updatedPerson.getEmail());
    }

    public void delete(int id) {
        jbdcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }
}
