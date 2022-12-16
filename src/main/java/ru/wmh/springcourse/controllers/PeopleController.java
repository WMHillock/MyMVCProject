package ru.wmh.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.wmh.springcourse.dao.PersonDAO;
import ru.wmh.springcourse.models.Person;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    /* Если убрать конструктор то поле personDAO станет красным, а все потому что
    у нас в конструкторе PersonController стоит аннотация @Autowired что создает его
    экземпляр при запуске программы, кроме того в классе PersonDAO есть аннтоация
    @Component что позволяет Spring автоматически создать его экземпляр, даже
    не смотря на отсутствие у PersonDAO конструктора!
     */
    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }


    //Тут мы получаем список всех объектов Person и выводим их с помощью Thymeleaf (там свой обходчик)
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    /* Через PathVariable вытаскиваем из запроса id и с помощью метода show
    из DAO выводим элемент с этим id. Напоминаю что Model model это пара
    ключ-значение для отображения в представлении,
    ниже ключ - "person", значение - personDAO.show(id) результат метода show
    в данном случае show возвращает объект Person с переданным id
     */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    /*Это создание объекта Person в методе newPerson мы отдаем пользователю анкету для заполнения
     * в методе create мы принимаем на стороне сервера введенные пользователем данные */
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);
        return "redirect:/people";
    }

    /* Здесь повеселей, у нас в параметрах @GetMapping указан динамический id
    который мы получаем из запроса с помощью PathVariable а затем отдаем в представление
    модель объекта с вызваным id, часть логики прописана в HTML файлах, так что по факту
    мы отдаем редактируемый вариант объекта
     */
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    /*Здесь мы отправляем обратно на сервер обновенные данные которые мы
    заполняли после метода выше
     */
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}
