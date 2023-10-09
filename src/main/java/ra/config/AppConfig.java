package ra.config;


import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ra"})
@EnableTransactionManagement
@EnableJpaRepositories("ra.dao")
@PropertySource("classpath:application.properties")
public class AppConfig implements WebMvcConfigurer {
    // application

    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".jsp");
        return  resolver;
    }
    // cấu hình xử lí đường dẫn
// khi  mọi người truy cập đường dẫn có dạng /css/style.css => map với đường dẫn resourse/assets/css/style.css
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = "C:\\Users\\hung1\\OneDrive\\Desktop\\ProjectMd4_demo\\src\\main\\webapp\\upload\\";
        registry.addResourceHandler("/css/**"
                                    ,"/js/**"
                                    ,"/fonts/**"
                                    ,"/img/**"
                                    ,"/upload/**")
                .addResourceLocations("classpath:/assets/css/"
                                    ,"classpath:/assets/js/"
                                    ,"classpath:/assets/fonts/"
                                    ,"classpath:/assets/img/"
                                    ,"file:"+ uploadPath);

    }
    // cấu hình kích thước upload tối đa
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new
                CommonsMultipartResolver();
        multipartResolver.setMaxUploadSizePerFile(50*1024*1024); // 50MB
        return multipartResolver;
    }
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("application");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }
    // cấu hình entityManager
    @Bean
    @Qualifier(value = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }
    // cấu hình entityManagerFactory

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("ra.model");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }
    // cấu hình kết nối csdl
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/hehehe");
        dataSource.setUsername("root");
        dataSource.setPassword("18061999");
        return dataSource;
    }
    // cấu hình transaction
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    public Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        return properties;
    }
}
