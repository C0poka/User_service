package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactoryInternal(null, null, null);
        }
        return sessionFactory;
    }

    public static SessionFactory buildSessionFactory(String jdbcUrl, String username, String password) {
        return buildSessionFactoryInternal(jdbcUrl, username, password);
    }

    private static SessionFactory buildSessionFactoryInternal(String jdbcUrl, String username, String password) {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml"); // загружаем конфигурацию

            String envUrl = System.getenv("DB_URL");
            String envUser = System.getenv("DB_USER");
            String envPass = System.getenv("DB_PASSWORD");

            if (jdbcUrl != null && username != null && password != null) {
                configuration.setProperty("hibernate.connection.url", jdbcUrl);
                configuration.setProperty("hibernate.connection.username", username);
                configuration.setProperty("hibernate.connection.password", password);
            } else if (envUrl != null && envUser != null && envPass != null) {
                configuration.setProperty("hibernate.connection.url", envUrl);
                configuration.setProperty("hibernate.connection.username", envUser);
                configuration.setProperty("hibernate.connection.password", envPass);
            }

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            System.err.println("Ошибка создания SessionFactory: " + e.getMessage());
            throw new RuntimeException("Не удалось создать SessionFactory", e);
        }
    }
}

