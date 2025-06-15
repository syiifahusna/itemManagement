package com.itemManagement.config;

import com.itemManagement.entity.*;
import com.itemManagement.repository.*;
import com.itemManagement.util.EncryptDecryptUtil;

import feign.codec.ErrorDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Configuration
public class InitializeConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailingRepository mailingRepository;

    @Autowired
    private EncryptDecryptUtil encryptDecryptUtil;

    @Transactional
    public void initData1() {

        Role roleAdmin = new Role("ADMIN");
        Role roleEmployee = new Role("EMPLOYEE");
        Role roleClient = new Role("CLIENT");

        List<Role> roleList = new ArrayList<>(
                Arrays.asList(roleAdmin,roleEmployee,roleClient)
        );


        roleRepository.saveAll(roleList);
    }

    @Transactional
    public void initData2(){

        Optional<Role> r = roleRepository.findById(1L);
        Optional<Role> r2 = roleRepository.findById(2L);
        Optional<Role> r3 = roleRepository.findById(3L);

        Set<Role> roleAdmin = new HashSet<>();
        roleAdmin.add(r.get());
        roleAdmin.add(r2.get());

        Set<Role> roleEmp = new HashSet<>();
        roleEmp.add(r2.get());

        Set<Role> roleClient = new HashSet<>();
        roleClient.add(r3.get());

        User user1 = new User("Admin",
                "admin@mail.com",
                "admin",
                passwordEncoder.encode("admin123"),
                new Image("/admin1.png"),
                roleAdmin,
                true,true,true,true);

        User user2 = new User("Employee",
                "employee@mail.com",
                "employee",
                passwordEncoder.encode("employee123"),
                new Image("/emp1.jpg"),
                roleEmp,
                true,true,true,true);

        User user3 = new User("Client",
                "iamclient@mail.com",
                "iamclient",
                passwordEncoder.encode("iamclient123"),
                new Image("/client1.jpg"),
                roleClient,
                true,true,true,true);

        User user4 = new User("Employee2",
                "employee2@mail.com",
                "employee2",
                passwordEncoder.encode("employee2123"),
                new Image("/emp1.jpg"),
                roleEmp,
                true,true,true,true);





        List<User> userList = new ArrayList<>(
                Arrays.asList(user1,user2,user3,user4)
        );

       userRepository.saveAll(userList);

       Cart cart = new Cart(user3,null,null);

       cartRepository.save(cart);;
    }

    @Transactional
    public void initData3() {
        try{

            Mailing mailing = new Mailing(
                    "POP7dSYbU+iiUAEZAzvbxg==",
                    "1HMaCN1Yl/Dhza6DCY9wAA==",
                    "cKm8LIcKLvMF8530Vhz4oQBH8whs4eKaPyWTqbFt4YM=",
                    "/b+FD3Bid29ZEiZn8xSz4eWJ9IUvQT2bL6B3GvjSqJA=",
                    "wMG4SRRk/eUxQKiTFhYoqA==",
                    true,
                    true,
                    true,
                    true
            );

            mailingRepository.save(mailing);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public void initData4(){
        List<Item> items = new ArrayList<>();

        String[] itemNames = {"Flour", "Bread", "Salmon Fillet", "Black Pepper", "Toothbrush", "Milk", "Eggs", "Butter",
                "Cheese", "Tomato", "Onion", "Garlic", "Chicken Breast", "Beef Steak", "Pasta", "Rice", "Olive Oil", "Salt",
                "Pepper", "Sugar", "Honey", "Tea", "Coffee", "Yogurt", "Cereal", "Oats", "Banana", "Apple", "Orange", "Lemon",
                "Lettuce", "Carrot", "Potato", "Broccoli", "Spinach", "Cucumber", "Bell Pepper", "Mushroom", "Ginger", "Chili Pepper",
                "Tuna", "Shrimp", "Baking Powder", "Vinegar", "Soy Sauce", "Ketchup", "Mustard", "Mayonnaise", "Butter Beans", "Chickpeas",
                "Lentils", "Canned Tomatoes", "Tomato Paste", "Peanut Butter", "Jam", "Bread Crumbs", "Coconut Milk", "Cornflakes", "Granola",
                "Chicken Stock", "Beef Stock", "Fish Sauce", "Sesame Oil", "Balsamic Vinegar", "White Wine", "Red Wine", "Pork Chops", "Lamb Chops",
                "Basil", "Thyme", "Rosemary", "Parsley", "Cilantro", "Paprika", "Cumin", "Turmeric", "Cinnamon", "Nutmeg", "Cloves", "Bay Leaves", "Vanilla Extract",
                "Baking Soda", "All-purpose Flour", "Self-raising Flour", "Bread Flour", "Cake Flour", "Caster Sugar", "Icing Sugar", "Brown Sugar", "Molasses", "Raisins",
                "Almonds", "Cashews", "Pistachios", "Walnuts", "Peanuts", "Hazelnuts", "Sunflower Seeds", "Pumpkin Seeds", "Chia Seeds", "Flax Seeds", "Quinoa"};

        for (int i = 0; i < itemNames.length; i++) {
            Item item = new Item();
            item.setName(itemNames[i]);
            item.setDescription("This is the description for " + itemNames[i]);
            item.setPrice(BigDecimal.valueOf((i + 1) * 5.00)); // Example pricing
            item.setStatus(true);

            items.add(item);
        }

        itemRepository.saveAll(items);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("Application has started using @Configuration and CommandLineRunner!");

            initData1();
            initData2();
            initData3();
            initData4();
        };
    }


}
