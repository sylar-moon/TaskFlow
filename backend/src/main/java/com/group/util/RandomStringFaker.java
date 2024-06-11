package com.group.util;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class RandomStringFaker {
    private final Faker faker;
    private final Random random;


    public RandomStringFaker() {
        this.faker = new Faker();
        this.random = new Random();
    }

    public String getFakeString() {
        String result = switch (random.nextInt(20)) {
            case (0) -> faker.animal().name();
            case (1) -> faker.book().title();
            case (2) -> faker.color().name();
            case (3) -> faker.cat().name();
            case (4) -> faker.company().name();
            case (5) -> faker.app().name();
            case (6) -> faker.food().fruit();
            case (7) -> faker.food().sushi();
            case (8) -> faker.funnyName().name();
            case (9) -> faker.country().name();
            case (10) -> faker.harryPotter().spell();
            case (11) -> faker.artist().name();
            case (12) -> faker.beer().name();
            case (13) -> faker.gameOfThrones().house();
            case (14) -> faker.book().author();
            case (15) -> faker.pokemon().name();
            case (16) -> faker.superhero().name();
            case (17) -> faker.backToTheFuture().character();
            case (18) -> faker.music().genre();
            case (19) -> faker.rickAndMorty().character();
            default -> faker.harryPotter().character();
        };
        return result.replace(" ", "");
    }

}
