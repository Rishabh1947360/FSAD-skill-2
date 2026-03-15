package com.example;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class MainApp {

    public static void main(String[] args) {

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();

        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        // Insert additional products
        session.save(new Product("Laptop","Gaming",75000,10));
        session.save(new Product("Phone","Android",20000,20));
        session.save(new Product("Tablet","Android",30000,15));
        session.save(new Product("Headphones","Audio",3000,50));
        session.save(new Product("Keyboard","Accessories",1500,40));
        session.save(new Product("Mouse","Accessories",800,60));

        tx.commit();

        // ---------------- SORTING ----------------

        System.out.println("Products sorted by price (Ascending)");

        Query<Product> q1 = session.createQuery(
                "FROM Product p ORDER BY p.price ASC", Product.class);

        List<Product> list1 = q1.list();

        for(Product p : list1) {
            System.out.println(p.getName()+" "+p.getPrice());
        }

        // ---------------- DESCENDING ----------------

        System.out.println("\nProducts sorted by price (Descending)");

        Query<Product> q2 = session.createQuery(
                "FROM Product p ORDER BY p.price DESC", Product.class);

        List<Product> list2 = q2.list();

        for(Product p : list2) {
            System.out.println(p.getName()+" "+p.getPrice());
        }

        // ---------------- SORT BY QUANTITY ----------------

        System.out.println("\nProducts sorted by quantity");

        Query<Product> q3 = session.createQuery(
                "FROM Product p ORDER BY p.quantity DESC", Product.class);

        List<Product> list3 = q3.list();

        for(Product p : list3) {
            System.out.println(p.getName()+" "+p.getQuantity());
        }

        // ---------------- PAGINATION ----------------

        System.out.println("\nFirst 3 Products");

        Query<Product> q4 = session.createQuery("FROM Product", Product.class);
        q4.setFirstResult(0);
        q4.setMaxResults(3);

        List<Product> page1 = q4.list();

        for(Product p : page1) {
            System.out.println(p.getName());
        }

        System.out.println("\nNext 3 Products");

        Query<Product> q5 = session.createQuery("FROM Product", Product.class);
        q5.setFirstResult(3);
        q5.setMaxResults(3);

        List<Product> page2 = q5.list();

        for(Product p : page2) {
            System.out.println(p.getName());
        }

        // ---------------- AGGREGATE FUNCTIONS ----------------

        Long total = (Long) session.createQuery(
                "SELECT COUNT(p) FROM Product p").uniqueResult();

        System.out.println("\nTotal Products: " + total);

        Long available = (Long) session.createQuery(
                "SELECT COUNT(p) FROM Product p WHERE p.quantity > 0").uniqueResult();

        System.out.println("Products with quantity > 0: " + available);

        Object[] price = (Object[]) session.createQuery(
                "SELECT MIN(p.price), MAX(p.price) FROM Product p").uniqueResult();

        System.out.println("Minimum Price: " + price[0]);
        System.out.println("Maximum Price: " + price[1]);

        // ---------------- GROUP BY ----------------

        System.out.println("\nGroup by Description");

        List<Object[]> group = session.createQuery(
                "SELECT p.description, COUNT(p) FROM Product p GROUP BY p.description")
                .list();

        for(Object[] row : group) {
            System.out.println(row[0] + " : " + row[1]);
        }

        // ---------------- WHERE FILTER ----------------

        System.out.println("\nProducts between price 1000 and 30000");

        Query<Product> q6 = session.createQuery(
                "FROM Product p WHERE p.price BETWEEN 1000 AND 30000", Product.class);

        List<Product> range = q6.list();

        for(Product p : range) {
            System.out.println(p.getName()+" "+p.getPrice());
        }

        // ---------------- LIKE QUERIES ----------------

        System.out.println("\nProducts starting with 'P'");

        Query<Product> q7 = session.createQuery(
                "FROM Product p WHERE p.name LIKE 'P%'", Product.class);

        List<Product> like1 = q7.list();

        for(Product p : like1) {
            System.out.println(p.getName());
        }

        System.out.println("\nProducts ending with 'e'");

        Query<Product> q8 = session.createQuery(
                "FROM Product p WHERE p.name LIKE '%e'", Product.class);

        List<Product> like2 = q8.list();

        for(Product p : like2) {
            System.out.println(p.getName());
        }

        System.out.println("\nProducts containing 'phone'");

        Query<Product> q9 = session.createQuery(
                "FROM Product p WHERE p.name LIKE '%phone%'", Product.class);

        List<Product> like3 = q9.list();

        for(Product p : like3) {
            System.out.println(p.getName());
        }

        session.close();
        factory.close();
    }
}