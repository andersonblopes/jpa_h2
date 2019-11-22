package com.lopes.jpa_h2;

import com.lopes.jpa_h2.model.Contato;
import com.lopes.jpa_h2.model.Employee;
import com.lopes.jpa_h2.model.Envio;
import com.lopes.jpa_h2.model.Mensagem;
import com.lopes.jpa_h2.model.TemperaturaContato;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Principal {


    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        atualizacaoDeVariosRegistros(entityManager);
        atualizacaoDeVariosRegistrosEmLote(entityManager);
        atualizacaoDeVariosRegistrosEmLoteCriteria(entityManager);
        remocaoDeVariosRegistrosEmLote(entityManager);
        insercaoDeVariosRegistros(entityManager);
        insercaoDeVariosRegistrosEmLote(entityManager);
        persistEmployees(entityManager);
        findEmployeeByPhoneCount(entityManager);
        findDeptHavingAboveNetAverage(entityManager);
        findEmployeeWithLessThanAverageSalary(entityManager);

        entityManager.close();
        entityManagerFactory.close();

    }

    @SuppressWarnings("unchecked")
    public static void persistEmployees(EntityManager em) {
        Employee employee1 = Employee.create("Diana", "IT", 3000, "111-111-111", "222-111-111");
        Employee employee2 = Employee.create("Rose", "Admin", 2000, "333-111-111", "444-111-111");
        Employee employee3 = Employee.create("Denise", "Admin", 4000, "555-111-111");
        Employee employee4 = Employee.create("Mike", "IT", 3500, "777-111-111");
        Employee employee5 = Employee.create("Linda", "Sales", 2000);
        em.getTransaction().begin();
        em.persist(employee1);
        em.persist(employee2);
        em.persist(employee3);
        em.persist(employee4);
        em.persist(employee5);
        em.getTransaction().commit();

        System.out.println("-- Employees persisted --");
        Query query = em.createQuery("SELECT e FROM Employee e");
        List<Employee> resultList = query.getResultList();
        resultList.forEach(System.out::println);
    }

    @SuppressWarnings("unchecked")
    private static void findEmployeeByPhoneCount(EntityManager em) {
        System.out.println("-- Employees who have more than 2 phones --");
        Query query = em.createQuery("SELECT e FROM Employee e where (SELECT COUNT(p) FROM e.phoneNumbers p) >= 2");
        List<Employee> resultList = query.getResultList();
        resultList.forEach(System.out::println);
    }

    @SuppressWarnings("unchecked")
    private static void findDeptHavingAboveNetAverage(EntityManager em) {
        System.out.println("-- net average salary --");
        Object singleResult = em.createQuery("SELECT AVG(e.salary) FROM Employee e").getSingleResult();
        System.out.println(singleResult);

        System.out.println("-- Dept by average salaries --");
        List<Object[]> list = em.createQuery("SELECT e.dept, AVG(e.salary) FROM Employee e GROUP BY e.dept")
                .getResultList();
        list.forEach(ar -> System.out.println(Arrays.toString(ar)));

        System.out.println("-- Dept having AVG salaries greater than net AVG salary --");
        Query query = em
                .createQuery("SELECT e.dept, AVG(e.salary) FROM Employee e GROUP BY e.dept HAVING AVG(e.salary) > "
                        + "(SELECT AVG(e2.salary) FROM Employee e2)");
        List<Object[]> resultList = query.getResultList();
        resultList.forEach(r -> System.out.println(Arrays.toString(r)));
    }

    @SuppressWarnings("unchecked")
    private static void findEmployeeWithLessThanAverageSalary(EntityManager em) {
        System.out.println("-- Employees who have less than average salary --");
        Query query = em
                .createQuery("SELECT e FROM Employee e where  e.salary < (SELECT AVG(e2.salary) FROM Employee e2)");
        List<Employee> resultList = query.getResultList();
        resultList.forEach(System.out::println);
    }

    public static void insercaoDeVariosRegistrosEmLote(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        List<Mensagem> mensagens = entityManager.createQuery("select m from Mensagem m").getResultList();

        List<Contato> contatos = entityManager.createQuery("select c from Contato c").getResultList();

        int limiteInsercoesMemoria = 3;
        int contadorLimite = 1;

        for (Mensagem mensagem : mensagens) {
            for (Contato contato : contatos) {
                Envio envio = new Envio();
                envio.setMensagem(mensagem);
                envio.setContato(contato);
                envio.setDataEnvio(LocalDateTime.now());
                entityManager.persist(envio);

                if ((contadorLimite++) == limiteInsercoesMemoria) {
                    entityManager.flush();
                    entityManager.clear();

                    contadorLimite = 1;
                    System.out.println("----------------- flush");
                }
            }
        }

        entityManager.getTransaction().commit();
    }

    public static void insercaoDeVariosRegistros(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        List<Mensagem> mensagens = entityManager.createQuery("select m from Mensagem m").getResultList();

        List<Contato> contatos = entityManager.createQuery("select c from Contato c").getResultList();

        mensagens.forEach(mensagem -> {
            contatos.forEach(contato -> {
                Envio envio = new Envio();
                envio.setMensagem(mensagem);
                envio.setContato(contato);
                envio.setDataEnvio(LocalDateTime.now());
                entityManager.persist(envio);
            });
        });

        entityManager.getTransaction().commit();
    }

    public static void remocaoDeVariosRegistrosEmLote(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        entityManager.createQuery("delete from Contato c").executeUpdate();

        entityManager.getTransaction().commit();
    }

    public static void atualizacaoDeVariosRegistrosEmLoteCriteria(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Contato> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Contato.class);
        Root<Contato> root = criteriaUpdate.from(Contato.class);

        criteriaUpdate.set(root.get("temperatura"), TemperaturaContato.FRIO);

        entityManager.createQuery(criteriaUpdate).executeUpdate();

        entityManager.getTransaction().commit();
    }

    public static void atualizacaoDeVariosRegistrosEmLote(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        entityManager.createQuery("update Contato c set c.temperatura = :temperatura")
                .setParameter("temperatura", TemperaturaContato.FRIO).executeUpdate();

        entityManager.getTransaction().commit();
    }

    public static void atualizacaoDeVariosRegistros(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        List<Contato> contatos = entityManager.createQuery("select c from Contato c").getResultList();

        contatos.forEach(c -> c.setTemperatura(TemperaturaContato.FRIO));

        entityManager.getTransaction().commit();
    }

}
