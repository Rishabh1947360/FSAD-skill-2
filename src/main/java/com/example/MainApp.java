package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class MainApp {

    public static void main(String[] args) {

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();

        // INSERT
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        Product p1 = new Product("Laptop", "Gaming Laptop", 75000, 10);
        Product p2 = new Product("Phone", "Android Phone", 20000, 20);

        session.persist(p1);   // ✅ replaces save()
        session.persist(p2);   // ✅ replaces save()

        tx.commit();
        session.close();       // ✅ close after commit

        // READ
        session = factory.openSession();
        Product p = session.get(Product.class, 1);

        System.out.println("Product Name: " + p.getName());
        System.out.println("Price: " + p.getPrice());

        session.close();
        factory.close();
    }
}